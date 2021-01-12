package com.takumi.wms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.takumi.wms.activity.NoticeListActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.model.Function;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())||JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION.equals(intent.getAction())) {
            Intent i = new Intent();
            i.setClass(context,NoticeListActivity.class);
            i.putExtra(Constant.Flags.MainFlag, Function.OutNotice);
            context.startActivity(i);
        }
    }
}
