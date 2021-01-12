package com.haphest.a3dtracking.utils.converter;

/**
 * Created by Administrator on 2019/5/7/007.
 */

public class PackUtil {
    public static boolean isBigPack(String r, String p) {
        if (r.substring(0, p.length()).equals(p)) {
            return true;
        }
        return false;
    }
}
