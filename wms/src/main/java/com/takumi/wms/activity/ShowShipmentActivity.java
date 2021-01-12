package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.StatusItem;
import com.takumi.wms.model.dto.shipment.CreateOrMergePatchShipmentDto;
import com.takumi.wms.model.dto.shipment.CreateOrMergePatchShipmentReceiptDto;
import com.takumi.wms.model.dto.shipment.CreateOrMergePatchShipmentReceiptImageDto;
import com.takumi.wms.model.dto.shipment.ShipmentCommandDtos;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.AttrInflater;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class ShowShipmentActivity extends LineBaseActivty implements View.OnClickListener {

    private String LineNumber;
    private DocumentLine info;
    private boolean isReEntry;
    private String serialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serial_query.setVisibility(View.VISIBLE);
        if (fun == Function.IncomingShipment) {
            setShowInOutLocator(false);
        } else {
            setShowInOutLocator(true);
        }
        setTargetLocatorChooseful(false);
        setPreLocatorChooseful(false);
        setProdctIdChoosable(false);
        setTitle("产品查看");
        info = getIntent().getParcelableExtra(Constant.Flags.Param2);
        String id = null;
        if (fun == Function.IncomingShipment) {
            id = NetUtil.makeId(true, NetService.queries, NetService.document, NetService.receipt);
        } else if (fun == Function.OutgoingShipment) {
            id = NetUtil.makeId(true, NetService.queries, NetService.document, NetService.issuances);
        }
        getDocumentLines(id);
    }

    private void getDocumentLines(String id) {
        Map<String, String> m = new HashMap<>();
        m.put("shipmentId", o.getDocumentNumber());
        m.put("receiptSeqId", info.getDocumentLineId());
        NetUtil.getInstance().NoBody_Response(this, m, id, NetService.getDocumentLineById, new NetUtil.MyCallBack<DocumentLine>() {
            @Override
            public void onSuccess(DocumentLine result) {
                info = result;
                init();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private void init() {
        ll_line.setVisibility(View.VISIBLE);
        tv_line_id.setText(info.getDocumentLineId());
        et_product.setTextNoWatch(info.getProductId());
        if (info.getInventoryattribute() != null) {
            serial_content.setText(info.getInventoryattribute().get("serialNumber").toString());
        } else {
            serial_layout.setVisibility(View.GONE);
        }
        getChooseProductById(info.getProductId());
        showLineInfo(info);
        quanlity_layout.setVisibility(View.GONE);
        product_acceptedcount.setText(info.getAcceptedCount() + "");
        //ll_acceptedcount_layout.setVisibility(View.GONE);
        serial_content.setEnabled(false);
        serial_query.setVisibility(View.GONE);
        product_quantity.setEnabled(false);
        product_quantity.setText(info.getAcceptedCount() + "");
        //ll_count_layout.setVisibility(View.VISIBLE);
        ll_image_upload.setVisibility(View.GONE);
        ll_image_download.setVisibility(View.VISIBLE);
        describe_layout.setVisibility(View.GONE);
        //initImageRV(true, rv_image_upload, ou.getUploadImages());
        initImageRV(false, rv_image_download, ou.getDownloadImages());
        serial_content.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        product_add.setVisibility(View.GONE);
        product_next.setVisibility(View.GONE);
        et_prelocator.setText(info.getLocatorId());
        et_targetlocator.setText(info.getLocatorId());
        ou.downloadPics(ShowShipmentActivity.this, info.getDocumentLineImageUrls(), new NetUtil.MyCallBack() {
            @Override
            public void onSuccess(Object result) {
                downloadGridImageAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }


    @Override
    protected void showLineInfo(DocumentLine info) {
        if (info != null) {
            if (info.getInventoryattribute() != null) {
                for (final Map.Entry<String, Object> entry : info.getInventoryattribute().entrySet()) {
                    String s = entry.getKey();
                    if (!s.equals("serialNumber") && !s.equals("attributeSetId") && !s.equals("Version") && !s.equals("AttributeSetInstanceId") && !s.equals("ImageUrl")) {
                        AttrInflater.replaceWord(s);
                        addProduct_item(s, entry.getValue() + "", false, ll_line_info);
                    }
                }
            }
            if (info.getDamageStatuss() != null) {
                StringBuilder sb = new StringBuilder();
                for (StatusItem i : info.getDamageStatuss()) {
                    sb.append(i.getDescription() + ";");
                }
                if (sb.length() != 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                addProduct_item("质量缺陷", sb.toString(), false, ll_line_info);
            }
        }
    }


    private <T> void addProduct_item(String key, String value, boolean enable, ViewGroup vg) {
        if (value != null && !TextUtils.isEmpty(value + "")) {
            if (!key.equals("serialNumber")) {
                if (!key.equals("AttributeSetInstanceId")) {
                    LinearLayout v = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.product_item, null);
                    TextView tv1 = v.findViewById(R.id.product_title);
                    EditText tv2 = v.findViewById(R.id.product_content);
                    tv2.setEnabled(false);
                    tv1.setText(key + "：");
                    tv2.setText(value);
                    tv2.setEnabled(enable);
                    vg.addView(v);
                } else {
                    vg.addView(createButtonView(key, value.toString(), false));
                }
            }
        }
    }


    private View createButtonView(String key, String value, boolean enable) {
        View v = LayoutInflater.from(this).inflate(R.layout.product_item_button, null);
        TextView tv1 = v.findViewById(R.id.product_title);
        EditText tv2 = v.findViewById(R.id.product_content);
        bt_reEntry = v.findViewById(R.id.item_button);
        bt_reEntry.setText("重新录入");
        bt_reEntry.setOnClickListener(this);
        tv1.setText(key);
        tv2.setText(value);
        tv2.setEnabled(enable);
        return v;
    }

    private void Recovery() {
        serial_layout.setVisibility(View.VISIBLE);
        ll_product_info.removeAllViews();
        ll_line_info.removeAllViews();
        product_quantity.getText().clear();
        serial_content.getText().clear();
    }

    @Override
    protected void setChooseProduct(Product p) {
        chooseProduct = p;
        ll_product.setVisibility(View.VISIBLE);
        et_product.setTextNoWatch(p.getProductId());
        showProdctInfo(p);
        if (p.isSerialNumbered()) {

        } else if (p.isManagedByLot()) {
            serial_layout.setVisibility(View.GONE);
            showLot();
        } else {
            serial_layout.setVisibility(View.GONE);
            getInventoryAttribute();
        }
    }

    /**
     * 得到库存属性
     */
    private void getInventoryAttribute() {

        String id = NetUtil.makeId(true, NetService.queries, NetService.InventoryAttribute, chooseProduct.getAttributeSetId());
        NetUtil.getInstance().NoBody_Response(ShowShipmentActivity.this, null, id, NetService.getAttrSet, new NetUtil.MyCallBack<InventoryAttribute>() {
            @Override
            public void onSuccess(InventoryAttribute result) {
                if (result != null) {
                    inventoryAttribute = result;
                    showInventoryInfo(inventoryAttribute);
                } else {
                    showInventoryInfo(null);
                }
            }

            @Override
            public boolean onError(String msg) {
                showInventoryInfo(null);
                return false;
            }
        });
    }

    /**
     * 根据产品属性生成界面
     *
     * @param set
     */
    private void showInventoryInfo(InventoryAttribute set) {
        ll_line_info.removeAllViews();//移除所有显示产品信息的布局
        serial_layout.setVisibility(View.GONE);
        if (chooseProduct.isSerialNumbered()) {//判断是否为批号管理的产品
            serial_layout.setVisibility(View.VISIBLE);
            new AttrInflater(set, this, ll_line_info, 0);
        } else if (chooseProduct.isManagedByLot()) {
            serial_layout.setVisibility(View.GONE);
            showLot();
        } else {
            serial_layout.setVisibility(View.GONE);
            new AttrInflater(set, this, ll_line_info, 0);
        }

    }


    /**
     * 添加出入库行项
     */
    private void addNewShipmentLine() {
        try {
            ou.uploadPics(this, ou.getUploadImages(), new NetUtil.MyCallBack() {
                @Override
                public void onSuccess(Object result) {
                    Observable ob = Observable.just(null);
                    Observer sub = new Observer() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Object ob) {
                            addLine(version);
                        }
                    };
                    ob.observeOn(AndroidSchedulers.mainThread()).subscribe(sub);

                }

                @Override
                public boolean onError(String msg) {
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long version;

    private void getVersion() {
        String id = NetUtil.makeId(true, NetService.Shipments, o.getDocumentNumber());
        Map<String, String> m = new HashMap<>();
        m.put("fields", "version");
        NetUtil.getInstance().NoBody_Response(ShowShipmentActivity.this, m, id, NetService.getShipmentVersion, new NetUtil.MyCallBack<ShipmentDetail>() {
            @Override
            public void onSuccess(ShipmentDetail result) {
                version = result.getVersion();
                if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                    addNewShipmentLine();
                } else {
                    addLine(version);
                }
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
    }

    private boolean checkCommit() {

        if (serial_layout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(serial_content.getText().toString())) {
                ToastUtil.showToast(this, "请填写卷号");
                return false;
            }
        }
        if (TextUtils.isEmpty(product_quantity.getText().toString().trim())) {
            ToastUtil.showToast(this, "请填写数量");
            return false;
        }
        if ("0".equals(product_quantity.getText().toString().trim())) {
            ToastUtil.showToast(this, "数量不能为0");
            return false;
        }
        /*if (!Double.valueOf(product_acceptedcount.getText().toString()).equals(info.getCount())) {
            AlertDialog dialog = MessageDialog.getDialog(this, "确认保存？", "实际数量和接收数量不一致，确认是否接收？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            getVersion();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }
                }
            });
            dialog.show();
            return false;
        }*/
        return true;
    }

    private void addLine(long version) {
        LineNumber = info.getDocumentLineId();
        final String id = NetUtil.makeId(true, NetService.Shipments, o.getDocumentNumber(), NetService._commands, NetService.ReceiveItem);
        //final ShipmentCommandDtos.AddLineRequestContent content = new ShipmentCommandDtos.AddLineRequestContent();
        final ShipmentCommandDtos.ReceiveItemRequestContent content = new ShipmentCommandDtos.ReceiveItemRequestContent();
        content.setShipmentId(o.getDocumentNumber());
        content.setShipmentItemSeqId(info.getDocumentLineId());
        content.setItemDescription(describe_content.getText().toString());
        content.setAcceptedQuantity(new BigDecimal(product_acceptedcount.getText().toString()));
        content.setVersion(version);
        content.setRequesterId(Operator.getOperator().getAccount());
        Map<String, Object> m = new HashMap<>();
        if (inventoryAttribute == null) {
            for (Map.Entry<String, Object> entry : info.getInventoryattribute().entrySet()) {
                if (!entry.getKey().equals("Version")) {
                    m.put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            for (InventoryAttribute.AttributesItem b : inventoryAttribute.getAttributes()) {
                m.put(b.getAttributeName(), b.getValue());
            }
            List<String> list = new ArrayList<>();
            for (StatusItem i : statusItems) {
                if (i.isChecked()) {
                    list.add(i.getStatusId());
                }
            }
            content.setDamageStatusIds((String[]) list.toArray());
        }
        if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
            m.put("ImageUrl", ou.getUploadImages().get(0).getObjectUrl());
        }
        content.setAttributeSetInstance(m);
        content.setRejectedQuantity(new BigDecimal(0));
        content.setDamagedQuantity(new BigDecimal(0));
        content.setCommandId(GUID.getUUID(content));
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.CommitShipmentItem, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                    addLineImageURL();
                    return;
                }
                ToastUtil.showToast(ShowShipmentActivity.this, "保存成功");
                finish();
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }

    private void addLineImageURL() {
        CreateOrMergePatchShipmentDto.MergePatchShipmentDto patchShipment = new CreateOrMergePatchShipmentDto.MergePatchShipmentDto();
        patchShipment.setShipmentId(o.getDocumentNumber());//单号（Id）
        patchShipment.setVersion(version + 1);// inventoryAttribute current version
        CreateOrMergePatchShipmentReceiptDto.MergePatchShipmentReceiptDto shipmentLine = new CreateOrMergePatchShipmentReceiptDto.MergePatchShipmentReceiptDto();
        shipmentLine.setReceiptSeqId(LineNumber);
        patchShipment.setShipmentReceipts(new CreateOrMergePatchShipmentReceiptDto[]{shipmentLine});

        CreateOrMergePatchShipmentReceiptImageDto[] dto = new CreateOrMergePatchShipmentReceiptImageDto[ou.getUploadImages().size()];
        for (int i = 0; i < ou.getUploadImages().size(); i++) {
            if (ou.getUploadImages().get(i).isUpload()) {
                CreateOrMergePatchShipmentReceiptImageDto.CreateShipmentReceiptImageDto img = new CreateOrMergePatchShipmentReceiptImageDto.CreateShipmentReceiptImageDto();
                img.setSequenceId(ou.getUploadImages().get(i).getName());
                img.setUrl(ou.getUploadImages().get(i).getObjectUrl());
                dto[i] = img;
            }
        }
        shipmentLine.setShipmentReceiptImages(dto);
        patchShipment.setCommandId(GUID.getUUID(patchShipment));
        final String id = NetUtil.makeId(true, NetService.Shipments, o.getDocumentNumber());
        NetUtil.getInstance().Body_NoResponse(this, patchShipment, id, NetService.AddShipmentImage, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                ToastUtil.showToast(ShowShipmentActivity.this, "保存成功");
                finish();
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProductScanRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String s = data.getStringExtra("number");
                    et_product.setTextNoWatch(s);
                    getChooseProductById(s);
                }
            }
        } else if (requestCode == PrelocatorScanRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String s = data.getStringExtra("number");
                    et_prelocator.setText(s);
                }
            }
        } else if (requestCode == TargetlocatorScanRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String s = data.getStringExtra("number");
                    et_targetlocator.setText(s);
                }
            }
        } else if (requestCode == SerialScanRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String s = data.getStringExtra("number");
                    serial_content.setText(s);
                }
            }
        }
    }


    @OnClick({R.id.product_add})
    void click(View v) {
        switch (v.getId()) {
            case R.id.product_add:
                if (findTargetLocator() && checkCommit()) {
                    getVersion();
                }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == bt_reEntry) {
            getInventoryAttribute();
            quanlity_layout.setVisibility(View.VISIBLE);
        }
    }
}

