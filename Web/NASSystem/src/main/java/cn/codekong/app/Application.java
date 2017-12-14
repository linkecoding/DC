package cn.codekong.app;

import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

import cn.codekong.provider.AuthRequestFilter;
import cn.codekong.provider.GsonProvider;
import cn.codekong.service.AccountService;

/**
 * Created by 尚振鸿 on 17-12-10. 15:58
 * mail:szh@codekong.cn
 */

public class Application extends ResourceConfig {
    public Application() {
        // 注册逻辑处理的包名
        //packages("cn.codekong.service");
        packages(AccountService.class.getPackage().getName());

        // 注册我们的全局请求拦截器
        register(AuthRequestFilter.class);

        // 注册Json解析器
        //替换解析器为Gson
        register(GsonProvider.class);

        // 注册日志打印输出
        register(Logger.class);

    }
}
