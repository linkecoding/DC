package cn.codekong.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cn.codekong.bean.api.base.ResponseModel;
import cn.codekong.bean.db.File;
import cn.codekong.factory.AndroidFileFactory;

/**
 * Created by 尚振鸿 on 17-12-27. 14:42
 * mail:szh@codekong.cn
 */


@Path("/android/file")
public class AndroidFileService extends BaseUserService{
    //获取下级文件目录列表
    @POST
    @Path("/getnextdirlist")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<File>> getNextDirList(String id) {
        List<File> fileList = AndroidFileFactory.getNextDirList(id);
        return ResponseModel.buildOk(fileList);
    }

    //获取上级级文件目录列表
    @POST
    @Path("/getpredirlist")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<File>> getPreDirList(String parentId) {
        List<File> fileList = AndroidFileFactory.getPreDirList(parentId);
        if (fileList == null){
            fileList = new ArrayList<File>();
        }
        return ResponseModel.buildOk(fileList);
    }
}

