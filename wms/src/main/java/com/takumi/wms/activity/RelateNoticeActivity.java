package com.takumi.wms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.RVItemClick;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.NoticeDocument;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.dto.shipment.CreateOrMergePatchShipmentDto;
import com.takumi.wms.model.dto.shipment.OrderIdShipGroupSeqIdPair;
import com.takumi.wms.model.dto.shipment.OrderShipGroupServiceCommandDtos;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.widget.DropEdit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class RelateNoticeActivity extends BottomActivity {

    @BindView(R.id.tv_relate_shipment)
    TextView tv_relate_shipment;
    @BindView(R.id.tv_relate_contract)
    TextView tv_relate_contract;
    @BindView(R.id.et_relate_notice)
    DropEdit et_relate_notice;
    @BindView(R.id.bt_relate_save)
    Button bt_relate_save;
    @BindView(R.id.ll_contract)
    LinearLayout ll_contract;
    @BindView(R.id.tv_relate_license)
    TextView tv_relate_license;
    @BindView(R.id.tv_relate_name)
    TextView tv_relate_name;
    @BindView(R.id.tv_relate_phone)
    TextView tv_relate_phone;


    private ShipmentDetail shipmentDetail;
    private NoticeDocument noticeDocument;
    private String shipId;

    private String funcAction;

    private static final String CREATE = "CREATE";
    private static final String RELATE = "RELATE";

    private List<String> notices;
    private List<String> contracts;
    private int noticeWhich = -1;
    private int contractWhich = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar(R.color.white, true);
        shipmentDetail = getIntent().getParcelableExtra(Constant.Flags.Param1);
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        if (fun == Function.IncomingShipment) {
            if (shipmentDetail == null) {
                funcAction = CREATE;
                bt_relate_save.setText("创建收货单");
                setTitle("创建收货单");
                shipmentDetail = new ShipmentDetail();
                shipmentDetail.setShipmentId(System.currentTimeMillis() + "");
            } else {
                funcAction = RELATE;
                setTitle("关联通知单号");
            }
        } else if (fun == Function.OutgoingShipment) {
            if (shipmentDetail == null) {
                funcAction = CREATE;
                bt_relate_save.setText("创建发货单");
                setTitle("创建发货单");
                shipmentDetail = new ShipmentDetail();
                shipmentDetail.setShipmentId(System.currentTimeMillis() + "");
            } else {
                funcAction = RELATE;
                setTitle("关联通知单号");
            }
        }
        tv_relate_shipment.setText(shipmentDetail.getShipmentId());
        if (shipmentDetail.getPrimaryOrderId() != null) {
            tv_relate_contract.setText(shipmentDetail.getPrimaryOrderId());
        }


        initNotice();
    }

    private void initNotice() {
        et_relate_notice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NetUtil.getInstance().cancelConnect();
                if (et_relate_notice.isTextChange) {
                    if (!TextUtils.isEmpty(s)) {
                        String id = NetUtil.makeId(true, NetService.queries, NetService.fuzzyQuery, NetService.noticeDocument, s.toString());
                        getFuzzyNotice(id, new NetUtil.MyCallBack<List<String>>() {
                            @Override
                            public void onSuccess(List<String> result) {
                                et_relate_notice.showDrop(result, new RVItemClick() {
                                    @Override
                                    public void itemClick(int position) {
                                        getContract(result.get(position));
                                        et_relate_notice.setTextNoWatch(result.get(position));
                                        noticeWhich = -1;
                                        contractWhich = -1;
                                        tv_relate_contract.setText("");
                                        noticeDocument = null;
                                        et_relate_notice.dismissDrop();
                                    }
                                });
                            }

                            @Override
                            public boolean onError(String msg) {
                                return false;
                            }
                        });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_relate_notice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getContract(et_relate_notice.getText().toString());
                }
                return true;
            }
        });
        String id = NetUtil.makeId(true, NetService.queries, NetService.fuzzyQuery, "allOrderGroup");
        getFuzzyNotice(id, new NetUtil.MyCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                notices = result;
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void getFuzzyNotice(String id, NetUtil.MyCallBack callBack) {
        Map<String, String> m = new HashMap<>();
        if (fun == Function.IncomingShipment) {
            m.put("type", "PURCHASE_ORDER");
        } else if (fun == Function.OutgoingShipment) {
            m.put("type", "SALES_ORDER");
        }
        NetUtil.getInstance().NoDialogNoBody_Response(RelateNoticeActivity.this, m, id, NetService.getBlurId, callBack);
    }

    private void getContract(String shipId) {
        noticeDocument = null;
        clearInfo();
        tv_relate_contract.setText("");
        String id = NetUtil.makeId(true, NetService.queries, NetService.fuzzyQuery, "orderId");
        Map<String, String> m = new HashMap<>();
        m.put("seqId", shipId);
        NetUtil.getInstance().NoBody_Response(RelateNoticeActivity.this, m, id, NetService.getContractByNotice, new NetUtil.MyCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                if (result != null && !result.isEmpty()) {
                    contracts = result;
                    RelateNoticeActivity.this.shipId = shipId;
                    tv_relate_contract.setText(result.get(0));
                    noticeDocument = new NoticeDocument(shipId, result.get(0));
                    contractWhich = 0;
                    getNoticeInfo();
                }
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void getNoticeInfo() {
        String id = NetUtil.makeId(true, NetService.queries, NetService.noticeDocument, "delivery");
        Map<String, String> m = new HashMap<>();
        m.put("seqId", noticeDocument.getId());
        m.put("orderId", noticeDocument.getContractNo());
        NetUtil.getInstance().NoBody_Response(RelateNoticeActivity.this, m, id, NetService.getNoticeInfo, new NetUtil.MyCallBack<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> result) {
                tv_relate_license.setText(result.get("vehiclePlateNumber"));
                tv_relate_name.setText(result.get("contactPartyId"));
                tv_relate_phone.setText(result.get("telecomContactMechId"));
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void clearInfo() {
        tv_relate_license.setText("");
        tv_relate_name.setText("");
        tv_relate_phone.setText("");
    }


    @Override
    protected int getContentViewID() {
        return R.layout.activity_relate_notice;
    }

    @OnClick({R.id.bt_relate_save, R.id.iv_notice_list, R.id.ll_contract})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_relate_save:
                if (noticeDocument == null) {
                    ToastUtil.showToast(this, "请选择合同号");
                    return;
                }
                if (fun == Function.IncomingShipment) {
                    if (funcAction == RELATE) {
                        relateContract();
                    } else if (funcAction == CREATE) {
                        createPurchaseShipment();
                    }
                } else if (fun == Function.OutgoingShipment) {
                    createSalesShipment();
                }
                break;
            case R.id.iv_notice_list:
                if (notices != null) {
                    chooseNotice();
                }
                break;
            case R.id.ll_contract:
                if (contracts != null) {
                    chooseContract();
                }
                break;
        }
    }

    protected void chooseContract() {
        if (contracts == null || contracts.isEmpty()) {
            ToastUtil.showToast(this, "没有相关合同号");
            return;
        }
        String[] items = new String[contracts.size()];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("单选");
        builder.setSingleChoiceItems(contracts.toArray(items), contractWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_relate_contract.setText(contracts.get(which));
                contractWhich = which;
                noticeDocument = new NoticeDocument(shipId, contracts.get(which));
                getNoticeInfo();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    protected void chooseNotice() {
        if (notices == null || notices.isEmpty()) {
            ToastUtil.showToast(this, "没有通知单");
            return;
        }
        String[] items = new String[notices.size()];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("单选");
        builder.setSingleChoiceItems(notices.toArray(items), noticeWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContract(items[which]);
                noticeWhich = which;
                contractWhich = -1;
                tv_relate_contract.setText("");
                et_relate_notice.setTextNoWatch(items[which]);
                noticeDocument = null;
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void createSalesShipment() {
        OrderShipGroupServiceCommandDtos.CreateSOShipmentDto dto = new OrderShipGroupServiceCommandDtos.CreateSOShipmentDto();
        OrderIdShipGroupSeqIdPair p = new OrderIdShipGroupSeqIdPair();
        p.setOrderId(noticeDocument.getContractNo());
        p.setShipGroupSeqId(noticeDocument.getId());
        dto.setOrderIdShipGroupSeqIdPairs(new OrderIdShipGroupSeqIdPair[]{p});
        dto.setRequesterId(Operator.getOperator().getAccount());
        dto.setShipmentId(shipmentDetail.getShipmentId());
        dto.setCommandId(GUID.getUUID(dto));
        String id = NetUtil.makeId(true, NetService.OrderShipGroupService, NetService.CreateSOShipment);
        NetUtil.getInstance().Body_NoResponse(this, dto, id, NetService.createSOShipment2, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                shipmentDetail.setPrimaryOrderId(noticeDocument.getContractNo());
                goOutNext(shipmentDetail);
                finish();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void relateContract() {
        CreateOrMergePatchShipmentDto.MergePatchShipmentDto dto = new CreateOrMergePatchShipmentDto.MergePatchShipmentDto();
        dto.setPrimaryOrderId(noticeDocument.getContractNo());
        //dto.setShipmentId(shipmentDetail.getShipmentId());
        dto.setPrimaryShipGroupSeqId(noticeDocument.getId());
        dto.setVersion((long) shipmentDetail.getVersion());
        String id = NetUtil.makeId(true, NetService.Shipments, shipmentDetail.getShipmentId());
        NetUtil.getInstance().Body_NoResponse(this, dto, id, NetService.RelateNotice, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                ToastUtil.showToast(RelateNoticeActivity.this, "已关联");
                finish();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }


    private void createPurchaseShipment() {
        OrderShipGroupServiceCommandDtos.CreatePOShipmentDto dto = new OrderShipGroupServiceCommandDtos.CreatePOShipmentDto();
        OrderIdShipGroupSeqIdPair p = new OrderIdShipGroupSeqIdPair();
        p.setOrderId(noticeDocument.getContractNo());
        p.setShipGroupSeqId(noticeDocument.getId());
        dto.setOrderIdShipGroupSeqIdPairs(new OrderIdShipGroupSeqIdPair[]{p});
        dto.setRequesterId(Operator.getOperator().getAccount());
        dto.setShipmentId(shipmentDetail.getShipmentId());
        dto.setCommandId(GUID.getUUID(dto));
        String id = NetUtil.makeId(true, NetService.Shipments);
        NetUtil.getInstance().Body_NoResponse(this, dto, id, NetService.createPOShipment, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                shipmentDetail.setPrimaryOrderId(noticeDocument.getContractNo());
                goInNext(shipmentDetail);
                finish();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void goInNext(ShipmentDetail o) {
        Intent intent = new Intent(RelateNoticeActivity.this, ShipmentDetailInActivity.class);
        intent.putExtra(Constant.Flags.MainFlag, fun);
        intent.putExtra(Constant.Flags.Param1, o);
        startActivity(intent);
    }

    private void goOutNext(ShipmentDetail o) {
        Intent intent = new Intent(RelateNoticeActivity.this, ShipmentDetailOutActivity.class);
        intent.putExtra(Constant.Flags.MainFlag, fun);
        intent.putExtra(Constant.Flags.Param1, o);
        startActivity(intent);
    }

}
