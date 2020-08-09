package com.zhou.demo.excel.spring;

import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.VersionExcelFactory;
import com.zhou.demo.excel.utils.ExcelFactories;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

/**
 * @see ExcelToBean
 */
public class ExcelBeanArguementResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ExcelToBean.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)
            throws Exception {
        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        ExcelToBean excelToBean = methodParameter.getParameterAnnotation(ExcelToBean.class);
        HttpInputMessage inputMessage = new RequestPartServletServerHttpRequest(servletRequest, excelToBean.file());
        int version = excelToBean.version();
        if (version <= 0) {
            ExcelFactory ef = ExcelFactories.simpleExcel();
            return ef.toBean(inputMessage.getBody(), excelToBean.targetClass());
        } else {
            VersionExcelFactory ef = ExcelFactories.versionExcel();
            return ef.toBean(inputMessage.getBody(), excelToBean.targetClass());
        }
    }

}
