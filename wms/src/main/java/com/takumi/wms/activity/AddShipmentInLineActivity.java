package com.takumi.wms.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.SingletonCache;
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

public class AddShipmentInLineActivity extends LineBaseActivty {


    private String LineNumber;
    private int seqId;
    private long version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setChooseProductNull();
        seqId = getIntent().getIntExtra(Constant.Flags.Param2, -1);
        ll_line.setVisibility(View.VISIBLE);
        tv_line_id.setText(seqId + "");
        ll_image_upload.setVisibility(View.VISIBLE);
        ll_acceptedcount_layout.setVisibility(View.GONE);
        initImageRV(true, rv_image_upload, ou.getUploadImages());
        setShowInOutLocator(false);
        setTargetlocatorChoosable(true);
        setProdctIdChoosable(true);
        setTitle("添加行项");

        et_product.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getChooseProductById(v.getText().toString().trim());
                }
                return true;
            }
        });
    }


    /**
     * 得到库存属性
     */
    private void getInventoryAttribute() {

        String id = NetUtil.makeId(true, NetService.queries, NetService.InventoryAttribute, chooseProduct.getAttributeSetId());
        NetUtil.getInstance().NoBody_Response(AddShipmentInLineActivity.this, null, id, NetService.getAttrSet, new NetUtil.MyCallBack<InventoryAttribute>() {
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
        }
        if (chooseProduct.isManagedByLot()) {
            serial_layout.setVisibility(View.GONE);
            showLot();
        }
        if (!chooseProduct.isSerialNumbered() && !chooseProduct.isManagedByLot()) {
            serial_layout.setVisibility(View.GONE);
            new AttrInflater(set, this, ll_line_info, 0);
        }

    }


    private void chooseProduct(int position) {
        chooseProduct = SingletonCache.products.get(position);//获得选中的产品
        inventoryAttribute = null;
        if (chooseProduct.getAttributeSetId() != null) {
            getInventoryAttribute();
            showProdctInfo(chooseProduct);
        } else {
            showInventoryInfo(null);
        }
    }

    private boolean checkCommit() {
        if (serial_layout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(serial_content.getText().toString())) {
                ToastUtil.showToast(this, "请填写卷号");
                return false;
            }
        }
        if (chooseProduct.isSerialNumbered() && inventoryAttribute != null) {
            for (InventoryAttribute.AttributesItem b : inventoryAttribute.getAttributes()) {
                if (b.getMandatory() && TextUtils.isEmpty(b.getValue())) {
                    ToastUtil.showToast(this, "请填写必填项");
                    return false;
                }
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
        return true;
    }

    /**
     * 添加行项
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

    private void getVersion() {
        String id = NetUtil.makeId(true, NetService.Shipments, o.getDocumentNumber());
        Map<String, String> m = new HashMap<>();
        m.put("fields", "version");
        NetUtil.getInstance().NoBody_Response(AddShipmentInLineActivity.this, m, id, NetService.getShipmentVersion, new NetUtil.MyCallBack<ShipmentDetail>() {
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


    private void addLine(long version) {
        final String id = NetUtil.makeId(true, NetService.Shipments, o.getDocumentNumber(), NetService._commands, "ReceiveItem");
        //final ShipmentCommandDtos.AddLineRequestContent content = new ShipmentCommandDtos.AddLineRequestContent();
        final ShipmentCommandDtos.ReceiveItemRequestContent content = new ShipmentCommandDtos.ReceiveItemRequestContent();
        content.setReceiptSeqId(seqId + "");
        content.setShipmentId(o.getDocumentNumber());
        content.setItemDescription(describe_content.getText().toString());
        content.setLocatorId(chooseTargetLocator.getLocatorId());
        content.setVersion(version);
        content.setRequesterId(Operator.getOperator().getAccount());
        String s = product_quantity.getText().toString();
        if (TextUtils.isEmpty(s)) {
            s = "0";
        }
        content.setAcceptedQuantity(new BigDecimal(s));
        List<String> list = new ArrayList<>();
        for (StatusItem i : statusItems) {
            if (i.isChecked()) {
                list.add(i.getStatusId());
            }
        }

        String[] l = new String[list.size()];
        if (list.size() > 0) {
            list.toArray(l);
        } else {
            l = null;
        }
        Map<String, Object> m = new HashMap<>();
        if (inventoryAttribute != null) {
            if (serial_layout.getVisibility() == View.VISIBLE) {
                m.put("serialNumber", serial_content.getText().toString());
            }
            for (InventoryAttribute.AttributesItem b : inventoryAttribute.getAttributes()) {
                if (b.getMandatory() && TextUtils.isEmpty(b.getValue())) {
                    ToastUtil.showToast(this, "请填写必填项");
                    return;
                }
                m.put(b.getAttributeName(), b.getValue());
            }
        }
        if (chooseProduct.isManagedByLot()) {
            m.put("lotId", chooseLot.getLotId());
            //m.put("expirationDate", chooseLot.getExpirationDate());
            //m.put("quantity", chooseLot.getQuantity());
        }
        m.put("StatusId", l);
        m.put("description", describe_content.getText().toString());
        String imageUrl = makeImageUrl();
        if (imageUrl != null) {
            m.put("ImageUrl", imageUrl);
        }
        //m.remove("weightKg");
        content.setAttributeSetInstance(m);
        content.setDamageStatusIds(l);
        content.setRejectedQuantity(new BigDecimal(0));
        content.setDamagedQuantity(new BigDecimal(0));
        content.setCommandId(GUID.getUUID(content));
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.AddShipmentItem, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                    addLineImageURL();
                    return;
                }
                ToastUtil.showToast(AddShipmentInLineActivity.this, "保存成功");
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
        shipmentLine.setReceiptSeqId(seqId + "");
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
                ToastUtil.showToast(AddShipmentInLineActivity.this, "保存成功");
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
    protected void setChooseProduct(Product p) {
        chooseProduct = p;
        ll_product.setVisibility(View.VISIBLE);
        et_product.setTextNoWatch(p.getProductId());
        showProdctInfo(p);
        if (!chooseProduct.isManagedByLot()) {
            getInventoryAttribute();
        } else {
            showInventoryInfo(null);
        }
    }


    @OnClick({R.id.product_add})
    void click(View v) {
        switch (v.getId()) {
            case R.id.product_add:
                BigDecimal fukuan = new BigDecimal(chooseProduct.getProductWidth());
                BigDecimal weight = new BigDecimal(product_quantity.getText().toString());
                if ((weight.multiply(new BigDecimal(0.1))).compareTo(fukuan) == 1) {
                    new AlertDialog.Builder(this)
                            .setTitle("确认添加")
                            .setMessage("同规格重量偏大，确认添加？")
                            .setNegativeButton("取消", (d, i) -> {
                                d.dismiss();
                            })
                            .setPositiveButton("确认", (d, i) -> {
                                d.dismiss();
                                clickAddLine();
                            }).show();
                } else if ((weight.multiply(new BigDecimal(0.1)).compareTo(fukuan.multiply(new BigDecimal(0.9))) == -1)) {
                    new AlertDialog.Builder(this)
                            .setTitle("确认添加")
                            .setMessage("同规格重量偏小，确认添加？")
                            .setNegativeButton("取消", (d, i) -> {
                                d.dismiss();
                            })
                            .setPositiveButton("确认", (d, i) -> {
                                d.dismiss();
                                clickAddLine();
                            }).show();
                } else {
                    clickAddLine();
                }
                break;
        }
    }

    private void clickAddLine() {
        product_add.setEnabled(false);
        if (findTargetLocator() && checkCommit()) {
            getVersion();
        }
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
}
