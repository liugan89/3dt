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

import com.takumi.wms.R;
import com.takumi.wms.utils.DateUtil;
import com.takumi.wms.utils.ToastUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LotDialog extends Dialog {
    @BindView(R.id.lot_number)
    EditText lot_number;
    @BindView(R.id.lot_time)
    EditText lot_time;
    @BindView(R.id.lot_quantity)
    EditText lot_quantity;

    LotOKListener listener;
    private Context context;

    public LotDialog(@NonNull Context context) {
        this(context, R.style.dialog);
    }

    public LotDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.product_new_lot);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.new_lot_ok, R.id.lot_time})
    void click(View v) {
        switch (v.getId()) {
            case R.id.new_lot_ok:
                if (TextUtils.isEmpty(getLot_number())) {
                    ToastUtil.showToast(context, "请填写批次号");
                    return;
                }
                if (TextUtils.isEmpty(getLot_time())) {
                    ToastUtil.showToast(context, "请填写过期时间");
                    return;
                }
                if (listener != null) {
                    listener.lotOK();
                }
                dismiss();
                break;
            case R.id.lot_time:
                Calendar c = null;
                c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        lot_time.setText(year + "年" + month + "月" + dayOfMonth + "日");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dialog.show();
                break;
        }
    }

    public void setLotOKListener(LotOKListener listener) {
        this.listener = listener;
    }

    public String getLot_number() {
        return lot_number.getText().toString();
    }

    public void setLot_number(String lot_number) {
        this.lot_number.setText(lot_number);
    }

    public String getLot_time() {
        return DateUtil.YMDToLong(lot_time.getText().toString()) + "";
    }

    public void setLot_time(String lot_time) {
        this.lot_time.setText(lot_time);
    }

    public String getLot_quantity() {
        String s = lot_quantity.getText().toString();
        return TextUtils.isEmpty(s) ? "0" : s;
    }

    public void setLot_quantity(String lot_quantity) {
        this.lot_quantity.setText(lot_quantity);
    }
}
