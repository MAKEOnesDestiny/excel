package com.zhou.demo.excel.xlsx;

import org.xml.sax.Attributes;

public abstract class AbstractXlsxTagHandler implements XlsxTagHandler {


    @Override
    public void startElement(String qName, Attributes attributes, AnalysisContext context) {

    }

    @Override
    public void endElement(String qName, AnalysisContext context) throws Exception{

    }

    @Override
    public void character(char[] ch, int start, int length, AnalysisContext context) {

    }
}
