package com.takumi.wms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.takumi.wms.R;
import com.takumi.wms.adapter.OutShipmentAdapter;
import com.takumi.wms.adapter.RVItemClick;
import com.takumi.wms.adapter.RVItemItemClick;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.OutDocumentInfo;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.shipment.ShipmentCommandDtos;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.widget.MessageDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ShipmentDetailOutActivity extends ShipmentDetailBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("装运发货单");
        iv_shipment_serial_scan.setVisibility(View.GONE);
        bt_create_shipmentLine.setVisibility(View.GONE);
        ll_shipment_serial.setVisibility(View.GONE);
        ll_shipment_contract.setVisibility(View.GONE);
        bt_scan.setText("添加行项");
        btn_commit_shipment.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInitInfo();
    }

    private void getInitInfo() {
        String id = NetUtil.makeId(true, NetService.queries, NetService.mandatoryAtts, NetService.issuance, ship.getShipmentId());
        getRequireds(id);
    }

    @Override
    protected void goNextById(String s) {
        goSerialNext(s);
    }

    @Override
    protected void getShipmentDoc() {
        String id = NetUtil.makeId(true, NetService.queries, "outDocument", "out", ship.getShipmentId());
        Map<String, String> map = new HashMap<>();
        map.put("DestinationFacilityId", Warehouse.getHouse().getWarehouseId());
        NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getOutShipment, new NetUtil.MyCallBack<OutDocumentInfo>() {
            @Override
            public void onSuccess(OutDocumentInfo result) {
                ol = result;
                if (ou.getDownloadImages().isEmpty() && ou.getUploadImages().isEmpty() && ol != null) {
                    getImages(ol.getDocumentImages());
                }
                OutShipmentAdapter adapter = new OutShipmentAdapter(expands, ol.getDocumentLines());
                adapter.setItemClick(new RVItemClick() {
                    @Override
                    public void itemClick(int position) {
                        goAddOutNext(position, adapter.getInfoItemSize(position));
                    }
                });
                adapter.setItemItemClick(new RVItemItemClick() {
                    @Override
                    public void itemClick(int p1, int p2) {
                        goShowNext(ol.getDocumentLines().get(p1).getItemissuance().get(p2));
                    }
                });
                adapter.setItemDeleteClick(new RVItemItemClick() {
                    @Override
                    public void itemClick(int p1, int p2) {
                        String id = NetUtil.makeId(true, NetService.Shipments, ol.getDocumentId(), "ItemIssuances", ol.getDocumentLines().get(p1).getItemissuance().get(p2).getDocumentLineId());
                        NetUtil.getInstance().NoBody_Response(ShipmentDetailOutActivity.this, null, id, NetService.DeleteShipmentOutLineItem, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                            @Override
                            public void onSuccess(Response<ResponseBody> result) {
                                if (result.code() == 200) {
                                    ToastUtil.showToast(ShipmentDetailOutActivity.this, "删除成功");
                                    if (i > 0 && j > 0 && k > 0) {
                                        dismissRepeat();
                                    }
                                    getInitInfo();
                                } else {
                                    ToastUtil.showToast(ShipmentDetailOutActivity.this, "删除失败");
                                }
                            }

                            @Override
                            public boolean onError(String msg) {
                                return false;
                            }
                        });
                    }
                });
                rv_shipment_item.setAdapter(adapter);
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    @Override
    void bt_scan() {
        goAddOutNext(0, 0);
    }

    @OnClick({R.id.btn_commit_shipment, R.id.bt_create_shipmentLine, R.id.btn_push})
    void click(View view) {
        switch (view.getId()) {
            case R.id.btn_commit_shipment:
                AlertDialog dialog = MessageDialog.getDialog(this, fun.getName(), "确认是否提交？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (checkRepeat()) {
                                commit();
                            }

                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.btn_push:
                String id = "api/queries/inboundPush/outComeBack";
                Map<String, String> m = new HashMap<>();
                m.put("noticeNumber", ship.getPrimaryShipGroupSeqId());
                NetUtil.getInstance().NoBody_Response(this, m, id, NetService.shipmentPush, new NetUtil.MyCallBack<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            btn_commit_shipment.setEnabled(true);
                            ToastUtil.showToast(ShipmentDetailOutActivity.this, "推送成功");
                        } else {
                            ToastUtil.showToast(ShipmentDetailOutActivity.this, "推送失败");
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

    protected void commit() {
        String id = NetUtil.makeId(true, NetService.Shipments, ship.getShipmentId());
        Map<String, String> m = new HashMap<>();
        m.put("fields", "version");
        NetUtil.getInstance().NoBody_Response(ShipmentDetailOutActivity.this, m, id, NetService.getShipmentVersion, new NetUtil.MyCallBack<ShipmentDetail>() {
            @Override
            public void onSuccess(ShipmentDetail result) {
                CommitShipmentComplete(result.getVersion());
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    int i = -1;
    int j = -1;
    int k = -1;

    private boolean checkRepeat() {
        List<OutDocumentInfo.DocumentLinesItem> beans = ol.getDocumentLines();
        for (int i = 0; i < beans.size(); i++) {
            List<DocumentLine> lines = beans.get(i).getItemissuance();
            if (lines != null) {
                for (int j = 0; j < lines.size(); j++) {
                    if (lines.get(j).getInventoryattribute().containsKey("serialNumber")) {
                        String serialNumber = lines.get(j).getInventoryattribute().get("serialNumber").toString();
                        if (serialNumber != null) {
                            for (int k = j + 1; k < lines.size(); k++) {
                                if (serialNumber.equals(lines.get(k).getInventoryattribute().get("serialNumber"))) {
                                    this.i = i;
                                    this.j = j;
                                    this.k = k;
                                    RecyclerView rv = llm.getChildAt(i).findViewById(R.id.rv_item);
                                    LinearLayout ll_expand = llm.getChildAt(i).findViewById(R.id.ll_expand);
                                    rv.setVisibility(View.VISIBLE);
                                    ll_expand.setVisibility(View.VISIBLE);
                                    expands.add(new Integer(i));
                                    AlertDialog dialog = MessageDialog.getCancelDialog(this, "检测重复", "卷号：" + serialNumber + " 重复,是否继续提交？", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    showRepeat(rv);
                                                    break;
                                                /*case DialogInterface.BUTTON_POSITIVE:
                                                    commit();
                                                    break;*/
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();

                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    private void showRepeat(RecyclerView rv) {
        rv_shipment_item.scrollToPosition(i);
        LinearLayoutManager lm = (LinearLayoutManager) (rv).getLayoutManager();
        lm.getChildAt(j).findViewById(R.id.ll_swipe_content).setBackgroundColor(Color.parseColor("#f25542"));
        lm.getChildAt(k).findViewById(R.id.ll_swipe_content).setBackgroundColor(Color.parseColor("#f25542"));
        rv.scrollToPosition(j);
    }

    private void dismissRepeat() {
        RecyclerView rv = llm.getChildAt(i).findViewById(R.id.rv_item);
        LinearLayoutManager lm = (LinearLayoutManager) (rv).getLayoutManager();
        lm.getChildAt(j).findViewById(R.id.ll_swipe_content).setBackgroundColor(Color.parseColor("#eeeeee"));
        lm.getChildAt(k).findViewById(R.id.ll_swipe_content).setBackgroundColor(Color.parseColor("#eeeeee"));
        i = -1;
        j = -1;
        k = -1;
    }

    @Override
    protected void CommitShipmentComplete(long version) {
        String id = NetUtil.makeId(true, NetService.Shipments, ship.getShipmentId(), NetService._commands, NetService.ConfirmAllItemsIssued);
        ShipmentCommandDtos.ConfirmAllItemsIssuedRequestContent content = new ShipmentCommandDtos.ConfirmAllItemsIssuedRequestContent();
        content.setVersion(version);
        content.setRequesterId(Operator.getOperator().getAccount());
        content.setShipmentId(ship.getShipmentId());
        content.setCommandId(GUID.getUUID(content));
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.CommitShipmentOut, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                ToastUtil.showToast(ShipmentDetailOutActivity.this, "提交成功");
                finish();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SerialScanRequest) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra("number");
                goSerialNext(s);
            }
        }
    }

    private void goSerialNext(String s) {
        et_shipment_serialnumber.setText(s);
        if (o != null && o.getDocumentLines() != null) {
            for (DocumentLine l : o.getDocumentLines()) {
                if (l.getInventoryattribute() != null && s.equals(l.getInventoryattribute().get("serialNumber"))) {
                    goShowNext(l);
                    return;
                }
            }
        }
        goAddOutNext(s);
    }

    private void goAddOutNext(String s) {
        Intent i = new Intent(this, AddShipmentOutLineActivity.class);
        i.putExtra(Constant.Flags.Param1, inOutDocument);
        i.putExtra(Constant.Flags.Param2, o == null || o.getDocumentLines() == null ? 0 : o.getDocumentLines().size());
        i.putExtra(Constant.Flags.Param3, s);
        i.putExtra(Constant.Flags.MainFlag, fun);
        startActivity(i);
    }
}
