package cn.codekong.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 用户Model
 */
@Entity
@Table(name = "T_USER")
public class User implements Principal, Serializable {
    public User() {
    }

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

    //用户名不能为空且长度128
    @Column(nullable = false, length = 128)
    private String name;

    //邮箱
    @Column(nullable = false, length = 62, unique = true)
    private String email;

    //密码不允许为空
    @Column(nullable = false)
    private String password;

    //token可以拉取用户信息,token必须唯一
    @Column(unique = true)
    private String token;

    //用于推送的设备Id
    @Column
    private String pushId;

    //定义为创建时间戳,在创建时已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    //定义为懒加载,默认加载User信息的时候,并不查询这个集合
    @LazyCollection(LazyCollectionOption.EXTRA)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "T_USER_DEVICE", joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "deviceId")})
    private Set<Device> devices = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }
}
