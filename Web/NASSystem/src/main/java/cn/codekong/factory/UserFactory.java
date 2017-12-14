package cn.codekong.factory;

import com.google.common.base.Strings;

import java.util.List;
import java.util.UUID;

import cn.codekong.bean.db.User;
import cn.codekong.utils.Hib;
import cn.codekong.utils.TextUtil;


/**
 * Created by 尚振鸿 on 17-11-27. 19:44
 * mail:szh@codekong.cn
 */

public class UserFactory {
    //通过token字段查询用户信息
    //只能自己使用,查询个人信息,非他人信息
    public static User findByToken(String token) {
        return Hib.query(session -> (User) session
                .createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    //通过phone找到User
    public static User findByEmail(String email) {
        return Hib.query(session -> (User) session
                .createQuery("from User where email=:email")
                .setParameter("email", email)
                .uniqueResult());
    }

    /**
     * 更新用户信息到数据库
     * @param user
     * @return
     */
    public static User update(User user){
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * 给当前用户绑定pushId
     *
     * @param user   当前用户
     * @param pushId 自己设备的pushId
     * @return User
     */
    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId)) {
            return null;
        }

        //先查询是否有其他账户绑定了这个pushId
        //如果其他账户已经绑定,则先取消绑定,避免推送混乱
        //查询的列表不能包括自己
        Hib.queryOnly(session -> {
            @SuppressWarnings("unchecked")
            List<User> userList = (List<User>) session
                    .createQuery("from User where lower(pushId)=:pushId and id !=:userId")
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();
            for (User u : userList) {
                //先将以前绑定了pushId的账号取消绑定
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
        });

        if (pushId.equalsIgnoreCase(user.getPushId())) {
            //如果当前绑定的pushId已经绑定到该账号了,那么不需要再次绑定
            return user;
        } else {
            //如果当前的账户的之前的pushId和当前绑定的不同
            //需要单点登录,需要让之前的设备退出账户
            //给之前的设备推送一条退出消息
            if (Strings.isNullOrEmpty(user.getPushId())){
                //TODO 推送一条退出消息
            }

            //更新新的pushId
            user.setPushId(pushId);
            return Hib.query(session -> {
                session.saveOrUpdate(user);
                return user;
            });
        }
    }


    /**
     * 使用账号密码进行登录
     *
     * @param account  账号
     * @param password 密码
     * @return
     */
    public static User login(String account, String password) {
        final String accountStr = account.trim();
        //对密码原文进行加密处理,以便于与数据库中的加密密码对比
        final String encodePassword = encodePassword(password);

        //查找
        User user = Hib.query(session -> (User) session
                .createQuery("from User  where email=:email and password=:password")
                .setParameter("email", accountStr)
                .setParameter("password", encodePassword).uniqueResult());

        if (user != null) {
            //登录user并更新token
            user = login(user);
        }
        return user;
    }

    /**
     * 把一个验证通过的user进行登录操作
     *
     * @param user User
     * @return user
     */
    private static User login(User user) {
        //使用随机的UUID值充当token
        String newToken = UUID.randomUUID().toString();
        //进行Base64格式化
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);
        return update(user);
    }

    /**
     * 用户注册
     * 将注册信息写入数据库,并返回数据库中的User信息
     *
     * @param email  账号
     * @param password 密码
     * @param name     用户名
     * @return User
     */
    public static User register(String email, String password, String name) {
        email = email.trim();
        //处理密码
        password = encodePassword(password);

        User user = createUser(email, password, name);
        if (user != null) {
            //注册成功自动登录
            user = login(user);
        }
        return user;
    }

    /**
     * 注册部分新建一个用户,并存储至数据库
     *
     * @param email  账号
     * @param password 密码
     * @param name     用户名
     * @return
     */
    private static User createUser(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        //数据库存储
        return Hib.query(session -> {
            session.save(user);
            return session.get(User.class, user.getId());
        });
    }

    /**
     * 对密码进行MD5加密之后进行Base64处理
     *
     * @param password 密码原文
     * @return
     */
    private static String encodePassword(String password) {
        password = password.trim();
        password = TextUtil.getMD5(password);
        return TextUtil.encodeBase64(password);
    }
}
