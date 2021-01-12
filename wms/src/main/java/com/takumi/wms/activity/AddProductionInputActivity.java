package com.takumi.wms.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.takumi.wms.R;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.OutInventoryAtt;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.production.ProductionCommandDtos;
import com.takumi.wms.net2.NetService;
import com.takumi.wms.net2.NetUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;
import com.takumi.wms.widget.ConditionProductDialog;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AddProductionInputActivity extends AddOutLineActivity {

    @BindView(R.id.ll_document)
    LinearLayout ll_document;
    @BindView(R.id.product_condition)
    Button product_condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fun = Function.Out;
        setTitle("Add Input");
        product_odd_title.setText("InputLine： ");
        setProdctIdChoosable(false);
        product_next.setVisibility(View.GONE);
        setPreLocatorChooseful(false);
        product_odd_number.setText(Integer.parseInt(o.getDocumentNumber()) + "");
        real_layout.setVisibility(View.VISIBLE);
        product_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                real_content.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void getSerialOutInfo() {
        serialNumber = serial_content.getText().toString();
        String id = com.takumi.wms.net.NetUtil.makeId(true, com.takumi.wms.net.NetService.queries, com.takumi.wms.net.NetService.InventoryAttribute, com.takumi.wms.net.NetService.serialNumber);
        Map<String, String> map = new HashMap<>();
        map.put("serialNumber", serial_content.getText().toString());
        map.put("warehouseId", Warehouse.getHouse().getWarehouseId());
        com.takumi.wms.net.NetUtil.getInstance().NoBody_Response(AddProductionInputActivity.this, map, id, com.takumi.wms.net.NetService.getOutInventoryAtt, new com.takumi.wms.net.NetUtil.MyCallBack<OutInventoryAtt>() {
            @Override
            public void onSuccess(OutInventoryAtt result) {
                /*if (result.getAttributes().get("POReference") != null && bean != null && !result.getAttributes().get("POReference").equals(bean.getPoNumberItem())) {
                    ToastUtil.showToast(LineBaseActivty.this, "合同号不符");
                    return;
                }
                if (bean != null && !result.getProductId().equals(bean.getProductAttributeDto().getProductId())) {
                    ToastUtil.showToast(LineBaseActivty.this, "产品ID不匹配");
                    return;
                }*/
                outInventoryAtt = result;
                getChooseProductById(result.getProductId());
                product_quantity.setText(result.getCount() + "");
                real_content.setText(new BigDecimal(result.getCount()).toPlainString());
                et_prelocator.setText(outInventoryAtt.getLocatorId());
                findPreLocator();
                prelocator_next.setVisibility(View.GONE);
                et_prelocator.setEnabled(false);
                String s = (String) result.getAttributes().get("ImageUrl");
                if (s != null) {
                    String[] ss = s.split(",");
                    ou.downloadPics(AddProductionInputActivity.this, ss, new com.takumi.wms.net.NetUtil.MyCallBack() {
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
                ToastUtil.showToast(AddProductionInputActivity.this, "未查询到该卷号");
                return false;
            }
        });
    }


    @OnClick(R.id.product_condition)
    void selectCondition() {
        ConditionProductDialog.Builder builder = new ConditionProductDialog.Builder(this);
        builder.setListener((d, p) -> {
            d.dismiss();
            if (p == null) {
                ToastUtil.showToast(AddProductionInputActivity.this, "未选择产品");
            } else {
                setChooseProduct(p);
            }
        });
        builder.create().show();
    }

    @Override
    protected void addLine() {
        ProductionCommandDtos.AddProductionLineInputRequestContent dto = new ProductionCommandDtos.AddProductionLineInputRequestContent();
        dto.setVersion(o.getVersion());
        dto.setProductionId(o.getProductionId());
        dto.setLineNumber(o.getLineNumber() + "");
        dto.setProductionLineInputSeqId(o.getDocumentNumber());
        dto.setProductId(chooseProduct.getProductId());
        dto.setLocatorId(choosePreLocator.getLocatorId());
        dto.setActualQuantity(new BigDecimal(real_content.getText().toString()));
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
            } else {
                if (serial_layout.getVisibility() == View.VISIBLE) {
                    m.put("serialNumber", serial_content.getText().toString());
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
        dto.setAttributeSetInstance(m);
        dto.setQuantity(new BigDecimal(product_quantity.getText().toString()));
        dto.setCommandId(GUID.getUUID(dto));
        NetUtil.getInstance().DialogCall(this, NetService.GetUrl(NetService.addProductionInput, dto.getProductionId()), null, dto, NetUtil.PUT, null, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                ToastUtil.showToast(AddProductionInputActivity.this, "添加成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public boolean onError(String msg) {
                product_add.setEnabled(true);
                return false;
            }
        });
    }
}
