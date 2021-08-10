package com.zhou.demo.excel.utils;

public class PositionUtil {

    public static int getCol(String currentCellIndex) {
        int col = 0;
        char[] currentIndex = currentCellIndex.replaceAll("[0-9]", "").toCharArray();
        for (int i = 0; i < currentIndex.length; i++) {
            col += (currentIndex[i] - '@') * Math.pow(26, (currentIndex.length - i - 1));
        }
        return col - 1;
    }

}
