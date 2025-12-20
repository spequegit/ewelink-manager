package com.looxon;

import com.github.realzimboguy.ewelink.api.EweLink;
import com.github.realzimboguy.ewelink.api.model.home.OutletSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EwelinkController {

    private static final Logger log = LoggerFactory.getLogger(EwelinkController.class);
    private EweLink eweLink;

    @GetMapping("/devices")
    public List<String> getDevices() throws Exception {
        eweLink = setupEwelink();
        return eweLink.getThings().stream().map(t -> t.getItemData().getName()+"-"+t.getItemData().getDeviceid()).toList();
    }

    @GetMapping("/device/{id}")
    public String switchDevice(@PathVariable("id") String id) throws Exception {
        eweLink = setupEwelink();
        eweLink.setDeviceStatus(id, "on");
        return "done";
    }

    private EweLink setupEwelink() throws Exception {
        if (eweLink == null) {
            log.info("logging...");
            eweLink = new EweLink("eu", "speque.login@gmail.com", "ekapi34!IE", "+48", 0);
            eweLink.login();
        }
        log.info("logged in");
        return eweLink;
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
        outletSwitch.set_switch(state ? "on" : "off");
        return outletSwitch;
    }
}