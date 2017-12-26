package util.yunba;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

/**
 * Created by 尚振鸿 on 17-12-26. 10:06
 * mail:szh@codekong.cn
 */

public class YunBaIM implements IOCallback, Runnable {
    private static Logger mLogger = Logger.getLogger(YunBaIM.class);

    private SocketIO mSocket;

    private YunBaSocketCallback mYunBaSocketCallback;


    public YunBaIM(YunBaSocketCallback yunBaSocketCallback) {
        this.mYunBaSocketCallback = yunBaSocketCallback;
    }

    @Override
    public void onMessage(JSONObject json, IOAcknowledge ack) {
        try {
            mYunBaSocketCallback.onMessage(json, ack);
            mLogger.info("Server said:" + json.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String data, IOAcknowledge ack) {
        mYunBaSocketCallback.onMessage(data, ack);
        mLogger.info("Server said: " + data);
    }

    @Override
    public void onError(SocketIOException socketIOException) {
        mYunBaSocketCallback.onError(socketIOException);
        mLogger.error("an Error occured");
        socketIOException.printStackTrace();
    }

    @Override
    public void onDisconnect() {
        mYunBaSocketCallback.onDisconnect();
        mLogger.info("Connection terminated.");
    }

    @Override
    public void onConnect() {
        mYunBaSocketCallback.onConnect();
        mLogger.info("Connection established");
    }

    @Override
    public void on(String event, IOAcknowledge ack, Object... args) {
        mLogger.info("Server triggered event '" + event + "'");

        try {
            if (event.equals(Constant.YUNBA_SOCKET_CONNECT_ACK)) {
               onSocketConnectAck();
            } else if (event.equals(Constant.YUNBA_CONNECT_ACK)) {
                mYunBaSocketCallback.onConnAck((JSONObject) args[0]);
            } else if (event.equals(Constant.YUNBA_SET_ALIAS_ACK)) {
                mYunBaSocketCallback.onSetAliasAck((JSONObject) args[0]);
            } else if (event.equals(Constant.YUNBA_PUB_ACK)){
                mYunBaSocketCallback.onPukAck((JSONObject) args[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSocketConnectAck() throws Exception {
        mLogger.info("onSocketConnectAck");

        String customId = "";
        try {
            List<String> file = java.nio.file.Files.readAllLines(Paths.get(Constant.YUNBA_CUSTOM_ID_FILENAME),
                    Charset.defaultCharset());
            customId = file.get(0);
        } catch (Exception e) {
            customId = UUID.randomUUID().toString();
        }

        // emit connect
        mSocket.emit(Constant.YUNBA_CONNECT_EVENT, new JSONObject(String.format(Constant.YUNBA_CONNECT_CONTENT_FORMAT,
                Constant.YUNBA_IM_APP_KEY, Constant.YUNBA_CUSTOM_ID_FILENAME)));

        try {
            PrintWriter writer = new PrintWriter(Constant.YUNBA_CUSTOM_ID_FILENAME, Charset.defaultCharset().name());
            writer.println(customId);
            writer.close();
        } catch (Exception e) {
        }
    }

    public void onConnAck(JSONObject json) throws Exception {
        mLogger.info("onConnAck success " + json.get("success"));

        mSocket.emit("subscribe", new JSONObject("{'topic': 't1'}"));
    }

    @Override
    public void run() {
        mSocket = new SocketIO();
        try {
            mSocket.connect(Constant.YUNBA_IM_SOCKET_URL, this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
