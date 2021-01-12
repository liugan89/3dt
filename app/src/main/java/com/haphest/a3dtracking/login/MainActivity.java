package com.haphest.a3dtracking.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.haphest.a3dtracking.R;
import com.haphest.a3dtracking.base.BaseActivity;
import com.haphest.a3dtracking.cometd.TruckClientTest;
import com.haphest.a3dtracking.model.Operator;
import com.haphest.a3dtracking.model.Token;
import com.haphest.a3dtracking.navigation.NavigationActivity;
import com.haphest.a3dtracking.net.NetService;
import com.haphest.a3dtracking.net.NetUtil;
import com.haphest.a3dtracking.utils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    TruckClientTest client;

    private EditText et_account;
    private EditText et_password;
    private Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new TruckClientTest();
        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                if (TextUtils.isEmpty(et_account.getText().toString())) {
                    ToastUtil.showToast(this, "Account could not be null");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    ToastUtil.showToast(this, "Password could not be null");
                    return;
                }
                Map<String, String> m = new HashMap<>();
                m.put("username", et_account.getText().toString());
                m.put("password", et_password.getText().toString());
                NetUtil.getInstance().DialogCall(this, NetService.login
                        , m, null, NetUtil.POST, null, new NetUtil.MyCallBack<Response<ResponseBody>>() {
                            @Override
                            public void onSuccess(Response<ResponseBody> result) {
                                try {
                                    String s = result.body().string();
                                    s = new JsonParser().parse(s).getAsJsonObject().get("token").getAsString();

                                    String payload = s.substring(s.indexOf(".") + 1, s.lastIndexOf("."));
                                    byte[] payloadBytes = android.util.Base64.decode(payload, android.util.Base64.DEFAULT);
                                    try {
                                        String payloadStr = new String(payloadBytes, "utf8");
                                        Gson gson = new Gson();
                                        Token t = gson.fromJson(payloadStr, Token.class);
                                        Token.setToken(t);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                        throw new RuntimeException("new String(payloadBytes, \"utf8\") ERROR.");
                                    }

                                    NetUtil.getInstance().setToken(s);
                                    Operator.getOperator().setAccount(et_account.getText().toString());
                                    startActivity(new Intent(MainActivity.this, NavigationActivity.class));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public boolean onError(String msg) {
                                return false;
                            }
                        });
                break;
        }
    }
}
