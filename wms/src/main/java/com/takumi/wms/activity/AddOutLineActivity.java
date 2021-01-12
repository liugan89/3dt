package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.DocumentInfo;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutDto;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutLineDto;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutLineImageDto;
import com.takumi.wms.model.dto.inout.InOutCommandDtos;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.AttrInflater;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class AddOutLineActivity extends LineBaseActivty {

    private String LineNumber;
    private boolean isReEntry;
    private DocumentInfo infos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infos = getIntent().getParcelableExtra(Constant.Flags.Param3);
        serial_query.setVisibility(View.VISIBLE);
        setShowInOutLocator(true);
        setPreLocatorChooseful(true);
        setProdctIdChoosable(true);
        setTitle("添加行项");
        quanlity_layout.setVisibility(View.GONE);
        ll_image_upload.setVisibility(View.GONE);
        ll_image_download.setVisibility(View.VISIBLE);
        initImageRV(true, rv_image_upload, ou.getUploadImages());
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
                    onOutSerialScan(v.getText().toString());
                }
                return false;
            }
        });
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
        /*if (chooseProduct.isManagedByLot()) {
            setPreLocatorChooseful(true);
        }
        if (chooseProduct.isSerialNumbered()) {
            setPreLocatorChooseful(false);
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
        NetUtil.getInstance().NoBody_Response(AddOutLineActivity.this, null, id, NetService.getAttrSet, new NetUtil.MyCallBack<InventoryAttribute>() {
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

    /**
     * 添加出入库行项
     */
    private void addNewInOutLine() {
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
                            addLine();
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
            product_add.setEnabled(true);
            e.printStackTrace();
        }
    }

    protected void addLine() {
        LineNumber = System.currentTimeMillis() + "";
        final String id = NetUtil.makeId(true, NetService.InOuts, o.getDocumentNumber(), NetService._commands, NetService.AddLine);
        final InOutCommandDtos.AddLineRequestContent content = new InOutCommandDtos.AddLineRequestContent();
        content.setProductId(chooseProduct.getProductId());
        content.setLineNumber(LineNumber);
        content.setLocatorId(choosePreLocator.getLocatorId());
        String[] l = getStatusArray();
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
        m.put("statusId", l);
        m.put("description", describe_content.getText().toString());
        String imageUrl = makeImageUrl();
        if (imageUrl != null) {
            m.put("ImageUrl", imageUrl);
        }
        //m.remove("weightKg");
        content.setAttributeSetInstance(m);
        content.setDescription(describe_content.getText().toString());
        content.setQuantityUomId("test");
        content.setMovementQuantity(new BigDecimal(product_quantity.getText().toString()).multiply(new BigDecimal(-1)));
        content.setDocumentNumber(o.getDocumentNumber());
        content.setVersion(o.getVersion());
        content.setRequesterId(Operator.getOperator().getAccount());
        if (chooseProduct.isSerialNumbered()) {
            if (outInventoryAtt != null) {
                content.setDamageStatusIds(new String[]{outInventoryAtt.getAttributes().get("StatusId") + ""});
            } else {
                content.setDamageStatusIds(l);
            }
        }
        content.setDescription(describe_content.getText().toString());
        content.setCommandId(GUID.getUUID(content));
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.AddLineFun, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                    addLineImageURL();
                    return;
                }
                ToastUtil.showToast(AddOutLineActivity.this, "保存成功");
                finish();
            }

            @Override
            public boolean onError(String msg) {
                System.out.println(msg);
                product_add.setEnabled(true);
                return false;
            }
        });
    }

    private void addLineImageURL() {
        CreateOrMergePatchInOutDto.MergePatchInOutDto patchInOut = new CreateOrMergePatchInOutDto.MergePatchInOutDto();
        patchInOut.setDocumentNumber(o.getDocumentNumber());//单号（Id）
        patchInOut.setVersion(o.getVersion() + 1);// inventoryAttribute current version

        CreateOrMergePatchInOutLineDto.MergePatchInOutLineDto inOutLine = new CreateOrMergePatchInOutLineDto.MergePatchInOutLineDto();
        inOutLine.setLineNumber(LineNumber);
        patchInOut.setInOutLines(new CreateOrMergePatchInOutLineDto[]{inOutLine});

        CreateOrMergePatchInOutLineImageDto[] dto = new CreateOrMergePatchInOutLineImageDto[ou.getUploadImages().size()];
        for (int i = 0; i < ou.getUploadImages().size(); i++) {
            if (ou.getUploadImages().get(i).isUpload()) {
                CreateOrMergePatchInOutLineImageDto.CreateInOutLineImageDto img = new CreateOrMergePatchInOutLineImageDto.CreateInOutLineImageDto();
                img.setSequenceId(ou.getUploadImages().get(i).getName());
                img.setUrl(ou.getUploadImages().get(i).getObjectUrl());
                dto[i] = img;
            }
        }
        inOutLine.setInOutLineImages(dto);
        patchInOut.setCommandId(GUID.getUUID(patchInOut));
        final String id = NetUtil.makeId(true, NetService.InOuts, o.getDocumentNumber());
        NetUtil.getInstance().Body_NoResponse(this, patchInOut, id, NetService.AddLineImage, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                ToastUtil.showToast(AddOutLineActivity.this, "保存成功");
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
    protected void onOutSerialScan(String s) {
        if (infos != null) {
            for (DocumentLine l : infos.getDocumentLines()) {
                if (s.equals(l.getInventoryattribute().get("serialNumber"))) {
                    setShowModel();
                    showLineInfo(l);
                    showMyProductInfo(l);
                    return;
                }
            }
        }
        getSerialOutInfo();
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


    @OnClick(R.id.product_add)
    void click(View v) {
        switch (v.getId()) {
            case R.id.product_add:
                product_add.setEnabled(false);
                if (findPreLocator() && checkCommit()) {
                    if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                        addNewInOutLine();
                    } else {
                        addLine();
                    }
                }
        }
    }

}

