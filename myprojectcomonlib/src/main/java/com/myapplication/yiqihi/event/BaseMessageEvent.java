package com.myapplication.yiqihi.event;



public class BaseMessageEvent  {

    public Object message;
    public String mAction;
    public BaseMessageEvent(Object message,String action){
        this.message=message;
        this.mAction=action;
    }
}
