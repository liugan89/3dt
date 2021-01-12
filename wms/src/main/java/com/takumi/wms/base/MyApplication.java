package com.takumi.wms.base;

import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.obm.mylibrary.ScanPrint7s;

public class MyApplication extends MultiDexApplication {

    public static boolean is7S;

    @Override
    public void onCreate() {
        super.onCreate();
        is7S = (Build.MODEL.equals("8610") && Build.VERSION.RELEASE.equals("4.4.2") && Build.MANUFACTURER.equals("SW")
                || (Build.MODEL.equals("msm8909") || Build.MODEL.equals("msm8909_512")) && Build.VERSION.RELEASE.equals("5.1.1") && Build.MANUFACTURER.equals("unknown"));
//        boolean is7S =( Build.MODEL.equals("8610") && Build.VERSION.RELEASE.equals("4.4.2") && Build.MANUFACTURER.equals("SW")
//                || Build.MODEL.equals("msm8909_512") && Build.VERSION.RELEASE.equals("5.1.1") && Build.MANUFACTURER.equals("unknown"));
        if (is7S) {
            ScanPrint7s.init(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
