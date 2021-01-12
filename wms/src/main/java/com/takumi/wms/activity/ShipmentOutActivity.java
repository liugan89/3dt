package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.takumi.wms.R;
import com.takumi.wms.adapter.OnItemClickListener;
import com.takumi.wms.adapter.ShipmentDocumentAdapter;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

public class ShipmentOutActivity extends ShipBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bt_add_shipment.setText("添加出库单");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShipmentUndoneDocs();
    }

    private void getShipmentUndoneDocs() {
        Map<String, String> map = new HashMap<>();
        map.put("statusId", "SHIPMENT_INPUT");
        map.put("shipmentTypeId", "SALES_SHIPMENT");
        map.put("DestinationFacilityId", Warehouse.getHouse().getWarehouseId());
        String id = NetUtil.makeId(true, NetService.Shipments);
        NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getShipments, new NetUtil.MyCallBack<List<ShipmentDetail>>() {
            @Override
            public void onSuccess(List<ShipmentDetail> result) {
                ShipmentOutActivity.this.shipmentDetails = result;
                initRV(shipmentDetails);
                if (result == null || result.isEmpty()) {
                    ToastUtil.showToast(ShipmentOutActivity.this, "无未完成" + fun.getName() + "单");
                    return;
                }
            }

            @Override
            public boolean onError(String msg) {
                initRV(null);
                System.out.println(msg);
                return false;
            }
        });
    }


    @OnClick({R.id.bt_add_shipment})
    void click(View view) {
        switch (view.getId()) {
            case R.id.bt_add_shipment:
                Intent i1 = new Intent(ShipmentOutActivity.this, RelateNoticeActivity.class);
                i1.putExtra(Constant.Flags.MainFlag, fun);
                startActivity(i1);
                break;
        }
    }

    @Override
    protected void initRV(List<ShipmentDetail> os) {
        ShipmentDocumentAdapter adapter = new ShipmentDocumentAdapter(os, fun);
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                o = os.get(position);
                goOutNext(o);
            }
        });
        adapter.setRelationClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(ShipmentOutActivity.this, RelateNoticeActivity.class);
                i.putExtra(Constant.Flags.Param1, os.get(position));
                i.putExtra(Constant.Flags.MainFlag, fun);
                startActivity(i);
            }
        });
        rv_shipment_undone.setAdapter(adapter);
    }

    @Override
    protected void findDocs(String id) {
        if (TextUtils.isEmpty(id)) {
            ToastUtil.showToast(this, "请输入单号");
            return;
        }
        if (shipmentDetails == null || shipmentDetails.isEmpty()) {
            ToastUtil.showToast(this, "无装运单");
            return;
        }
        for (ShipmentDetail o : shipmentDetails) {
            if (id.equals(o.getShipmentId())) {
                this.o = o;
                goOutNext(o);
                return;
            }
        }
        ToastUtil.showToast(this, "未找到此装运单");
    }
}
