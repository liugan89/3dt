package com.takumi.wms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.takumi.wms.R;
import com.takumi.wms.base.BottomActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Function;
import com.takumi.wms.model.ShipmentDetail;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShipBaseActivity extends BottomActivity {

    @BindView(R.id.et_shipment_search)
    EditText et_shipment_search;
    @BindView(R.id.iv_shipment_scan)
    ImageView iv_shipmen_scan;
    @BindView(R.id.rv_shipment_undone)
    RecyclerView rv_shipment_undone;
    @BindView(R.id.bt_add_shipment)
    Button bt_add_shipment;


    protected List<ShipmentDetail> shipmentDetails;//全部列表
    protected List<ShipmentDetail> filteredShipmentDetails;//模糊查询过滤后的列表
    protected ShipmentDetail o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar(R.color.white, true);
        fun = (Function) getIntent().getSerializableExtra(Constant.Flags.MainFlag);
        setTitle(fun.getName());
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_shipment_undone.setLayoutManager(llm);
        rv_shipment_undone.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        filteredShipmentDetails = new LinkedList<>();
        et_shipment_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredShipmentDetails.clear();
                for (ShipmentDetail o : shipmentDetails) {
                    if (o.getShipmentId().contains(s.toString().toUpperCase())) {
                        filteredShipmentDetails.add(o);
                    }
                }
                initRV(filteredShipmentDetails);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_shipment_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    findDocs(et_shipment_search.getText().toString());
                }
                return false;
            }
        });
    }

    protected void initRV(List<ShipmentDetail> os) {
    }

    protected void goInNext(ShipmentDetail o) {
        Intent intent = new Intent(ShipBaseActivity.this, ShipmentDetailInActivity.class);
        intent.putExtra(Constant.Flags.MainFlag, fun);
        intent.putExtra(Constant.Flags.Param1, o);
        startActivity(intent);
    }

    protected void goOutNext(ShipmentDetail o) {
        Intent intent = new Intent(ShipBaseActivity.this, ShipmentDetailOutActivity.class);
        intent.putExtra(Constant.Flags.MainFlag, fun);
        intent.putExtra(Constant.Flags.Param1, o);
        startActivity(intent);
    }

    protected void findDocs(String id) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_shipment_search.getText().clear();
    }

    @OnClick(R.id.iv_shipment_scan)
    void iv_shipment_scan() {
        Intent i = new Intent(this, WeChatCaptureActivity.class);
        startActivityForResult(i, SerialScanRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SerialScanRequest) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra("number");
                et_shipment_search.setText(s);
                findDocs(s);
            }
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_shipment;
    }
}
