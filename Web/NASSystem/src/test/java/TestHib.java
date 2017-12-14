import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        User user = new User();
        user.setName("小龙");
        user.setEmail("szh@codekong.cn");
        user.setPassword("1234");
    }


    @After
    public void after(){
        mTransaction.commit();
        mSession.close();
        mSessionFactory.close();
    }
}
