
package com.looxon.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DevConfig {

    @SerializedName("timerCount")
    @Expose
    private String timerCount;

    public String getTimerCount() {
        return timerCount;
    }

    public void setTimerCount(String timerCount) {
        this.timerCount = timerCount;
    }

}
