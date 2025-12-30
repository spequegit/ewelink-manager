package com.looxon;

import com.github.realzimboguy.ewelink.api.EweLink;
import com.github.realzimboguy.ewelink.api.model.home.ItemData;
import com.github.realzimboguy.ewelink.api.model.home.OutletSwitch;
import com.github.realzimboguy.ewelink.api.model.home.Thing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

import static com.looxon.Application.log;

@Service
public class EwelinkService {

    public static final String WATER_SOFTENER_ID = "10010916ac";  // softener
    public static final int ONE_MINUTE = 1000 * 60;
    public static final String ON = "on";
    public static final String OFF = "off";
    private EweLink eweLink;

    @Autowired
    private LoginService loginService;

    public List<Device> getDevices() throws Exception {
        eweLink = loginService.prepareEweLink();
        List<Thing> things = eweLink.getThings();
        log.info(things.toString());
        return things.stream().map(thing -> createDevice(thing)).toList();
    }

    public String runWaterSoftenerProcess() throws Exception {
        log.info("WaterSoftenerProcess started at: " + new Date());

        log.info("filling...");
        String id = WATER_SOFTENER_ID;
        switchDevice(id, ON, OFF, OFF, OFF);    // filling
        sleepHours(1);
        switchDevice(id, OFF, OFF, OFF, OFF);   // wait for salt
        log.info("filling DONE");

        log.info("waiting for salt solution...");
        sleepHours(1);
        log.info("salt solution DONE");

        log.info("rinsing...");
        switchDevice(id, OFF, ON, OFF, OFF);   // rinsing
        sleepHours(2);
        switchDevice(id, OFF, OFF, OFF, OFF);   // end
        log.info("rinsing DONE");

        log.info("WaterSoftenerProcess finished at: " + new Date());
        return "executed";
    }

    public String off() {
        new Thread() {
            @Override
            public void run() {
                try {
                    log.info("turning off...");
                    Thread.sleep(3000);
                    System.exit(0);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                super.run();
            }
        }.start();
        return "service turned off";
    }

    public String switchDevice(@PathVariable("id") String id, @RequestParam(value = "status", required = false) String status) throws Exception {
        return switchDevice(id, status, null, null, null);
    }

    public String switchDevice(
            @PathVariable("id") String id,
            @RequestParam(value = "switch1", required = false) String switch1,
            @RequestParam(value = "switch2", required = false) String switch2,
            @RequestParam(value = "switch3", required = false) String switch3,
            @RequestParam(value = "switch4", required = false) String switch4
    ) throws Exception {

        eweLink = loginService.prepareEweLink();
        Thing thing = getThing(id);
        List<OutletSwitch> switches = getSwitches(thing);

        if (switches != null) {
            for (OutletSwitch outletSwitch : switches) {
                switch (outletSwitch.getOutlet()) {
                    case 0 -> switchIfProvided(switch1, outletSwitch);
                    case 1 -> switchIfProvided(switch2, outletSwitch);
                    case 2 -> switchIfProvided(switch3, outletSwitch);
                    case 3 -> switchIfProvided(switch4, outletSwitch);
                }
            }
            eweLink.setMultiDeviceStatus(id, switches);
            log.info("device [" + thing.getItemData().getName() + "] switched [" + switch1 + " " + switch2 + " " + switch3 + " " + switch4 + "]");
            return switches.toString();
        } else {
            String oneSwitch = thing.getItemData().getParams().getSwitch();
            switch1 = switch1 != null ? switch1 : (ON.equals(oneSwitch) ? OFF : ON);
            log.info("switching " + id + " " + switch1 + " [" + thing.getItemData().getName() + "]");
            eweLink.setDeviceStatus(id, switch1);
            return oneSwitch;
        }
    }

    private Device createDevice(Thing thing) {
        Device device = new Device();
        ItemData item = thing.getItemData();
        device.setDeviceId(item.getDeviceid());
        device.setDeviceName(item.getName());
        device.setProductModel(item.getProductModel());
        device.setOneSwitch(item.getParams().getSwitch());
        device.setSwitches(item.getParams().getSwitches());
        device.setTimers(item.getParams().getTimers());
        return device;
    }

    private void sleepHours(int hours) throws InterruptedException {
        int step = 10;  // minutes
        for (int i = hours * 60; i > 0; i -= step) {
            log.info("remaining time: " + i + " minutes");
            Thread.sleep(ONE_MINUTE * step);
        }
    }

    private void sleepMinutes(int minutes) throws InterruptedException {
        int step = 10;  // seconds
        for (int i = minutes * 60; i > 0; i -= step) {
            log.info("remaining time: " + i + " seconds");
            Thread.sleep(1000 * step);
        }
    }

    private void switchIfProvided(String value, OutletSwitch outletSwitch) {
        if (value != null) {
            outletSwitch.set_switch(value);
        }
    }

    private Thing getThing(String id) throws Exception {
        return eweLink.getThings().stream().filter(t -> t.getItemData().getDeviceid().equals(id)).findFirst().get();
    }

    private List<OutletSwitch> getSwitches(Thing thing) {
        return thing.getItemData().getParams().getSwitches();
    }
}