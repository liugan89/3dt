package com.takumi.wms.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.JsonParser;
import com.takumi.wms.BuildConfig;
import com.takumi.wms.R;
import com.takumi.wms.base.BaseActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.DistanceUtil;
import com.takumi.wms.utils.ToastUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements AMapLocationListener {

    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.btn_test)
    Button btn_test;

    private SharedPreferences sp;

    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private double longitude;
    private double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar(R.color.base_blue, false);
        if (!BuildConfig.isDebug) {
            et_account.setText("");
            et_password.setText("");
        } else {
            et_account.setText("zhaofeng");
            et_password.setText("86092898zf");
            /*et_account.setText("admin");
            et_password.setText("qwe123");*/
        }
        sp = getSharedPreferences(Constant.SPConfig.SPName, MODE_PRIVATE);
        ckeckLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetUtil.getInstance().setToken(null);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_login, R.id.btn_test})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_account.getText().toString())) {
                    ToastUtil.showToast(this, "账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    ToastUtil.showToast(this, "密码不能为空");
                    return;
                }
                if (longitude == 0 || latitude == 0) {
                    ToastUtil.showToast(this, "等待定位成功");
                    return;
                }
                //String id = "api/iam/oauth2/token";
                String id = "api/login";
                Map<String, String> m = new HashMap<>();
                m.put("username", et_account.getText().toString());
                m.put("password", et_password.getText().toString());
                //if (BuildConfig.isDebug) {
                //m.put("aud", "wms");
               /* } else {
                    m.put("client_id", "malls");
                    m.put("grant_type", "password");
                }*/
                NetUtil.getInstance().NoBody_NoResponse(this, m, id, NetService.login, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                    @Override
                    public void onSuccess(Response<ResponseBody> result) {
                        try {
                            double l1 = Double.valueOf(sp.getString(Constant.SPConfig.WarehouseLatitude, "0"));
                            double l2 = Double.valueOf(sp.getString(Constant.SPConfig.WarehouseLongitude, "0"));
                            int scope = sp.getInt(Constant.SPConfig.WarehouseScope, 0);
                            boolean isLimit = sp.getBoolean(Constant.SPConfig.isLimit, false);
                            Warehouse.getHouse().setLimit(isLimit);
                            Double distance = DistanceUtil.distance(latitude, longitude, l1, l2);
                            if (distance < scope) {
                                Warehouse.getHouse().setWarehouseId(sp.getString(Constant.SPConfig.WarehouseID, null));
                            } else {
                                Warehouse.getHouse().setWarehouseId("");
                            }
                            String s = result.body().string();
                            s = new JsonParser().parse(s).getAsJsonObject().get("token").getAsString();
                            NetUtil.getInstance().setToken(s);
                            com.takumi.wms.net2.NetUtil.getInstance().setToken(s);
                            Operator.getOperator().setAccount(et_account.getText().toString());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            if (Warehouse.getHouse() != null && !TextUtils.isEmpty(Warehouse.getHouse().getWarehouseId())) {
                                JPushInterface.init(getApplicationContext());
                                BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
                                builder.statusBarDrawable = R.mipmap.app_logo;
                                JPushInterface.setDefaultPushNotificationBuilder(builder);

                                String tag = Warehouse.getHouse().getWarehouseId();
                                if (tag != null) {
                                    Set<String> set = new HashSet<>();
                                    set.add(tag);
                                    JPushInterface.setTags(getApplicationContext(), 1, set);
                                }
                            }
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public boolean onError(String msg) {
                        return false;
                    }
                });
                break;
        }
    }

    private void ckeckLocation() {
        //if (longitude == 0 || latitude == 0) return;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ToastUtil.showToast(this, "请打开定位权限");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作
                ckeckLocation();
            } else {
                //用户不同意，向用户展示该权限作用
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this).setMessage("权限申请").setPositiveButton("OK", (dialog1, which) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100)).setNegativeButton("Cancel", null).create().show();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                longitude = amapLocation.getLongitude();
                latitude = amapLocation.getLatitude();
                mlocationClient.stopLocation();
                mlocationClient.onDestroy();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
            }
        }
    }
}
