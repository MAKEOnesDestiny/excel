package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Excel;
import com.zhou.demo.excel.annotation.ExcelBeanMetaData;
import com.zhou.demo.excel.annotation.Version;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.VersionExcelFactory;
import java.io.IOException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhou.demo.excel.utils.BeanUtil.findSetMethod;
import static com.zhou.demo.excel.utils.SelfAnnotationUtil.getMemberValuesMap;

//Thread-safe
//using thread-local
public class VersionExcelImplFactory extends SimpleExcelFactory implements VersionExcelFactory {

    private static final ThreadLocal<Integer> VERSION_TL = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    @Override
    public <T> List<T> toBean(InputStream inputStream, Class<T> targetClass, final int version) throws Exception {
        inputStream.reset();
        Workbook wb = new XSSFWorkbook(inputStream);
        return toBean(wb, targetClass, version);
    }

    @Override
    public <T> List<T> toBean(Workbook workbook, Class<T> targetClass, int version) throws Exception {
        if (version < 0) {
            if (version == -1) throw new IllegalArgumentException("使用@Version时,其成员@Column必须配置version且version非负");
            else
                throw new IllegalArgumentException("错误的版本信息:" + version + ",版本信息必须>=0");
        }
        VERSION_TL.set(version);
        return toBean(workbook, targetClass);
    }

    @Override
    public <T> Workbook generateExcel(List<T> list, Class<T> targetClass, int version) throws IOException {
        VERSION_TL.set(version);
        return generateExcel(list, targetClass);
    }

    @Override
    public <T> ExcelBeanMetaData resolveExcelBeanMeta(Class<T> targetClass) {
        Excel excel = targetClass.getAnnotation(Excel.class);
        Field[] fields = targetClass.getDeclaredFields();
        Class<? super T> temp = targetClass.getSuperclass();
        //找到父类的所有字段
        while (temp != Object.class) {
            Field[] parentFields = temp.getDeclaredFields();
            //fix me?
            fields = (Field[]) ArrayUtils.addAll(fields, parentFields);
            temp = temp.getSuperclass();
        }
        int count = 0;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Version v = f.getAnnotation(Version.class);
            if (v == null) continue;
            Column[] cs = v.value();
            int requireVersion = getVersion();
            Column findColumn = null;
            for (Column c : cs) {
                int version = c.version();
                if (version == requireVersion) {
                    //找到对应版本的Column
                    findColumn = c;
                    break;
                }
            }
            if (findColumn != null) {
                count++;
            }
            //else skip
        }
        ColumnWrap[] cws = new ColumnWrap[count];
        int j = 0;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Version v = f.getAnnotation(Version.class);
            if (v == null) continue;
            Column[] cs = v.value();
            int requireVersion = getVersion();
            Column findColumn = null;
            for (Column c : cs) {
                int version = c.version();
                if (version == requireVersion) {
                    //找到对应版本的Column
                    findColumn = c;
                    break;
                }
            }
            if (findColumn != null) {
                cws[j] = new ColumnWrap(findColumn, f, null);
                j++;
            }
            //else skip
        }
        ExcelBeanMetaData metaData = new ExcelBeanMetaData(excel, cws);
        return metaData;
    }

    @Override
    public int getVersion() {
        return VERSION_TL.get();
    }

    @Override
    public <T> Map<ColumnWrap, ExcelPos> resolveMapping(Row row, Class<T> clazz) {
        Map<ColumnWrap, ExcelPos> map = new HashMap();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            Version v = f.getAnnotation(Version.class);
            if (v == null) continue;
            Column[] cs = v.value();
            int requireVersion = getVersion();
            Column findColumn = null;
            for (Column c : cs) {
                int version = c.version();
                if (version == requireVersion) {
                    //找到对应版本的Column
                    findColumn = c;
                    break;
                }
            }
            ExcelPos pos;
            if (findColumn != null && (pos = findPos(findColumn.headerName(), row)) != null) {
                //获取set方法的名称
                String setMethodName = findSetMethod(f, true);
                String initSetMethodName = findColumn.setter();
                if (initSetMethodName.equals("") && setMethodName != null) {
                    //获取注解内部的值map
                    Map<String, Object> valuesMap = getMemberValuesMap(findColumn);
                    //如果用户没有自定义setter,则使用默认的setter方法
                    valuesMap.put("setter", setMethodName);
                }
                Class[] validClass = findColumn.valid();
                ValidPipeLine validPipeLine = initPipeLine(validClass);
                map.put(new ColumnWrap(findColumn, f, validPipeLine), pos);
            }
        }
        return map;
    }


}
