package com.codekong.fileexplorer.config;

/**
 * Created by szh on 2017/2/9.]
 * 常量表
 */

public class Constant {
    /*************************文件排序方式(文件夹在前,文件在后)****************************/
    //按文件名和文件大小排序时，文件夹按照数字在前，字母在后的顺序先排序，再按文件的具体要求排序
    //文件名
    public static final int SORT_BY_FILE_NAME = 1;
    //文件大小(从小到大)
    public static final int SORT_BY_FILE_SIZE_ASC = 2;
    //文件大小(从大到小)
    public static final int SORT_BY_FILE_SIZE_DESC = 3;
    //文件类型(将文件后缀相同的排列到一起，后缀按数字字母顺序排列)
    public static final int SORT_BY_FILE_TYPE = 4;
    //文件修改时间
    public static final int SORT_BY_FILE_MIDIFY_TIME = 4;
    /*************************文件排序方式****************************/

    /*************************文件分类****************************/
    public static final String[] FILE_CATEGORY_NAME = {"视频", "文档", "图片", "音乐", "安装包", "压缩包",
    "收藏夹", "蓝牙", "下载", "小米云盘", "远程管理", "热门表情"};

    public static final String[] FILE_CATEGORY_ICON = {"ic_video", "ic_document", "ic_picture", "ic_music",
    "ic_apk", "ic_zip", "ic_favorites", "ic_bluetooth", "ic_download", "ic_cloud_disk",
    "ic_remote_manage", "ic_popular_expression"};
    /*************************文件分类****************************/

}
