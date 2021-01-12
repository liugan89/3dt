package com.takumi.wms.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.takumi.wms.R;

/**
 * 简单的显示加载中的Dialog
 */
public class LoadingDialog extends ProgressDialog {

    private static int count = 0;
    private static LoadingDialog ld;

    public LoadingDialog(Context context) {
        this(context, R.style.CustomDialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.loading);//loading的xml文件
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        setCancelable(false);
    }

    public static void dismissDialog() {
        if (--count <= 0 && ld != null) {
            ld.dismiss();
            ld = null;
        }
    }

    public static void showDialog(Context context) {
        count++;
        if (ld == null || !ld.isShowing()) {
            ld = new LoadingDialog(context);
            ld.show();
        }
    }


}
