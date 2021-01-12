package com.takumi.wms.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionUtil {
    public static void onPermissionRequests(String permission, Activity context) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_CONTACTS)) {
                //权限已有
            } else {
                //没有权限，申请一下
                ActivityCompat.requestPermissions(context, new String[]{permission}, 1);
            }
        } else {
            Log.d("MainActivity", "2" + ContextCompat.checkSelfPermission(context, permission));
        }
    }
}
