package com.myproduct.freetogether.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mahaifeng on 2015/4/25.
 */
public class LocationResponse extends BaseResponse implements Serializable {
    public List<Item> items;
}
