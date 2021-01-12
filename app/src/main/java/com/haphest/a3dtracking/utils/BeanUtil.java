package com.haphest.a3dtracking.utils;

import java.util.List;

public class BeanUtil {

    public static boolean isNull(List list) {
        if (list == null || list.isEmpty() || list.get(0) == null) {
            return true;
        }
        return false;
    }
}
