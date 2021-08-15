package com.zhou.demo.excel.xlsx.tag;

import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;

import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.ExcelBeanMetaData;
import com.zhou.demo.excel.annotation.Validator;
import com.zhou.demo.excel.config.ApplicationContextAccessor;
import com.zhou.demo.excel.exception.ExcelDataWrongException;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.converter.Converter;
import com.zhou.demo.excel.factory.converter.EmptyConverter;
import com.zhou.demo.excel.factory.impl.DefaultExcelFactory.ValidPipeLine;
import com.zhou.demo.excel.xlsx.AbstractXlsxTagHandler;
import com.zhou.demo.excel.xlsx.AnalysisContext;
import com.zhou.demo.excel.xlsx.CellData;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("all")
public class SheetDataHandler extends AbstractXlsxTagHandler {

    @Override
    public void endElement(String qName, AnalysisContext context) throws Exception {
        List<Map<Integer, CellData>> results = context.tempResults();
        if (CollectionUtils.isEmpty(results) || results.size() < 2) {
            return;
        }
        Integer index = 0;
        Map<Integer, CellData> head = results.get(index++);
        Map<Integer, ColumnWrap> headName2Index = resolveHead(head, context.analysisInfo().getExcelBeanMetaData());
        while (index < results.size()) {
            Map<Integer, CellData> dataMap = results.get(index);
            Object bean = context.analysisInfo().getTargetClass().newInstance();
            for (Map.Entry<Integer, ColumnWrap> t : headName2Index.entrySet()) {
                try {
                    processData(bean, dataMap.get(t.getKey()), t.getValue(), context.analysisInfo().getSheetName());
                } catch (Exception e) {
                    if (context.getConfig().isCatchAllException()) {
                        context.processExceptions().add(e);
                    } else {
                        throw e;
                    }
                }
            }
            context.results().add(bean);
            index++;
        }
    }

    /**
     * return map : headName -> columnIndex
     *
     * @param head
     * @param excelBeanMetaData
     */
    private Map<Integer, ColumnWrap> resolveHead(Map<Integer, CellData> head, ExcelBeanMetaData excelBeanMetaData) {
        if (head == null || head.size() == 0) {
            throw new RuntimeException("excel head can't be empty");
        }
        Map<Integer, ColumnWrap> index2HeadName = new LinkedHashMap<>();
        ColumnWrap[] columnWraps = excelBeanMetaData.getColumnWraps();
        for (ColumnWrap cw : columnWraps) {
            String headName = cw.getColumn().headerName();
            Map.Entry<Integer, CellData> find =
                    head.entrySet().stream().filter(t -> headName.equals(t.getValue().getDataString())).findFirst()
                            .orElse(null);
            if (find == null) {
                throw new IllegalArgumentException("can't find head ==> " + headName);
            }
            index2HeadName.put(find.getKey(), cw);
        }
        return index2HeadName;
    }

    private void processData(Object bean, CellData cellData, ColumnWrap cw, String sheetName)
            throws Exception {
        String data = cellData.getDataString();

        Object parsedValue;
        ExcelPos dataPos = new ExcelPos(cellData.getRowIndex(), cellData.getColumnIndex(), sheetName);
        //转换前校验
        if (!validBeforeConvert(cellData.getDataString(), cw, dataPos, cw.getField().getType())) {
            throw new ExcelDataWrongException("Excel数据校验失败", cellData.getDataString(), cw.getColumn().headerName(),
                                              dataPos);
        }
        //转换数据
        parsedValue = convert(cellData.getDataString(), cw.getColumn().convert(), dataPos,
                              cw.getColumn().headerName(), cw.getField().getType());
        if (!validAfterConvert(parsedValue, cellData.getDataString(), cw, dataPos, cw.getField().getType())) {
            throw new ExcelDataWrongException("Excel数据校验失败", cellData.getDataString(), cw.getColumn().headerName(),
                                              dataPos);
        }

        Method setMethod = findMethod(bean.getClass(), cw.getColumn().setter(), cw.getField().getType());
        invokeMethod(setMethod, bean, parsedValue);
    }

    protected final <T> Object convert(String rawValue, Class<? extends Converter> converterClazz, ExcelPos pos,
                                       String columnName, Class<T> tClass) throws ExcelDataWrongException {
        ConversionService conversionService = ApplicationContextAccessor.getApplicationContext()
                .getBean(ConversionService.class);
        Class<? extends Converter> converterClass = converterClazz;
        //有自定义的转换器
        if (converterClass != EmptyConverter.class) {
            Converter converter = BeanUtils.instantiateClass(converterClass);
            try {
                return converter.convert(rawValue);
            } catch (Exception e1) {
                throw new ExcelDataWrongException(e1.getMessage(), (Object) rawValue, columnName, pos);
            }
        }
        Object parsedValue;
        //复用Web请求参数解析服务
        if (tClass != String.class) {
            if (conversionService.canConvert(String.class, tClass)) {
                try {
                    parsedValue = conversionService.convert(rawValue, tClass);
                } catch (ConversionException e1) {
                    //发生转换异常
                    throw new ExcelDataWrongException(e1.getMessage(), (Object) rawValue, pos);
                }
            } else {
                //                log.info("无法转换[{" + rawValue + "}]为{" + tClass.getCanonicalName() + "}类型");
                parsedValue = null;
            }
        } else {
            parsedValue = rawValue;
        }
        return parsedValue;
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
                Validator validator = line.getValidator();
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
                Validator validator = line.getValidator();
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


}
