package com.codekong.fileexplorer.bean;

import com.google.gson.Gson;

/**
 * 文件bean
 * Created by 尚振鸿 on 17-12-23. 21:52
 * mail:szh@codekong.cn
 */

public class FileCard {
    //文件Id
    private String id;
    //文件名
    private String name;
    //文件类型(文件[-1]还是文件夹[1])
    private int type;
    //文件后缀
    private String suffix;
    //文件层级(1-n[最顶层为0])
    private int level;
    //文件大小
    private String size;
    //文件父级目录
    private String parentId;

    public FileCard(String id, String name, int type,
                    String suffix, int level, String size, String parentId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.suffix = suffix;
        this.level = level;
        this.size = size;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "FileCard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", suffix='" + suffix + '\'' +
                ", level=" + level +
                ", size='" + size + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }

    /**
     * 输出json字符串
     * @return
     */
    public String toJsonStr(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}