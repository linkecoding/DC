package cn.codekong.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.codekong.bean.api.android.file.DirInfoModel;
import cn.codekong.bean.api.base.ResponseModel;
import cn.codekong.bean.card.FileCard;
import cn.codekong.bean.db.User;
import cn.codekong.factory.AndroidFileFactory;

/**
 * Created by 尚振鸿 on 17-12-27. 14:42
 * mail:szh@codekong.cn
 */


@Path("/android/file")
public class AndroidFileService extends BaseUserService{
    private static final Logger mLogger = Logger.getLogger("AndroidNetwork");

    //获取下级文件目录列表
    @POST
    @Path("/getnextdirlist")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<FileCard>> getNextDirList(DirInfoModel model) {
        if(!DirInfoModel.check(model)){
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();

        if(!AndroidFileFactory.checkUserDevicePermission(self.getId(), model.getDeviceId())){
            //用户没有设备访问权限
            return ResponseModel.buildNoPermissionError();
        }

        List<FileCard> fileCardList = AndroidFileFactory.getNextDirList(model.getDeviceId(), model.getDirId());
        if (fileCardList == null){
            fileCardList = new ArrayList<FileCard>();
        }
        return ResponseModel.buildOk(fileCardList);
    }

    //获取上级级文件目录列表
    @POST
    @Path("/getpredirlist")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<FileCard>> getPreDirList(DirInfoModel model) {
        if (!DirInfoModel.check(model)){
            return ResponseModel.buildParameterError();
        }
        List<FileCard> fileCardList = AndroidFileFactory.getPreDirList(model.getDeviceId(), model.getDirId());
        if (fileCardList == null){
            fileCardList = new ArrayList<FileCard>();
        }
        return ResponseModel.buildOk(fileCardList);
    }
}

