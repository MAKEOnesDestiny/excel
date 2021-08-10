package com.zhou.demo.excel.xlsx;

import com.zhou.demo.excel.xlsx.tag.CTagHandler;
import com.zhou.demo.excel.xlsx.tag.RowTagHandler;
import com.zhou.demo.excel.xlsx.tag.VTagHandler;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//根据不同的标签发送给不同的标签处理器XlsxTagHandler
public class ExcelContentHandler extends DefaultHandler {

    private static final Map<String, XlsxTagHandler> HANDLER_MAP = new HashMap<String, XlsxTagHandler>() {{
        put("c", new CTagHandler());
        put("v", new VTagHandler());
        put("row", new RowTagHandler());
    }};

    private AnalysisContext analysisContext;

    private Deque<String> tagDeque;

    public ExcelContentHandler(AnalysisContext analysisContext) {
        this.analysisContext = analysisContext;
        this.tagDeque = new LinkedList<>();
        this.analysisContext.setProcessInfo(new ProcessInfo());
    }

    public AnalysisContext getAnalysisContext() {
        return analysisContext;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        XlsxTagHandler handler = HANDLER_MAP.get(qName);
        tagDeque.push(qName); //push tag name
        if (handler != null) {
            handler.startElement(qName, attributes, analysisContext);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        XlsxTagHandler handler = HANDLER_MAP.get(qName);
        tagDeque.pop();
        if (handler != null) {
            handler.endElement(qName, analysisContext);
        }
    }

    //need a stack to record tag info
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String currentTag = tagDeque.getFirst();
        XlsxTagHandler handler = HANDLER_MAP.get(currentTag);
        if (handler != null) {
            handler.character(ch, start, length, analysisContext);
        }
    }
}
