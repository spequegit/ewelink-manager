package com.looxon;

import ch.qos.logback.classic.Level;
import com.github.realzimboguy.ewelink.api.EweLink;
import com.github.realzimboguy.ewelink.api.model.home.ItemData;
import com.github.realzimboguy.ewelink.api.model.home.OutletSwitch;
import com.github.realzimboguy.ewelink.api.model.home.Thing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Service
public class LoginService {
    public static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private static final String EMAIL = "speque.login@gmail.com";
    public static final String PASSWORD = "ek"+"api"+2*17+"!IE";
    private EweLink eweLink;

    public EweLink prepareEweLink() throws Exception {
        if (eweLink == null) {
            turnOffEwelinkLogs();
            eweLink = new EweLink("eu", EMAIL, PASSWORD, "+48", 0);
            eweLink.login();
            log.info("logged in " + EMAIL);
        } else {
            log.info("session active " + EMAIL);
        }
        return eweLink;
    }

    private void turnOffEwelinkLogs() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.github.realzimboguy.ewelink");
        logger.setLevel(Level.OFF);  // or Level.ERROR
    }
}