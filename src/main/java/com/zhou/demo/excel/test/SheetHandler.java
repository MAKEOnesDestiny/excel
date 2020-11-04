package com.zhou.demo.excel.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SheetHandler extends DefaultHandler {

    private static Pattern COLUMN_A = Pattern.compile("A([\\d]+)");

    private ParsedRow       rowData   = new ParsedRow();

    public static final List<ParsedRow> sheetData = new ArrayList<>();

    private SharedStringsTable sst;
    private String             lastContents;
    private boolean            nextIsString;
    private Short              index;

    public SheetHandler(SharedStringsTable sst) {
        this.sst = sst;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // c => cell
        if (name.equals("c")) {
            // Print the cell reference
            String        coordinate    = attributes.getValue("r");
            CellReference cellReference = new CellReference(coordinate);
            index = cellReference.getCol();
            Matcher matcher = COLUMN_A.matcher(coordinate);

            // 第一行单独解析行号
            if (matcher.matches() && rowData.getCellMap().isEmpty()) {
                rowData.setRowIndex(Integer.valueOf(matcher.group(1)) - 1);
            }

            if (matcher.matches() && !rowData.getCellMap().isEmpty()) {
                sheetData.add(rowData);
                rowData = new ParsedRow();
                rowData.setRowIndex(Integer.valueOf(matcher.group(1)) - 1);
            }

            // Figure out if the value is an index in the SST
            String cellType = attributes.getValue("t");
            if (cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
        }
        // Clear contents cache
        lastContents = "";
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        // Process the last contents as required.
        // Do now, as characters() may be called more than once
        if (nextIsString) {
            int idx = Integer.parseInt(lastContents);
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            nextIsString = false;
        }

        // v => contents of a cell
        // Output after we've seen the string contents
        if (name.equals("v")) {
            rowData.getCellMap().put(index.intValue(), lastContents);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        if (!rowData.getCellMap().isEmpty()) {
            sheetData.add(rowData);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastContents += new String(ch, start, length);
    }


}
