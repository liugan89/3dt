package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.OutInventoryAtt;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.StatusItem;
import com.takumi.wms.model.Warehouse;
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

public class AddInLineActivity extends LineBaseActivty {

    private String LineNumber;
    private boolean isQuery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setChooseProductNull();
        ll_image_upload.setVisibility(View.VISIBLE);
        initImageRV(true, rv_image_upload, ou.getUploadImages());
        setShowInOutLocator(false);
        setTargetlocatorChoosable(true);
        setProdctIdChoosable(true);
        setTitle("添加行项");
        serial_query.setVisibility(View.VISIBLE);
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
        NetUtil.getInstance().NoBody_Response(AddInLineActivity.this, null, id, NetService.getAttrSet, new NetUtil.MyCallBack<InventoryAttribute>() {
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
    protected void showInventoryInfo(InventoryAttribute set) {
        if (!isQuery) {
            ll_line_info.removeAllViews();//移除所有显示产品信息的布局
            serial_layout.setVisibility(View.GONE);
            if (chooseProduct.isSerialNumbered()) {//判断是否为批号管理的产品
                serial_layout.setVisibility(View.VISIBLE);
                new AttrInflater(set, this, ll_line_info, 0);
            } else {
                serial_layout.setVisibility(View.GONE);
            }
            if (!chooseProduct.isSerialNumbered() && !chooseProduct.isManagedByLot()) {
                serial_layout.setVisibility(View.GONE);
                new AttrInflater(set, this, ll_line_info, 0);
            }
        }

        if (chooseProduct.isManagedByLot()) {
            showLot();
        }
        isQuery = false;
    }


    private boolean checkCommit() {
        if (serial_layout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(serial_content.getText().toString())) {
                ToastUtil.showToast(this, "请填写卷号");
                return false;
            }
        }
        if (outInventoryAtt == null) {
            if (chooseProduct.isSerialNumbered() && inventoryAttribute != null) {
                for (InventoryAttribute.AttributesItem b : inventoryAttribute.getAttributes()) {
                    if (b.getMandatory() && TextUtils.isEmpty(b.getValue())) {
                        ToastUtil.showToast(this, "请填写必填项");
                        return false;
                    }
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
                    product_add.setEnabled(true);
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void getSerialInInfo() {
        outInventoryAtt = null;
        serialNumber = serial_content.getText().toString();
        String id = NetUtil.makeId(true, NetService.queries, NetService.InventoryAttribute, "serial");
        Map<String, String> map = new HashMap<>();
        map.put("serialNumber", serial_content.getText().toString());
        map.put("warehouseId", Warehouse.getHouse().getWarehouseId());
        NetUtil.getInstance().NoBody_Response(AddInLineActivity.this, map, id, NetService.getOutInventoryAtt, new NetUtil.MyCallBack<OutInventoryAtt>() {
            @Override
            public void onSuccess(OutInventoryAtt result) {
                if (result.getAttributes().get("POReference") != null && bean != null && !result.getAttributes().get("POReference").equals(bean.getPoNumberItem())) {
                    ToastUtil.showToast(AddInLineActivity.this, "合同号不符");
                    return;
                }
                if (bean != null && !result.getProductId().equals(bean.getProductAttributeDto().getProductId())) {
                    ToastUtil.showToast(AddInLineActivity.this, "产品ID不匹配");
                    return;
                }
                outInventoryAtt = result;
                getChooseProductById(result.getProductId());
                product_quantity.setText(result.getCount() + "");
                et_prelocator.setText(outInventoryAtt.getLocatorId());
                findPreLocator();
                prelocator_next.setVisibility(View.GONE);
                et_prelocator.setEnabled(false);
                String s = (String) result.getAttributes().get("ImageUrl");
                if (s != null) {
                    String[] ss = s.split(",");
                    ou.downloadPics(AddInLineActivity.this, ss, new NetUtil.MyCallBack() {
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
                OutProductInfo(outInventoryAtt, false);
            }

            @Override
            public boolean onError(String msg) {
                ToastUtil.showToast(AddInLineActivity.this, "未查询到该卷号");
                return false;
            }
        });
    }


    protected void addLine() {
        LineNumber = System.currentTimeMillis() + "";
        final String id = NetUtil.makeId(true, NetService.InOuts, o.getDocumentNumber(), NetService._commands, NetService.AddLine);
        final InOutCommandDtos.AddLineRequestContent content = new InOutCommandDtos.AddLineRequestContent();
        content.setProductId(chooseProduct.getProductId());
        content.setLineNumber(LineNumber);
        content.setLocatorId(chooseTargetLocator.getLocatorId());
        List<String> list = new ArrayList<>();
        for (StatusItem i : statusItems) {
            if (i.isChecked()) {
                list.add(i.getStatusId());
            }
        }
        String[] l = new String[list.size()];
        list.toArray(l);
        Map<String, Object> m = new HashMap<>();
        if (outInventoryAtt == null && inventoryAttribute != null) {
            if (serial_layout.getVisibility() == View.VISIBLE) {
                m.put("serialNumber", serial_content.getText().toString());
            }
            for (InventoryAttribute.AttributesItem b : inventoryAttribute.getAttributes()) {
                if (b.getMandatory() && TextUtils.isEmpty(b.getValue())) {
                    ToastUtil.showToast(this, "请填写必填项");
                    product_add.setEnabled(true);
                    return;
                }
                m.put(b.getAttributeName(), b.getValue());
            }
        }
        if (outInventoryAtt != null) {
            m.putAll(outInventoryAtt.getAttributes());
        }
        if (chooseProduct.isManagedByLot() && chooseLot != null) {
            m.put("lotId", chooseLot.getLotId());
            //m.put("expirationDate", chooseLot.getExpirationDate());
            //m.put("quantity", chooseLot.getQuantity());
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
        content.setMovementQuantity(new BigDecimal(product_quantity.getText().toString()));
        content.setDocumentNumber(o.getDocumentNumber());
        content.setVersion(o.getVersion());
        content.setRequesterId(Operator.getOperator().getAccount());
        content.setDamageStatusIds(l);
        content.setDescription(describe_content.getText().toString());
        if (content.getAttributeSetInstance().containsKey("POReference")) {
            Object s = content.getAttributeSetInstance().get("POReference");
            content.getAttributeSetInstance().put("_F_C20_3_", s);
            content.getAttributeSetInstance().remove("POReference");
        }
        content.setCommandId(GUID.getUUID(content));
        NetUtil.getInstance().Body_NoResponse(this, content, id, NetService.AddLineFun, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                    addLineImageURL();
                    return;
                }
                ToastUtil.showToast(AddInLineActivity.this, "保存成功");
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
                ToastUtil.showToast(AddInLineActivity.this, "保存成功");
                finish();
            }

            @Override
            public boolean onError(String msg) {
                ToastUtil.showToast(AddInLineActivity.this, "上传图片失败");
                return false;
            }
        });
    }


    @Override
    protected void setChooseProduct(Product p) {
        chooseProduct = p;
        ll_product.setVisibility(View.VISIBLE);
        et_product.setTextNoWatch(p.getProductId());
        product_quantity.setText("0");
        showProdctInfo(p);
        if (chooseProduct.isManagedByLot()) {
            quanlity_layout.setVisibility(View.GONE);
            describe_layout.setVisibility(View.GONE);
        } else {
            quanlity_layout.setVisibility(View.VISIBLE);
            describe_layout.setVisibility(View.VISIBLE);
        }
        if (chooseProduct.isSerialNumbered()) {
            getInventoryAttribute();
        } else {
            showInventoryInfo(null);
        }
    }


    @OnClick({R.id.product_add})
    void click(View v) {
        switch (v.getId()) {
            case R.id.product_add:
                product_add.setEnabled(false);
                if (findTargetLocator() && checkCommit()) {
                    if (ou.getUploadImages() != null && !ou.getUploadImages().isEmpty()) {
                        addNewInOutLine();
                    } else {
                        addLine();
                    }
                } else {
                    product_add.setEnabled(true);
                }
                break;
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
        } else if (requestCode == SerialScanRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String s = data.getStringExtra("number");
                    if (isSerialRepeat(s.toUpperCase())) return;
                    serial_content.setText(s);
                    isQuery = true;
                    getSerialInInfo();
                }
            }
        }
    }
}
