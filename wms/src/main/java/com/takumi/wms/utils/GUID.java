package com.takumi.wms.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * UUID相关工具类
 */
public class GUID {

    private static Gson gson;

    private static void initGson() {
        if (gson == null) {
            gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        }
    }

    public static String getUUID(Object o) {
        initGson();
        String s = gson.toJson(o);
        return md5(s);
    }

    @NonNull
    private static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
