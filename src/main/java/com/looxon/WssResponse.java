package com.looxon;

public interface WssResponse {

    void onMessage(String s);

    void onMessageParsed(WssRspMsg rsp);

    void onError(String error);
}
