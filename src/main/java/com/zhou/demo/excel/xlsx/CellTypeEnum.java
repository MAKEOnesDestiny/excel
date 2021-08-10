package com.zhou.demo.excel.xlsx;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public enum CellTypeEnum {

    //needed to be seek real value in sharing table
    STRING,
    DIRECT_STRING,
    NUMBER,
    BOOLEAN,
    EMPTY,
    ERROR,
    ;

    private static final Map<String, CellTypeEnum> LOOK_UP_TABLE = new HashMap<String, CellTypeEnum>() {{
        put("s", STRING);
        put("str", DIRECT_STRING);
        put("inline", STRING);
        put("e", ERROR);
        put("b", BOOLEAN);
        put("n", NUMBER);
        //no empty
    }};

    public static CellTypeEnum lookCellTypeEnum(String rawType) {
        if (StringUtils.isBlank(rawType)) {
            return EMPTY;
        }
        if (!LOOK_UP_TABLE.containsKey(rawType)) {
            throw new IllegalArgumentException("Wrong rawType ==> " + rawType);
        }
        return LOOK_UP_TABLE.get(rawType);
    }

}
