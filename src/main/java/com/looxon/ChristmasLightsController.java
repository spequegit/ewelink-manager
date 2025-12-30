package com.looxon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.looxon.Application.log;
import static com.looxon.EwelinkRestController.*;
import static com.looxon.EwelinkService.OFF;
import static com.looxon.EwelinkService.ON;

@Component
public class ChristmasLightsController {

    public static final String ID = "10007bcf6a";
    @Autowired
    private EwelinkRestController controller;

    public void randomize() throws Exception {
        List<String> states = randomizeStates();
        boolean noneOn = isNone(states, ON);
        boolean noneOff = isNone(states, OFF);
        while (noneOff || noneOn) {
            states = randomizeStates();
            noneOn = isNone(states, ON);
            noneOff = isNone(states, OFF);
        }
        controller.switchDevice(ID, states.get(0), states.get(1), states.get(2), states.get(3));
        log.info("christmas lights randomized");
    }

    private boolean isNone(List<String> states, String state) {
        return states.stream().noneMatch(s -> s.equals(state));
    }

    private List<String> randomizeStates() {
        return new ArrayList<>(List.of(randomizeState(), randomizeState(), randomizeState(), randomizeState()));
    }

    private String randomizeState() {
        return new Random().nextBoolean() ? ON : OFF;
    }

    public void turnOffAllLights() throws Exception {
        controller.switchDevice(ID, OFF, OFF, OFF, OFF);
        log.info("christmas lights off");
    }
}
