package com.takumi.wms.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Lot;
import com.takumi.wms.model.dto.lot.CreateOrMergePatchLotDto;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.utils.DateUtil;
import com.takumi.wms.utils.GUID;
import com.takumi.wms.utils.ToastUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AddLotActivity extends BottomActivity {

    @BindView(R.id.lot_number)
    EditText lot_number;
    @BindView(R.id.lot_time)
    TextView lot_time;
    @BindView(R.id.lot_quantity)
    EditText lot_quantity;
    @BindView(R.id.new_lot_ok)
    Button new_lot_ok;
    @BindView(R.id.iv_lot_scan)
    ImageView iv_lot_scan;

    private static final int ScanLotRequest = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar(R.color.white, true);
        setTitle("添加批次");

    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_add_lot;
    }

    @OnClick({R.id.new_lot_ok, R.id.rl2, R.id.iv_lot_scan})
    void click(View v) {
        switch (v.getId()) {
            case R.id.new_lot_ok:
                if (TextUtils.isEmpty(getLot_number())) {
                    ToastUtil.showToast(this, "请填写批次号");
                    return;
                }
                if (TextUtils.isEmpty(getLot_time())) {
                    ToastUtil.showToast(this, "请填写过期时间");
                    return;
                }
                final Lot lot = new Lot();
                lot.setLotId(getLot_number());
                lot.setExpirationDate(new Date(Long.valueOf(getLot_time())));
                lot.setQuantity(Long.valueOf(getLot_quantity()));

                CreateOrMergePatchLotDto.CreateLotDto dto = new CreateOrMergePatchLotDto.CreateLotDto();
                dto.setActive(true);
                dto.setQuantity(new BigDecimal(lot.getQuantity()));
                dto.setExpirationDate(new Timestamp(lot.getExpirationDate().getTime()));
                dto.setLotId(lot.getLotId());
                //dto.setVersion(0L);
                dto.setCommandId(GUID.getUUID(dto));
                String id = NetUtil.makeId(true, NetService.Lots, getLot_number());
                NetUtil.getInstance().Body_NoResponse(this, dto, id, NetService.addLot, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                    @Override
                    public void onSuccess(Response<ResponseBody> result) {
                        Intent i = new Intent();
                        i.putExtra(Constant.Flags.ResultParam1, lot);
                        setResult(RESULT_OK, i);
                        finish();
                    }

                    @Override
                    public boolean onError(String msg) {
                        System.out.println(msg);
                        return false;
                    }
                });
                break;
            case R.id.rl2:
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        lot_time.setText(year + "年" + month + "月" + dayOfMonth + "日");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dialog.show();
                break;
            case R.id.iv_lot_scan:
                Intent i = new Intent(this, WeChatCaptureActivity.class);
                startActivityForResult(i, ScanLotRequest);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ScanLotRequest) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra("number");
                lot_number.setText(s);
            }
        }
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
