package com.codekong.fileexplorer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.yunba.android.manager.YunBaManager;

/**
 * 接受控制指令的Receiver
 * Created by 尚振鸿 on 17-12-23. 15:27
 * mail:szh@codekong.cn
 */

public class ControlCommandReceiver extends BroadcastReceiver {
    private static final String TAG = "ControlCommandReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

            String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
            String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);

            //在这里处理从服务器发布下来的消息， 比如显示通知栏， 打开 Activity 等等
            StringBuilder showMsg = new StringBuilder();
            showMsg.append("Received message from server: ")
                    .append(YunBaManager.MQTT_TOPIC)
                    .append(" = ")
                    .append(topic)
                    .append(" ")
                    .append(YunBaManager.MQTT_MSG)
                    .append(" = ")
                    .append(msg);
            Log.d(TAG, "onReceive: " + showMsg);
        }
    }
}
