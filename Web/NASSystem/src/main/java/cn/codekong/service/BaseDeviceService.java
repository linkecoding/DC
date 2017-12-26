package cn.codekong.service;


import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import cn.codekong.bean.db.Device;


public class BaseDeviceService {
    // 添加一个上下文注解，该注解会给securityContext赋值
    // 具体的值为我们的拦截器中所返回的SecurityContext
    @Context
    protected SecurityContext securityContext;


    /**
     * 从上下文中直接获取自己的信息
     *
     * @return Devie
     */
    protected Device getSelf() {
        return (Device) securityContext.getUserPrincipal();
    }
}
