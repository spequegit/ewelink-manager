
package com.looxon.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OnlyDevice {

    @SerializedName("ota")
    @Expose
    private String ota;

    public String getOta() {
        return ota;
    }

    public void setOta(String ota) {
        this.ota = ota;
    }

}
