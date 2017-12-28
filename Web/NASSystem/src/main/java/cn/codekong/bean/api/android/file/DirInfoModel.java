package cn.codekong.bean.api.android.file;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

/**
 * Created by 尚振鸿 on 17-12-28. 16:13
 * mail:szh@codekong.cn
 */

public class DirInfoModel {
    //目录Id
    @Expose
    private String dirId;
    //设备Id
    @Expose
    private String deviceId;

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public static boolean check(DirInfoModel model){
        return model != null
                && !Strings.isNullOrEmpty(model.deviceId)
                && !Strings.isNullOrEmpty(model.dirId);
    }
}
