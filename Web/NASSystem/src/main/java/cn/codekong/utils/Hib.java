package cn.codekong.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Hibernate工具类封装
 */
public class Hib {
    // 全局SessionFactory
    private static SessionFactory sessionFactory;

    static {
        // 静态初始化sessionFactory
        init();
    }

    private static void init() {
        // 从hibernate.cfg.xml文件初始化
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            // build 一个sessionFactory
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // 错误则打印输出，并销毁
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * 获取全局的SessionFactory
     *
     * @return SessionFactory
     */
    public static SessionFactory sessionFactory() {
        return sessionFactory;
    }

    /**
     * 从SessionFactory中得到一个Session会话
     *
     * @return Session
     */
    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 关闭sessionFactory
     */
    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    /**
     * 用户实际操作的一个接口
     */
    public interface QueryOnly{
        void query(Session session);
    }

    /**
     * 简化Session操作的一个工具方法
     * @param query
     */
    public static void queryOnly(QueryOnly query){
        //重开一个Session
        Session session = sessionFactory().openSession();
        //开启事务
        final Transaction transaction = session.beginTransaction();
        try {
            //调用接口
            query.query(session);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            //回滚
            try {
                transaction.rollback();
            }catch (RuntimeException e1){
                e1.printStackTrace();
            }
        }finally {
            session.close();
        }
    }

    /**
     * 有返回值的查询
     * @param <T>
     */
    public interface Query<T>{
        T query(Session session);
    }

    /**
     * 有返回值的查询
     * @param query
     * @param <T>
     * @return
     */
    public static <T> T query(Query<T> query){
        Session session = sessionFactory().openSession();
        final Transaction transaction = session.beginTransaction();

        T t = null;
        try {
            t = query.query(session);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            //回滚
            try {
                transaction.rollback();
            }catch (RuntimeException e1){
                e1.printStackTrace();
            }
        }finally {
            session.close();
        }
        return t;
    }

}
