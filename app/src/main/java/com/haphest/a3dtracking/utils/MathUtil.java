package com.haphest.a3dtracking.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {


    public static boolean isDigit(Object o) {
        if (o instanceof Integer || o instanceof Long || o instanceof Float || o instanceof Double) {
            return true;
        }
        return false;
    }

    public static boolean isDigit(String o) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(o);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
