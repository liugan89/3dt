package com.takumi.wms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.takumi.wms.R;
import com.takumi.wms.adapter.RVItemClick;
import com.takumi.wms.adapter.ShipmentCardAdapter;
import com.takumi.wms.model.DocumentInfo;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.shipment.OrderShipGroupServiceCommandDtos;
import com.takumi.wms.model.dto.shipment.ShipmentCommandDtos;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.widget.MessageDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ShipmentDetailInActivity extends ShipmentDetailBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("装运收货单");
        changeShipStatus();
        bt_create_shipmentLine.setVisibility(View.GONE);
        ll_shipment_notice.setVisibility(View.GONE);
        if (ship.getShipmentId().contains("IN_ZJDB")) {
            btn_push.setVisibility(View.VISIBLE);
            btn_commit_shipment.setEnabled(false);
        } else {
            btn_push.setVisibility(View.GONE);
            btn_commit_shipment.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        String id = NetUtil.makeId(true, NetService.queries, NetService.mandatoryAtts, NetService.shipment, ship.getShipmentId());
        getRequireds(id);
    }

    private void changeShipStatus() {
        if ("PURCH_SHIP_CREATED".equals(ship.getStatusId())) {
            //String id1 = NetUtil.makeId(true, NetService.Shipments, ship.getShipmentId(), NetService._commands, "PurchaseShipmentAction");
            String id1 = NetUtil.makeId(true, "OrderShipGroupService", "ShipPOShipment");
            /*ShipmentCommandDtos.PurchaseShipmentActionRequestContent content = new ShipmentCommandDtos.PurchaseShipmentActionRequestContent();
            content.setRequesterId(Operator.getOperator().getAccount());
            content.setShipmentId(ship.getShipmentId());
            content.setVersion((long) ship.getVersion());
            content.setValue("Ship");
            content.setCommandId(GUID.getUUID(content));*/
            OrderShipGroupServiceCommandDtos.ShipPOShipmentDto content = new OrderShipGroupServiceCommandDtos.ShipPOShipmentDto();
            content.setShipmentId(ship.getShipmentId());
            content.setRequesterId(Operator.getOperator().getAccount());
            content.setHintShipmentItemsEnabled(true);
            content.setCommandId(GUID.getUUID(content));
            NetUtil.getInstance().Body_NoResponse(this, content, id1, NetService.changeShipmentStatus, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                @Override
                public void onSuccess(Response<ResponseBody> result) {
                    System.out.println(1);
                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        }
    }


    @Override
    protected void getShipmentDoc() {
        String id = NetUtil.makeId(true, NetService.queries, NetService.document, NetService.shipment, ship.getShipmentId());
        Map<String, String> map = new HashMap<>();
        map.put("DestinationFacilityId", Warehouse.getHouse().getWarehouseId());
        NetUtil.getInstance().NoBody_Response(this, map, id, NetService.getShipment, new NetUtil.MyCallBack<DocumentInfo>() {
            @Override
            public void onSuccess(DocumentInfo result) {
                o = result;
                if (ou.getDownloadImages().isEmpty() && ou.getUploadImages().isEmpty() && o != null) {
                    getImages(o.getDocumentImages());
                }
                ls.clear();
                if (o.getDocumentLines() != null) {
                    for (DocumentLine l : o.getDocumentLines()) {
                        if (l.getAcceptedCount() != null && l.getAcceptedCount().doubleValue() > 0) {
                            ls.add(l);
                        }
                    }
                }

                ShipmentCardAdapter adapter = new ShipmentCardAdapter(ls);
                adapter.setClick(new RVItemClick() {
                    @Override
                    public void itemClick(int position) {
                        goShowNext(ls.get(position));
                    }
                });
                adapter.setDeleteClick(new RVItemClick() {
                    @Override
                    public void itemClick(int position) {
                        getVersion(new NetUtil.MyCallBack<ShipmentDetail>() {
                            @Override
                            public void onSuccess(ShipmentDetail result) {
                                String id = NetUtil.makeId(true, "Shipments", o.getDocumentId(), "ShipmentReceipts", ls.get(position).getDocumentLineId());
                                Map<String, String> map = new HashMap<>();
                                map.put("version", result.getVersion() + "");
                                map.put("requesterId", Operator.getOperator().getAccount());
                                map.put("commandId", GUID.getUUID(map));
                                NetUtil.getInstance().NoBody_Response(ShipmentDetailInActivity.this, map, id, NetService.deleteShipmentInItem, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                                    @Override
                                    public void onSuccess(Response<ResponseBody> result) {
                                        if (result.code() < 210) {
                                            refresh();
                                        } else {
                                            ToastUtil.showToast(ShipmentDetailInActivity.this, "deleteShipmentInItem_" + result.code());
                                        }
                                    }

                                    @Override
                                    public boolean onError(String msg) {
                                        return false;
                                    }
                                });
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

    @OnClick({R.id.btn_commit_shipment, R.id.bt_create_shipmentLine, R.id.btn_push})
    void click(View view) {
        switch (view.getId()) {
            case R.id.btn_commit_shipment:
                AlertDialog dialog = MessageDialog.getDialog(this, fun.getName(), "确认是否提交？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            String id = NetUtil.makeId(true, NetService.Shipments, ship.getShipmentId());
                            Map<String, String> m = new HashMap<>();
                            m.put("fields", "version");
                            NetUtil.getInstance().NoBody_Response(ShipmentDetailInActivity.this, m, id, NetService.getShipmentVersion, new NetUtil.MyCallBack<ShipmentDetail>() {
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
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.bt_create_shipmentLine:
                if (o == null || o.getDocumentLines() == null) {
                    goAddInNext(0);
                } else {
                    goAddInNext(o.getDocumentLines().size());
                }
                break;
            case R.id.btn_push:
                String id = "api/queries/inboundPush/outComeBack";
                Map<String, String> m = new HashMap<>();
                m.put("noticeNumber", ship.getShipmentId());
                NetUtil.getInstance().NoBody_Response(this, m, id, NetService.shipmentPush, new NetUtil.MyCallBack<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            btn_commit_shipment.setEnabled(true);
                            ToastUtil.showToast(ShipmentDetailInActivity.this, "推送成功");
                        } else {
                            ToastUtil.showToast(ShipmentDetailInActivity.this, "推送失败");
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

    @Override
    protected void goNextById(String s) {
        String ups = s.toUpperCase();
        if (o != null && o.getDocumentLines() != null) {
            for (DocumentLine l : o.getDocumentLines()) {
                if (l.getInventoryattribute() != null) {
                    if (ups.equals((l.getInventoryattribute().get("serialNumber") + "").toUpperCase())) {
                        if (l.getAcceptedCount() != null && l.getAcceptedCount().doubleValue() > 0) {
                            goShowNext(l);
                        } else {
                            goNext(l);
                        }
                        return;
                    }
                }
            }
        }
        ToastUtil.showToast(this, "无此卷号");
    }

    @Override
    protected void CommitShipmentComplete(long version) {
        String id = NetUtil.makeId(true, NetService.Shipments, ship.getShipmentId(), NetService._commands, NetService.ConfirmAllItemsReceived);
        ShipmentCommandDtos.ConfirmAllItemsReceivedRequestContent content = new ShipmentCommandDtos.ConfirmAllItemsReceivedRequestContent();
        content.setVersion(version);
        content.setRequesterId(Operator.getOperator().getAccount());
        content.setShipmentId(ship.getShipmentId());
        content.setCommandId(GUID.getUUID(content));
        content.setDestinationLocatorId(SingletonCache.locators.get(0).getLocatorId());
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.CommitShipment, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                ToastUtil.showToast(ShipmentDetailInActivity.this, "提交成功");
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
                ToastUtil.showToast(this, s);
                et_shipment_serialnumber.setText(s);
                goNextById(s);
            }
        }
    }
}
