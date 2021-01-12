package com.takumi.wms.utils;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.OutInventoryAtt;
import com.takumi.wms.model.Product;

import java.util.HashMap;
import java.util.Map;

public class AttrInflater {

    private LinearLayout v;
    private Button bt_reEntry;


    public AttrInflater(final InventoryAttribute set, Context context, ViewGroup vg, int position) {
        if (set != null) {
            for (final InventoryAttribute.AttributesItem bean : set.getAttributes()) {
                if (!"weightKg".equals(bean.getAttributeName())) {
                    v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.product_item, null);
                    v.setTag(bean.getAttributeName());
                    TextView tv1 = v.findViewById(R.id.product_title);
                    EditText tv2 = v.findViewById(R.id.product_content);
                    ImageView iv = v.findViewById(R.id.product_required);
                    if (bean.getAttributeName().equals("POReference")) {
                        tv1.setText("合同号：");
                        tv2.setId(R.id.po_reference);
                    } else {
                        tv1.setText(bean.getAttributeName() + "：");
                    }
                    if (bean.getMandatory()) {
                        iv.setVisibility(View.VISIBLE);
                    }
                    setType(tv2, bean);
                    tv2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            bean.setValue(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    vg.addView(v);
                }
            }
        }
    }


    public AttrInflater(final Product productAttribute, Context context, ViewGroup vg) {
        if (!TextUtils.isEmpty(productAttribute.getProductName())) {
            addUneditItem(context, vg, "产品名称", productAttribute.getProductName());
        }
        /*if (!TextUtils.isEmpty(productAttribute.getDescription())) {
            addUneditItem(context, vg, "描述", productAttribute.getDescription());
        }*/
        if (!TextUtils.isEmpty(productAttribute.getGrade())) {
            addUneditItem(context, vg, "grade", productAttribute.getGrade());
        }
        if (!TextUtils.isEmpty(productAttribute.getGsm())) {
            addUneditItem(context, vg, "克重", productAttribute.getGsm());
        }
        if (null != productAttribute.getProductDiameter()) {
            addUneditItem(context, vg, "productDiameter", CombUnit(productAttribute.getProductDiameter(), productAttribute.getDiameterUomId()));
        }
        if (null != productAttribute.getOutsideDiameter()) {
            addUneditItem(context, vg, "卷径", CombUnit(productAttribute.getOutsideDiameter(), productAttribute.getDiameterUomId()));
        }
        if (null != productAttribute.getCoreDiameter()) {
            addUneditItem(context, vg, "卷芯", CombUnit(productAttribute.getCoreDiameter(), productAttribute.getDiameterUomId()));
        }
        if (null != productAttribute.getProductWidth()) {
            addUneditItem(context, vg, "幅宽", CombUnit(productAttribute.getProductWidth(), productAttribute.getWidthUomId()));
        }
        /*if (!TextUtils.isEmpty(productAttribute.getBrandName())) {
            addUneditItem(context, vg, "brandName", productAttribute.getBrandName());
        }*/
        if (null != productAttribute.getMoisturePct()) {
            addUneditItem(context, vg, "moisturePct", productAttribute.getMoisturePct() + "");
        }
        if (null != productAttribute.getQuantityUomId()) {
            addUneditItem(context, vg, "计量单位", productAttribute.getQuantityUomId());
        }


        if (productAttribute != null && productAttribute.getAttributes() != null) {
            for (final Map.Entry<String, Object> bean : productAttribute.getAttributes().entrySet()) {
                addUneditItem(context, vg, bean.getKey(), bean.getValue() + "");
            }
        }
    }

    private String CombUnit(Double n, String u) {
        return n + "(" + u + ")";
    }

    private void addUneditItem(Context context, ViewGroup vg, String key, String value) {
        v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.product_item_unedit, null);
        TextView tv1 = v.findViewById(R.id.product_title);
        TextView tv2 = v.findViewById(R.id.product_content);
        tv1.setText(key + "：");
        tv2.setText(value);
        vg.addView(v);
    }

    public <T> AttrInflater(final Map<String, T> map, boolean enable, Context context, ViewGroup vg, boolean canEntry) {
        for (final Map.Entry<String, T> entry : map.entrySet()) {
            if (FilterField(entry)) {
                String s = entry.getKey();
                replaceWord(s);
                v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.sumgroup_item, null);
                TextView tv1 = v.findViewById(R.id.product_title);
                TextView tv2 = v.findViewById(R.id.product_content);
                tv1.setText(s + "：");
                tv2.setText(entry.getValue().toString());
                if (canEntry && s.equals("AttributeSetInstanceId")) {
                    bt_reEntry = new Button(context);
                    bt_reEntry.setText("重新录入");
                    v.addView(bt_reEntry);
                }
                tv2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        entry.setValue((T) s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                vg.addView(v);
            }
        }
    }

    public <T> AttrInflater(final DocumentLine line, boolean enable, Context context, ViewGroup vg, boolean canEntry) {
        if (line.getCount() != null) {
            Map<String, Object> m = new HashMap<>();
            m.put("数量", line.getCount() + "");
            addProduct_item(m.entrySet().iterator().next(), enable, context, vg);
        }
        for (final Map.Entry<String, Object> entry : line.getInventoryattribute().entrySet()) {
            if (FilterField(entry)) {
                addProduct_item(entry, enable, context, vg);
            }
        }
    }

    private <T> void addProduct_item(final Map.Entry<String, Object> entry, boolean enable, Context context, ViewGroup vg) {
        if (entry.getValue() != null && !TextUtils.isEmpty(entry.getValue() + "")) {
            String s = entry.getKey();
            replaceWord(s);
            v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.product_item, null);
            TextView tv1 = v.findViewById(R.id.product_title);
            EditText tv2 = v.findViewById(R.id.product_content);
            if (!s.equals("serialNumber")) {
                tv1.setText(s + "：");
            } else {
                tv1.setText("卷号：");
            }
            tv2.setText(entry.getValue() + "");
            tv2.setEnabled(enable);
            tv2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    entry.setValue((T) s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            if (!entry.getKey().equals("serialNumber")) {
                vg.addView(v);
            } else {
                vg.addView(v, 0);
            }
        }
    }


    public AttrInflater(final OutInventoryAtt att, boolean enable, Context context, ViewGroup vg, int position) {
        for (final Map.Entry<String, Object> entry : att.getAttributes().entrySet()) {
            String s = entry.getKey();
            if (!"AttributeSetInstanceId".equals(s) && !"serialNumber".equals(s)
                    && !"Version".equals(s) && !"attributeSetId".equals(s) && !"ImageUrl".equalsIgnoreCase(s)) {
                s = replaceWord(s);
                vg.addView(createNormalView(s + "：", entry.getValue() + "", enable, context, new ValueChangeListener() {
                    @Override
                    public void valueChange(String s) {
                        entry.setValue(s);
                    }
                }));
            }
        }
    }

    public static String replaceWord(String s) {
        switch (s) {
            case "description":
                s = "描述";
                break;
            case "lotId":
                s = "批次号";
                break;
            case "POReference":
                s = "合同号";
                break;
        }
        return s;
    }

    private View createNormalView(String key, String value, boolean enable, Context context, final ValueChangeListener listener) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_item, null);
        TextView tv1 = v.findViewById(R.id.product_title);
        EditText tv2 = v.findViewById(R.id.product_content);
        tv1.setText(key);
        tv2.setText(value);
        tv2.setEnabled(enable);

        tv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.valueChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    private View createButtonView(String key, String value, boolean enable, Context context, final ValueChangeListener listener) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_item_button, null);
        TextView tv1 = v.findViewById(R.id.product_title);
        EditText tv2 = v.findViewById(R.id.product_content);
        Button bt = v.findViewById(R.id.item_button);
        tv1.setText(key);
        tv2.setText(value);
        tv2.setEnabled(enable);
        bt_reEntry = bt;
        tv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.valueChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    public Button getBt_reEntry() {
        return bt_reEntry;
    }

    private void setType(EditText tv2, InventoryAttribute.AttributesItem bean) {
        if (bean.getAttributeValueType() != null) {
            switch (bean.getAttributeValueType()) {
                case "Decimal":
                case "Int32":
                    tv2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
            }
        }
    }

    public View getV() {
        return v;
    }


    interface ValueChangeListener {
        void valueChange(String s);
    }

    public static boolean FilterField(Map.Entry entry) {
        String s = (String) entry.getKey();
        if (entry.getValue() != null /*&& !s.equals("serialNumber")*/ && !s.equals("attributeSetId") && !s.equals("ImageUrl") && !s.equals("AttributeSetInstanceId") && !s.equals("Version") && !s.equals("weightKg")) {
            return true;
        }
        return false;
    }
}
