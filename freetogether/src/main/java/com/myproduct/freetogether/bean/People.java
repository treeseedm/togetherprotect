package com.myproduct.freetogether.bean;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

public class People {
    public int person;

    public BasicDBObject getJsonPeople() {
        BasicDBObject object = new BasicDBObject();
        object.put("person", person);
        return object;
    }
}
