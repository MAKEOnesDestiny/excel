package com.zhou.demo.excel.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;

public abstract class ConversionUtil {

    public static final Log log = LogFactory.getLog(ConversionUtil.class.getName());

    public static Object[] convertWithConversionService(ConversionService conversionService, String[] args,
                                                        Class[] classes) {
        if (args.length != classes.length) {
            throw new IllegalArgumentException("args's length must be equal to classes's length");
        }
        Object[] res = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            Object parsedValue = null;
            if (conversionService.canConvert(String.class, classes[i])) {
                try {
                    parsedValue = conversionService.convert(args[i], classes[i]);
                } catch (ConversionException e1) {
                    //发生转换异常
                    log.info("无法转换[{" + args[i] + "}]为{" + classes[i].getCanonicalName() + "}类型");
                }
            } else {
                log.info("无法转换[{" + args[i] + "}]为{" + classes[i].getCanonicalName() + "}类型");
            }
            res[i] = parsedValue;
        }
        return res;
    }

}
