package com.zhou.demo.excel.xlsx.tag;

import com.zhou.demo.excel.utils.PositionUtil;
import com.zhou.demo.excel.xlsx.AbstractXlsxTagHandler;
import com.zhou.demo.excel.xlsx.AnalysisContext;
import com.zhou.demo.excel.xlsx.CellData;
import com.zhou.demo.excel.xlsx.CellTypeEnum;
import com.zhou.demo.excel.xlsx.POIXmlConstant;
import com.zhou.demo.excel.xlsx.ProcessInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.xml.sax.Attributes;

public class CTagHandler extends AbstractXlsxTagHandler {

    @Override
    public void startElement(String qName, Attributes attributes, AnalysisContext context) {
        String columnIndex = attributes.getValue(POIXmlConstant.ATTRIBUTE_R);
        context.processInfo().setColumnIndex(PositionUtil.getCol(columnIndex));

        String rawType = attributes.getValue(POIXmlConstant.ATTRIBUTE_T);
        CellData cellData = new CellData(context.processInfo().getRowIndex(), context.processInfo().getColumnIndex(),
                                         CellTypeEnum.lookCellTypeEnum(rawType));

        String dateFormatIndex = attributes.getValue(POIXmlConstant.ATTRIBUTE_S);
        Integer dateFormatIndexInteger;
        if (StringUtils.isBlank(dateFormatIndex)) {
            dateFormatIndexInteger = 0;
        } else {
            dateFormatIndexInteger = Integer.valueOf(dateFormatIndex);
        }
        XSSFCellStyle xssfCellStyle = context.stylesTable().getStyleAt(dateFormatIndexInteger);
        cellData.setDataFormat(xssfCellStyle.getDataFormatString());

        context.processInfo().setTemp(cellData);
    }

    @Override
    public void endElement(String qName, AnalysisContext context) {
        ProcessInfo pi = context.processInfo();
        CellData temp = pi.getTemp();
        pi.getMap().put(pi.getColumnIndex(), temp);
    }

}
