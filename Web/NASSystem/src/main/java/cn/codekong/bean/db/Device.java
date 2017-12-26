package cn.codekong.bean.db;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 设备实体
 * Created by 尚振鸿 on 17-12-11. 11:27
 * mail:szh@codekong.cn
 */

@Entity
@Table(name = "T_DEVICE")
public class Device implements Serializable, Principal {
    public Device() {
    }

    private static final String DEFAULT_DEVICE_NAME = "设备";

    //这是一个id主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生产存储的类型是UUID
    @GeneratedValue(generator = "uuid")
    //设置uuid的生成器为uuid2,uuid2是常规的UUID toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //该列不允许更改,不允许为空
    @Column(updatable = false, nullable = false)
    private String id;

    //设备名
    @Column(nullable = false, length = 128)
    private String name = DEFAULT_DEVICE_NAME;

    //激活码(用于激活设备)
    @Column(nullable = false, updatable = false)
    private String activeCode;

    //设备的状态(激活[1]/未激活[-1])
    @Column(nullable = false)
    private int status = -1;

    @JoinColumn(name = "deviceId")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<File> files = new HashSet<>();

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

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }
}
