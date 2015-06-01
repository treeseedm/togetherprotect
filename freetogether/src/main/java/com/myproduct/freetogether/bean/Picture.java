package com.myproduct.freetogether.bean;

import com.alibaba.fastjson.JSONObject;

public class Picture {

    public String shotUrl;
    public String id;
    public String title;
    public String fileName;
    public byte[] datacenter;

    public String getJsonPicture() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("title", title);
        object.put("fileName", fileName);
        object.put("datacenter", datacenter);
        return object.toString();
    }
}
