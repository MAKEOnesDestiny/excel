package com.zhou.demo.excel.factory.impl;

import com.zhou.demo.excel.factory.ExcelFactory;
import com.zhou.demo.excel.factory.ExcelFactoryConfigInner;
import com.zhou.demo.excel.xlsx.AnalysisContext;
import com.zhou.demo.excel.xlsx.ExcelContentHandler;
import com.zhou.demo.excel.xlsx.RawContextInfo;
import com.zhou.demo.excel.xlsx.SharingStringsHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.springframework.util.CollectionUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

@SuppressWarnings("all")
public abstract class SaxExcelFactory implements ExcelFactory {

    private AnalysisContext analysisContext;

    public SaxExcelFactory() {
    }

    protected void setAnalysisContext(AnalysisContext analysisContext) {
        this.analysisContext = analysisContext;
    }

    public AnalysisContext getAnalysisContext() {
        return analysisContext;
    }

    @Override
    public ExcelFactoryConfigInner getConfig() {
        return analysisContext.getConfig();
    }

    //todo: 考虑将流保存为本地文件系统，然后对本地文件系统进行处理，可以节省内存
    @Override
    public <T> List<T> toBean(InputStream inputStream, Class<T> targetClass) throws Exception {
        AnalysisContext analysisContext = getAnalysisContext();
        analysisContext.initContext(new RawContextInfo(targetClass, null, inputStream));

        OPCPackage pkg = OPCPackage.open(inputStream);
        ArrayList<PackagePart> parts = pkg
                .getPartsByContentType(XSSFRelation.SHARED_STRINGS.getContentType());//获取共享字符串部分
        PackagePart sharingStringPart = null;
        if (!CollectionUtils.isEmpty(parts)) {
            sharingStringPart = parts.get(0);
        }
        SharingStringsHandler sharingStringsHandler = new SharingStringsHandler();
        parse(sharingStringPart.getInputStream(), sharingStringsHandler);

        XSSFReader xssfReader = new XSSFReader(pkg);
        boolean use1904 = use1904(xssfReader);
        analysisContext.getConfig().setUse1904(use1904);

        StylesTable stylesTable = xssfReader.getStylesTable();
        analysisContext.initContext(sharingStringsHandler.cache, stylesTable);

        ExcelContentHandler contentHandler = new ExcelContentHandler(analysisContext);
        SheetIterator sheetIterator = (SheetIterator) xssfReader.getSheetsData();
        InputStream sheetIS = analysisContext.analysisInfo().getSheetInputStream(sheetIterator);
        parse(sheetIS, contentHandler);
        return contentHandler.getAnalysisContext().results();
    }


    //用于判断是否使用1904时间格式
    protected boolean use1904(XSSFReader reader) throws IOException, InvalidFormatException, XmlException {
        InputStream workbookXml = reader.getWorkbookData();
        WorkbookDocument ctWorkbook = WorkbookDocument.Factory.parse(workbookXml);
        CTWorkbook wb = ctWorkbook.getWorkbook();
        CTWorkbookPr prefix = wb.getWorkbookPr();
        if (prefix != null && prefix.getDate1904()) {
            return true;
        } else {
            return false;
        }
    }

    protected void parse(InputStream is, ContentHandler handler) throws Exception {
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


    @Override
    public void setConfig(ExcelFactoryConfigInner excelFactoryConfigInner) {

    }
}
