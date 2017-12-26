package cn.codekong.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 文件实体
 * Created by 尚振鸿 on 17-12-11. 11:11
 * mail:szh@codekong.cn
 */

@Entity
@Table(name = "T_FILE")
public class File implements Serializable{
    public File(){}

    //这是一个id主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生产存储的类型是UUID
    @GeneratedValue(generator = "assignGen")
    //设置uuid的生成器为uuid2,uuid2是常规的UUID toString
    @GenericGenerator(name = "assignGen", strategy = "assigned")
    //该列不允许更改,不允许为空
    @Column(updatable = false, nullable = false)
    private String id;

    //文件名
    @Column(nullable = false, length = 128)
    private String name;

    //文件类型(文件/文件夹)
    @Column(nullable = false, updatable = false)
    private int type;

    //文件后缀
    @Column(length = 8, nullable = true)
    private String suffix;

    //文件层级(便于目录跳转)
    @Column(nullable = false)
    private int level;

    //父级目录Id
    @Column(nullable = false)
    private String parentId;

    //文件大小
    @Column()
    private String size;

    @Column(nullable = false)
    private String deviceId;

    //定义为创建时间戳,在创建时已经写入
    @CreationTimestamp
    @Column(nullable = false)

    private LocalDateTime createAt = LocalDateTime.now();

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
