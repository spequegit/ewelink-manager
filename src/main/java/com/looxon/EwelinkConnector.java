package com.looxon;

import com.google.gson.Gson;
import com.looxon.model.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EwelinkConnector {

    private static final String HMAC_SHA_256 = "HmacSHA256";
    private static final int TIMEOUT = 5000;
    private static final String APP_ID = "Uw83EKZFxdif7XFXEsrpduz5YyjP7nTl";
    private static final String APP_SECRET = "mXLOjea0woSMvK9gw7Fjsy7YlFO4iSu6";
//    private static final String UTF_8 = "utf-8";
    public static final String UTF_8 = "UTF-8";
    private static String region;
    private String email;
    private String password;
    private String countryCode;
    private String baseUrl = "TBA";
    private String accessToken;
    private String apiKey;

    private Gson gson = new Gson();
    private EweLinkWebSocketClient client = null;
    private WssResponse clientWssResponse;

    public EwelinkConnector(String region, String email, String password, String countryCode) {
        this.region = region;
        this.email = email;
        this.password = password;
        this.countryCode = countryCode;
        this.baseUrl = "https://" + region + "-apia.coolkit.cc/v2/";
    }

    public void login() throws Exception {

        URL url = new URL(baseUrl + "user/login");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        LoginRequest request = new LoginRequest();
        request.setLang("en");
        request.setCountryCode(countryCode);
        request.setOs("Android");
        request.setModel("Pixel 4a (5G)_bramble");
        request.setRomVersion("13");
        request.setAppVersion("5.0.0");
        request.setEmail(email);
        request.setPassword(password);

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Sign " + getAuthMac(gson.toJson(request)));
        connection.setRequestProperty("X-Ck-Nonce", randomString(8));
        connection.setRequestProperty("X-Ck-Appid", APP_ID);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

        outputStream.writeBytes(gson.toJson(request));
        outputStream.flush();
        outputStream.close();
        System.out.println("Response code:" + connection.getResponseCode());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Login Response Raw:{}" + response.toString());
            LoginResponse loginResponse = gson.fromJson(response.toString(), LoginResponse.class);

            if (loginResponse.getError() > 0) {
                throw new Exception(loginResponse.getMsg());
            } else {
                accessToken = loginResponse.getData().getAt();
                apiKey = loginResponse.getData().getUser().getApikey();
                System.out.println("accessToken: " + accessToken);
                System.out.println("apiKey: " + apiKey);
            }
        }
    }

    private String getAuthMac(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] byteKey = APP_SECRET.getBytes(UTF_8);
        Mac mac = Mac.getInstance(HMAC_SHA_256);
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA_256);
        mac.init(keySpec);
        byte[] bytes = mac.doFinal(data.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    public List<Thing> getDevices() throws Exception {
        List<Family> list = getFamily().getData().getFamilyList();
        return list.stream().map(family -> {
            try {
                return getDevices(family.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Thing> getDevices(String familyId) throws Exception {

        URL url = new URL(baseUrl + "device/thing?familyid=" + familyId + "&num=300&beginIndex=-999999");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);
        int responseCode = connection.getResponseCode();
        System.out.println("Responce code: " + responseCode);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            System.out.println("getHome Response Raw: " + response.toString());
            Homepage homepage = gson.fromJson(response.toString(), Homepage.class);
            System.out.println("getHome Response: " + gson.toJson(homepage));

            if (homepage.getError() > 0) {
                //something wrong with login, throw exception back up with msg
                throw new Exception("getHome Error:" + gson.toJson(homepage));
            } else {
                System.out.println("getHome: " + gson.toJson(homepage));
                return homepage.getData().getThingList();
            }
        }
    }

    public FamilyPage getFamily() throws Exception {

        URL url = new URL(baseUrl + "family");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        System.out.println("Responce code: " + connection.getResponseCode());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            System.out.println("getFamily Response Raw: " + response.toString());

            FamilyPage familyPage = gson.fromJson(response.toString(), FamilyPage.class);
            System.out.println("getFamily Response: " + gson.toJson(familyPage));

            if (familyPage.getError() > 0) {
                //something wrong with login, throw exception back up with msg
                throw new Exception("getFamily Error:" + gson.toJson(familyPage));
            } else {
                System.out.println("getFamily:{}" + gson.toJson(familyPage));
                return familyPage;
            }
        }
    }

    public boolean setDeviceStatus(String deviceId, String status) throws Exception {

        if (client == null) {
            System.out.println("No WSS Connection, creating one");
            initWebSocketClient(new MyWssResponse());
            while (!client.isOpen()) {
                System.out.print(".");
                Thread.sleep(1000);
            }
            System.out.println("WSS Connected");
        }

        System.out.println("Setting device {} status to {}" + deviceId + " " + status);

        StatusChange change = new StatusChange();
        change.setSequence(new Date().getTime() + "");
        change.setUserAgent("app");
        change.setAction("update");
        change.setDeviceid(deviceId);
        change.setApikey(apiKey);
        change.setSelfApikey(apiKey);
        Params params = new Params();
        params.setSwitch(status);
        change.setParams(params);
        System.out.println("StatusChange WS Request:{}" + gson.toJson(change));
        return client.sendAndWait(gson.toJson(change), change.getSequence());
    }

    public boolean setMultiDeviceStatus(String deviceId, List<OutletSwitch> outletSwitches) throws Exception {
        System.out.println("Setting device status on multi output" + deviceId + " " + gson.toJson(outletSwitches));
        StatusChange statusChange = new StatusChange();
        statusChange.setSequence(new Date().getTime() + "");
        statusChange.setUserAgent("app");
        statusChange.setAction("update");
        statusChange.setDeviceid(deviceId);
        statusChange.setApikey(apiKey);
        statusChange.setSelfApikey(apiKey);
        Params params = new Params();
        params.setSwitches(outletSwitches);
        statusChange.setParams(params);
        System.out.println("StatusChange WS Request:" + gson.toJson(statusChange));

        return client.sendAndWait(gson.toJson(statusChange), statusChange.getSequence());
    }

    public void initWebSocketClient(WssResponse response) throws Exception {
        client = new EweLinkWebSocketClient(new URI("wss://" + region + "-pconnect3.coolkit.cc:8080/api/ws"));
        clientWssResponse = response;
        client.setWssResponse(clientWssResponse);
        client.setWssLogin(gson.toJson(new WssLogin(accessToken, apiKey, APP_ID, randomString(8))));
        client.connect();
    }

    private String randomString(int l) {
        SecureRandom random = new SecureRandom();
        String letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(l);
        for (int i = 0; i < l; i++) {
            stringBuilder.append(letters.charAt(random.nextInt(letters.length())));
        }
        return stringBuilder.toString();
    }
}
