package com.looxon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class EwelinkRestController {

    @Autowired
    private EwelinkService service;

    @GetMapping("/devices")
    public List<Device> getDevices() throws Exception {
        return service.getDevices();
    }

    @PostMapping("/softener")
    public String runWaterSoftenerProcess() throws Exception {
        return service.runWaterSoftenerProcess();
    }

    @PostMapping("/off")
    public String off() {
        return service.off();
    }

    @PostMapping("/device/{id}")
    public String switchDevice(
            @PathVariable("id") String id,
            @RequestParam(value = "switch1", required = false) String switch1,
            @RequestParam(value = "switch2", required = false) String switch2,
            @RequestParam(value = "switch3", required = false) String switch3,
            @RequestParam(value = "switch4", required = false) String switch4
    ) throws Exception {
        return service.switchDevice(id, switch1, switch2, switch3, switch4);
    }
}