package cn.codekong.bean.card;

import com.google.gson.annotations.Expose;

import cn.codekong.bean.db.User;


/**
 * Created by 尚振鸿 on 17-11-27. 19:29
 * mail:szh@codekong.cn
 */

public class UserCard {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String email;

    public UserCard(final User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
