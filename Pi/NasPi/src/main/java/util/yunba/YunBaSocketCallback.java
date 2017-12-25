package util.yunba;

import org.json.JSONObject;

/**
 * Created by 尚振鸿 on 17-12-25. 22:28
 * mail:szh@codekong.cn
 */

public interface YunBaSocketCallback {
    void onSocketConnectAck();
    void onConnAck(JSONObject json);
    //订阅ack
    void onSubAck(JSONObject json);
}
