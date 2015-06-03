package com.myapplication.yiqihi.http;

import android.util.Log;

import com.mongodb.BasicDBObject;
import com.myapplication.yiqihi.constant.Constants;

import org.x.net.Client;
import org.x.net.Msg;
import org.x.net.MsgEvent;

public class ClientUtil {
    private static Client mClient;
    public static  Client getClientInstant(){
        if(mClient==null){
            mClient = new Client(Constants.IP,Constants.PORT);
            mClient.bind(new MsgEvent() {
                @Override
                public void onResponse(Msg.DataType type, String action, String key, Object data) {
                    switch (type) {
                        case json:
                        case string:
                            Log.v("connect","action"+action);
                            BasicDBObject result = (BasicDBObject) data;
                            System.out.println(data.toString());
                            if (action.equalsIgnoreCase("connect")
                                    && result.getBoolean("xeach", false) == true) {
                                ClientUtil.getClientInstant().login("3@yiqihi.com", "abc123");
                            } else if (action.equalsIgnoreCase("login")
                                    && result.getBoolean("xeach", false) == true) {
                                ClientUtil.getClientInstant().auth = true;
                                ClientUtil.getClientInstant().privateKey = result.getString("key");
                                ClientUtil.getClientInstant().userId = result.getLong("id");
                                ClientUtil.getClientInstant().userName = result.getString("name");
                                ClientUtil.getClientInstant().initAliyun(result.getString("aliyun-hostId"),
                                        result.getString("aliyun-key"),
                                        result.getString("aliyun-secret"),
                                        result.getString("imageDomain"));
                            }
                            break;
                        case bytes:
//                byte[] bytes = (byte[]) data;
                            break;
                        case error:
//                System.out.println(data);
                            break;
                    }
                }
            });
            mClient.connect();
        }
        return  mClient;
    }
}
