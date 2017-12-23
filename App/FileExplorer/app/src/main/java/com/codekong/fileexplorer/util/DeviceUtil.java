package com.codekong.fileexplorer.util;

import android.os.Build;

import java.util.UUID;

/**
 * 和设备相关的工具类
 * Created by 尚振鸿 on 17-12-23. 16:15
 * mail:szh@codekong.cn
 */

public class DeviceUtil {

    /**
     * 获取一个唯一的虚拟的设备Id
     * @return
     */
    public static String getUniquePsuedoID() {
        String serial = null;

        StringBuffer deviceId = new StringBuffer();
        deviceId.append("35")
                .append(Build.BOARD.length() % 10)
                .append(Build.BRAND.length() % 10)
                .append(Build.DEVICE.length() % 10)
                .append(Build.DISPLAY.length() % 10)
                .append(Build.HOST.length() % 10)
                .append(Build.ID.length() % 10)
                .append(Build.MANUFACTURER.length() % 10)
                .append(Build.MODEL.length() % 10)
                .append(Build.PRODUCT.length() % 10)
                .append(Build.TAGS.length() % 10)
                .append(Build.TYPE.length() % 10)
                .append(Build.USER.length() % 10)
                .append(Build.CPU_ABI.length() % 10);

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(deviceId.hashCode(), serial.hashCode()).toString().replaceAll("-", "_");
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(deviceId.hashCode(), serial.hashCode()).toString().replaceAll("-", "_");
    }
}
