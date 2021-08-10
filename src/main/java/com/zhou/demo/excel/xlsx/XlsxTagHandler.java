package com.zhou.demo.excel.xlsx;

import org.xml.sax.Attributes;

/**
 * Handler used to process specific tag.
 * All handlers need to be state-less.
 */
public interface XlsxTagHandler {

    void startElement(String qName, Attributes attributes, AnalysisContext context);

    void endElement(String qName, AnalysisContext context);

    void character(char ch[], int start, int length, AnalysisContext context);

}
