package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.annotation.*;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.ExcelPos;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

@Log4j2
public abstract class DefaultExcelFactory implements ExcelFactory {
    //用户自定义函数
//////////////////////////////////////////////////////////////////////////////////////

    public void validExcel(Workbook workBook, Excel excel) throws Exception {
        //do nothing
    }

    /**
     * 配置项,是否忽略空白行
     *
     * @return
     */
    @Override
    public boolean skipBlank() {
        return false;
    }

    protected Sheet getSheet(Excel excel, Workbook workbook) {
        return workbook.getSheetAt(excel.sheet());
    }

    /**
     * 用于扩展数据转换的操作
     *
     * @param rawValue
     * @param cw
     * @param pos
     * @param tClass
     * @param <T>
     * @return
     * @throws ExcelDataWrongException
     */
    protected <T> Object convert(String rawValue, ColumnWrap cw, ExcelPos pos, Class<T> tClass) throws ExcelDataWrongException {
        return rawValue;
    }

    /**
     * 根据表头解析和bean的映射关系
     *
     * @param row   表头行
     * @param clazz
     * @param <T>
     * @return
     */
    public abstract <T> Map<ColumnWrap, ExcelPos> resolveMapping(Row row, Class<T> clazz);

//////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <T> List<T> toBean(Workbook wb, Class<T> targetClass) throws Exception {
        Excel excel = targetClass.getAnnotation(Excel.class);
        if (excel == null) throw new RuntimeException(targetClass.getCanonicalName() + "没有配置@Excel注解!");

        validExcel(wb, excel);

        Sheet sheet = getSheet(excel, wb);
        List<T> result = new ArrayList<>();
        Map<ColumnWrap, ExcelPos> mapping = null;

        for (int i = excel.offset(); i <= sheet.getLastRowNum(); i++) {
            //表头校验
            if (i == excel.offset()) {
                Row row = sheet.getRow(i);
                if (row == null)
                    throw new RuntimeException("[" + excel.sheetName() + "]--->第" + (excel.offset() + 1) + "行处表头信息错误");
                mapping = resolveMapping(row, targetClass);
                continue;
            }
            //处理数据
            Row row = sheet.getRow(i);
            if (row == null || isRowAllBlank(row)) continue;
            T bean = targetClass.newInstance();
            boolean evictBlank = true;  //剔除全空行
            for (Map.Entry<ColumnWrap, ExcelPos> e : mapping.entrySet()) {
                ColumnWrap cw = e.getKey();
                Column column = cw.getColumn();
                ExcelPos pos = e.getValue();
                Class tClass = cw.getField().getType();

                Cell cell = row.getCell(pos.getColumnIndex());
                if (cell == null) continue; //代表单元格为空
                if (cell.getCellTypeEnum() == CellType.BLANK && skipBlank()) continue;
                cell.setCellType(CellType.STRING); //统一设置为string
                String rawValue = cell.getStringCellValue(); //单元格值
                Object parsedValue;
                ExcelPos dataPos = new ExcelPos(cell.getRowIndex(), cell.getColumnIndex(), row.getSheet());
                //转换前校验
                if (!validBeforeConvert(rawValue, cw, dataPos, tClass))
                    throw new ExcelDataWrongException("Excel数据校验失败", rawValue, column.headerName(), pos);
                //转换数据
                parsedValue = convert(rawValue, cw, dataPos, tClass);
                //转换后校验
                if (!validAfterConvert(parsedValue, cw, pos, tClass))
                    throw new ExcelDataWrongException("Excel数据校验失败", rawValue, column.headerName(), pos);
                Method setMethod = findMethod(targetClass, column.setter(), tClass);
                invokeMethod(setMethod, bean, parsedValue);
                evictBlank = false;
            }
            if (!evictBlank) result.add(bean);
        }
        return result;
    }

    @Override
    public <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws Exception {
        inputStream.reset();
        Workbook wb = new XSSFWorkbook(inputStream);
        return toBean(wb, targetClass);
    }

    //判断是否为全空行，全空行为无效行
    private boolean isRowAllBlank(Row row) {
        int first = row.getFirstCellNum();
        int last = row.getLastCellNum();
        for (int i = first; i <= last; i++) {
            Cell c = row.getCell(i);
            if (c != null && c.getCellTypeEnum() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    protected boolean validBeforeConvert(String rawValue, ColumnWrap cw, ExcelPos pos, Class tClass) throws Exception {
        ValidPipeLine line = cw.getPipeLine();
        //null代表没有配置校验,默认通过
        if (line == null) return true;
        for (; ; ) {
            boolean result = line.validator.validBefore(rawValue, cw, pos, tClass);
            if ((line = line.getNext()) != null && result == true)
                continue;
            else
                return result;
        }
    }

    protected boolean validAfterConvert(Object convertedValue, ColumnWrap cw, ExcelPos pos, Class tClass) throws Exception {
        ValidPipeLine line = cw.getPipeLine();
        //null代表没有配置校验,默认通过
        if (line == null) return true;
        for (; ; ) {
            if (line.getNext() != null)
                line = line.getNext();
            else
                break;
        }
        for (; ; ) {
            boolean result = line.validator.validAfter(convertedValue, cw, pos, tClass);
            if ((line = line.getPrev()) != null && result == true)
                continue;
            else
                return result;
        }
    }

    @Data
    public static final class ValidPipeLine {
        volatile ValidPipeLine next;

        volatile ValidPipeLine prev;

        Validator validator;

        public ValidPipeLine(Validator validator) {
            this.validator = validator;
        }
    }


    @Override
    public <T> Workbook generateEmptyExcel(Class<T> targetClass) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Excel excel = targetClass.getAnnotation(Excel.class);
        String sheetName = excel.sheetName();
        Sheet sheet = wb.createSheet(sheetName.equals("") ? "sheet1" : sheetName);
        Row row = sheet.createRow(excel.offset());
        ExcelBeanMetaData excelBeanMetaData = getExcelBeanMeta(targetClass);
        ColumnWrap[] cws = excelBeanMetaData.getColumnWraps();
        for (int i = 0; i < cws.length; i++) {
            if (cws == null) continue;
            ColumnWrap cw = cws[i];
            Column c = cw.getColumn();
            Cell cell = row.createCell(i);
            cell.setCellValue(c.headerName());
        }
        return wb;
    }

    private final static Map<Class, ExcelBeanMetaData> cache = new ConcurrentHashMap<>();

    public <T> ExcelBeanMetaData getExcelBeanMeta(Class<T> targetClass) {
        ExcelBeanMetaData excelBeanMetaData = cache.get(targetClass);
        if (excelBeanMetaData == null) {
            excelBeanMetaData = resolveExcelBeanMeta(targetClass);
            cache.put(targetClass, excelBeanMetaData);
        }
        return excelBeanMetaData;
    }

    public <T> ExcelBeanMetaData resolveExcelBeanMeta(Class<T> targetClass) {
        Excel excel = targetClass.getAnnotation(Excel.class);
        Field[] fields = targetClass.getDeclaredFields();
        ColumnWrap[] cws = new ColumnWrap[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column c = f.getAnnotation(Column.class);
            if (c != null) {
                cws[i] = new ColumnWrap(c, f, null);
            }
            //else skip
        }
        ExcelBeanMetaData metaData = new ExcelBeanMetaData(excel, cws);
        cache.put(targetClass, metaData);
        return metaData;
    }

}
