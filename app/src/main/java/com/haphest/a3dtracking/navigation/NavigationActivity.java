package com.haphest.a3dtracking.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haphest.a3dtracking.R;
import com.haphest.a3dtracking.base.BaseActivity;
import com.haphest.a3dtracking.input.InputActivity;
import com.haphest.a3dtracking.navigation.widget.NavigationView;
import com.haphest.a3dtracking.output.OutputActivity;

public class NavigationActivity extends BaseActivity implements NavigationView.Click, View.OnClickListener {

    private NavigationView nv;
    private TextView tv_output;
    private TextView tv_input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        nv = findViewById(R.id.nv);
        nv.setWarehouse(NavigationMap.createWarehouse());
        tv_output = findViewById(R.id.tv_three);
        tv_output.setOnClickListener(this);
        tv_input = findViewById(R.id.tv_two);
        tv_input.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_three:
                startActivity(new Intent(this, OutputActivity.class));
                break;
            case R.id.tv_two:
                startActivity(new Intent(this, InputActivity.class));
                break;
        }
    }
}
