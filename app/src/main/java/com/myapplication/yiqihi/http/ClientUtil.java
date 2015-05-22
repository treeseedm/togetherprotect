package com.myapplication.yiqihi.http;

import com.myapplication.yiqihi.constant.Constants;

import org.x.net.Client;

public class ClientUtil {
    private static Client mClient;
    public static  Client getClientInstant(){
        if(mClient==null){
            mClient = new Client(Constants.IP,Constants.PORT);
        }
        return  mClient;
    }
}
