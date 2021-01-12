package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.takumi.wms.R;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InOutNotice;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.OrderShipGroup;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.shipment.OrderIdShipGroupSeqIdPair;
import com.takumi.wms.model.dto.shipment.OrderShipGroupServiceCommandDtos;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.GUID;

import java.io.IOException;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class NoticeLineList2Activity extends NoticeLineListActivity {
    private InOutNotice notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notice = getIntent().getParcelableExtra(Constant.Flags.Param1);
        et_notice_id.setTextNoWatch(notice.getInOutNoticeId());
        et_notice_id.setEnabled(false);
        if (notice != null) {
            ll_contract_number.setVisibility(View.GONE);
            bt_notice_complete.setText("创建装运单");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (notice != null) {
            String id = NetUtil.makeId(true, NetService.queries, NetService.noticeDocument, "noticeInf", notice.getInOutNoticeId());
            NetUtil.getInstance().NoBody_Response(this, null, id, NetService.getNoticeInf, new NetUtil.MyCallBack<OrderShipGroup>() {
                @Override
                public void onSuccess(OrderShipGroup result) {
                    order = result;
                    bt_notice_complete.setText(result.getShipmentId() == null ? "创建装运单" : "装运单号：" + result.getShipmentId());
                    tv_notice_shipmentid.setText(result.getShipmentId() == null ? "" : result.getShipmentId());
                    rv_notice_list.setAdapter(new OrderAdapter(result));
                    tv_inf_name.setText("司机姓名：" + (order.getName() == null ? "" : order.getName()));
                    tv_inf_phone.setText("电话：" + (order.getPhoneNo() == null ? "" : order.getPhoneNo()));
                    tv_inf_number.setText("车牌号：" + (order.getPlateNumber() == null ? "" : order.getPlateNumber()));
                    tv_inf_instructions.setText("备注：" + (order.getInstructions() == null ? "" : order.getInstructions()));
                    ll_realnumber.setVisibility(View.VISIBLE);
                    et_realnumber.setText(order.getPlateNumber() == null ? "" : order.getPlateNumber());
                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        }
    }

    @Override
    @OnClick({R.id.bt_notice_complete, R.id.ll_notice_shipment})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_notice_complete:
                if (order.getShipmentId() == null) {
                    OrderShipGroupServiceCommandDtos.CreateSOShipmentDto o = new OrderShipGroupServiceCommandDtos.CreateSOShipmentDto();
                    OrderIdShipGroupSeqIdPair[] ps = new OrderIdShipGroupSeqIdPair[order.getOrderShipGroupLine().size()];

                    for (int i = 0; i < order.getOrderShipGroupLine().size(); i++) {
                        OrderIdShipGroupSeqIdPair p = new OrderIdShipGroupSeqIdPair();
                        p.setOrderId(order.getOrderShipGroupLine().get(i).getOrderId());
                        p.setShipGroupSeqId(order.getId());
                        ps[i] = p;
                    }
                    o.setOrderIdShipGroupSeqIdPairs(ps);
                    o.setRequesterId(Operator.getOperator().getAccount());
                    o.setDestinationFacilityId(Warehouse.getHouse().getWarehouseId());
                    o.setVehicleId(et_realnumber.getText().toString());
                    o.setCommandId(GUID.getUUID(o));
                    String id = NetUtil.makeId(true, "OrderShipGroupService", "CreateSOShipment");
                    NetUtil.getInstance().Body_NoResponse(this, o, id, NetService.createSOShipment, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                        @Override
                        public void onSuccess(Response<ResponseBody> result) {
                            try {
                                goNext(result.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public boolean onError(String msg) {
                            return false;
                        }
                    });
                } else {
                    //goNext(order.getShipmentId());
                }
                break;
            case R.id.ll_notice_shipment:
                break;
        }
    }

    private void goNext(String shipmentId) {
        Intent i = new Intent(this, ShipmentDetailOutActivity.class);
        i.putExtra(Constant.Flags.MainFlag, Function.OutgoingShipment);
        ShipmentDetail ship = new ShipmentDetail();
        ship.setShipmentId(shipmentId);
        i.putExtra(Constant.Flags.Param1, ship);
        startActivity(i);
    }
}
