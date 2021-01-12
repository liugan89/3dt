package com.haphest.a3dtracking.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {
    public static void onPermissionRequests(String permission, AppCompatActivity context) {
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
