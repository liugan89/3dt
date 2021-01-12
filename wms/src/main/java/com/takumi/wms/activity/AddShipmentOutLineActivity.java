package com.takumi.wms.activity;

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
import com.takumi.wms.model.OutDocumentInfo;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.Warehouse;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class AddShipmentOutLineActivity extends LineBaseActivty {

    private String LineNumber;
    private int seqId;
    private String serialNumber;

    @BindView(R.id.product_quantity_title)
    TextView product_quantity_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product_quantity_title.setText("出库重量： ");
        seqId = getIntent().getIntExtra(Constant.Flags.Param2, -1);
        serialNumber = getIntent().getStringExtra(Constant.Flags.Param3);
        LineNumber = getIntent().getStringExtra(Constant.Flags.Param4);
        real_layout.setVisibility(View.VISIBLE);
        serial_query.setVisibility(View.VISIBLE);
        setShowInOutLocator(true);
        setPrelocatorChoosable(true);
        setProdctIdChoosable(true);
        setTitle("添加行项");
        quanlity_layout.setVisibility(View.GONE);
        ll_image_upload.setVisibility(View.GONE);
        ll_image_download.setVisibility(View.VISIBLE);
        initImageRV(false, rv_image_download, ou.getDownloadImages());
        describe_layout.setVisibility(View.GONE);
        et_product.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Recovery();
                    getChooseProductById(v.getText().toString().trim());
                }
                return false;
            }
        });
        serial_content.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        serial_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (isSerialRepeat(v.getText().toString().toUpperCase())) return false;
                    getSerialOutInfo();
                }
                return false;
            }
        });
        if (serialNumber != null) {
            serial_content.setText(serialNumber);
            serial_query.setVisibility(View.GONE);
            et_product.setEnabled(false);
            product_next.setVisibility(View.GONE);
            serial_content.setEnabled(false);
        }
        getSerialOutInfo();
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
        if (chooseProduct.isManagedByLot()) {
            setPreLocatorChooseful(true);
        }
        if (chooseProduct.isSerialNumbered()) {
            setPreLocatorChooseful(false);
        }
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
        NetUtil.getInstance().NoBody_Response(AddShipmentOutLineActivity.this, null, id, NetService.getAttrSet, new NetUtil.MyCallBack<InventoryAttribute>() {
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
        return true;
    }

    private long version;

    private void getVersion() {
        String id = NetUtil.makeId(true, NetService.Shipments, o.getDocumentNumber());
        Map<String, String> m = new HashMap<>();
        m.put("fields", "version");
        NetUtil.getInstance().NoBody_Response(AddShipmentOutLineActivity.this, m, id, NetService.getShipmentVersion, new NetUtil.MyCallBack<ShipmentDetail>() {
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
                        public void onNext(Object o) {
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

    private void addLine(long version) {
        final String id = NetUtil.makeId(true, NetService.Shipments, o.getDocumentNumber(), NetService._commands, "IssueItem");
        final ShipmentCommandDtos.IssueItemRequestContent content = new ShipmentCommandDtos.IssueItemRequestContent();
        content.setShipmentId(o.getDocumentNumber());
        content.setItemDescription(describe_content.getText().toString());
        content.setVersion(version);
        content.setRequesterId(Operator.getOperator().getAccount());
        content.setProductId(chooseProduct.getProductId());
        content.setLocatorId(choosePreLocator.getLocatorId());
        Map<String, Object> m = new HashMap<>();
        if (chooseProduct.isSerialNumbered()) {//按序列号
            if (outInventoryAtt != null) {
                m.putAll(outInventoryAtt.getAttributes());
            } else if (inventoryAttribute != null) {
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
                m.put("StatusId", statusItems.get(0).getStatusId());
            }
        } else if (chooseProduct.isManagedByLot()) {//按批次
            m.put("lotId", chooseLot.getLotId());
            //m.put("expirationDate", DateUtil.toDateTime(chooseLot.getExpirationDate()));
            //m.put("quantity", chooseLot.getQuantity());
        } else {//无序列号，无批次
            for (InventoryAttribute.AttributesItem b : inventoryAttribute.getAttributes()) {
                m.put(b.getAttributeName(), b.getValue());
            }
        }
        m.put("description", describe_content.getText().toString());
        String imageUrl = makeImageUrl();
        if (imageUrl != null) {
            m.put("ImageUrl", imageUrl);
        }
        if (!TextUtils.isEmpty(real_content.getText())) {
            m.put("_F_N_0_", new BigDecimal(real_content.getText().toString()));
        }
        //m.remove("weightKg");
        content.setAttributeSetInstance(m);
        content.setVersion(version);
        content.setRequesterId(Operator.getOperator().getAccount());
        content.setItemDescription(describe_content.getText().toString());
        content.setQuantity(new BigDecimal(product_quantity.getText().toString()));
        content.setF_D_1_(new BigDecimal(real_content.getText().toString()));
        LineNumber = getLineNumber();
        if (TextUtils.isEmpty(LineNumber)) {
            ToastUtil.showToast(this, "未找到相应行号");
            return;
        }
        content.setItemIssuanceSeqId(LineNumber + System.currentTimeMillis());
        content.setShipmentItemSeqId(LineNumber);
        content.setCancelQuantity(new BigDecimal(0));
        content.setCommandId(GUID.getUUID(content));
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.CommitShipmentItemOut, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                    addLineImageURL();
                    return;
                }
                ToastUtil.showToast(AddShipmentOutLineActivity.this, "保存成功");
                finish();
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                return false;
            }
        });
    }

    private String getLineNumber() {
        for (OutDocumentInfo.DocumentLinesItem item : outLine.getDocumentLines()) {
            if (Warehouse.getHouse().isLimit()) {
                if (item.getPoNumberItem().equals(outInventoryAtt.getAttributes().get("POReference"))
                        && item.getProductAttributeDto().getProductId().equals(chooseProduct.getProductId())) {
                    return item.getDocumentLineId();
                }
            } else {
                if (item.getProductAttributeDto().getProductId().equals(chooseProduct.getProductId())) {
                    return item.getDocumentLineId();
                }
            }

        }
        return null;
    }

    private void addLineImageURL() {
        CreateOrMergePatchShipmentDto.MergePatchShipmentDto patchShipment = new CreateOrMergePatchShipmentDto.MergePatchShipmentDto();
        patchShipment.setShipmentId(o.getDocumentNumber());//单号（Id）
        patchShipment.setVersion(version + 1);// inventoryAttribute current version
        CreateOrMergePatchShipmentReceiptDto.MergePatchShipmentReceiptDto shipmentLine = new CreateOrMergePatchShipmentReceiptDto.MergePatchShipmentReceiptDto();
        shipmentLine.setShipmentItemSeqId(LineNumber);
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
                ToastUtil.showToast(AddShipmentOutLineActivity.this, "保存成功");
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
                    if (isSerialRepeat(s.toUpperCase())) return;
                    serial_content.setText(s);
                    getSerialOutInfo();
                }
            }
        }
    }

    @OnClick({R.id.product_add})
    void click(View v) {
        switch (v.getId()) {
            case R.id.product_add:
                product_add.setEnabled(false);
                if (findPreLocator() && checkCommit()) {
                    getVersion();
                }
        }
    }
}

