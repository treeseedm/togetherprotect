package com.myproduct.freetogether.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class GuideRequest {
    public long bizType;
    public int catalog;
    public String title;
    public String location;
    public String deptData;
    public String Day;
    public Car car;
    public People people;
    public int recruiteCount;
    public String price;
    public String creditPrice;
    public int chargeWay;
    public String description;
    public int expire;
    public List<Picture> pictures;
    public Detail detail;
    public Me me;

    public String getRequestMsg() {
        JSONObject object = new JSONObject();
        object.put("bizType", bizType);
        object.put("catalog", catalog);
        object.put("title", title);
        object.put("location", location);
        object.put("deptData", deptData);
        object.put("Day", Day);
        object.put("car", car.getJsonCar());
        object.put("people", people.getJsonPeople());
        object.put("recruiteCount", recruiteCount);
        object.put("price", price);
        object.put("creditPrice", creditPrice);
        object.put("chargeWay", chargeWay);
        object.put("description", description);
        object.put("expire", expire);
        JSONArray array = new JSONArray();
        for (Picture picture : pictures) {
            array.add(picture.getJsonPicture());
        }
        object.put("pictures", array);
        object.put("detail", detail.getJsonDetail());
        object.put("me", me.getJsonMe());
        return object.toString();
    }
}
