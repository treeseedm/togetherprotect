package com.myapplication.yiqihi.event;

import com.myapplication.yiqihi.adapter.GridViewAdapter;
import com.myapplication.yiqihi.bean.Country;

public class MessageEvent  {

    public Object message;
    public String mAction;
    public Country mCountry;
    public GridViewAdapter mAdapter;
    public MessageEvent(Object message,String action){
        this.message=message;
        this.mAction=action;
    }
    public MessageEvent(Country country,String action){
        this.mCountry=country;
        this.mAction=action;
    }
}
