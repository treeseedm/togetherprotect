package com.myapplication.yiqihi.http;

import android.text.TextUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class MethodHelper {
    private static final String MODULENAME="mobile";
    public static BasicDBObject getSearchRequestParams(){

        BasicDBObject oReq=ClientUtil.getClientInstant().newQuery(MODULENAME,3);
        BasicDBList items = new BasicDBList();
        oReq.append("items", items);

        BasicDBObject oItem = new BasicDBObject();
        items.add(oItem);
        oItem.append("name", "guide");
        oItem.append("value", "3vai");

        oItem = new BasicDBObject();
        items.add(oItem);
        oItem.append("name", "car");
        oItem.append("value", "36mN");

        oItem = new BasicDBObject();
        items.add(oItem);
        oItem.append("name", "product");
        oItem.append("value", "index");

        oItem = new BasicDBObject();
        items.add(oItem);
        oItem.append("name", "slider");
        oItem.append("value", "mobile-search");

        oItem = new BasicDBObject();
        items.add(oItem);
        oItem.append("name", "group");
        oItem.append("value", "all");
        return oReq;
    }

    /**
     *
     * @param continent           value
     * @param countryName      value
     * @param cityName            value
     * @param category        id
     * @param language           id-id
     * @param field            1:默认排序，2:价格 3:更新日期
     * @param mode  0:升序1:降序
     * @param serviceTag       id-id
     * @param priceTag           id-id
     * @return
     */
    public static BasicDBObject searchTrip(String continent,String countryName,String cityName,
                                           String category,String language,int field,int mode,String serviceTag,String priceTag,int currentPage){
        BasicDBObject oReq=ClientUtil.getClientInstant().newQuery(MODULENAME,8);
        if(!TextUtils.isEmpty(category))
        oReq.append("category",category);
        if(!TextUtils.isEmpty(countryName)&&!"全部".equals(countryName))
        oReq.append("country",countryName);
        if(!TextUtils.isEmpty(cityName)&&!"全部".equals(cityName))
        oReq.append("location",cityName);
        if(!TextUtils.isEmpty(continent)&&!"全部".equals(continent))
        oReq.append("continent",continent);
        oReq.append("language", language);
        BasicDBObject sort = new BasicDBObject();
        sort.append("field", field);// 1:默认排序，2:价格 3:更新日期
        sort.append("mode", mode);// 0:升序1:降序
        oReq.append("sort", sort);
        oReq.append("serviceTag", serviceTag);
      oReq.append("priceTag", priceTag);
        oReq.append("pageNumber",currentPage);
        return oReq;
    }
    public static BasicDBObject searchTrip(String continent,String countryName,String cityName,
                                           String category,int currentPage){
        BasicDBObject oReq=ClientUtil.getClientInstant().newQuery(MODULENAME,8);
        oReq.append("category",category);
        oReq.append("country",countryName);
        oReq.append("location",cityName);
        oReq.append("continent",continent);
        oReq.append("pageNumber",currentPage);
        return oReq;
    }
    public static BasicDBObject searchTrip(String category,int currentPage){
        BasicDBObject object=ClientUtil.getClientInstant().newQuery(MODULENAME,8);
        BasicDBObject sort = new BasicDBObject();
//        sort.append("field", field);// 1:默认排序，2:价格 3:更新日期
//        object.append("sort", sort);
        object.append("category",category);
        object.append("pageNumber",currentPage);
        return object;
    }
    public static BasicDBObject findLocationParams(String location){
        BasicDBObject object=ClientUtil.getClientInstant().newQuery(MODULENAME,5);
        if(TextUtils.isEmpty(location)){
            location="";
        }
        object.append("q",location);
        return object;
    }

}
