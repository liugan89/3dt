package com.takumi.wms.widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.takumi.wms.R;
import com.takumi.wms.model.Product;
import com.takumi.wms.utils.AttrInflater;
import com.takumi.wms.utils.DateUtil;
import com.takumi.wms.utils.ToastUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDialog extends Dialog {
    @BindView(R.id.ll_product)
    LinearLayout ll_product;

    private Context context;
    private Product chooseProduct;


    public ProductDialog(@NonNull Context context, Product chooseProduct) {
        super(context, R.style.dialog);
        this.context = context;
        this.chooseProduct = chooseProduct;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setContentView(R.layout.dialog_product);
        ButterKnife.bind(this);
        new AttrInflater(chooseProduct, context, ll_product);
    }

}
