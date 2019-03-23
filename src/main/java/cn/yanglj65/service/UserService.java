package cn.yanglj65.service;

import cn.yanglj65.dao.UserDao;
import cn.yanglj65.entity.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class UserService {
    private static UserDao userDao=new UserDao();
public static String getTecentStatus(String fromQQ){
    try {
        User user=userDao.findByQQ(fromQQ);
        Document document = Jsoup.connect("http://join.qq.com/login.php").header("cookie",user.getCookie()).get();
        Element element=document.getElementsByClass("item mt45").get(0).getAllElements().get(0);
        String status=element.text();
        user.setStatus(status);
        userDao.update(user);
        return status;
    } catch (IOException e) {
        e.printStackTrace();
    }catch(IndexOutOfBoundsException e){
        e.printStackTrace();
        return "cookie失效或程序错误";
    }
    return null;
}

    public static String getNetEaseStatus(String fromQQ){
        try {
            User user=userDao.findByQQ(fromQQ);
            Document document = Jsoup.connect("http://gzgame.campus.163.com/applyJob.do?username=17665070189&lan=zh").header("cookie",user.getCookie()).get();
            Element element=document.getElementsByClass("table_1").get(0).getAllElements().get(1).getAllElements().get(22);
            String status=element.text();
            //String status="";
            user.setStatus(status);
            userDao.update(user);
            return status;
        } catch (IOException e) {
            e.printStackTrace();
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
            return "cookie失效或程序错误";
        }
        return null;
    }

    public static void changeCookie(String cookie,String fromQQ){
        User user=userDao.findByQQ(fromQQ);
        user.setCookie(cookie);
        userDao.update(user);
    }

}
