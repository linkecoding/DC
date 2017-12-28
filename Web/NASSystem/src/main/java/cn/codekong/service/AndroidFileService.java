package cn.codekong.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.codekong.bean.api.base.ResponseModel;
import cn.codekong.bean.card.FileCard;
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
    public ResponseModel<List<FileCard>> getNextDirList(String id) {
        List<FileCard> fileCardList = AndroidFileFactory.getNextDirList(id);
        if (fileCardList == null){
            fileCardList = new ArrayList<FileCard>();
        }

        mLogger.log(Level.WARNING, ResponseModel.buildOk(fileCardList).toString());
        return ResponseModel.buildOk(fileCardList);
    }

    //获取上级级文件目录列表
    @POST
    @Path("/getpredirlist")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<FileCard>> getPreDirList(String parentId) {
        List<FileCard> fileCardList = AndroidFileFactory.getPreDirList(parentId);
        if (fileCardList == null){
            fileCardList = new ArrayList<FileCard>();
        }
        return ResponseModel.buildOk(fileCardList);
    }
}

