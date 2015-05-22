package http;

import android.app.Activity;
import android.text.TextUtils;

/**
 * http请求工具类,根据版本号进行http请求方式切换
 * 
 * @author qiudongchao
 * 
 */
public class HttpUtils {
    public static final String TAG = HttpUtils.class.getSimpleName();

    /**
     * 连接失败标识
     */
    public static final String NO_CONN = "FAIL";

    /**
     * 设置请求超时30秒
     */
    public static final int REQUEST_TIMEOUT = 30 * 1000;

    /**
     * 设置等待数据超时时间30秒钟
     */
    public static final int SO_TIMEOUT = 30 * 1000;

    public interface Method {
        int GET = 0;
        int POST = 1;
    }

    /**
     * 执行GET请求
     * 
     * @param mActivity
     *            上下文
     * 
     * @param url
     *            请求URL
     * @return
     */
    public static String sendHttpRequestByGet(Activity mActivity, String url, boolean isGoGome) {
        return sendHttpRequest(Method.GET,mActivity,url,isGoGome,"",false);
    }

    /**
     * @param mActivity
     *            上下文
     * 
     * @param url
     *            请求 URL
     * @param json
     *            请求 JSON
     * @return
     */
    public static String sendHttpRequestByPost(Activity mActivity, String url, String json, boolean isGoGome,
            boolean isjson) {
        return sendHttpRequest(Method.POST,mActivity,url,isGoGome,json,isjson);
    }

    /**
     * 获取 HttpStack【注：版本判断】
     * 
     * @return
     */
    private static HttpStack getHttpStack() {
        return OkHttpStack.getInstance();
    }

    /**
     * 发送http request
     * @return
     */
    private static String sendHttpRequest(int requestType, Activity context, String url,boolean isGoGome,String json,
                                          boolean isjson){
        boolean isPost = requestType == Method.POST;
        String threadId = String.valueOf(Thread.currentThread().getId()) + "-";


        String result = NO_CONN;
        HttpStack http = getHttpStack();
        long time1 = System.currentTimeMillis();

        if(isPost){
            if (isjson){
                result = http.post(context, url, json, isjson);
            }
            else{
                result = http.post(context, url, json, isGoGome);
            }
        }else{
            result = http.get(context, url);
        }
        result = TextUtils.isEmpty(result) ? NO_CONN : result;


        return result;
    }
}