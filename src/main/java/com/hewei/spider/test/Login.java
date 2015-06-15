package com.hewei.spider.test;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hewei
 * 
 * @date 2015/6/15  16:22
 *
 * @version 5.0
 *
 * @desc 客户端自动登录
 *
 */
public class Login {
    private static final String userName = "colin.he";
    private static final String password = "shuyun_504";
    private static final String redirectURL = "http://wiki.yunat.com/";

    // Don't change the following URL
    private static final String renRenLoginURL = "http://wiki.yunat.com/dologin.action";

    // The HttpClient is used in one session
    private HttpResponse response;
    private DefaultHttpClient httpclient = new DefaultHttpClient();

    private boolean login() {
        HttpPost httpost = new HttpPost(renRenLoginURL);
        List<NameValuePair> nvps = new ArrayList<>();
        //        nvps.add(new BasicNameValuePair("origURL", redirectURL));
        //        nvps.add(new BasicNameValuePair("domain", "renren.com"));
        //        nvps.add(new BasicNameValuePair("isplogin", "true"));
        //        nvps.add(new BasicNameValuePair("formName", ""));
        //        nvps.add(new BasicNameValuePair("method", ""));
        //        nvps.add(new BasicNameValuePair("submit", "登录"));
        nvps.add(new BasicNameValuePair("os_username", userName));
        nvps.add(new BasicNameValuePair("os_password", password));
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = httpclient.execute(httpost);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            httpost.abort();
        }
        return true;
    }

    private String getRedirectLocation() {
        Header locationHeader = response.getFirstHeader("Location");
        if (locationHeader == null) {
            return null;
        }
        return locationHeader.getValue();
    }

    private String getText(String redirectLocation) {
        HttpGet httpget = new HttpGet(redirectLocation);
        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = "";
        try {
            responseBody = httpclient.execute(httpget, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = null;
        } finally {
            httpget.abort();
            httpclient.getConnectionManager().shutdown();
        }
        return responseBody;
    }

    public void printText() {
        boolean login=login();

        if (login) {
            //            String redirectLocation = getRedirectLocation();
            //            if (redirectLocation != null) {
            System.out.println(getText(redirectURL));
            //            }
        }
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.printText();
    }
}
