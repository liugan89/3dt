package com.takumi.wms.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.takumi.wms.R;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.Locator;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.FileUtil;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.utils.RxUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BottomActivity {
    @BindView(R.id.rl_in)
    LinearLayout btn_into;
    @BindView(R.id.rl_out)
    LinearLayout btn_out;
    @BindView(R.id.rl_movement)
    LinearLayout btn_move;
    @BindView(R.id.rl_switch)
    LinearLayout btn_switch;
    @BindView(R.id.rl_inventory)
    LinearLayout btn_check;
    @BindView(R.id.rl_shipment)
    LinearLayout btn_delivery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar(R.color.white, true);
        setTitle("Takumi  WMS");
        setIvBackVisibility(false);
        isBottomShow(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getLocators();
    }

    private void getLocators() {
        if (SingletonCache.locators == null) {
            Map<String, String> map = new HashMap<>();
            map.put("WarehouseId", Warehouse.getHouse().getWarehouseId());
            String id = NetUtil.makeId(true, NetService.Locators);
            NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getLocators, new NetUtil.MyCallBack<List<Locator>>() {
                @Override
                public void onSuccess(List<Locator> result) {
                    SingletonCache.locators = result;
                    System.out.println(1);
                }

                @Override
                public boolean onError(String msg) {
                    return true;
                }
            });
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtil.doInIO(new RxUtil.RxDo() {
            @Override
            public void doSomething() {
                FileUtil.deleteFile(FileUtil.getImageDir());
                FileUtil.deleteFile(FileUtil.getTempDir());
            }
        });

    }

    @OnClick({R.id.rl_in, R.id.rl_out, R.id.rl_movement, R.id.rl_switch, R.id.rl_inventory, R.id.rl_shipment
            , R.id.rl_ordership, R.id.rl_notice_in, R.id.rl_notice_out, R.id.rl_production})
    void click(View view) {

        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_in://其它入库
                if (hasWarehouse()) {
                    intent = new Intent(this, UndoneActivity.class);
                    intent.putExtra(Constant.Flags.MainFlag, Function.In);
                    startActivity(intent);
                }
                break;
            case R.id.rl_out://其它出库
                if (hasWarehouse()) {
                    intent = new Intent(this, UndoneActivity.class);
                    intent.putExtra(Constant.Flags.MainFlag, Function.Out);
                    startActivity(intent);
                }
                break;
            case R.id.rl_movement://移动单
                if (hasWarehouse()) {
                    intent = new Intent(this, UndoneActivity.class);
                    intent.putExtra(Constant.Flags.MainFlag, Function.Movement);
                    startActivity(intent);
                }
                break;
            case R.id.rl_switch://切换仓库
                intent = new Intent(this, WarehouseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constant.Flags.MainFlag, Function.SwitchWarehouse);
                startActivity(intent);
                break;
            case R.id.rl_inventory://盘点，暂时不使用
                /*intent = new Intent(this, UndoneActivity.class);
                intent.putExtra(Constant.Flags.MainFlag, Function.PhysicalInventory);
                startActivity(intent);*/
                ToastUtil.showToast(this, "暂未开放");
                break;
            case R.id.rl_shipment://装运单入库
                if (hasWarehouse()) {
                    intent = new Intent(this, ShipmentInActivity.class);
                    intent.putExtra(Constant.Flags.MainFlag, Function.IncomingShipment);
                    startActivity(intent);
                }
                break;
            case R.id.rl_ordership://装运单出库
                if (hasWarehouse()) {
                    intent = new Intent(this, ShipmentOutActivity.class);
                    intent.putExtra(Constant.Flags.MainFlag, Function.OutgoingShipment);
                    startActivity(intent);
                }
                break;
            case R.id.rl_notice_in://看起来是通知单相关，事实上是盘点查询
               /* if (hasWarehouse()) {
                    intent = new Intent(this, InventoryQueryActivity.class);
                    intent.putExtra(Constant.Flags.MainFlag, Function.InventoryItemQuery);
                    startActivity(intent);
                }*/
                ToastUtil.showToast(this, "暂未开放");
                break;
            case R.id.rl_notice_out://出库通知单
                if (hasWarehouse()) {
                    intent = new Intent(this, NoticeListActivity.class);
                    intent.putExtra(Constant.Flags.MainFlag, Function.OutNotice);
                    startActivity(intent);
                }
                break;
            case R.id.rl_production:
                if (hasWarehouse()) {
                    intent = new Intent(this, ProductionListActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    private boolean hasWarehouse() {
        if (Warehouse.getHouse() != null && TextUtils.isEmpty(Warehouse.getHouse().getWarehouseId())) {
            ToastUtil.showToast(this, "请先选择仓库");
            return false;
        }
        return true;
    }


}
