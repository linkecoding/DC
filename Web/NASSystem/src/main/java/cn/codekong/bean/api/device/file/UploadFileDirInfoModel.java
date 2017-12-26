package cn.codekong.bean.api.device.file;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

import java.util.List;

import cn.codekong.bean.card.FileCard;

/**
 * Created by 尚振鸿 on 17-12-26. 13:09
 * mail:szh@codekong.cn
 */

public class UploadFileDirInfoModel {
    @Expose
    private String deviceId;

    @Expose
    private List<FileCard> fileCardList;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<FileCard> getFileCardList() {
        return fileCardList;
    }

    public void setFileCardList(List<FileCard> fileCardList) {
        this.fileCardList = fileCardList;
    }

    public static boolean check(UploadFileDirInfoModel model){
        return model != null && !Strings.isNullOrEmpty(model.deviceId);
    }
}
