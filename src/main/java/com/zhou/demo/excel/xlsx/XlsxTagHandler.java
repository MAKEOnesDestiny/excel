package com.zhou.demo.excel.xlsx;

import org.xml.sax.Attributes;

/**
 * Handler used to process specific tag.
 */
public interface XlsxTagHandler {

    void startElement(String qName, Attributes attributes);

    void endElement(String qName);

    void character(char ch[], int start, int length);

}
