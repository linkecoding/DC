package com.codekong.fileexplorer.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * SharePreferences相关操作工具类
 * Created by 尚振鸿 on 17-12-23. 16:02
 * mail:szh@codekong.cn
 */

public class SfUtil {

    /**
     * 保存map数据到SharePreferences
     * @param context
     * @param name
     * @param dataMap
     */
    public static void saveToSharePreference(Context context, String name, Map<String, String> dataMap){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Map.Entry<String, String> entry : dataMap.entrySet()){
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }

    /**
     * 保存数据到Sharepreferences
     * @param context
     * @param name
     * @param key
     * @param value
     */
    public static void saveToSharePreference(Context context, String name, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 从Sharepreference中读取指定key的value
     * @param context
     * @param name
     * @param key
     * @return
     */
    public static String getFromSharepreference(Context context, String name, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
