package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.bean.*;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.DynamicExcelFactory;
import com.zhou.demo.excel.factory.ExcelPos;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SimpleDynamicExcelFactory extends SimpleExcelFactory implements DynamicExcelFactory {

    @Override
    public List<DynamicExcelBean> toDynamicBean(Sheet sheet, DynamicExcelHeaders headers) throws Exception {
        List<DynamicExcelHeaders.Header> headerList = headers.getHeaders();
        List<DynamicExcelBean> result = new ArrayList<>();
        Integer headersRowNum = headers.getHeadersRowNum();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            if (i == headersRowNum) {
                //略过表头行
                continue;
            }
            DynamicExcelBean bean = new DefaultDynamicExcelBean();
            Row row = sheet.getRow(i);
            if (row == null || isRowAllBlank(row)) continue;
            boolean evictBlank = true;  //剔除全空行
            for (DynamicExcelHeaders.Header h : headerList) {
                Cell cell = row.getCell(h.getHeaderPos().getColumnIndex(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if ((cell.getCellTypeEnum() == CellType.BLANK && skipBlank())) {
                    //nop
                } else {
                    cell.setCellType(CellType.STRING); //统一设置为string
                    String rawValue = cell.getStringCellValue(); //单元格值
                    Object parsedValue;
                    ExcelPos dataPos = new ExcelPos(cell.getRowIndex(), cell.getColumnIndex(), row.getSheet());
                    //转换前校验
                    if (!validBeforeConvert(rawValue, h))
                        throw new ExcelDataWrongException("Excel数据校验失败", rawValue, h.getHeaderInStr(), h.getHeaderPos());
                    //转换数据
                    parsedValue = convert0(rawValue, h.getConverter(), dataPos, h.getTargetClass());
                    if (!validAfterConvert(parsedValue, h))
                        throw new ExcelDataWrongException("Excel数据校验失败", rawValue, h.getHeaderInStr(), h.getHeaderPos());
                    Map<DynamicExcelHeaders.Header, CellWrap> map = bean.getResolvedMap();
                    map.put(h, new CellWrap(parsedValue, h, cell));
                    evictBlank = false;
                }
                if (!evictBlank) result.add(bean);
            }
        }
        return result;
    }

    protected boolean validBeforeConvert(String rawValue, DynamicExcelHeaders.Header header) throws Exception {
        ExcelPos pos = header.getHeaderPos();
        Class<Validator>[] validators = header.getValidators();
        if (validators == null) {
            return true;
        }
        for (Class<Validator> validatorClass : validators) {
            boolean result;
            try {
                Validator validator =  BeanUtils.instantiateClass(validatorClass);
                result = (validator == null) ? true : validator.validBefore(rawValue);
            } catch (Exception e) {
                throw new ExcelDataWrongException(e.getMessage(), rawValue, header.getHeaderInStr(), pos);
            }
            if (result == true)
                continue;
            else
                return false;
        }
        return true;
    }

    protected boolean validAfterConvert(Object convertedValue, DynamicExcelHeaders.Header header) throws Exception {
        ExcelPos pos = header.getHeaderPos();
        Class<Validator>[] validators = header.getValidators();
        //null代表没有配置校验,默认通过
        if (validators == null) {
            return true;
        }
        for (Class<Validator> validatorClass  : validators) {
            boolean result;
            try {
                Validator validator =  BeanUtils.instantiateClass(validatorClass);
                result = (validator == null) ? true : validator.validAfter(convertedValue);
            } catch (Exception e) {
                throw new ExcelDataWrongException(e.getMessage(), convertedValue, header.getHeaderInStr(), pos);
            }
            if (result == true)
                continue;
            else
                return false;
        }
        return true;
    }


    @Override
    public DynamicExcelHeaders getHeadersFromExcel(Sheet sheet, Integer headerPosition) {
        if (sheet == null || headerPosition == null) {
            throw new IllegalArgumentException("错误的参数:sheet-->" + sheet + ",headerPosition-->" + headerPosition);
        }
        Row row = sheet.getRow(headerPosition);
        if (row == null) {
            throw new IllegalArgumentException("sheet[" + sheet.getSheetName() + "]表头不存在");
        }
        short lastCellNum = row.getLastCellNum();
        for (int i = 0; i <= lastCellNum; i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if(cell==null) continue;
            if (cell.getCellTypeEnum() == CellType.BLANK && skipBlank()){
                continue;
            }
            cell.setCellType(CellType.STRING); //统一设置为string
            String rawValue = cell.getStringCellValue(); //单元格值
//            if(r)

        }
        return null;
    }

    //cglib动态生成字段
    public Class getEnhancedClass() {
        Map<String, Class> pMap = new HashMap<>();
        pMap.put("cglib$prop1", String.class);

        BeanGenerator generator = new BeanGenerator();
        generator.setSuperclass(AbstractDynamicExcelBean.class);
        BeanGenerator.addProperties(generator, pMap);
        return (Class) generator.createClass();
    }


    public static void main(String[] args) {
        Class clazz = new SimpleDynamicExcelFactory().getEnhancedClass();
        System.out.println();
    }

}
