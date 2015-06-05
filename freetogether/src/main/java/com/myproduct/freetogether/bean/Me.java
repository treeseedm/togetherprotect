package com.myproduct.freetogether.bean;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

import java.io.Serializable;

public class Me implements Serializable {
    public static final String[] sexValues = new String[]{ "男生", "女生", "情侣", "家庭"};//不限(文本显示：不限)，男生(男生), 女生(女生), 情侣(情侣), 家庭(小家庭)
    public static final String[] bloodValues=new String[]{"O型","A型","B型","AB型"};// 0( O型) ,1(A型), 2(B型),3(AB型)
    public static final String[] weightValues=new String[]{"40-50","50-60","60-70","70-80","80-90","90以上"};//
    public String contactPerson;
    public String contactMobile;
    public String contactQQ;
    public String contactMSN;
    public String sex;
    public String birthday;
    public String height;
    public String blood;
    public String weight;
    public String lang;
    public String myInfo;

    public BasicDBObject getJsonMe() {
        BasicDBObject object = new BasicDBObject();
        object.put("contactPerson", contactPerson);
        object.put("contactMobile", contactMobile);
        object.put("contactQQ", contactQQ);
        object.put("contactMSN", contactMSN);
        object.put("sex", sex);
        object.put("birthday", birthday);
        object.put("height", height);
        object.put("blood", blood);
        object.put("weight", weight);
        object.put("lang", lang);
        return object;
    }
}
