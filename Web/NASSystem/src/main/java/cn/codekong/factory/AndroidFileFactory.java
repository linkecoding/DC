package cn.codekong.factory;

import java.util.List;

import cn.codekong.bean.db.File;
import cn.codekong.utils.Hib;

/**
 * Created by 尚振鸿 on 17-12-27. 14:46
 * mail:szh@codekong.cn
 */

public class AndroidFileFactory {


    /**
     * 获取给定目录的下级目录文件列表
     *
     * @param id
     * @return
     */
    public static List<File> getNextDirList(String id) {
        return Hib.query(session -> session
                .createQuery("from File where parentId=:parentId")
                .setParameter("parentId", id).list());
    }

    /**
     * 获取给定目录的上级目录的文件列表
     *
     * @param parentId
     * @return
     */
    public static List<File> getPreDirList(String parentId) {
        return Hib.query(session ->{
            File parentFile = (File) session
                    .createQuery("from File where id=:id")
                    .setParameter("id", parentId)
                    .uniqueResult();
            if (parentFile == null){
                return null;
            }else{
                return session.createQuery("from File where parentId=:parentId")
                        .setParameter("parentId", parentFile.getParentId()).list();
            }
        });
    }
}
