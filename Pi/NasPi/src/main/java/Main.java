import com.google.gson.Gson;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.FileCard;
import listener.RequestCallback;
import okhttp3.Response;
import util.FileUtil;
import util.OkHttpUtil;

/**
 * Created by 尚振鸿 on 17-12-25. 20:57
 * mail:szh@codekong.cn
 */

public class Main {
    private static Logger mLogger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        List<FileCard> fileCardList =  FileUtil.fileSystemToList("/home/szh/phone");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(fileCardList);

        String url = "http://localhost:8080/api/pi/file/uploadfiledirinfo";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("deviceId", "62d5e827-e5aa-457d-b42f-4fe25dcdc124");
        OkHttpUtil.getInstance().post(url, OkHttpUtil.MEDIA_TYPE_JSON, jsonStr, headerMap, new RequestCallback() {
            @Override
            public void onResponse(Response response) {
                mLogger.info("收到 " + response.body());
            }

            @Override
            public void onFailure() {
                mLogger.error("请求失败");
            }
        });
    }
}
