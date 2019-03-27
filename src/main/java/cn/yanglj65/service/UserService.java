package cn.yanglj65.service;

import cn.yanglj65.dao.UserDao;
import cn.yanglj65.entity.User;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserDao userDao = new UserDao();

    public static String getTencentStatus(String fromQQ) {
        User user;
        user = userDao.findByQQ(fromQQ);
        try {
            Document document = Jsoup.connect("http://join.qq.com/center.php").header("cookie", user.gettCookie()).get();
            Element element = document.getElementsByClass("item mt45").get(0).getAllElements().get(0);
            String status = element.text();
            //user.setStatus(status);
           // userDao.update(user);
            return status;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "cookie失效或程序错误";
        }
        return null;
    }

    public static String getNetEaseStatus(String fromQQ) {
        User user;
        user = userDao.findByQQ(fromQQ);
        try {
            Document document = Jsoup.connect("http://gzgame.campus.163.com/applyJob.do?username=17665070189&lan=zh").header("cookie", user.getCookie()).get();
            Element element = document.getElementsByClass("table_1").get(0).getAllElements().get(1).getAllElements().get(22);
            String status = element.text();
            //String status="";
            user.setStatus(status);
            userDao.update(user);
            return status;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            CloseableHttpClient httpClient;
            CookieStore cookieStore = new BasicCookieStore();
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpPost httpPost = new HttpPost("https://reg.163.com/logins.jsp");
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("type", "0"));
            param.add(new BasicNameValuePair("url", "http://gzgame.campus.163.com/applyJob.do?lan=zh"));
            param.add(new BasicNameValuePair("domains", "163.com"));
            param.add(new BasicNameValuePair("username", "17665070189@163.com"));
            param.add(new BasicNameValuePair("password", "622d2d72f00d34d0e945e715e575ca52"));
            CloseableHttpResponse httpResponse = null;

            // String formParam=URLEncodedUtils.format(param,"UTF-8");
            try {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param, "UTF-8");
                httpPost.setEntity(urlEncodedFormEntity);
                httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                List<Cookie> cookies = cookieStore.getCookies();
                StringBuilder cookieStr = new StringBuilder();
                for (int i = 0; i < cookies.size(); i++) {
                    if (i != cookies.size() - 1) {
                        cookieStr.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue()).append(";");
                    } else {
                        cookieStr.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue());
                    }
                }
                user.setCookie(cookieStr.toString());
                userDao.update(user);
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (httpResponse != null) {
                        httpResponse.close();
                    }
                    httpClient.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

            return "已重新设置失效的cookie";
        }
        return null;
    }

    public static void changeCookie(String cookie, String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        user.setCookie(cookie);
        userDao.update(user);
    }

    public static void changeTCookie(String cookie, String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        user.settCookie(cookie);
        userDao.update(user);
    }
    public static String getCookie(String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        return user.getCookie();
    }
    public static String getTCookie(String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        return user.gettCookie();
    }
}
