package cn.codekong.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.codekong.bean.api.base.ResponseModel;
import cn.codekong.bean.card.FileCard;
import cn.codekong.factory.FileFactory;

/**
 * Created by 尚振鸿 on 17-12-26. 12:01
 * mail:szh@codekong.cn
 */

@Path("/pi/file")
public class FileService extends BaseDeviceService {

    @POST
    @Path("/uploadfiledirinfo")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<String> uploadFileDirInfo(List<FileCard> fileCardList) {
        FileFactory.uploadFileDirInfo(getSelf(), fileCardList);
        return ResponseModel.buildOk();
    }
}
