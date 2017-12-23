package com.codekong.fileexplorer.app;

import android.app.Application;
import android.util.Log;

import com.codekong.fileexplorer.util.DeviceUtil;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by 尚振鸿 on 17-12-23. 15:20
 * mail:szh@codekong.cn
 */

public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: deviceId = " + DeviceUtil.getUniquePsuedoID());

        YunBaManager.start(getApplicationContext());
        YunBaManager.setAlias(getApplicationContext(), DeviceUtil.getUniquePsuedoID(), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken arg0) {
                Log.d(TAG, "set Alias succeed");
            }

            @Override
            public void onFailure(IMqttToken arg0, Throwable arg1) {
                Log.d(TAG, "set Alias failed");
            }
        });
    }
}
