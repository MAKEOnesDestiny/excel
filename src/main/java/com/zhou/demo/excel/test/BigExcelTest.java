package com.zhou.demo.excel.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BigExcelTest {

    //151MB使用了6s进行读取
    public static void main(String[] args) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
//        FileChannel fc = new
        File file = new File("/Users/hfzhou/Desktop/origin_order.xls");
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        System.out.println(new Date());
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Workbook wb = new HSSFWorkbook(bis);
        System.out.println(new Date());
        System.out.println(1111);
    }

}
