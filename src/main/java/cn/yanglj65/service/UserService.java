package cn.yanglj65.service;

import cn.yanglj65.dao.UserDao;
import cn.yanglj65.entity.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class UserService {
    private static UserDao userDao = new UserDao();

    public static User getUser(String fromQQ) {
        return userDao.findByQQ(fromQQ);
    }

    public static boolean statusHBChanged = false;


    public static String getHbrskswContent() {
        try {
            statusHBChanged = false;
            Document document = Jsoup.connect("http://www.hbsrsksy.cn/").get();
            Elements elements = document.getElementsByClass("ewb-comp-items").get(0).getAllElements();
            StringBuilder content = new StringBuilder();
            Element element = elements.get(0);
            Elements liElements = element.getElementsByClass("ewb-comp-item clearfix");
            for (Element liElement : liElements) {
                String text = liElement.text();
                if (text.contains("2019") && text.contains("法官助理") && (text.contains("体检") || text.contains("拟录用"))) {
                    statusHBChanged = true;
                }
                content.append(text).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException 网页读取错误";
        }
    }
}
