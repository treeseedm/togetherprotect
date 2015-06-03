package com.myapplication.yiqihi.task;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.myapplication.yiqihi.event.BaseMessageEvent;
import com.myapplication.yiqihi.http.ClientUtil;

import org.x.net.Msg;
import org.x.net.MsgEvent;

import de.greenrobot.event.EventBus;
import yiqihi.mobile.com.commonlib.BaseTask;

public class YQHBaseTaskByListeners extends BaseTask implements MsgEvent{
    private static final  String TAG="YQHBaseTask";
    public static final String ACTION_READACTIVITIES="readActivities";
    public static final String ACTION_WRITEREQUIRE="writeRequire";
    private String mAction;
    private String mParams;
    private Context mContext;
    private MsgEvent mMsgEvent;

    /**
     * 构造方法
     *
     * @param context      上下文
     * @param showProgress
     */
    public YQHBaseTaskByListeners(Context context, boolean showProgress, String action, String params,MsgEvent msgEvent) {
        super(context, showProgress);
        this.mContext=context;
        this.mAction=action;
        this.mParams=params;
        this.mMsgEvent=msgEvent;
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
    protected Object doInBackground(Object... params) {
        ClientUtil.getClientInstant().bind(this);
        Log.e(TAG,"request params:"+mParams);
        ClientUtil.getClientInstant().postUrl(mAction, getServerUrl(), mParams);
        return null;
    }

    @Override
    public void onResponse(final Msg.DataType type, final String action, final String key, final Object data) {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    mMsgEvent.onResponse(type,action,key,data);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
