package http;

import android.app.Activity;
import android.text.TextUtils;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * http stack by OKHTTP
 * Created by qiudongchao on 2015/2/26.
 */
public class OkHttpStack extends OkHttpClient implements HttpStack {

    /**
     *  单例实体
     */
    private static OkHttpStack mInstance;

    private OkHttpStack(){
        this.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);
        this.setWriteTimeout(TIMEOUT, TimeUnit.SECONDS);
        this.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
        this.setRetryOnConnectionFailure(true);//连接超时重试
    }

    /**
     * 获取单例
     * @return
     */
    public static OkHttpStack getInstance(){
        if(mInstance == null){
            mInstance = new OkHttpStack();
        }
        return mInstance;
    }

    @Override
    public String get(String url) {
        return get(null,url);
    }

    @Override
    public String get(Activity activity, String url) {
        Request.Builder builder = new Request.Builder();
        builder.url(url).headers(getRequestHeader().build()).get();
        return getResponse(activity,builder.build());
    }

    @Override
    public String post(Activity activity, String url, String json) {
        return post(activity,url,json,false);
    }

    @Override
    public String post(Activity activity, String url, String json,boolean isJson) {
        Headers.Builder headerBuilder = getRequestHeader();
        RequestBody requestBody = null;
        if(!TextUtils.isEmpty(json)){
            if (isJson) {
                requestBody = RequestBody.create(MediaType.parse(JSON_CHARSET), json);
            } else {
                FormEncodingBuilder formBody = new FormEncodingBuilder();
                formBody.add(HTTP_POST_BODY, json);
                requestBody = formBody.build();
            }
        }
        Request.Builder builder = new Request.Builder();
        if(requestBody != null){
            builder.url(url).headers(headerBuilder.build()).post(requestBody);
        }else{
            builder.url(url).headers(headerBuilder.build()).get();
        }
        return getResponse(activity,builder.build());
    }

    /**
     * 文件上传
     * @param activity
     * @param url
     * @param textParams
     * @return
     */
    public String post(Activity activity, String url, HashMap textParams,byte[] data) {
        Headers.Builder headerBuilder = getRequestHeader();

        RequestBody requestBody = null;
        MultipartBuilder multBuilder = new MultipartBuilder();
        multBuilder.type(MediaType.parse("multipart/form-data" + ";boundary=" + BOUNDARY));
        //表单
        if(textParams != null && textParams.size() > 0){
            Set<String> keySet = textParams.keySet();
            for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
                String name = it.next();
                String value = (String)textParams.get(name);
                multBuilder.addFormDataPart(name,value);
            }
        }
        multBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"jpg\""), RequestBody.create(MediaType.parse("image/jpg;charset=UTF-8"), data));
        requestBody = multBuilder.build();

        Request.Builder builder = new Request.Builder();
        builder.url(url).headers(headerBuilder.build()).post(requestBody);
        return getResponse(activity,builder.build());
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 处理response
     * @param activity
     * @param request
     * @return
     */
    public String getResponse(Activity activity, Request request){
        String responseString = null;
        try {
            Response response = this.newCall(request).execute();
            if(response.isSuccessful()){
                responseString = response.body().string();
                //更新本地信息
//                HttpBuilder.modifyLoginState(activity, responseString, HttpBuilder.GET, isGoGome);
//                List<String> cookies = response.headers(SET_COOKIE);
//                responseUpdateCookieHttpClient(cookies);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(responseString) ? NO_CONN : responseString;
    }

    /**
     * 获取请求头
     * @return
     */
    public Headers.Builder getRequestHeader(){
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add(HTTP_USER_AGENT, HTTP_USER_AGENT_MESSAGE);
//        if (config.cookieMap != null && config.cookieMap.size() > 0) {
//            headerBuilder.add(HTTP_COOKIE, getCookieInfo(config.cookieMap));
//        }
        return headerBuilder;
    }

    /**
     * 获取cookie信息
     * @param cookieMap
     * @return
     */
    public static String getCookieInfo(HashMap<String, String> cookieMap) {
        StringBuilder cookieInfo = new StringBuilder();
        if (cookieMap != null && cookieMap.size() > 0) {
            Iterator<Map.Entry<String, String>> iter = cookieMap.entrySet().iterator();
            Map.Entry<String, String> entry;
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
     * 更新本地cookie
     * @param cookies
     */
//    public static void responseUpdateCookieHttpClient(List<String> cookies) {
//        boolean needUpdate = false;
//        HashMap<String, String> cookieMap = config.cookieMap;
//        if (cookieMap == null) {
//            cookieMap = new HashMap<String, String>();
//        }
//        for (String cookie : cookies) {
//            String[] temp = cookie.split(";");
//            String cookieString = temp[0];
//            if(!TextUtils.isEmpty(cookieString)){
//                String[] cookieKeyAndValue = cookieString.split("=");
//                if(cookieKeyAndValue.length == 2){
//                    String key = cookieKeyAndValue[0];
//                    String value = cookieKeyAndValue[1];
//                    if (cookieMap.size() == 0 || !value.equals(cookieMap.get(key))) {
//                        needUpdate = true;
//                    }
//                    cookieMap.put(key, value);
//                }
//            }
//        }
//        if (needUpdate) {
//            config.cookieInfo = getCookieInfo(config.cookieMap);
//            Tools.updateConfigInfo();
//            PreferenceUtils.setObjectInfo(config.cookieMap, GlobalConfig.COOKIE_INFO_KEY);
//        }
//    }
}
