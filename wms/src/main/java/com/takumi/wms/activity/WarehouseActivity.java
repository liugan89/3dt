package com.takumi.wms.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.takumi.wms.BuildConfig;
import com.takumi.wms.R;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.DistanceUtil;
import com.takumi.wms.utils.ToastUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class WarehouseActivity extends BottomActivity implements AMapLocationListener {
    @BindView(R.id.warehouese_list)
    RecyclerView warehouese_list;

    //private List<Warehouse> houses;
    private SharedPreferences sp;
    private List<Warehouse> ws;

    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(Constant.SPConfig.SPName, MODE_PRIVATE);
        initImmersionBar(R.color.white, true);
        setTitle("切换仓库");
        ckeckLocation();
        getWarehouse();
    }

    private void getWarehouse() {
        String id = NetUtil.makeId(true, "queries/permission/warehouses");
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", Operator.getOperator().getAccount());
        NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getWarehouses, new NetUtil.MyCallBack<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> result) {
                ws = result;
                LinearLayoutManager llm = new LinearLayoutManager(WarehouseActivity.this);
                warehouese_list.setLayoutManager(llm);
                WarehouseAdapter adapter = new WarehouseAdapter();
                warehouese_list.setAdapter(adapter);
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
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
    protected int getContentViewID() {
        return R.layout.activity_warehouse;
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

    class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(WarehouseActivity.this).inflate(R.layout.warehouse_item, null);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.warehouse_item.setText(ws.get(position).getWarehouseId());
            holder.warehouse_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (latitude == 0 || longitude == 0) {
                        ToastUtil.showToast(WarehouseActivity.this, "等待定位成功");
                        return;
                    }
                    try {
                        boolean isDebug = BuildConfig.isDebug;//debug版链接杨总服务器，没有经纬度的设置
                        if (!isDebug) {
                            Double l1 = Double.valueOf(ws.get(position).getLongitude());
                            Double l2 = Double.valueOf(ws.get(position).getLatitude());


                            Double distance = DistanceUtil.distance(latitude, longitude, l2, l1);
                            if (distance > ws.get(position).getScope()) {
                                ToastUtil.showToast(WarehouseActivity.this, "手机位置不在指定范围内");
                                finish();
                                return;
                            }
                        }

                        Warehouse.setHouse(ws.get(position));
                        if (isDebug) {
                            sp.edit().putString(Constant.SPConfig.WarehouseID, Warehouse.getHouse().getWarehouseId())
                                    .putInt(Constant.SPConfig.WarehouseScope, ws.get(position).getScope())
                                    .putBoolean(Constant.SPConfig.isLimit, ws.get(position).isLimit())
                                    .commit();
                        }else {
                            sp.edit().putString(Constant.SPConfig.WarehouseID, Warehouse.getHouse().getWarehouseId())
                                    .putString(Constant.SPConfig.WarehouseLongitude, ws.get(position).getLongitude())
                                    .putString(Constant.SPConfig.WarehouseLatitude, ws.get(position).getLatitude())
                                    .putInt(Constant.SPConfig.WarehouseScope, ws.get(position).getScope())
                                    .putBoolean(Constant.SPConfig.isLimit, ws.get(position).isLimit())
                                    .commit();
                        }

                        if (Warehouse.getHouse() != null) {
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
                        SingletonCache.locators = null;
                        finish();
                    } catch (Exception e) {
                        ToastUtil.showToast(WarehouseActivity.this, "选择仓库出错");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return ws == null ? 0 : ws.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView warehouse_item;

            public ViewHolder(View itemView) {
                super(itemView);
                warehouse_item = itemView.findViewById(R.id.warehouse_item);
            }
        }
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
}
