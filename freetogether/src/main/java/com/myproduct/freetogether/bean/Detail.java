package com.myproduct.freetogether.bean;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;

import java.io.Serializable;

public class Detail implements Serializable{
    public static final String[] ageValues = new String[]{"8-26岁", "27-35岁", "5-43岁", "44-52岁", "53-63岁"};//0(文本显示：18-26岁),1(27-35岁),2(35-43岁),3(44-52岁),4(53-63岁)
    public static final String[] sexValues = new String[]{"不限", "男生", "女生", "情侣", "家庭"};//不限(文本显示：不限)，男生(男生), 女生(女生), 情侣(情侣), 家庭(小家庭)
    public static final String[] jobValues=new String[]{"不限","学生","上班族","自由职业","老板"};//0(不限),1(学生),2(上班族),3(自由职业),4(老板)
    public static final String[] langValues=new String[]{"中文",
            "英语","法语","德语","西班牙语","俄语","日语","韩语","泰语"};//0(中文),1(英语),2(法语),3(德语),4(西班牙语),5(俄语),6(日语),7(韩语),8(泰语)
    public static final String[] heightValues=new String[]{"160-165","65-170","170-175","175-180","180以上"};//0(160-165),1(165-170),2(170-175),3(175-180),4(180以上)
   public static final String[] buildValues=new String[]{"苗条","匀称","健壮","小巧","丰满","高挑","较胖","较瘦","运动型"};
    //身材build取值:0(苗条),1(匀称),2(健壮),3(高大),4(小巧),5(丰满),6(高挑),7(较胖),8(较瘦),9(运动型)
    public static final String[] xingeValues=new String[]{"活泼开朗","外向幽默","低调内向","闷骚"};//0(活泼开朗),1(外向幽默),2(低调内向),3(闷骚)
    public static final String[] typeValues=new String[]{"萌萌哒","甜美公主","花季少女",
    "熟女","优雅女士","阳光男孩","靠谱青年","少年大叔","青春大爷"};//0(萌萌哒)，1(甜美公主),2(花季少女),3(熟女),4(优雅女士),5(阳光男孩),6(靠谱青年),7(少年大叔),8(青春大爷)
    public int age;
    public int sex;
    public int job;
    public int lang;
    public int height;
    public int build;
    public int xinge;
    public int type;
    public String note;
    public String model;
    public String detail;
    public BasicDBObject getJsonDetail() {
        BasicDBObject object = new BasicDBObject();
        object.put("age", age);
        object.put("sex", sex);
        object.put("job", job);
        object.put("lang", lang);
        object.put("height", height);
        object.put("build", build);
        object.put("xinge", xinge);
        object.put("type", type);
        object.put("note", note);
        object.put("model", model);
        return object;
    }
}
