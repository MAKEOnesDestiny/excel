package com.zhou.demo.excel.xlsx.tag;

import com.zhou.demo.excel.xlsx.AbstractXlsxTagHandler;
import com.zhou.demo.excel.xlsx.AnalysisContext;
import com.zhou.demo.excel.xlsx.CellData;
import com.zhou.demo.excel.xlsx.CellTypeEnum;
import com.zhou.demo.excel.xlsx.ProcessInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.ss.usermodel.DateUtil;

public class VTagHandler extends AbstractXlsxTagHandler {

    @Override
    public void character(char[] ch, int start, int length, AnalysisContext context) {
        String v = new String(ch, start, length);
        ProcessInfo pi = context.processInfo();
        CellData temp = pi.getTemp();
        CellTypeEnum cte = temp.getCellTypeEnum();
        switch (cte) {
            case ERROR:
            case EMPTY:
            case DIRECT_STRING:
                temp.setStringValue(v);
                break;
            case STRING:
                //replace
                String sv = (String) context.sharingStrings().get(Integer.valueOf(v));
                temp.setStringValue(sv);
                break;
            case NUMBER:
                Date date = DateUtil.getJavaDate(Double.valueOf(v), context.getConfig().isUse1904());
                SimpleDateFormat sdf = new SimpleDateFormat(temp.getDataFormat());
                temp.setStringValue(sdf.format(date));
                break;
            case BOOLEAN:
                //todo:
                break;
            default:
                throw new IllegalArgumentException("unknow type ==> " + cte);
        }
    }

}
