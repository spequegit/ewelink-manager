package com.looxon;

import com.github.realzimboguy.ewelink.api.model.home.OutletSwitch;
import com.github.realzimboguy.ewelink.api.model.home.Timer;
import lombok.Data;

import java.util.List;

@Data
public class Device {
    private String deviceName;
    private String deviceId;
    private String productModel;
    private String oneSwitch;
    private List<OutletSwitch> switches;
    private List<Timer> Timers;
}


