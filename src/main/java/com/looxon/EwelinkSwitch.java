package com.looxon;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EwelinkSwitch {
    private static final Logger log = LoggerFactory.getLogger(EwelinkSwitch.class);
    public static final String OFF = "off";

    public static void main(String[] args) throws Exception {
        EwelinkController ewelinkController = new EwelinkController();
        String id = args[0];
        String status = args.length == 2 ? args[1] : null;
        ewelinkController.switchDevice(id, status);
        System.exit(0);
    }
}
