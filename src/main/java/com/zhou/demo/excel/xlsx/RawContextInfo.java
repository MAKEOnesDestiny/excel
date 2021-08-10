package com.zhou.demo.excel.xlsx;

import java.io.InputStream;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;

@Data
public class RawContextInfo {

    private Class targetClass;
    private Workbook workbook;
    private InputStream inputStream;

    public RawContextInfo(Class targetClass, Workbook workbook, InputStream inputStream) {
        this.targetClass = targetClass;
        this.workbook = workbook;
        this.inputStream = inputStream;
    }


}
