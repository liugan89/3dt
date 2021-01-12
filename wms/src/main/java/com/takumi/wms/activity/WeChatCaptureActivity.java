package com.takumi.wms.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceView;

import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.takumi.wms.R;

import java.io.UnsupportedEncodingException;

/**
 * 模仿微信的扫描界面
 */
public class WeChatCaptureActivity extends BaseCaptureActivity {

    private static final String TAG = WeChatCaptureActivity.class.getSimpleName();

    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;
    public ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar();
        setContentView(R.layout.activity_wechat_capture);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);
        //PermissionUtil.onPermissionRequests(Manifest.permission.CAMERA, this);
    }

    //沉浸式状态栏初始化
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);

        //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，
        // 如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.statusBarDarkFont(true, 0.2f);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();  //在BaseActivity里销毁
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
    }


    @Override
    public SurfaceView getSurfaceView() {
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }

    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        try {
            playBeepSoundAndVibrate(true, false);
            Intent i = new Intent();
            String s = new String(rawResult.getText().getBytes(), "utf-8");
            i.putExtra("number", s.trim());
            i.putExtra("type", "camera_scan");
            setResult(RESULT_OK, i);
            finish();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        对此次扫描结果不满意可以调用
//        reScan();
    }

}
