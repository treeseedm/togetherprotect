package http;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * HttpBuilder
 */
public class HttpBuilder {
    public static final int GET = 0;
    public static final int POST = 1;
    public static final String HTTP_POST_BODY = "body";
    public static final String HTTP_COOKIE = "Cookie";
    public static final String HTTP_USER_AGENT = "User-Agent";


    /**
     * 打开并配置 HttpURLConnection
     * 
     * @param url
     * @return
     * @throws java.io.IOException
     */
    public static HttpURLConnection openAndConfigConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置连接超时时间
        connection.setConnectTimeout(HttpUtils.REQUEST_TIMEOUT);
        connection.setReadTimeout(HttpUtils.SO_TIMEOUT);
        // 设置 Cookie
//        if (config.cookieMap != null && config.cookieMap.size() > 0) {
//            connection.setRequestProperty(HTTP_COOKIE, getCookieInfo(config.cookieMap));
//            BDebug.w("HttpUtils", getCookieInfo(config.cookieMap) + "----------" + url.toString());
//        }
        // 设置 User-Agent
//        connection.setRequestProperty(HTTP_USER_AGENT, HTTP_USER_AGENT_MESSAGE);
        connection.setDoInput(true);
        return connection;
    }

    /**
     * 创建HttpGet，并且配置http头【Cookie】
     * 
     * @param url
     * @return
     */
    public static HttpGet createHttpGetAndConfig(String url) {
        HttpGet httpGet = new HttpGet(url);

//        if (config.cookieMap != null && config.cookieMap.size() > 0) {
//            httpGet.setHeader(HTTP_COOKIE, getCookieInfo(config.cookieMap));
//            BDebug.w("HttpUtils", getCookieInfo(config.cookieMap) + "***********" + url);
//        }

//        httpGet.setHeader(HTTP_USER_AGENT, HTTP_USER_AGENT_MESSAGE);

        return httpGet;
    }

    /**
     * 创建HttpPost，并且配置http头【Cookie】
     * 
     * @param url
     * @param json
     * @return
     */
    public static HttpPost createHttpPostAndConfig(String url, String json) {
        HttpPost httpPost = new HttpPost(url);

//        if (config.cookieMap != null && config.cookieMap.size() > 0) {
//
//            httpPost.setHeader(HTTP_COOKIE, getCookieInfo(config.cookieMap));
//            BDebug.w("HttpUtils", getCookieInfo(config.cookieMap) + "***********" + url);
//
//        }

//        httpPost.setHeader(HTTP_USER_AGENT, HTTP_USER_AGENT_MESSAGE);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(HTTP_POST_BODY, json));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpPost;
    }
 /*   *//**
     *  
     * @param data 文件内容
     * @param url
     * @param json
     * @return
     *//*
    public static HttpPost createHttpPostAndConfig(String url, String json,byte[] data) {
        HttpPost httpPost = new HttpPost(url);
        
        if (config.getCookieMap() != null && config.getCookieMap().size() > 0) {
            
            httpPost.setHeader(HTTP_COOKIE, getCookieInfo(config.getCookieMap()));
            BDebug.w("HttpUtils", getCookieInfo(config.getCookieMap()) + "***********" + url);
            
        }
        
        httpPost.setHeader(HTTP_USER_AGENT, HTTP_USER_AGENT_MESSAGE);
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(HTTP_POST_BODY, json));
        org.apache.http.client.entity. entity = new MultipartEntity(
                HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return httpPost;
    }*/

    /**
     * 获取cookie信息
     * 
     * @param cookieMap
     * @return
     */
    public static String getCookieInfo(HashMap<String, String> cookieMap) {

        StringBuilder cookieInfo = new StringBuilder();

        if (cookieMap != null && cookieMap.size() > 0) {

            Iterator<Entry<String, String>> iter = cookieMap.entrySet().iterator();

            Entry<String, String> entry;

            while (iter.hasNext()) {

                String key = "";
                String value = "";
                entry = iter.next();
                key = entry.getKey();
                value = entry.getValue();
                cookieInfo.append(key).append("=").append(value).append(";");

            }

        }

        return cookieInfo.toString();
    }

    /**
     * 更新本地Cookie信息
     * 
     * @param defaultHttpClient
     */
//    public static void responseUpdateCookieHttpClient(DefaultHttpClient defaultHttpClient) {
//
//        boolean needUpdate = false;
//        List<Cookie> cookies = defaultHttpClient.getCookieStore().getCookies();
//        HashMap<String, String> cookieMap = config.cookieMap;
//
//        if (cookieMap == null) {
//            cookieMap = new HashMap<String, String>();
//        }
//
//        // StringBuilder cookiesInfo = new StringBuilder();
//        for (Cookie cookie : cookies) {
//            // cookie.getDomain();//返回Cookie的域属性
//
//            String key = cookie.getName();
//
//            String value = cookie.getValue();
//
//            if (cookieMap.size() == 0 || !value.equals(cookieMap.get(key))) {
//                needUpdate = true;
//            }
//
//            cookieMap.put(key, value);
//
//            // if (cookie.getName().equals(GlobalConfig.JSESSIONID)) {
//            // String value = cookie.getValue();
//            // if (!TextUtils.isEmpty(value) && !value.equals(config.getjSessionId())) {
//            // config.setjSessionId(value);
//            // needUpdate = true;
//            // }
//            // } else if (GlobalConfig.DYN_USER_CONFIRM.equals(cookie.getName())) {
//            // String value = cookie.getValue();
//            // if (!TextUtils.isEmpty(value) && !config.getCookieInfo().contains(value)) {
//            // config.setUserConfirm(value);
//            // needUpdate = true;
//            // }
//            // } else if (GlobalConfig.DYN_USER_ID.equals(cookie.getName())) {
//            // String value = cookie.getValue();
//            // if (!TextUtils.isEmpty(value) && !config.getCookieInfo().contains(value)) {
//            // config.setUserId(value);
//            // needUpdate = true;
//            // }
//            // } else if (GlobalConfig.BIG_ATGMOBILE.equals(cookie.getName())) {
//            // String value = cookie.getValue();
//            // if (!TextUtils.isEmpty(value) && !config.getCookieInfo().contains(value)) {
//            // config.setBig_atgMobile(value);
//            // needUpdate = true;
//            // }
//            // }
//            // cookiesInfo.append(cookie.getName() + "=" + cookie.getValue()).append(";");
//        }
//
////        if (needUpdate) {
////            // cookiesInfo = updateInfo(cookiesInfo);
////            config.cookieInfo=getCookieInfo(config.cookieMap);
////            Tools.updateConfigInfo();
////            PreferenceUtils.setObjectInfo(config.cookieMap, GlobalConfig.COOKIE_INFO_KEY);
////            // PreferenceUtils.setStringValue(GlobalConfig.COOKIEINFO, cookiesInfo.toString());
////        }
//    }

    /**
     * 更新本地Cookie信息
     *
     */
//    public static void responseUpdateCookieHttpURL(CookieStore store) {
//
//        boolean needUpdate = false;
//        List<HttpCookie> cookies = store.getCookies();
//        HashMap<String, String> cookieMap = config.cookieMap;
//
//        if (cookieMap == null) {
//            cookieMap = new HashMap<String, String>();
//        }
//
//        for (HttpCookie cookie : cookies) {
//
//            // cookie.getDomain();//返回Cookie的域属性
//
//            String key = cookie.getName();
//
//            String value = cookie.getValue();
//
//            if (cookieMap.size() == 0 || !value.equals(cookieMap.get(key))) {
//                needUpdate = true;
//            }
//
//            cookieMap.put(key, value);
//
//            // if (cookie.getName().equals(GlobalConfig.JSESSIONID)) {
//            // String value = cookie.getValue();
//            // if (!TextUtils.isEmpty(value) && !value.equals(config.getjSessionId())) {
//            // config.setjSessionId(value);
//            // needUpdate = true;
//            // }
//            // } else if (GlobalConfig.DYN_USER_CONFIRM.equals(cookie.getName())) {
//            // String value = cookie.getValue();
//            // if (!TextUtils.isEmpty(value) && !config.getCookieInfo().contains(value)) {
//            // config.setUserConfirm(value);
//            // needUpdate = true;
//            // }
//            // } else if (GlobalConfig.DYN_USER_ID.equals(cookie.getName())) {
//            // String value = cookie.getValue();
//            // if (!TextUtils.isEmpty(value) && !config.getCookieInfo().contains(value)) {
//            // config.setUserId(value);
//            // needUpdate = true;
//            // }
//            // } else if (GlobalConfig.BIG_ATGMOBILE.equals(cookie.getName())) {
//            // String value = cookie.getValue();
//            // if (!TextUtils.isEmpty(value) && !config.getCookieInfo().contains(value)) {
//            // config.setBig_atgMobile(value);
//            // needUpdate = true;
//            // }
//            // }
//            // cookiesInfo.append(cookie.getName() + "=" + cookie.getValue()).append(";");
//
//            BDebug.e(HTTP_COOKIE, cookie.getName() + "---->" + cookie.getDomain() + "------>" + cookie.getPath());
//        }
//
//        // BDebug.e("HttpUtils", cookiesInfo.toString());
//
//        if (needUpdate) {
//
//            config.cookieInfo=getCookieInfo(config.cookieMap);
//            Tools.updateConfigInfo();
//            PreferenceUtils.setObjectInfo(config.cookieMap, GlobalConfig.COOKIE_INFO_KEY);
//            // PreferenceUtils.setStringValue(GlobalConfig.COOKIEINFO, cookiesInfo.toString());
//        }
//    }

    /**
     * 更新本地登录状态
     * 
     * @param result
     * @param flag
     *            0--get 1--post
     */
//    public static void modifyLoginState(final Activity activity, String result, int flag, final boolean isGoGome) {
//        if (!TextUtils.isEmpty(result) && !HttpUtils.NO_CONN.equals(result)) {
//            JsonResult jsonresult = new JsonResult(result);
//            if ("Y".equalsIgnoreCase(jsonresult.getIsSessionExpired())) {
//                String message;
//                if (GlobalConfig.isLogin) {// 登录超时状态
//                    config.userConfirm="";
//                    config.userId="";
//                    GlobalConfig.isLogin = false;
//                    message = "对不起您的登录已超时，请重新登录。";
//                } else {// 未登录状态
//                    message = "对不起您尚未登录，请登录。";
//                }
//                toLogin(activity, message, isGoGome);
//            }
//        }
//    }

//    private static void toLogin(final Activity activity, final String message, final boolean isGoGome) {
//        if (activity != null && !activity.isFinishing()) {
//            activity.runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    Tools.toLogin(activity, message, isGoGome);
//                }
//            });
//        }
//    }

    /**
     * 创建 DefaultHttpClient
     * 
     * @return
     */
    public static DefaultHttpClient createHttpClientAndConfig() {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, HttpUtils.REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, HttpUtils.SO_TIMEOUT);
        return new DefaultHttpClient(httpParams);
    }

    // --------------------------公共方法-----------------------------------------------

    /**
     * InputStream --> String 将流转化为字符串
     * 
     * @param is
     * @return
     */
    protected static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return HttpUtils.NO_CONN;
        } finally {
//            ReleaseUtils.releaseInputstream(is);
            try{
                is.close();
            }catch (Exception e){

            }
        }
        return sb.toString();
    }
}
