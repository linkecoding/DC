package util.yunba;

/**
 * Created by 尚振鸿 on 17-12-26. 10:01
 * mail:szh@codekong.cn
 */

public class Constant {
    //云巴消息通信的APP_KEY
    public static final String YUNBA_IM_APP_KEY = "5a3deb9dc58f91fa31b3d004";
    //云巴消息通信请求的url
    public static final String YUNBA_IM_SOCKET_URL = "http://sock.yunba.io:3000/";
    //存储自定义Id的文件名
    public static final String YUNBA_CUSTOM_ID_FILENAME = "customid.dat";;


    //连接服务器事件
    public static final String YUNBA_CONNECT_EVENT = "connect";
    public static final String YUNBA_CONNECT_CONTENT_FORMAT = "{'appkey': '%s', 'customid': '%s'}";

    //设置别名事件
    public static final String YUNBA_SET_ALIAS_EVENT = "set_alias";
    public static final String YUNBA_SET_ALIAS_CONTENT_FORMAT = "{'alias': '%s'}";

    //向别名发消息
    public static final String YUNBA_PUBLISH_TO_ALIAS_EVENT = "publish_to_alias";
    public static final String YUNBA_PUBLISH_TO_ALIAS_CONTENT_FORMAT = "{'alias': '%s', 'msg': '%s'}";

    //获取在线状态
    public static final String YUNBA_GET_ONLINE_EVENT = "get_state";
    public static final String YUNBA_GET_ONLINE_CONTENT_FORMAT = "{'alias': '%s'}";

    /***************************************IM通信回应**************************************/
    public static final String YUNBA_SOCKET_CONNECT_ACK = "socketconnectack";
    public static final String YUNBA_CONNECT_ACK = "connack";


    public static final String YUNBA_SET_ALIAS_ACK = "set_alias_ack";
    public static final String YUNBA_PUB_ACK = "puback";







}
