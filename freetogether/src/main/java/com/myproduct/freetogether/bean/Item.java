package com.myproduct.freetogether.bean;

import java.util.List;

public class Item {

    public String id;
    public Long userId;//头像
    public String userName;
    public Integer catalog;//类型7：AA拼导游;8：AA拼车;9：海外代购;10：带你玩 11：陪你玩
    public String title;//标题
    public Integer person;//人数
    public Integer day;//天数
    public String deptDate;//出发日期
    public String description;
    public List<Picture> pictures = null;//4)	图片：pictures，横向平铺最多3个
    public String ago;
    public String location;//地点
    public Integer recruiteCount;//还差几人
    public Integer personPrice;//费用平摊
    public String car;//车辆类型
    public Integer joinStatus;//加入状态

    public String sex;//性别要求
    public Boolean isFull;
    public Boolean isDeal;
    public Boolean isOwner;
    public String expireTime;//自动销毁
    public String msgId;
    public Integer unReadMsg;
    public String creditPrice;//费用平摊
    //城市
    public String value;
    public String continent;
    public String type;
    public String country;

}
