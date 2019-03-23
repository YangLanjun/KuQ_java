package cn.yanglj65.util;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
   // private static Configuration configuration;
    static{
        try{
            Configuration configuration=new Configuration().configure("/hibernate/hibernate.cfg.xml");
            sessionFactory=configuration.buildSessionFactory();
        }catch(Throwable ex){
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    public static void close(){
        sessionFactory.close();
    }
}
