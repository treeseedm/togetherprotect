package com.myapplication.yiqihi.bean;

import java.io.Serializable;
import java.util.List;

public class ReadSearchHotResponse extends BaseResponse implements Serializable{


    public List<Guide> guide = null;
    public List<Guide> car = null;
    public List<Guide> product = null;
    public List<Guide> group=null;
    public List<Slider> slider = null;

}
