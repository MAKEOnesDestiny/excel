package com.zhou.demo.excel.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class SaxModelExcel {

    public SheetHandler sheetHandler;

    public void processSheet(String fileName) throws OpenXML4JException, IOException, SAXException {
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);

        InputStream sheet2 = r.getSheet("rId1");
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader(
                        "org.apache.xerces.parsers.SAXParser"
                );
        SheetHandler handler = new SheetHandler(sst);
        sheetHandler = handler;
        parser.setContentHandler(handler);
        return parser;
    }

    public static void main(String[] args) throws IOException, SAXException, OpenXML4JException, InterruptedException {
        String name = "/Users/hfzhou/Desktop/test-07.xlsx";
        Thread.sleep(5000);
        System.out.println(new Date());
        SaxModelExcel sme = new SaxModelExcel();
        sme.processSheet(name);
        System.out.println(new Date());
        Thread.sleep(5000);

        System.gc();

        List<ParsedRow> result = SheetHandler.sheetData;
//        createExcel(result);
//        create03Excel(result);
        create07ExcelWithXSSF(result);
    }

    public static void createExcel(List<ParsedRow> rows) throws IOException {
        System.out.println("开始生成excel," + new Date());
        SXSSFWorkbook wb = new SXSSFWorkbook();
        // 初始化EXCEL
        SXSSFSheet eachSheet = wb.createSheet();

        for (int i = 0; i < rows.size(); i++) {
            SXSSFRow row = eachSheet.createRow(i);
            int[] c = new int[1];
            rows.get(i).getCellMap().forEach((k, v) -> {
                SXSSFCell cell = row.createCell(c[0]++);
                cell.setCellValue(String.valueOf(v));
            });
        }
        FileOutputStream fos = new FileOutputStream("/Users/hfzhou/Desktop/generate-excel-test.xlsx");
        wb.write(fos);
        System.out.println("生成Excel结束," + new Date());
    }

    public static void create03Excel(List<ParsedRow> rows) throws IOException {
        System.out.println("开始生成excel," + new Date());
        HSSFWorkbook wb = new HSSFWorkbook();
        // 初始化EXCEL
        HSSFSheet eachSheet = wb.createSheet();

        for (int i = 0; i < rows.size(); i++) {
            HSSFRow row = eachSheet.createRow(i);
            int[] c = new int[1];
            rows.get(i).getCellMap().forEach((k, v) -> {
                HSSFCell cell = row.createCell(c[0]++);
                cell.setCellValue(String.valueOf(v));
            });
        }
        FileOutputStream fos = new FileOutputStream("/Users/hfzhou/Desktop/generate-excel-test-03.xlsx");
        wb.write(fos);
        System.out.println("生成Excel结束," + new Date());
    }

    public static void create07ExcelWithXSSF(List<ParsedRow> rows) throws IOException {
        System.out.println("开始生成excel," + new Date());
        XSSFWorkbook wb = new XSSFWorkbook();
        // 初始化EXCEL
        XSSFSheet eachSheet = wb.createSheet();

        for (int i = 0; i < rows.size(); i++) {
            XSSFRow row = eachSheet.createRow(i);
            int[] c = new int[1];
            rows.get(i).getCellMap().forEach((k, v) -> {
                Cell cell = row.createCell(c[0]++);
                cell.setCellValue(String.valueOf(v));
            });
        }
        FileOutputStream fos = new FileOutputStream("/Users/hfzhou/Desktop/generate-excel-test-03.xlsx");
        wb.write(fos);
        System.out.println("生成Excel结束," + new Date());
    }

}