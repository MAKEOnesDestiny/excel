package com.zhou.demo.excel.xlsx.tag;

import com.zhou.demo.excel.xlsx.AbstractXlsxTagHandler;
import com.zhou.demo.excel.xlsx.AnalysisContext;
import com.zhou.demo.excel.xlsx.CellData;
import com.zhou.demo.excel.xlsx.POIXmlConstant;
import java.util.Map;
import org.xml.sax.Attributes;

public class RowTagHandler extends AbstractXlsxTagHandler {

    @Override
    public void startElement(String qName, Attributes attributes, AnalysisContext context) {
        String rValue = attributes.getValue(POIXmlConstant.ATTRIBUTE_R);
        context.processInfo().setRowIndex(Integer.valueOf(rValue));
    }

    @Override
    public void endElement(String qName, AnalysisContext context) {
        //process row data
        Map<Integer, CellData> map = context.processInfo().getMap();
        CellData cellData = context.processInfo().getTemp();
        map.put(context.processInfo().getColumnIndex(), cellData);
    }
}
