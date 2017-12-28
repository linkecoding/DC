package cn.codekong.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.codekong.bean.card.FileCard;
import cn.codekong.bean.db.Device;
import cn.codekong.bean.db.File;
import cn.codekong.bean.db.User;
import cn.codekong.utils.Hib;

/**
 * Created by 尚振鸿 on 17-12-27. 14:46
 * mail:szh@codekong.cn
 */

public class AndroidFileFactory {

    private static final String ROOT_DIR_ID = "0";

    /**
     * 获取给定目录的下级目录文件列表
     * @param deviceId
     * @param dirId
     * @return
     */
    public static List<FileCard> getNextDirList(String deviceId, String dirId) {
        List<File> fileList = null;
        if (ROOT_DIR_ID.equals(dirId)) {
            System.out.println("进入");
            //获取根目录文件夹
            fileList = Hib.query(session -> session
                    .createQuery("from File where level=:rootLevel and deviceId=:deviceId order by type desc")
                    .setParameter("rootLevel", 1)
                    .setParameter("deviceId", deviceId)
                    .list());
        } else {
            fileList = Hib.query(session -> session
                    .createQuery("from File where parentId=:parentId and deviceId=:deviceId order by type desc")
                    .setParameter("parentId", dirId)
                    .setParameter("deviceId", deviceId)
                    .list());
        }

        return fileListToFileCardList(fileList);
    }


    /**
     * 获取给定目录的上级目录的文件列表
     *
     * @param parentId
     * @return
     */
    public static List<FileCard> getPreDirList(String deviceId, String parentId) {
        List<File> fileList = null;
        fileList = Hib.query(session -> {
            File parentFile = (File) session
                    .createQuery("from File where id=:id")
                    .setParameter("id", parentId)
                    .uniqueResult();
            if (parentFile == null) {
                return null;
            } else {
                return session.createQuery("from File where parentId=:parentId order by type desc")
                        .setParameter("parentId", parentFile.getParentId()).list();
            }
        });
        return fileListToFileCardList(fileList);
    }

    /**
     * 将File的列表转化为fileCard的列表
     * @param fileList
     * @return
     */
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


    /**
     * 检查该用户是否有该设备的访问权限
     * @param userId
     * @param deviceId
     * @return
     */
    public static boolean checkUserDevicePermission(String userId, String deviceId){
        User user = Hib.query(session -> (User) session.createQuery("from User where id=:userId")
                .setParameter("userId", userId)
                .uniqueResult());
        Set<Device> deviceSet = user.getDevices();
        for (Device device : deviceSet){
            if (device.getId().equals(deviceId)){
                return true;
            }
        }
        return false;
    }

}
