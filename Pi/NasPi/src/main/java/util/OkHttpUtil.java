package util;

import java.io.IOException;
import java.util.Map;

import listener.RequestCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp工具类
 * Created by 尚振鸿 on 17-12-25. 16:28
 * mail:szh@codekong.cn
 */

public class OkHttpUtil {
    private OkHttpClient mOkHttpClient;

    public static final String MEDIA_TYPE_JSON = "application/json;charset=utf-8";

    //单例模式
    private OkHttpUtil(){
        mOkHttpClient = new OkHttpClient();
    }

    private static class SingletonHolder{
        private static final OkHttpUtil INSTANCE = new OkHttpUtil();
    }

    public static OkHttpUtil getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 发送请求
     * @param callback
     */
    public void post(String url, String mediaType, String jsonContent,
                     Map<String, String> headerMap, RequestCallback callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), jsonContent);
        Request.Builder builder = new Request.Builder();

        if (headerMap != null){
            for (Map.Entry<String, String> entry : headerMap.entrySet()){
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder
                .post(requestBody)
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response);
            }
        });
    }
}
