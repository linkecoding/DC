package bean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by 尚振鸿 on 17-11-27. 20:47
 * mail:szh@codekong.cn
 */

public class ResponseModel<M> implements Serializable {

    //成功
    public static final int SUCCEED = 1;
    //未知错误
    public static final int ERROR_UNKNOWN = 0;

    //没有找到用户信息
    public static final int ERROR_NOT_FOUND_USER = 4041;

    //创建用户失败
    public static final int ERROR_CREATE_USER = 3001;

    //请求参数错误
    public static final int ERROR_PARAMETERS = 4001;


    //服务器错误
    public static final int ERROR_SERVICE = 5001;

    //账户token错误,需要重新登录
    public static final int ERROR_DEVICE_TOKEN = 2001;


    private int code;

    private String message;

    private LocalDateTime time = LocalDateTime.now();

    private M result;

    public ResponseModel(){
        code = SUCCEED;
        message = "ok";
    }

    public ResponseModel(M result){
        this();
        this.result = result;
    }

    public ResponseModel(int code, String message){
        this.code = code;
        this.message = message;
    }

    public ResponseModel(int code, String message, M result){
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public boolean isSucceed(){
        return code == SUCCEED;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public M getResult() {
        return result;
    }

    public void setResult(M result) {
        this.result = result;
    }

    public static <M> ResponseModel<M> buildOk(){
        return new ResponseModel<M>();
    }

    public static <M> ResponseModel<M> buildOk(M result){
        return new ResponseModel<M>(result);
    }

    public static <M> ResponseModel<M> buildParameterError(){
        return new ResponseModel<M>(ERROR_PARAMETERS, "Parameters Error.");
    }

    public static <M> ResponseModel<M> buildServiceError() {
        return new ResponseModel<M>(ERROR_SERVICE, "Service Error.");
    }

    public static <M> ResponseModel<M> buildNotFoundUserError(String str) {
        return new ResponseModel<M>(ERROR_NOT_FOUND_USER, str != null ? str : "Not Found User.");
    }

    public static <M> ResponseModel<M> buildTokenError() {
        return new ResponseModel<M>(ERROR_DEVICE_TOKEN, "Account Error; you need login.");
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", result=" + result +
                '}';
    }
}
