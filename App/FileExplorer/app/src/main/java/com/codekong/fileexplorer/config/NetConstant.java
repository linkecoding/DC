package com.codekong.fileexplorer.config;

/**
 * Created by 尚振鸿 on 17-12-27. 16:33
 * mail:szh@codekong.cn
 */

public class NetConstant {
    //网络请求url
    public static final String BASE_URL = "http://192.168.43.171:8080/api/android";

    //获取下级目录信息
    public static final String GET_NEXT_DIR_LIST_URL = "/file/getnextdirlist";
    public static final String GET_NEXT_DIR_LIST_FORMAT = "{'id': %s}";

    //获取上级目录信息
    public static final String GET_PRE_DIR_LIST_URL = "/file/getpredirlist";
    public static final String GET_PRE_DIR_LIST_FORMAT = "{'parentId': %s}";
}
