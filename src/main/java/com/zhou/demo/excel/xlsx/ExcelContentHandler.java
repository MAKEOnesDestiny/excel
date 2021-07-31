package com.zhou.demo.excel.xlsx;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//根据不同的标签发送给不同的标签处理器XlsxTagHandler
public class ExcelContentHandler extends DefaultHandler {

    private static final Map<String, XlsxTagHandler> HANDLER_MAP = new HashMap<String, XlsxTagHandler>() {{
        put("c", null);
        put("v", null);
        put("row",null);
    }};

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }
}
