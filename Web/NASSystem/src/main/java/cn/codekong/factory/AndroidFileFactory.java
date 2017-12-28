package cn.codekong.factory;

import java.util.ArrayList;
import java.util.List;

import cn.codekong.bean.card.FileCard;
import cn.codekong.bean.db.File;
import cn.codekong.utils.Hib;

/**
 * Created by 尚振鸿 on 17-12-27. 14:46
 * mail:szh@codekong.cn
 */

public class AndroidFileFactory {

    private static final String ROOT_DIR_ID = "0";

    /**
     * 获取给定目录的下级目录文件列表
     *
     * @param id
     * @return
     */
    public static List<FileCard> getNextDirList(String id) {
        List<File> fileList = null;
        if (ROOT_DIR_ID.equals(id)) {
            System.out.println("进入");
            //获取根目录文件夹
            fileList = Hib.query(session -> session
                    .createQuery("from File where level=:rootLevel")
                    .setParameter("rootLevel", 1).list());
        } else {
            fileList = Hib.query(session -> session
                    .createQuery("from File where parentId=:parentId")
                    .setParameter("parentId", id).list());
        }

        return fileListToFileCardList(fileList);
    }


    /**
     * 获取给定目录的上级目录的文件列表
     *
     * @param parentId
     * @return
     */
    public static List<FileCard> getPreDirList(String parentId) {
        List<File> fileList = null;
        fileList = Hib.query(session -> {
            File parentFile = (File) session
                    .createQuery("from File where id=:id")
                    .setParameter("id", parentId)
                    .uniqueResult();
            if (parentFile == null) {
                return null;
            } else {
                return session.createQuery("from File where parentId=:parentId")
                        .setParameter("parentId", parentFile.getParentId()).list();
            }
        });
        return fileListToFileCardList(fileList);
    }

    private static List<FileCard> fileListToFileCardList(List<File> fileList) {
        if (fileList == null){
            return null;
        }

        List<FileCard> fileCardList = new ArrayList<>();
        for (File file : fileList){
            FileCard fileCard = new FileCard(file.getId(), file.getName(), file.getType(),
                    file.getSuffix(), file.getLevel(), file.getSize(), file.getParentId());
            fileCardList.add(fileCard);

        }
        return fileCardList;
    }
}
