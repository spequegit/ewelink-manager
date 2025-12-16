package com.looxon;

import com.looxon.model.FamilyPage;
import com.looxon.model.OutletSwitch;
import com.looxon.model.Thing;

import java.util.ArrayList;
import java.util.List;

public class EwelinkController {

    private EwelinkConnector connector;

    public List<Thing> loginAndResolveDevices() throws Exception {
        connector = new EwelinkConnector("eu", "speque.login@gmail.com", "ekapi34!IE", "+48");
        connector.login();
        FamilyPage family = connector.getFamily();
        Thread.sleep(100);
        return connector.getDevices();
    }

    public void doEwelink() throws Exception {
        List<Thing> things = loginAndResolveDevices();
        for (Thing thing : things) {
            List<OutletSwitch> switches = thing.getItemData().getParams().getSwitches();
            System.out.println(
                    thing.getItemData().getDeviceid() + " / " +
                            thing.getItemData().getName() + " / " +
                            thing.getItemData().getOnline() + " / " +
                            thing.getItemData().getParams().getSwitch()
            );
            if (switches != null) {
                for (OutletSwitch outletSwitch : switches) {
                    System.out.println(outletSwitch);
                }
            }
            System.out.println();
        }
        connector.setDeviceStatus("kitchen hood", "off");
        setDeviceStates("10017b1fc5", false, false, false, false);
    }

    private void setDeviceStates(String id, boolean state0, boolean state1, boolean state2, boolean state3) throws Exception {
        List<OutletSwitch> switches = new ArrayList<>();
        switches.add(prepareSwitch(0, state0));
        switches.add(prepareSwitch(1, state1));
        switches.add(prepareSwitch(2, state2));
        switches.add(prepareSwitch(3, state3));
        connector.setMultiDeviceStatus(id, switches);
    }

    private void setDeviceStates(String id, boolean state0, boolean state1) throws Exception {
        List<OutletSwitch> switches = new ArrayList<>();
        switches.add(prepareSwitch(0, state0));
        switches.add(prepareSwitch(1, state1));
        connector.setMultiDeviceStatus(id, switches);
    }


    private OutletSwitch prepareSwitch(int outlet, boolean state) {
        OutletSwitch outletSwitch = new OutletSwitch();
        outletSwitch.setOutlet(outlet);
        outletSwitch.set_switch(state ? "on" : "off");
        return outletSwitch;
    }
}