package com.takumi.wms.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MessageDialog {

    public static AlertDialog getDialog(Context context, String title, String content, DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context).setTitle(title).setMessage(content).setNegativeButton("取消", listener).setPositiveButton("确认", listener).create();
    }

    public static AlertDialog getCancelDialog(Context context, String title, String content, DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context).setTitle(title).setMessage(content).setNegativeButton("取消", listener).create();
    }

}
