package com.zhou.demo.excel.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BigExcelTest {

    //151MB使用了10s进行读取
    public static void main(String[] args) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
//        FileChannel fc = new
        File file = new File("/Users/hfzhou/Desktop/test-03.xls");
//        File file = new File("/Users/hfzhou/Desktop/small-07.xlsx");
//        File file = new File("/Users/hfzhou/Desktop/test.xlsx");
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        System.out.println(new Date());
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Workbook wb = new HSSFWorkbook(bis);
//        Workbook wb = new XSSFWorkbook(bis);
        System.out.println(new Date());
        while(true){
            System.out.println("休眠");
            Thread.sleep(100000);
        }
    }

}
