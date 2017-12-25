package listener;

import okhttp3.Response;

/**
 * Created by 尚振鸿 on 17-12-25. 16:40
 * mail:szh@codekong.cn
 */

public interface RequestCallback {
    void onResponse(Response response);
    void onFailure();
}
