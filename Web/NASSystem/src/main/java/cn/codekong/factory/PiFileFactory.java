package cn.codekong.factory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.codekong.bean.card.FileCard;
import cn.codekong.bean.db.Device;
import cn.codekong.bean.db.File;
import cn.codekong.utils.Hib;

/**
 * Created by 尚振鸿 on 17-12-26. 12:00
 * mail:szh@codekong.cn
 */

public class PiFileFactory {

    /**
     * 根据设备Id查询设备
     *
     * @param deviceId
     * @return
     */
    public static Device findDeviceById(String deviceId) {
        return Hib.query(session -> (Device) session
                .createQuery("from Device where id=:deviceId")
                .setParameter("deviceId", deviceId)
                .uniqueResult());
    }

    /**
     * 更新文件目录到数据库
     * @param self
     * @param fileCardList
     */
    public static void uploadFileDirInfo(Device self, List<FileCard> fileCardList){
        Hib.queryOnly(session -> {
            //首先删除数据
            session.createQuery("delete from File where deviceId=:deviceId")
                    .setParameter("deviceId", self.getId())
                    .executeUpdate();
            session.flush();

            Set<File> fileSet = new HashSet<>();
            for (FileCard fileCard : fileCardList) {
                File file = fileCardToFile(fileCard);
                file.setDeviceId(self.getId());
                session.save(file);
                session.flush();
            }
        });
    }

    public static File fileCardToFile(FileCard fileCard) {
        File file = new File();
        file.setId(fileCard.getId());
        file.setLevel(fileCard.getLevel());
        file.setName(fileCard.getName());
        file.setParentId(fileCard.getParentId());
        file.setSize(fileCard.getSize());
        file.setSuffix(fileCard.getSuffix());
        file.setType(fileCard.getType());
        return file;
    }
}
