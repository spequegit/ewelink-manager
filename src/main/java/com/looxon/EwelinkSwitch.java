package com.looxon;

public class EwelinkSwitch {
    public static void main(String[] parameter) throws Exception {
        EwelinkRestController ewelinkController = new EwelinkRestController();
        if (parameter.length == 3) {
            ewelinkController.switchDevice(parameter[0], parameter[1], parameter[2], null, null);
        }
        if (parameter.length == 2) {
            ewelinkController.switchDevice(parameter[0], parameter[1]);
        }
        if (parameter.length == 1) {
            ewelinkController.switchDevice(parameter[0], null);
        }
        System.exit(0);
    }
}
