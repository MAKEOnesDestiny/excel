package com.zhou.demo.excel.test;

import com.zhou.demo.excel.xlsx.ExcelContentHandler;
import com.zhou.demo.excel.xlsx.SharingStringsHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.springframework.util.CollectionUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

@SuppressWarnings("all")
public class Excel07Test {

    public static OPCPackage pkg;

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/hfzhou/Desktop/excel内存测试/test-07-small.xlsx");
        InputStream is = resolve(file);

        SharingStringsHandler sharingHandler = new SharingStringsHandler();
        parse(is, sharingHandler);
        System.out.println("parse finished");

        Map<Integer, InputStream> sheetMap = resolveCore(pkg);
        sheetMap.entrySet().forEach(t -> {
            ExcelContentHandler contentHandler = new ExcelContentHandler();
            try {
                parse(t.getValue(), contentHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void parse(InputStream is, ContentHandler handler) throws Exception {
        InputSource source = new InputSource(is);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(handler);
        reader.parse(source);
    }

    public static InputStream resolve(File file) throws IOException, InvalidFormatException {
        OPCPackage pkg = OPCPackage.open(file); //
        Excel07Test.pkg = pkg;
        ArrayList<PackagePart> parts = pkg
                .getPartsByContentType(XSSFRelation.SHARED_STRINGS.getContentType());//获取共享字符串部分
        if (!CollectionUtils.isEmpty(parts)) {
            PackagePart part = parts.get(0);
            return part.getInputStream();
        }
        return null;
    }

    public static Map<Integer, InputStream> resolveCore(OPCPackage pkg) throws IOException, OpenXML4JException {
        XSSFReader reader = new XSSFReader(pkg);
        //todo: 解析window date format
        //todo: 解析style
        Map<Integer, InputStream> sheetMap = new HashMap<>();
        Integer index = 0;
        XSSFReader.SheetIterator ite = (XSSFReader.SheetIterator) reader.getSheetsData();
        while (ite.hasNext()) {
            InputStream inputStream = ite.next();
            sheetMap.put(index++, inputStream);
        }
        return sheetMap;
    }


}
