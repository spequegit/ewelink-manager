package com.looxon.model;

import java.util.Arrays;

public enum DeviceDefinition {
    MINI("MINI", 1),
    _4CH("4CH", 4),
    BASICR4("BASICR4", 1),
    DUALR2("DUALR2", 2),
    Basic2("Basic2", 1),
    MINIR3("MINIR3", 1),
    BASIC_R3("BASIC_R3", 1),
    Dual_R2("Dual_R2", 2),
    DUALR3_Lite("DUALR3 Lite", 2),
    Device_0285("0285", 2),
    NON_OTA_GL_177("NON-OTA-GL(177)", 0),
    DW2_Wi_Fi_L("DW2-Wi-Fi-L", 0),
    _4CHPROR3("4CHPROR3", 4),
    S_CAM("S-CAM", 0);

    private String model;
    private int switchesCount;

    DeviceDefinition(String model, int switchesCount) {
        this.model = model;
        this.switchesCount = switchesCount;
    }

    public static int getSwitchesCount(String model) {
        return Arrays.stream(values()).filter(device -> {
            String model1 = device.model;
            return model1.equals(model);
        }).findFirst().get().switchesCount;
    }
}
