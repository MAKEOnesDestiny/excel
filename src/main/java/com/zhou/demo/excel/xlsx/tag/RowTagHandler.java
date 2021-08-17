package com.zhou.demo.excel.xlsx.tag;

import com.zhou.demo.excel.annotation.ColumnWrap;
import com.zhou.demo.excel.utils.TransferUtil;
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
    public void endElement(String qName, AnalysisContext context) throws Exception {
        //process row data
        Map<Integer, CellData> map = context.processInfo().getMap();
        //        context.tempResults().add(map);
        context.processInfo().resetMap();

        if (!context.processInfo().getHeadResolved()) {
            Map<Integer, ColumnWrap> headName2Index = TransferUtil
                    .resolveHead(map, context.analysisInfo().getExcelBeanMetaData());
            context.processInfo().setHeadName2Index(headName2Index);
            context.processInfo().setHeadResolved(true);
        } else {
            TransferUtil.endElement(map, context.processInfo().getHeadName2Index(), context);
        }

    }
}
