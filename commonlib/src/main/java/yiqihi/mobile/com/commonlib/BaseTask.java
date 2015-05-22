package yiqihi.mobile.com.commonlib;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import http.AsyncUtils;
import http.HttpUtils;
import yiqihi.mobile.com.commonlib.customview.LoadingDialog;

/**
 * 任务栈基类 V2.0
 * 
 * @author qiudongchao
 * 
 * @param <T>
 */
public abstract class BaseTask<T> extends AsyncTask<Void, Void, T> {

    /**
     * 是否显示进度框
     */
    protected boolean isShowProgress;

    /**
     * 是否主动弹出错误信息_默认不主动弹Toast
     */
    protected boolean isShowErrorMessage = false;

    /**
     * 进度框
     */
    protected LoadingDialog progressDialog;

    /**
     * 上下文
     */
    protected Context mContext;

    /**
     * 信息_保存错误信息
     */
    protected String mMessage;
    protected boolean isGoGome = false;
    protected Activity mActivity;
    protected boolean isJson = false;// 是否是JSon 请求

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     * @param showProgress
     *            是否显示进度条
     */
    public BaseTask(Context context, boolean showProgress) {
        inits(context, showProgress);
    }

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     * @param showProgress
     *            是否显示进度条
     * @param showError
     *            是否主动提示错误信息
     */
    public BaseTask(Context context, boolean showProgress, boolean showError) {
        inits(context, showProgress);
        isShowErrorMessage = showError;
    }

    public BaseTask(Context context, boolean showProgress, boolean showError, boolean json) {
        inits(context, showProgress);
        isShowErrorMessage = showError;
        isJson = json;
    }

    /**
     * 初始化基础数据
     * 
     * @param context
     * @param showProgress
     */
    private void inits(Context context, boolean showProgress) {
        mContext = context;
        isShowProgress = showProgress;
    }

    @Override
    protected void onPreExecute() {
        if (mContext == null) {
            return;
        }
        if (isShowProgress) {
            if (TextUtils.isEmpty(getLoadingMessage())) {
                progressDialog = CommonUtility.showLoadingDialog(mContext, "加载中", true,
                        new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                                onCancelDialog();
                            }
                        });
            } else {

            }
        }
        onPre();
    }

    @Override
    protected T doInBackground(Void... params) {
        try {
            String response = "";

            JSONObject jsonObj = new JSONObject();
            /**
             * 对jsonobj赋值
             */
            builderJSON(jsonObj);
            String jsonObjString = jsonObj.isEmpty() ? "" : jsonObj.toJSONString();
            String buildJson = builder();
            String request = !TextUtils.isEmpty(buildJson) ? buildJson : jsonObjString;
            response = getResponse(request);
            if (!TextUtils.isEmpty(response) && !"FAIL".equals(response)) {
                return parser(response);
            } else {
                mMessage = mContext.getString(R.string.data_load_fail_exception);
                return null;
            }
        } catch (Exception e) {
           e.printStackTrace();
            mMessage = "客户端异常！";
            return null;
        }
    }

    /**
     * http请求【若修改http请求方式，请在子类里重写该方法】
     * 
     * @param request
     *            HTTP请求参数
     * @return 服务器端返回数据
     */
    protected String getResponse(String request) {
        String response = null;
        if (TextUtils.isEmpty(request)) {
            response = HttpUtils.sendHttpRequestByGet(mActivity, getServerUrl(), isGoGome);
        } else {
            response = HttpUtils.sendHttpRequestByPost(mActivity, getServerUrl(), request, isGoGome, isJson);
        }
        return response;
    }

    @Override
    protected void onPostExecute(T result) {
        if (mContext == null) {
            return;
        }
        if (isShowProgress&&progressDialog!=null) {
            progressDialog.dismiss();
        }
        if (result != null) {
//            if (result instanceof BaseResponse) {
//                BaseResponse res = (BaseResponse) result;
//                onPost(res.isSuccess(), result, res.getFailReason());
//            } else {
                onPost(true, result, "");
//            }
        } else {
            onPost(false, null, mMessage);
        }
    }

    /**
     * 执行异步请求
     * 
     * @param isNeedNetwork
     *            是否需要判断网络
     */
    public void exec(boolean isNeedNetwork) {
        exec(isNeedNetwork, null);
    }

    /**
     * 执行异步请求
     * 
     * @param isNeedNetwork
     *            是否需要判断网络
     * @param listener
     *            当没有网络的情况执行接口
     */
    public void exec(boolean isNeedNetwork, OnNoNetWorkListener listener) {
        /**
         * 判断网络
         */
        if (isNeedNetwork && !NetUtility.isNetworkAvailable(mContext)) {
            if (listener != null) {
                listener.onNoNetWork();
            }
//            CommonUtility.showToast(mContext,
//                    mContext.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        AsyncUtils.execute(this);
    }

    /**
     * 执行异步请求
     */
    public void exec() {
        exec(true);
    }

    public static interface OnNoNetWorkListener {
        void onNoNetWork();
    }

    // ###################可以重写的方法###################

    /**
     * 数据解析
     * 
     * @param response
     * @return
     */
    public T parser(String response) {
        Class<T> tClass = getTClass();
        if (tClass == null) {
            mMessage = "请重写getTClass方法";
            return null;
        } else {
            /**
             * 通过fastjson解析Object
             */
            return JSON.parseObject(response, tClass);
        }
    }

    /**
     * 获取泛型T的Class
     * 
     * @return
     */
    public Class<T> getTClass() {
        return null;
    }

    /**
     * 构造请求参数 为空 默认为get请求
     * 
     * @return
     */
    public String builder() {
        return null;
    }

    /**
     * 直接对json进行赋值
     * 
     * @param obj
     */
    public void builderJSON(JSONObject obj) {
    }

    /**
     * 获取服务器URL
     * 
     * @return
     */
    public abstract String getServerUrl();

    /**
     * 获取加载提示信息_正在加载进度条文字
     * 
     * @return
     */
    public String getLoadingMessage() {
        return null;
    }

    /**
     * 取消加载进度_执行事件
     */
    public void onCancelDialog() {

    }

    /**
     * 异步加载前_执行事件
     */
    public void onPre() {

    }

    /**
     * 异步加载结束___注意：子类的的代码必须在super.onPost(success, result, errorMessage)下面，并且super.onPost(success, result,
     * errorMessage)必须存在
     */
    public void onPost(boolean success, T result, String errorMessage) {
        if (isShowErrorMessage && !success) {
            String message = TextUtils.isEmpty(errorMessage) ? mContext.getString(R.string.data_load_fail_exception)
                    : errorMessage;
            CommonUtility.showToast(mContext, message);
            return;
        }
    }

    public void setAcitivity(Activity activity) {
        this.mActivity = activity;
    }

    public void setIsGoGome(boolean isGoGome) {
        this.isGoGome = isGoGome;
    }

}
