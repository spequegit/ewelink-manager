package com.looxon;

public class MyWssResponse implements WssResponse {
    public MyWssResponse() {
    }

    @Override
    public void onMessage(String s) {
        System.out.println("onMessage: "+s);
    }

    @Override
    public void onMessageParsed(WssRspMsg rsp) {
        System.out.println("onMessageParsed: "+rsp.toString());
    }

    @Override
    public void onError(String error) {
        System.out.println("onError: "+error);
    }
}