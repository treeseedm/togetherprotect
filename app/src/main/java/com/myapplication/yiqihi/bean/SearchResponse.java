package com.myapplication.yiqihi.bean;

import java.util.List;

public class SearchResponse extends BaseResponse{
    public Integer pageNumber;
    public Integer pageCount;
    public Integer totalCount;
    public List<Item> requires = null;
    public List<Facet> facets = null;
    public List<Facet> filters = null;
    public List<Product> items = null;

}
