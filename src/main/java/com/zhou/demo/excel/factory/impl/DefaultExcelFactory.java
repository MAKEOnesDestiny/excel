package com.zhou.demo.excel.factory.impl;

import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.annotation.ExcelBeanMetaData;
import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.bean.DataWrap;
import com.zhou.demo.excel.exception.ExcelCompositionException;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.ExcelFactoryConfigInner;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.formatter.Formatter;
import com.zhou.demo.excel.utils.ExcelDataUtil;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;


@SuppressWarnings("all")
public abstract class DefaultExcelFactory implements ExcelFactory {

    public static final Log log = LogFactory.getLog(DefaultExcelFactory.class.getName());
    //给一个默认的配置
    private ExcelFactoryConfigInner config = new ExcelFactoryConfigInner();

    @Override
    public synchronized ExcelFactoryConfigInner getConfig() {
        return config;
    }

    @Override
    public synchronized void setConfig(ExcelFactoryConfigInner config) {
        this.config = config;
    }

    public void validExcel(Workbook workBook, Excel excel) throws Exception {
    }

    @Override
    public boolean skipBlank() {
        if (getConfig() == null) {
            return false;
        } else {
            return getConfig().isSkipBlank();
        }
    }

    //用户自定义函数
    //////////////////////////////////////////////////////////////////////////////////////
    protected Sheet getSheet(Excel excel, Workbook workbook) {
        return workbook.getSheetAt(excel.sheet());
    }

    protected <T> Object convert(String rawValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass)
            throws ExcelDataWrongException {
        return rawValue;
    }

    /**
     * 根据表头解析和bean的映射关系
     *
     * @param row   表头行
     * @param clazz
     * @param <T>
     */
    public abstract <T> Map<ColumnWrap, ExcelPos> resolveMapping(Row row, Class<T> clazz);

    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws Exception {
        BufferedInputStream bis = null;
        if (!(inputStream instanceof FileInputStream)) {
            bis = new BufferedInputStream(inputStream);
            bis.reset();
        }
        Workbook wb = new XSSFWorkbook(bis == null ? inputStream : bis);
        return toBean(wb, targetClass);
    }

    @Override
    public <T> List<T> toBean(Workbook wb, Class<T> targetClass) throws Exception {
        Excel excel = targetClass.getAnnotation(Excel.class);
        if (excel == null) {
            throw new RuntimeException(targetClass.getCanonicalName() + "没有配置@Excel注解!");
        }
        validExcel(wb, excel);

        Sheet sheet = getSheet(excel, wb);
        List<T> result = new ArrayList<>();
        Map<ColumnWrap, ExcelPos> mapping = null;
        if (sheet == null) {
            log.debug("找不到excel:" + excel.sheetName());
            return result;
        }
        List<ExcelDataWrongException> exceptions = new ArrayList<>();
        for (int i = excel.offset(); i <= sheet.getLastRowNum(); i++) {
            //表头校验
            if (i == excel.offset()) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    throw new RuntimeException("[" + excel.sheetName() + "]--->第" + (excel.offset() + 1) + "行处表头信息错误");
                }
                mapping = resolveMapping(row, targetClass);
                continue;
            }
            //处理数据
            Row row = sheet.getRow(i);
            if (row == null || isRowAllBlank(row)) {
                continue;
            }
            //新增举例行过滤
            if (getConfig().isFilterExample() && i == excel.offset() + 1 && ExcelDataUtil.isExampleRow(row, mapping)) {
                continue;
            }
            T bean = targetClass.newInstance();
            boolean evictBlank = true;  //剔除全空行
            for (Map.Entry<ColumnWrap, ExcelPos> e : mapping.entrySet()) {
                ColumnWrap cw = e.getKey();
                Column column = cw.getColumn();
                ExcelPos pos = e.getValue();
                Class tClass = cw.getField().getType();

                Cell cell = row.getCell(pos.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell == null) {
                    continue; //代表单元格为空
                }
                if (cell.getCellTypeEnum() == CellType.BLANK && skipBlank()) {
                    continue;
                }
                cell.setCellType(CellType.STRING); //统一设置为string
                String rawValue = cell.getStringCellValue(); //单元格值
                Object parsedValue;
                ExcelPos dataPos = new ExcelPos(cell.getRowIndex(), cell.getColumnIndex(), row.getSheet());
                try {
                    //转换前校验
                    if (!validBeforeConvert(rawValue, cw, dataPos, tClass)) {
                        throw new ExcelDataWrongException("Excel数据校验失败", rawValue, column.headerName(), pos);
                    }
                    //转换数据
                    parsedValue = convert(rawValue, cw, dataPos, tClass);
                    if (!validAfterConvert(parsedValue, rawValue, cw, pos, tClass)) {
                        throw new ExcelDataWrongException("Excel数据校验失败", rawValue, column.headerName(), pos);
                    }
                } catch (ExcelDataWrongException e1) {
                    if (getConfig().isCatchAllException()) {
                        exceptions.add(e1);
                        continue;
                    } else {
                        throw e1;
                    }
                }
                Method setMethod = findMethod(targetClass, column.setter(), tClass);
                invokeMethod(setMethod, bean, parsedValue);
                evictBlank = false;
            }
            if (!evictBlank) {
                result.add(bean);
            }
        }
        if (exceptions.size() > 0) {
            throw new ExcelCompositionException(exceptions);
        }
        return result;
    }

    @Override
    public <T> Map<Row, T> toBeanWithPos(Workbook wb, Class<T> targetClass) throws Exception {
        Excel excel = targetClass.getAnnotation(Excel.class);
        if (excel == null) {
            throw new RuntimeException(targetClass.getCanonicalName() + "没有配置@Excel注解!");
        }
        validExcel(wb, excel);

        Sheet sheet = getSheet(excel, wb);
        Map<Row, T> result = new HashMap<>();
        Map<ColumnWrap, ExcelPos> mapping = null;
        if (sheet == null) {
            log.debug("找不到excel:" + excel.sheetName());
            return result;
        }

        for (int i = excel.offset(); i <= sheet.getLastRowNum(); i++) {
            //表头校验
            if (i == excel.offset()) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    throw new RuntimeException("[" + excel.sheetName() + "]--->第" + (excel.offset() + 1) + "行处表头信息错误");
                }
                mapping = resolveMapping(row, targetClass);
                continue;
            }
            //处理数据
            Row row = sheet.getRow(i);
            if (row == null || isRowAllBlank(row)) {
                continue;
            }
            T bean = targetClass.newInstance();
            boolean evictBlank = true;  //剔除全空行
            for (Map.Entry<ColumnWrap, ExcelPos> e : mapping.entrySet()) {
                ColumnWrap cw = e.getKey();
                Column column = cw.getColumn();
                ExcelPos pos = e.getValue();
                Class tClass = cw.getField().getType();

                Cell cell = row.getCell(pos.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell == null) {
                    continue; //代表单元格为空
                }
                if (cell.getCellTypeEnum() == CellType.BLANK && skipBlank()) {
                    continue;
                }
                cell.setCellType(CellType.STRING); //统一设置为string
                String rawValue = cell.getStringCellValue(); //单元格值
                Object parsedValue;
                ExcelPos dataPos = new ExcelPos(cell.getRowIndex(), cell.getColumnIndex(), row.getSheet());
                //转换前校验
                if (!validBeforeConvert(rawValue, cw, dataPos, tClass)) {
                    throw new ExcelDataWrongException("Excel数据校验失败", rawValue, column.headerName(), pos);
                }
                //转换数据
                parsedValue = convert(rawValue, cw, dataPos, tClass);
                if (!validAfterConvert(parsedValue, rawValue, cw, pos, tClass)) {
                    throw new ExcelDataWrongException("Excel数据校验失败", rawValue, column.headerName(), pos);
                }
                Method setMethod = findMethod(targetClass, column.setter(), tClass);
                invokeMethod(setMethod, bean, parsedValue);
                evictBlank = false;
            }
            if (!evictBlank) {
                result.put(row, bean);
            }
        }
        return result;
    }

    @Override
    public <T> List<DataWrap<T>> toWrapBean(Workbook wb, Class<T> targetClass) throws Exception {
        Excel excel = targetClass.getAnnotation(Excel.class);
        if (excel == null) {
            throw new RuntimeException(targetClass.getCanonicalName() + "没有配置@Excel注解!");
        }
        validExcel(wb, excel);

        Sheet sheet = getSheet(excel, wb);
        List<DataWrap<T>> result = new ArrayList<>();
        Map<ColumnWrap, ExcelPos> mapping = null;
        if (sheet == null) {
            log.debug("找不到excel:" + excel.sheetName());
            return result;
        }

        for (int i = excel.offset(); i <= sheet.getLastRowNum(); i++) {
            //表头校验
            if (i == excel.offset()) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    throw new RuntimeException("[" + excel.sheetName() + "]--->第" + (excel.offset() + 1) + "行处表头信息错误");
                }
                mapping = resolveMapping(row, targetClass);
                continue;
            }
            //处理数据
            Row row = sheet.getRow(i);
            if (row == null || isRowAllBlank(row)) {
                continue;
            }
            T bean = targetClass.newInstance();
            boolean evictBlank = true;  //剔除全空行
            for (Map.Entry<ColumnWrap, ExcelPos> e : mapping.entrySet()) {
                ColumnWrap cw = e.getKey();
                Column column = cw.getColumn();
                ExcelPos pos = e.getValue();
                Class tClass = cw.getField().getType();

                Cell cell = row.getCell(pos.getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (cell == null) {
                    continue; //代表单元格为空
                }
                if (cell.getCellTypeEnum() == CellType.BLANK && skipBlank()) {
                    continue;
                }
                cell.setCellType(CellType.STRING); //统一设置为string
                String rawValue = cell.getStringCellValue(); //单元格值
                Object parsedValue;
                ExcelPos dataPos = new ExcelPos(cell.getRowIndex(), cell.getColumnIndex(), row.getSheet());
                //转换前校验
                if (!validBeforeConvert(rawValue, cw, dataPos, tClass)) {
                    throw new ExcelDataWrongException("Excel数据校验失败", rawValue, column.headerName(), pos);
                }
                //转换数据
                parsedValue = convert(rawValue, cw, dataPos, tClass);
                if (!validAfterConvert(parsedValue, rawValue, cw, pos, tClass)) {
                    throw new ExcelDataWrongException("Excel数据校验失败", rawValue, column.headerName(), pos);
                }
                Method setMethod = findMethod(targetClass, column.setter(), tClass);
                invokeMethod(setMethod, bean, parsedValue);
                evictBlank = false;
            }
            if (!evictBlank) {
                result.add(new DataWrap<>(bean, row));
            }
        }
        return result;
    }

    protected boolean validBeforeConvert(String rawValue, ColumnWrap cw, ExcelPos pos, Class tClass) throws Exception {
        ValidPipeLine line = cw.getPipeLine();
        //null代表没有配置校验,默认通过
        if (line == null) {
            return true;
        }
        for (; ; ) {
            boolean result;
            try {
                Validator validator = line.validator;
                result = (validator == null) ? true : validator.validBefore(rawValue);
            } catch (Exception e) {
                throw new ExcelDataWrongException(e.getMessage(), rawValue, cw.getColumn().headerName(), pos);
            }
            if ((line = line.getNext()) != null && result == true) {
                continue;
            } else {
                return result;
            }
        }
    }

    protected boolean validAfterConvert(Object convertedValue, String rawValue, ColumnWrap cw, ExcelPos pos,
                                        Class tClass) throws Exception {
        ValidPipeLine line = cw.getPipeLine();
        //null代表没有配置校验,默认通过
        if (line == null) {
            return true;
        }
        for (; ; ) {
            if (line.getNext() != null) {
                line = line.getNext();
            } else {
                break;
            }
        }
        for (; ; ) {
            boolean result;
            try {
                Validator validator = line.validator;
                result = (validator == null) ? true : validator.validAfter(convertedValue);
            } catch (Exception e) {
                throw new ExcelDataWrongException(e.getMessage(), rawValue, cw.getColumn().headerName(), pos);
            }
            if ((line = line.getPrev()) != null && result == true) {
                continue;
            } else {
                return result;
            }
        }
    }

    //判断是否为全空行，全空行为无效行
    protected boolean isRowAllBlank(Row row) {
        int first = row.getFirstCellNum();
        int last = row.getLastCellNum();
        if (first < 0 || first < 0) {
            return true;
        }
        for (int i = first; i <= last; i++) {
            Cell c = row.getCell(i);
            if (c != null && c.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    public static final class ValidPipeLine {

        public static final ValidPipeLine EMPTY_VALID_PIPELINE;

        static {
            EMPTY_VALID_PIPELINE = new ValidPipeLine(null);
            EMPTY_VALID_PIPELINE.setNext(null);
            EMPTY_VALID_PIPELINE.setPrev(null);
        }

        volatile ValidPipeLine next;

        volatile ValidPipeLine prev;

        Validator validator;

        public ValidPipeLine(Validator validator) {
            this.validator = validator;
        }

        public ValidPipeLine getNext() {
            return next;
        }

        public void setNext(ValidPipeLine next) {
            this.next = next;
        }

        public ValidPipeLine getPrev() {
            return prev;
        }

        public void setPrev(ValidPipeLine prev) {
            this.prev = prev;
        }

        public Validator getValidator() {
            return validator;
        }

        public void setValidator(Validator validator) {
            this.validator = validator;
        }
    }

    public <T> ExcelBeanMetaData getExcelBeanMeta(Class<T> targetClass) {
        ExcelBeanMetaData excelBeanMetaData = resolveExcelBeanMeta(targetClass);
        return excelBeanMetaData;
    }

    public <T> ExcelBeanMetaData resolveExcelBeanMeta(Class<T> targetClass) {
        Excel excel = targetClass.getAnnotation(Excel.class);
        Field[] fields = targetClass.getDeclaredFields();
        int count = 0;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column c = f.getAnnotation(Column.class);
            if (c != null) {
                count++;
            }
            //else skip
        }
        ColumnWrap[] cws = new ColumnWrap[count];
        int j = 0;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column c = f.getAnnotation(Column.class);
            if (c != null) {
                cws[j] = new ColumnWrap(c, f, null);
                j++;
            }
            //else skip
        }
        ExcelBeanMetaData metaData = new ExcelBeanMetaData(excel, cws);
        return metaData;
    }

    public void initExcelBeanMetaData(ExcelBeanMetaData excelBeanMetaData) {
        //empty realization
    }

    @Override
    public <T> Workbook generateExcel(List<T> list, Class<T> targetClass) throws IOException {
        Workbook wb = new XSSFWorkbook();
        generateSheet(list, targetClass, wb);
        return wb;
    }

    public <T> void generateSheet(List<T> list, Class<T> targetClass, Workbook wb) throws IOException {
        Excel excel = targetClass.getAnnotation(Excel.class);
        String sheetName = excel.sheetName();
        Sheet sheet = wb.createSheet(sheetName.equals("") ? "sheet1" : sheetName);
        ExcelBeanMetaData excelBeanMetaData = getExcelBeanMeta(targetClass);
        initExcelBeanMetaData(excelBeanMetaData);
        ColumnWrap[] cws = excelBeanMetaData.getColumnWraps();

        for (int i = -1; i < list.size(); i++) {
            Row row = sheet.createRow(excel.offset() + i + 1);
            if (i == -1) {
                //设置表头
                for (int j = 0; j < cws.length; j++) {
                    if (cws == null) {
                        continue;
                    }
                    ColumnWrap cw = cws[j];
                    Column c = cw.getColumn();
                    Cell cell = row.createCell(j);
                    cell.setCellValue(c.headerName());
                }
            } else {
                //设置数据
                for (int j = 0; j < cws.length; j++) {
                    T t = list.get(i);
                    if (cws == null) {
                        continue;
                    }
                    ColumnWrap cw = cws[j];
                    Column c = cw.getColumn();
                    Cell cell = row.createCell(j);
                    Method getMethod = findMethod(targetClass, c.getter());
                    Object get = ReflectionUtils.invokeMethod(getMethod, t);
                    Formatter formatter = BeanUtils.instantiateClass(c.format());
                    Object fGet = formatter.format(get);
                    if (fGet != null) {
                        cell.setCellValue(fGet.toString());
                    }
                }
            }
        }
    }
}
