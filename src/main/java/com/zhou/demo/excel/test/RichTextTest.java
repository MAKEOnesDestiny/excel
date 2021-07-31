package com.zhou.demo.excel.test;

import org.apache.poi.xssf.usermodel.XSSFRichTextString;

public class RichTextTest {

    public static void main(String[] args) {
        XSSFRichTextString rts = new XSSFRichTextString("im kobe");
        System.out.println(rts.toString());
    }

}
