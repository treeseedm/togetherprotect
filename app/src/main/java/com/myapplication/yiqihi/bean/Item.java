package com.myapplication.yiqihi.bean;

public class Item {

    public String id;
    public String value;
    public Boolean selected=false;
    public Integer count;
    public String label;
    public String name;


    public String continent;
    public String type;
    public String country;

    @Override
    public boolean equals(Object o) {
        Item old=(Item)o;
        if(this.value.equals(old.value))
            return true;
        return false;
    }
}
