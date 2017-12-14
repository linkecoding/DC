package cn.codekong.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

/**
 * Created by 尚振鸿 on 17-11-27. 21:34
 * mail:szh@codekong.cn
 */

public class LoginModel {

    @Expose
    private String email;

    @Expose
    private String password;

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

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    /**
     * 非空检验
     * @param model
     * @return
     */
    public static boolean check(LoginModel model){
        return model != null
                && !Strings.isNullOrEmpty(model.email)
                && !Strings.isNullOrEmpty(model.password);
    }
}
