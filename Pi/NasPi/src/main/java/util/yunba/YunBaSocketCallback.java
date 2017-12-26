package util.yunba;

import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.SocketIOException;

/**
 * Created by 尚振鸿 on 17-12-25. 22:28
 * mail:szh@codekong.cn
 */

public interface YunBaSocketCallback {
    void onConnAck(JSONObject json);
    //当收到消息
    void onMessage(JSONObject json, IOAcknowledge ack);
    void onMessage(String data, IOAcknowledge ack);

    void onError(SocketIOException socketIOException);
    void onConnect();
    void onDisconnect();

    void onSetAliasAck(JSONObject json);
    void onPukAck(JSONObject json);
}
