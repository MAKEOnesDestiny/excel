package com.zhou.demo.excel.xlsx;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//共享字符串处理器
public class SharingStringsHandler extends DefaultHandler {

    public static final String T_TAG = "t";
    public static final String SI_TAG = "si";
    public static final String RPH_TAG = "rPh";

    public StringBuilder currentData; //代表si里面的内容
    public StringBuilder currentElement; //代表t标签里面的内容
    public boolean isTag = false;

    public Map<Integer, String> cache = new HashMap<>();
    public Integer index = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (T_TAG.equals(qName)) {
            isTag = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (T_TAG.equals(qName)) {
            cache.put(index++, currentElement.toString());
            isTag = false;
            currentElement = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isTag) {
            if (currentElement == null) {
                currentElement = new StringBuilder();
            }
            currentElement.append(new String(ch, start, length));
        }
    }
}
