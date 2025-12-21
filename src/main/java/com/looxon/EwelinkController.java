package com.looxon;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.realzimboguy.ewelink.api.EweLink;
import com.github.realzimboguy.ewelink.api.model.home.OutletSwitch;
import com.github.realzimboguy.ewelink.api.model.home.Thing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EwelinkController {

    private static final Logger log = LoggerFactory.getLogger(EwelinkController.class);
    public static final String OFF = "off";
    public static final String ON = "on";
    public static final String EMAIL = "speque.login@gmail.com";
    private EweLink eweLink;

    @GetMapping("/devices")
    public ResponseEntity<String> getDevices() throws Exception {
        eweLink = setupEwelink();


        List<Thing> things = eweLink.getThings();

        List<String> result = things.stream()
                .map(t -> t.getItemData().getDeviceid() + ":" + t.getItemData().getName())
                .toList();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);

        return ResponseEntity.ok(json);
    }

    @PostMapping("/device/{id}")
    public String switchDevice(@PathVariable("id") String id, String givenStatus) throws Exception {
        eweLink = setupEwelink();
        Thing thing = eweLink.getThings().stream().filter(t -> t.getItemData().getDeviceid().equals(id)).findFirst().get();
        log.info(givenStatus == null ? "no parameter - switch" : "parameter - set to " + givenStatus);
        String value = thing.getItemData().getParams().getSwitch();
        log.info("current state: " + value);
        String status = givenStatus != null ? givenStatus : ON.equals(value) ? OFF : ON;
        log.info("switching " + id + " " + status + " [" + thing.getItemData().getName() + "]");
        eweLink.setDeviceStatus(id, status);
        log.info("done");
        return thing.getItemData().getParams().getSwitch();
    }

    private EweLink setupEwelink() throws Exception {
        if (eweLink == null) {
            turnOffEwelinkLogs();
            log.info("login " + EMAIL);
            eweLink = new EweLink("eu", EMAIL, "ekapi34!IE", "+48", 0);
            eweLink.login();
        }
        log.info("successfuly logged in");
        return eweLink;
    }

    private static void turnOffEwelinkLogs() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.github.realzimboguy.ewelink");
        Level original = logger.getLevel();
        logger.setLevel(Level.OFF);  // or Level.ERROR
    }

    private void setDeviceStates(String id, boolean state0, boolean state1, boolean state2, boolean state3) throws Exception {
        List<OutletSwitch> switches = new ArrayList<>();
        switches.add(createSwitch(0, state0));
        switches.add(createSwitch(1, state1));
        switches.add(createSwitch(2, state2));
        switches.add(createSwitch(3, state3));
        eweLink.setMultiDeviceStatus(id, switches);
    }

    private OutletSwitch createSwitch(int outlet, boolean state) {
        OutletSwitch outletSwitch = new OutletSwitch();
        outletSwitch.setOutlet(outlet);
        outletSwitch.set_switch(state ? ON : OFF);
        return outletSwitch;
    }
}