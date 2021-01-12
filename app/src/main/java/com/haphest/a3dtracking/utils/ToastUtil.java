package com.haphest.a3dtracking.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast相关工具类
 */
public class ToastUtil {

    private static Toast toast;


    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        }
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(content);
        toast.show();
    }
}
