package com.looxon;


public class EwelinkResponse implements WssResponse {
    @Override
    public void onMessage(String s) {
        System.out.println("Message received:" + s);
    }

    @Override
    public void onMessageParsed(WssRspMsg rsp) {
        if (rsp.getError() == null) {
            System.out.println("Device:" + rsp.getDeviceid());
            if (rsp.getParams() != null) {
                System.out.println("Switch:" + rsp.getParams().getSwitch());
            }
        } else if (rsp.getError() == 0) {
            System.out.println("login success");
        } else if (rsp.getError() > 0) {
            System.out.println("login error:" + rsp.toString());
        }
    }

    @Override
    public void onError(String error) {
        System.out.println("Error: " + error);
    }
}
