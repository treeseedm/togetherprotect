package yiqihi.mobile.com.commonlib;

import android.content.Context;

import java.util.HashMap;

import http.OkHttpStack;

/**
 * 任务栈基类 V2.0
 * 
 * @author qiudongchao
 *
 */
public class BaseTaskForFile extends BaseTask<UploadPictureResult> {
    private byte[] mData;

    public BaseTaskForFile(Context context, boolean showProgress, byte[] data) {
        super(context, showProgress);
        mData = data;
    }

    @Override
    protected String getResponse(String request) {
        String response = OkHttpStack.getInstance().post(null, getServerUrl(), getParams(), mData);
        return response;
    }

    @Override
    public String getServerUrl() {
        return null;
    }
    
    @Override
    public Class<UploadPictureResult> getTClass() {
        // TODO Auto-generated method stub
        return UploadPictureResult.class;
    }
    
    
    public HashMap<String,String> getParams(){
        return null;
        
    }

}
