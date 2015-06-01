package com.myproduct.freetogether.bean;

import com.alibaba.fastjson.JSONObject;

public class People {
    public int person;

    public String getJsonPeople() {
        JSONObject object = new JSONObject();
        object.put("person", person);
        return object.toString();
    }
}
