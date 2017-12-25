package factory;

import com.google.gson.Gson;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bean.FileCard;
import bean.ResponseModel;
import config.RequestParam;
import listener.RequestCallback;
import okhttp3.Response;
import util.FileUtil;
import util.OkHttpUtil;

/**
 * Created by 尚振鸿 on 17-12-25. 21:02
 * mail:szh@codekong.cn
 */

public class FileFactory {
    private static Logger mLogger = Logger.getLogger(FileFactory.class);
    /**
     * 上传文件目录信息
     * @param rootPath
     */
    public static void uploadFileDirInfo(String token, String rootPath){
        ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
        singleExecutor.submit(new Runnable() {
            @Override
            public void run() {
                List<FileCard> fileCardList = FileUtil.fileSystemToList(rootPath);
                Gson gson = new Gson();
                String jsonStr = gson.toJson(fileCardList);
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put(RequestParam.TOKEN, token);

                OkHttpUtil.getInstance().post(RequestParam.UPLOAD_FILE_DIR_INFO_URL, OkHttpUtil.MEDIA_TYPE_JSON,
                        jsonStr, headerMap, new RequestCallback() {
                            @Override
                            public void onResponse(Response response) {
                                if (response.isSuccessful()){
                                    if (response.body() != null){
                                        String body = response.body().toString();
                                        ResponseModel<String> responseModel = gson.fromJson(body, ResponseModel.class);
                                        mLogger.info(responseModel);
                                    }
                                }
                            }

                            @Override
                            public void onFailure() {
                                mLogger.error("请求失败");
                            }
                        });
            }
        });

    }
}
