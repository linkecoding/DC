package cn.codekong.bean.api.account;

import com.google.gson.annotations.Expose;

import cn.codekong.bean.card.UserCard;
import cn.codekong.bean.db.User;

/**
 * 账户相关返回的信息
 * Created by 尚振鸿 on 17-11-27. 22:17
 * mail:szh@codekong.cn
 */

public class AccountRspModel {

    //用户基本信息
    @Expose
    private UserCard user;

    //登录成功的token,可通过token获取用户的信息
    @Expose
    private String token;
    //是否已经绑定到设备的PushId
    @Expose
    private boolean isBind;

    public AccountRspModel(User user){
        //默认没有绑定
        this(user, false);
    }

    public AccountRspModel(User user, boolean isBind){
        this.user = new UserCard(user);
        this.token = user.getToken();
        this.isBind = isBind;
    }

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
