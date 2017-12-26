package cn.codekong.bean.api.android.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

/**
 * Created by 尚振鸿 on 17-11-27. 19:40
 * mail:szh@codekong.cn
 */

public class RegisterModel {
    @Expose
    private String email;
    @Expose
    private String password;
    @Expose
    private String name;
    @Expose
    private String pushId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public static boolean check(RegisterModel model){
        return model != null
                && !Strings.isNullOrEmpty(model.email)
                && !Strings.isNullOrEmpty(model.password)
                && !Strings.isNullOrEmpty(model.name);
    }
}
