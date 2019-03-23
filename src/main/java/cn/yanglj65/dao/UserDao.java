package cn.yanglj65.dao;

import cn.yanglj65.entity.User;
import cn.yanglj65.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;


public class UserDao {
    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(user);
            tx.commit();// 提交事务
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();// 回滚事务
        } finally {
            session.close();
        }

    }

    public User findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = session.get(User.class, id);
        session.close();
        return user;
    }

    public User findByQQ(String fromQQ) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from User where QQ= :qq");
            query.setParameter("qq", fromQQ);
            User user = (User) query.getSingleResult();
            tx.commit();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return null;
    }

    public void update(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.update(user);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
    }


}
