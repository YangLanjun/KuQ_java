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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserDao userDao = new UserDao();

    public static User getUser(String fromQQ) {
        return userDao.findByQQ(fromQQ);
    }

    public static String getTencentStatus(String fromQQ) {
        User user;
        user = userDao.findByQQ(fromQQ);
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
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.getOptions().setCssEnabled(false);
//        webClient.getOptions().setUseInsecureSSL(true);//忽略ssl认证
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        webClient.getOptions().setRedirectEnabled(true);
//        webClient.getOptions().setTimeout(10000);
//       // webClient.waitForBackgroundJavaScript(3000);
//        try {
//          //  HtmlPage homePage = webClient.getPage("https://campus.alibaba.com/index.htm");
//           // HtmlElement aLogin = homePage.getHtmlElementById("login");
//            HtmlPage loginPage = webClient.getPage("https://campus.alibaba.com/login.htm?spm=a1z3e1.11770841.0.0.60133e3aWwNTeB&params=https%3A%2F%2Fcampus.alibaba.com%2Findex.htm");
//            webClient.waitForBackgroundJavaScript(5000);
//            String loginHtml = loginPage.asXml();
//            System.out.println(loginHtml);
//            HtmlForm loginForm = loginPage.getFormByName("login-form");
//            HtmlInput userIdInput = loginForm.getInputByName("loginId");
//            HtmlPasswordInput passwordInput = loginForm.getInputByName("password");
//            userIdInput.setTextContent("17665070189");
//            passwordInput.setText("a1069148429.");
//            HtmlSubmitInput submitButton = loginForm.getInputByName("submit-btn");
//            HtmlPage userPage = submitButton.click();
//            String userPageHtml = userPage.getWebResponse().getContentAsString();
//            System.out.println(userPageHtml);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        // chromeOptions.addArguments("--headless");
        ChromeDriver chromeDriver=new ChromeDriver(chromeOptions);
        chromeDriver.get("http://campus.alibaba.com/login.htm?params=https%3A%2F%2Fcampus.alibaba.com%2FmyJobApply.htm%3Fspm%3Da1z3e1.11770841.0.0.60133e3a6ZuKkl");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //String source = chromeDriver.getPageSource();
        chromeDriver.switchTo().frame("alibaba-login-box");
        WebElement userIdInput=chromeDriver.findElementByName("loginId");
        WebElement pwdInput=chromeDriver.findElementByName("password");
        userIdInput.sendKeys("17665070189");
        pwdInput.sendKeys("a1069148429.");
       WebElement submitButton=chromeDriver.findElementByName("submit-btn");
       submitButton.click();
        String source = chromeDriver.getPageSource();
       // System.out.println(source);
        chromeDriver.close();
        return "由于滑块验证，重设cookie失败，请手动设置cookie";
    }

    public static String getAlibabaStatus(String fromQQ) {
        User user;
        user = userDao.findByQQ(fromQQ);
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
            return "已重新设置网易失效的cookie";
        } else {
            return "cookie获取异常，请手动检查";
        }
    }

    public static String getNetEaseStatus(String fromQQ) {
        User user;
        user = userDao.findByQQ(fromQQ);
        try {
            Document document = Jsoup.connect("http://gzgame.campus.163.com/applyJob.do?username=17665070189&lan=zh").header("cookie", user.getNCookie()).get();
            Element element = document.getElementsByClass("table_1").get(0).getAllElements().get(1).getAllElements().get(22);
            String status = element.text();
            //String status="";
            if (status == null) {
                return NetEaseLogin(user);
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
            return NetEaseLogin(user);
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
}
