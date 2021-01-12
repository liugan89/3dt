package com.takumi.wms.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.OutInventoryAtt;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.Production;
import com.takumi.wms.model.SingletonCache;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.production.ProductionCommandDtos;
import com.takumi.wms.net2.NetService;
import com.takumi.wms.net2.NetUtil;
import com.takumi.wms.utils.AttrInflater;
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

public class AddProductionOutputActivity extends AddInLineActivity {

    @BindView(R.id.product_condition)
    Button product_condition;

    Production.ProductionLine.ProductionLineInput production;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fun = Function.In;
        setTitle("Add Output");
        product_odd_title.setText("OutputLine： ");
        product_condition.setVisibility(View.VISIBLE);
        production = getIntent().getParcelableExtra("production");
        product_odd_number.setText(Integer.parseInt(o.getDocumentNumber()) + "");
    }

    @OnClick(R.id.product_condition)
    void selectCondition() {
        ConditionProductDialog.Builder builder = new ConditionProductDialog.Builder(this);
        builder.setListener((d, p) -> {
            d.dismiss();
            if (p == null) {
                ToastUtil.showToast(AddProductionOutputActivity.this, "未选择产品");
            } else {
                setChooseProduct(p);
            }
        });
        builder.create().show();
    }

    @Override
    protected void getSerialInInfo() {
        outInventoryAtt = null;
        serialNumber = serial_content.getText().toString();
        String id = com.takumi.wms.net.NetUtil.makeId(true, com.takumi.wms.net.NetService.queries, com.takumi.wms.net.NetService.InventoryAttribute, "serial");
        Map<String, String> map = new HashMap<>();
        map.put("serialNumber", serial_content.getText().toString());
        map.put("warehouseId", Warehouse.getHouse().getWarehouseId());
        com.takumi.wms.net.NetUtil.getInstance().NoBody_Response(AddProductionOutputActivity.this, map, id, com.takumi.wms.net.NetService.getOutInventoryAtt, new com.takumi.wms.net.NetUtil.MyCallBack<OutInventoryAtt>() {
            @Override
            public void onSuccess(OutInventoryAtt result) {
                if (result.getAttributes().get("POReference") != null && bean != null && !result.getAttributes().get("POReference").equals(bean.getPoNumberItem())) {
                    ToastUtil.showToast(AddProductionOutputActivity.this, "合同号不符");
                    return;
                }
                if (bean != null && !result.getProductId().equals(bean.getProductAttributeDto().getProductId())) {
                    ToastUtil.showToast(AddProductionOutputActivity.this, "产品ID不匹配");
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
                    ou.downloadPics(AddProductionOutputActivity.this, ss, new com.takumi.wms.net.NetUtil.MyCallBack() {
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
                ToastUtil.showToast(AddProductionOutputActivity.this, "未查询到该卷号");
                return false;
            }
        });
    }

    @Override
    protected void showInventoryInfo(InventoryAttribute set) {
        ll_line_info.removeAllViews();//移除所有显示产品信息的布局
        serial_layout.setVisibility(View.GONE);
        if (chooseProduct.isSerialNumbered()) {//判断是否为批号管理的产品
            serial_layout.setVisibility(View.VISIBLE);
            new AttrInflater(set, this, ll_line_info, 0);
            TextView po = ll_line_info.findViewById(R.id.po_reference);
            if (po != null) {
                po.setText(production.getAttributeSetInstance().get("POReference") == null ? "" : production.getAttributeSetInstance().get("POReference").toString());
            }
            serial_content.setText(production.getAttributeSetInstance().get("serialNumber") == null ? "" :
                    production.getAttributeSetInstance().get("serialNumber").toString() + "-" +
                            (Integer.parseInt(o.getDocumentNumber())));
        } else {
            serial_layout.setVisibility(View.GONE);
        }
        if (!chooseProduct.isSerialNumbered() && !chooseProduct.isManagedByLot()) {
            serial_layout.setVisibility(View.GONE);
            new AttrInflater(set, this, ll_line_info, 0);
        }

        if (chooseProduct.isManagedByLot()) {
            showLot();
        }
    }

    @Override
    protected void addLine() {
        ProductionCommandDtos.AddProductionLineOutputRequestContent dto = new ProductionCommandDtos.AddProductionLineOutputRequestContent();
        dto.setVersion(o.getVersion());
        dto.setProductionId(o.getProductionId());
        dto.setLineNumber(o.getLineNumber() + "");
        dto.setProductionLineOutputSeqId(o.getDocumentNumber());
        dto.setProductId(chooseProduct.getProductId());
        dto.setLocatorId(chooseTargetLocator.getLocatorId());
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
        dto.setAttributeSetInstance(m);
        dto.setQuantity(new BigDecimal(product_quantity.getText().toString()));
        dto.setCommandId(GUID.getUUID(dto));
        NetUtil.getInstance().DialogCall(this, NetService.GetUrl(NetService.addProductionOutput, dto.getProductionId()), null, dto, NetUtil.PUT, null, new NetUtil.MyCallBack<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> result) {
                ToastUtil.showToast(AddProductionOutputActivity.this, "添加成功");
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
