package com.myproduct.freetogether.bean;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.BasicDBObject;

import java.util.List;

public class GuideRequest {
    public static final String[] CARGEWAY=new String[]{"AA制平摊","我付60%，你付40%","我付70%，你付30%"};
    public String bizType;
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
    public static String[] getExpire(){
        String[] e=new String[30];
        for(int i=1;i<31;i++){
            e[i-1]=i+"天后，自动销毁";
        }
        return e;
    }
    public BasicDBObject getRequestMsg() {
        BasicDBObject object=new BasicDBObject();
        object.put("bizType", bizType);
        object.put("catalog", catalog);
        object.put("title", title);
        object.put("location", location);
        object.put("deptData", deptData);
        object.put("Day", Day);
        if(car!=null)
        object.put("car", car.getJsonCar());
        if(people!=null)
        object.put("people", people.getJsonPeople());
        object.put("recruiteCount", recruiteCount);
        object.put("price", price);
        object.put("creditPrice", creditPrice);
        object.put("chargeWay", chargeWay);
        object.put("description", description);
        object.put("expire", expire);
        JSONArray array = new JSONArray();
        if(pictures!=null){
            for (Picture picture : pictures) {
                array.add(picture.getJsonPicture());
            }
        }
        object.put("pictures", array);
        if(detail!=null)
        object.put("detail", detail.getJsonDetail());
        if(me!=null)
        object.put("me", me.getJsonMe());

        return object;
    }
}
