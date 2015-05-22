package com.myapplication.yiqihi.task;

import android.content.Context;
import android.util.Log;

import com.myapplication.yiqihi.event.MessageEvent;
import com.myapplication.yiqihi.http.ClientUtil;
import com.myapplication.yiqihi.http.MethodHelper;
import org.x.net.Msg;
import org.x.net.MsgEvent;

import de.greenrobot.event.EventBus;
import yiqihi.mobile.com.commonlib.BaseTask;
import yiqihi.mobile.com.commonlib.CommonUtility;

public class YQHBaseTask extends BaseTask implements MsgEvent {
    private static final  String TAG="YQHBaseTask";
    public static String ACTION_READSEARCHHOT="readSearchHot";
    public static String ACTION_SEARCHTRIP="searchTrip";
    public static final String ACTION_FINDLOCATION="findLocation";
    public static final String ACTION_UPDATEPANEL="updatepanel";
    public static final String ACTION_UPDATEFILTER="updtefilter";
    public static final String ACTION_CHANGETEXT="changetext";
    private String mAction;
    private String mParams;
    /**
     * 构造方法
     *
     * @param context      上下文
     * @param showProgress
     */
    public YQHBaseTask(Context context, boolean showProgress,String action,String params) {
        super(context, showProgress);
        this.mAction=action;
        this.mParams=params;
    }
    /**
     * 获取服务器URL
     *
     * @return
     */
    @Override
    public String getServerUrl() {
        return "/module";
    }


    @Override
    protected Object doInBackground(Void... params) {
        ClientUtil.getClientInstant().bind(this);
        Log.e(TAG,"request params:"+mParams);
        ClientUtil.getClientInstant().postUrl(mAction, getServerUrl(), mParams);
        return null;
    }

    @Override
    public void onResponse(Msg.DataType type, String action, String key, Object data) {
        EventBus.getDefault().post(new MessageEvent(data,mAction));
    }



















}
