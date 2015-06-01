package com.myproduct.freetogether.bean;

import android.util.SparseIntArray;

import com.alibaba.fastjson.JSONObject;

public class Car {
   public static final String[] carValues=new String[]{"不需要用车","导游私家车","租车"};//0：不需要用车，1：导游私家车，2：租车
    public int enabled;
    public int type;
    public String brand;
    public int count;

    public String getJsonCar() {
        JSONObject object = new JSONObject();
        object.put("enabled", enabled);
        object.put("type", type);
        object.put("brand", brand);
        object.put("count", count);
        return object.toString();
    }
}
