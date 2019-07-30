package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.annotation.Column;
import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.annotation.Version;
import com.zhou.demo.excel.factory.ExcelPos;
import com.zhou.demo.excel.factory.VersionExcelFactory;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhou.demo.excel.utils.BeanUtil.findSetMethod;
import static com.zhou.demo.excel.utils.SelfAnnotationUtil.getMemberValuesMap;

//Thread-safe
public class VersionExcelImplFactory extends SimpleExcelFactory implements VersionExcelFactory {

    private static final ThreadLocal<Integer> VERSION_TL = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    @Override
    public <T> List<T> toBean(InputStream inputStream, Class<T> targetClass, final int version) throws Exception {
        if (version < 0) {
            if (version == -1) throw new IllegalArgumentException("使用@Version时,其成员@Column必须配置version且version非负");
            else
                throw new IllegalArgumentException("错误的版本信息:" + version + ",版本信息必须>=0");
        }
        VERSION_TL.set(version);
        return toBean(inputStream, targetClass);
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
                map.put(new ColumnWrap(findColumn, f,null), pos);
            }
        }
        return map;
    }


}
