
package com.looxon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ThingInfo {

    @SerializedName("thingList")
    @Expose
    private List<Thing> thingList;
    @SerializedName("total")
    @Expose
    private Integer total;

    @Override
    public String toString() {
        return "ThingInfo{" +
                "thingList=" + thingList +
                ", total=" + total +
                '}';
    }

    public List<Thing> getThingList() {
        return thingList;
    }

    public void setThingList(List<Thing> thingList) {
        this.thingList = thingList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
