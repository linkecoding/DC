import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import cn.codekong.bean.db.Device;
import cn.codekong.bean.db.User;

/**
 * Created by 尚振鸿 on 17-12-11. 21:20
 * mail:szh@codekong.cn
 */

public class TestHib {

    private SessionFactory mSessionFactory;

    private Session mSession;

    private Transaction mTransaction;

    @Before
    public void before(){
        // 从hibernate.cfg.xml文件初始化
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            // build 一个sessionFactory
            mSessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // 错误则打印输出，并销毁
            StandardServiceRegistryBuilder.destroy(registry);
        }

        mSession = mSessionFactory.getCurrentSession();
        mTransaction = mSession.beginTransaction();
    }


    @Test
    public void testHib(){

        User user = mSession.load(User.class, "94822e22-df4f-496e-97e9-b570dc97c1a7");
        Set<Device> deviceSet = new HashSet<>();
        Device device = mSession.load(Device.class, "62d5e827-e5aa-457d-b42f-4fe25dcdc124");
        deviceSet.add(device);
        user.setDevices(deviceSet);
        mSession.saveOrUpdate(user);
//        User user = new User();
//        user.setName("admin");
//        user.setEmail("123@qq.com");
//        user.setPassword("123456");
//        user.setToken("token");
//
//        mSession.save(user);
        //System.out.println(Arrays.toString(AndroidFileFactory.getNextDirList("0").toArray()));
        //System.out.println(Arrays.toString(AndroidFileFactory.getNextDirList("1a7b7e2f-8380-4295-9931-bf922e5c71a3").toArray()));
        //System.out.println(AndroidFileFactory.getNextDirList("1"));

        //System.out.println(Arrays.toString(AndroidFileFactory.getPreDirList("1a7b7e2f-8380-4295-9931-bf922e5c71a3").toArray()));
        //System.out.println(Arrays.toString(AndroidFileFactory.getPreDirList("1").toArray()));
    }


    @After
    public void after(){
        mTransaction.commit();
        mSession.close();
        mSessionFactory.close();
    }
}
