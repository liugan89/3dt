package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.adapter.StringSpinnerAdapter;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.ShipmentDetail;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class CheckShipmentActivity extends LineBaseActivty implements View.OnClickListener {

    private String LineNumber;
    private DocumentLine info;
    private boolean isReEntry;
    private String serialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serial_query.setVisibility(View.VISIBLE);
        setShowInOutLocator(false);
        setTargetlocatorChoosable(true);
        setProdctIdChoosable(false);
        setTitle("产品核对");
        info = getIntent().getParcelableExtra(Constant.Flags.Param2);
        ll_line.setVisibility(View.VISIBLE);
        tv_line_id.setText(info.getDocumentLineId());
        et_product.setTextNoWatch(info.getProductId());
        product_modify.setVisibility(View.VISIBLE);
        serial_content.setText(info.getInventoryattribute().get("serialNumber").toString());
        getChooseProductById(info.getProductId());
        showLineInfo(info);
        product_next.setVisibility(View.GONE);
        //quanlity_layout.setVisibility(View.GONE);
        //ll_acceptedcount_layout.setVisibility(View.VISIBLE);
        product_acceptedcount.setText(info.getAcceptedCount() + "");
        serial_content.setEnabled(false);
        serial_query.setVisibility(View.GONE);
        product_quantity.setText(info.getCount() + "");
        ll_image_upload.setVisibility(View.VISIBLE);
        ll_image_download.setVisibility(View.GONE);
        initImageRV(true, rv_image_upload, ou.getUploadImages());
        initImageRV(false, rv_image_download, ou.getDownloadImages());
        serial_content.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        ou.downloadPics(this, info.getDocumentLineImageUrls(), new NetUtil.MyCallBack() {
            @Override
            public void onSuccess(Object result) {
                downloadGridImageAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onError(String msg) {
                return false;
            }
        });
        damage_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                computeCount();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void computeCount() {
        if (chooseProduct != null && !TextUtils.isEmpty(product_quantity.getText()) && !TextUtils.isEmpty(damage_content.getText())) {
            BigDecimal old = new BigDecimal(info.getCount().toString());
            BigDecimal kezhong = new BigDecimal(chooseProduct.getGsm());
            BigDecimal out = new BigDecimal(chooseProduct.getOutsideDiameter());
            BigDecimal width = new BigDecimal(chooseProduct.getProductWidth());
            BigDecimal damage = new BigDecimal(damage_content.getText().toString());
            product_quantity.setText(old.subtract(kezhong.multiply(out).multiply(width).multiply(damage).multiply(new BigDecimal(0.0000001)).multiply(new BigDecimal(3.14))).setScale(5, BigDecimal.ROUND_HALF_UP).toPlainString());
        } else {
            product_quantity.setText(info.getCount().toPlainString());
        }
    }


    @Override
    protected void showLineInfo(DocumentLine info) {
        if (info != null) {
            if (info.getInventoryattribute() != null) {
                for (final Map.Entry<String, Object> entry : info.getInventoryattribute().entrySet()) {
                    String s = entry.getKey();
                    if ("lotId".equals(s) || "description".equals(s)) {
                        addProduct_item(entry, true, ll_line_info);
                    }
                }
                setStatusItemCheckByMap(info.getDamageStatuss());
            }
        }
    }


    private <T> void addProduct_item(final Map.Entry<String, Object> entry, boolean enable, ViewGroup vg) {
        if (entry.getValue() != null && !TextUtils.isEmpty(entry.getValue() + "")) {
            if (!entry.getKey().equals("serialNumber")) {
                if (!entry.getKey().equals("AttributeSetInstanceId")) {
                    LinearLayout v = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.product_item, null);
                    TextView tv1 = v.findViewById(R.id.product_title);
                    EditText tv2 = v.findViewById(R.id.product_content);
                    tv1.setText(entry.getKey() + "：");
                    tv2.setText(entry.getValue() + "");
                    tv2.setEnabled(enable);
                    tv2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            entry.setValue(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    vg.addView(v);
                } else {
                    vg.addView(createButtonView(entry.getKey(), entry.getValue().toString(), false));
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
        /*if (p.getDescription().equals("牛卡纸")) {
            damage_layout.setVisibility(View.VISIBLE);
        }*/
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
        NetUtil.getInstance().NoBody_Response(CheckShipmentActivity.this, null, id, NetService.getAttrSet, new NetUtil.MyCallBack<InventoryAttribute>() {
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
        NetUtil.getInstance().NoBody_Response(CheckShipmentActivity.this, m, id, NetService.getShipmentVersion, new NetUtil.MyCallBack<ShipmentDetail>() {
            @Override
            public void onSuccess(ShipmentDetail result) {
                product_add.setEnabled(true);
                version = result.getVersion();
                if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                    addNewShipmentLine();
                } else {
                    addLine(version);
                }
            }

            @Override
            public boolean onError(String msg) {
                product_add.setEnabled(true);
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
        content.setLocatorId(chooseTargetLocator.getLocatorId());
        content.setShipmentItemSeqId(info.getDocumentLineId());
        content.setItemDescription(describe_content.getText().toString());
        content.setAcceptedQuantity(new BigDecimal(product_quantity.getText().toString()));
        content.setVersion(version);
        content.setRequesterId(Operator.getOperator().getAccount());
        String[] l = getStatusArray();
        Map<String, Object> m = new HashMap<>();
        if (inventoryAttribute == null) {
            for (Map.Entry<String, Object> entry : info.getInventoryattribute().entrySet()) {
                if (!entry.getKey().equals("Version") && !entry.getKey().equals("weightKg")) {
                    m.put(entry.getKey(), entry.getValue());
                }
            }
        } else {
            for (InventoryAttribute.AttributesItem b : inventoryAttribute.getAttributes()) {
                m.put(b.getAttributeName(), b.getValue());
            }
        }
        m.put("statusId", l);
        m.put("description", describe_content.getText().toString());
        String imageUrl = makeImageUrl();
        if (imageUrl != null) {
            m.put("ImageUrl", imageUrl);
        }
        //m.remove("weightKg");
        m.remove("AttributeSetInstanceId");
        content.setAttributeSetInstance(m);
        content.setDamageStatusIds(l);
        content.setRejectedQuantity(new BigDecimal(0));
        content.setDamagedQuantity(new BigDecimal(0));
        content.setCommandId(GUID.getUUID(content));
        content.setReceiptSeqId(info.getDocumentLineId());
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.CommitShipmentItem, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                    addLineImageURL();
                    return;
                }
                ToastUtil.showToast(CheckShipmentActivity.this, "保存成功");
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
                ToastUtil.showToast(CheckShipmentActivity.this, "保存成功");
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
        }

    }


    private String tempProduct;

    @OnClick({R.id.product_add, R.id.product_modify})
    void click(View v) {
        switch (v.getId()) {
            case R.id.product_add:
                product_add.setEnabled(false);
                if (findTargetLocator() && checkCommit()) {
                    getVersion();
                }
                break;
            case R.id.product_modify:
                NetUtil.getInstance().NoBody_Response(this, null, "api/queries/product/name", NetService.getProductName, new NetUtil.MyCallBack<List<String>>() {
                    @Override
                    public void onSuccess(List<String> result) {
                        AppCompatDialog dialog = new AppCompatDialog(CheckShipmentActivity.this);
                        //dialog.setTitle("重新选择产品");
                        dialog.setContentView(R.layout.dialog_chooseproduct);
                        Spinner sp_product = dialog.findViewById(R.id.sp_product);
                        EditText et_gsm = dialog.findViewById(R.id.et_gsm);
                        EditText et_width = dialog.findViewById(R.id.et_width);
                        Button bt_query = dialog.findViewById(R.id.bt_query);
                        sp_product.setAdapter(new StringSpinnerAdapter(CheckShipmentActivity.this, result));
                        sp_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                tempProduct = result.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        sp_product.setSelection(0);
                        bt_query.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Map<String, String> m = new HashMap<>();
                                m.put("productName", tempProduct);
                                m.put("gsm", et_gsm.getText().toString());
                                m.put("productWith", et_width.getText().toString());
                                NetUtil.getInstance().NoBody_Response(CheckShipmentActivity.this, m, "api/queries/product/no", NetService.getCheckProduct, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                                    @Override
                                    public void onSuccess(Response<ResponseBody> result) {
                                        try {
                                            String p = result.body().string();
                                            if (!TextUtils.isEmpty(p)) {
                                                getChooseProductById(p);
                                            } else {
                                                ToastUtil.showToast(CheckShipmentActivity.this, "找不到产品");
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }finally {
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public boolean onError(String msg) {
                                        return false;
                                    }
                                });
                            }
                        });
                        dialog.show();
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
    public void onClick(View v) {
        if (v == bt_reEntry) {
            getInventoryAttribute();
            quanlity_layout.setVisibility(View.VISIBLE);
        }
    }
}

