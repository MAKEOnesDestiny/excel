package com.zhou.demo.excel.config;

import org.springframework.core.convert.ConversionService;

import java.util.HashMap;
import java.util.Map;

/**
 * for 隐式参数传递
 */
public abstract class ParameterPassHelp {

    private static final ThreadLocal<Map<String, Object>> PARAM_INNER = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public static <T> T getParamByName(String key, Class<T> clazz) {
        if (clazz == null || key == null) throw new IllegalArgumentException("key or clazz can't be null");
        Object rawResult = PARAM_INNER.get().get(key);
        if (rawResult == null) return null;
        if (clazz.isAssignableFrom(rawResult.getClass())) {
            return (T) rawResult;
        } else {
            ConversionService cs = ApplicationContextAccessor.getApplicationContext().getBean(ConversionService.class);
            T convertedResult = null;
            if (cs != null) {
                if (cs.canConvert(rawResult.getClass(), clazz)) {
                    try {
                        convertedResult = cs.convert(rawResult, clazz);
                    } catch (Throwable e) {
                        //do nothing
                    }
                }
            }
            return convertedResult;
        }
    }

    public static void setParam(String key, Object obj) {
        if (key == null || obj == null) throw new IllegalArgumentException("key or obj can't be null");
        Object prev = PARAM_INNER.get().put(key, obj);
    }


}
