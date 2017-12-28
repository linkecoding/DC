package com.codekong.fileexplorer.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by szh on 2017/2/9.]
 * 常量表
 */

public class Constant {

    //token存储文件名
    public static final String TOEKN_SF_FILE_NAME = "sf_token";
    /*************************文件分类****************************/
    public static final String[] FILE_CATEGORY_NAME = {"视频", "文档", "图片", "音乐", "安装包", "压缩包"};

    public static final String[] FILE_CATEGORY_ICON = {"ic_video", "ic_document", "ic_picture", "ic_music",
    "ic_apk", "ic_zip"};
    /*************************文件分类****************************/

    //每种类型的文件包含的后缀
    public static final Map<String, Set<String>> CATEGORY_SUFFIX;

    static {
        //初始化赋值
        CATEGORY_SUFFIX = new HashMap<>(FILE_CATEGORY_ICON.length);
        Set<String> set = new HashSet<>();
        set.add("mp4");
        set.add("avi");
        set.add("wmv");
        set.add("flv");
        CATEGORY_SUFFIX.put("video", set);

        set.add("txt");
        set.add("pdf");
        set.add("doc");
        set.add("docx");
        set.add("xls");
        set.add("xlsx");
        CATEGORY_SUFFIX.put("document", set);

        set = new HashSet<>();
        set.add("jpg");
        set.add("jpeg");
        set.add("png");
        set.add("bmp");
        set.add("gif");
        CATEGORY_SUFFIX.put("picture", set);

        set = new HashSet<>();
        set.add("mp3");
        set.add("ogg");
        CATEGORY_SUFFIX.put("music", set);

        set = new HashSet<>();
        set.add("apk");
        CATEGORY_SUFFIX.put("apk", set);

        set = new HashSet<>();
        set.add("zip");
        set.add("rar");
        set.add("7z");
        CATEGORY_SUFFIX.put("zip", set);
    }
}
