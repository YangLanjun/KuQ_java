package cn.yanglj65.service;

import cn.yanglj65.dao.UserDao;
import cn.yanglj65.entity.User;
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
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserDao userDao = new UserDao();

    public static User getUser(String fromQQ) {
        return userDao.findByQQ(fromQQ);
    }

    public static boolean statusHBChanged = false;

    public static String register(String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        if (user != null) {
            return "用户已注册";
        } else {
            user = new User();
            user.setQQ(fromQQ);
            userDao.save(user);
            return "注册成功";
        }
    }

    public static String getTencentStatus(String fromQQ) {
        User user;
        user = userDao.findByQQ(fromQQ);
        if (user == null) {
            return "请先发送 注册 成为用户，或发送 注册说明 了解详情";
        }
        try {
            Document document = Jsoup.connect("http://join.qq.com/center.php").header("cookie", user.getTCookie()).get();
            Element element = document.getElementsByClass("item mt45").get(0).getAllElements().get(0);
            String status = element.text();
            if (status == null) {
                return "cookie失效或程序错误";
            }
            String lastStatus = user.getTStatus();
            if (!status.equals(lastStatus)) {
                user.setTStatus(status);
                userDao.update(user);
            }
            return status;
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "cookie失效或程序错误";
        }
    }

    private static String getLoginCookie(String url, List<NameValuePair> param) {

        CloseableHttpClient httpClient;
        CookieStore cookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse httpResponsePost = null;
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            httpResponsePost = httpClient.execute(httpPost);

            //   HttpEntity entity = httpResponse.getEntity();
            List<Cookie> cookies = cookieStore.getCookies();
            StringBuilder cookieStr = new StringBuilder();
            for (int i = 0; i < cookies.size(); i++) {
                if (i != cookies.size() - 1) {
                    cookieStr.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue()).append(";");
                } else {
                    cookieStr.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue());
                }
            }
            return cookieStr.toString();
        } catch (IOException e1) {
            e1.printStackTrace();
            return "IOException";
        } finally {
            try {
                if (httpResponsePost != null) {
                    httpResponsePost.close();
                }
                httpClient.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static String AlibabaLogin(User user) {
        return "由于滑块验证，重设cookie失败，请手动设置cookie";
    }

    public static String getAlibabaStatus(String fromQQ) {
        User user;
        user = userDao.findByQQ(fromQQ);
        if (user == null) {
            return "请先发送 注册 成为用户，或发送 注册说明 了解详情";
        }
        try {
            Document document = Jsoup.connect("http://campus.alibaba.com/myJobApply.htm").header("cookie", user.getACookie()).get();
            Element element = document.getElementsByClass("state-name").get(0).getAllElements().get(1).getElementsByClass("strong-new").get(0);
            String status = element.text();
            //String status="";
            if (status == null) {
                return AlibabaLogin(user);
                //return "cookie失效或程序错误";
            }
            String lastStatus = user.getAStatus();
            if (!status.equals(lastStatus)) {
                user.setAStatus(status);
                userDao.update(user);
            }
            return status;
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return AlibabaLogin(user);
            // return "cookie失效或程序错误";
        }
    }

    private static String NetEaseLogin(User user) {
        List<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("type", "0"));
        param.add(new BasicNameValuePair("url", "http://gzgame.campus.163.com/applyJob.do?lan=zh"));
        param.add(new BasicNameValuePair("domains", "163.com"));
        param.add(new BasicNameValuePair("username", "17665070189@163.com"));
        param.add(new BasicNameValuePair("password", "622d2d72f00d34d0e945e715e575ca52"));
        String cookie = getLoginCookie("https://reg.163.com/logins.jsp", param);
        if (!cookie.equals("IOException")) {
            user.setNCookie(cookie);
            userDao.update(user);
            return user.getNStatus();
        } else {
            return "cookie获取异常，请手动检查";
        }
    }

    public static String getNetEaseStatus(String fromQQ) {
        User user;
        user = userDao.findByQQ(fromQQ);
        if (user == null) {
            return "请先发送 注册 成为用户，或发送 注册说明 了解详情";
        }
        try {
            Document document = Jsoup.connect("http://gzgame.campus.163.com/applyJob.do?username=17665070189&lan=zh").header("cookie", user.getNCookie()).get();
            Element element = document.getElementsByClass("table_1").get(0).getAllElements().get(1).getAllElements().get(22);
            String status = element.text();
            //String status="";
            if (status == null) {
                if (fromQQ.equals("1069148429")) {
                    return NetEaseLogin(user);
                } else {
                    return "cookie错误或失效";
                }

            }
            String lastStatus = user.getNStatus();
            if (!status.equals(lastStatus)) {
                user.setNStatus(status);
                userDao.update(user);
            }
            return status;
        } catch (IOException e) {
            e.printStackTrace();
            return "IOException";
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            if (fromQQ.equals("1069148429")) {
                return NetEaseLogin(user);
            } else {
                return "cookie错误或失效";
            }
        }
    }

    public static void changeNCookie(String cookie, String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        user.setNCookie(cookie);
        userDao.update(user);
    }

    public static void changeTCookie(String cookie, String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        user.setTCookie(cookie);
        userDao.update(user);
    }

    public static void changeACookie(String cookie, String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        user.setACookie(cookie);
        userDao.update(user);
    }

    public static String getNCookie(String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        return user.getNCookie();
    }

    public static String getTCookie(String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        return user.getTCookie();
    }

    public static String getACookie(String fromQQ) {
        User user = userDao.findByQQ(fromQQ);
        return user.getACookie();
    }

    public static String getHbrskswContent() {
        try {
            statusHBChanged = false;
            Document document = Jsoup.connect("http://www.hbsrsksy.cn/").get();
            Elements elements = document.getElementsByClass("ewb-comp-items").get(0).getAllElements();
            StringBuilder content = new StringBuilder();
//            for (Element element : elements) {
//                content.append(element.text()).append("\n");
//            }

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
