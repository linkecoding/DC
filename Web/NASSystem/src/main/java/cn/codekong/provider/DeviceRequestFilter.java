package cn.codekong.provider;

import com.google.common.base.Strings;

import org.glassfish.jersey.server.ContainerRequest;

import java.io.IOException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import cn.codekong.bean.api.base.ResponseModel;
import cn.codekong.bean.db.Device;
import cn.codekong.factory.PiFileFactory;

/**
 * Created by 尚振鸿 on 17-12-26. 13:51
 * mail:szh@codekong.cn
 */

public class DeviceRequestFilter implements ContainerRequestFilter {

    //设备激活状态
    private static final int DEVICE_ACTIVE_STATE = 1;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 检查是否是登录注册接口
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (!relationPath.startsWith("pi")) {
            // 直接走正常逻辑，不做拦截
            return;
        }

        String deviceId = requestContext.getHeaders().getFirst("deviceId");

        if (Strings.isNullOrEmpty(deviceId)){
            //参数错误
            ResponseModel model = ResponseModel.buildParameterError();
            // 构建一个返回
            Response response = Response.status(Response.Status.OK)
                    .entity(model)
                    .build();
            // 拦截，停止一个请求的继续下发，调用该方法后之间返回请求
            // 不会走到Service中去
            requestContext.abortWith(response);
        }

        Device device = PiFileFactory.findDeviceById(deviceId);
        if (device == null || device.getStatus() != DEVICE_ACTIVE_STATE){
            //设备没有激活
            ResponseModel model = ResponseModel.buildDeviceNotActive();
            // 构建一个返回
            Response response = Response.status(Response.Status.OK)
                    .entity(model)
                    .build();
            // 拦截，停止一个请求的继续下发，调用该方法后之间返回请求
            // 不会走到Service中去
            requestContext.abortWith(response);
        }

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return device;
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        });
    }
}
