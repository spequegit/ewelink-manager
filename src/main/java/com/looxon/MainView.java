package com.looxon;

import com.looxon.model.DeviceDefinition;
import com.looxon.model.OutletSwitch;
import com.looxon.model.Thing;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.IntStream;

@Route()
public class MainView extends VerticalLayout {

    private EwelinkController controller;

    public MainView() throws Exception {
        controller = new EwelinkController();

        List<Thing> things = controller.loginAndResolveDevices();

        for(Thing thing:things){
            Text name = new Text(thing.getItemData().getName() + " - ");
            String productModel = thing.getItemData().getProductModel();


            Text model = new Text(productModel);

            add(name,model);

            List<OutletSwitch> switches = thing.getItemData().getParams().getSwitches();
            int switchesCount = DeviceDefinition.getSwitchesCount(productModel);

            if(switchesCount > 1){
                IntStream.range(0, switchesCount).forEach(i -> {
                    Button button = new Button(switches.get(i).get_switch().toString(), e -> {
                        Notification.show("done");
                    });
                    add(button);
                });
            }
            if(switchesCount == 1){
                IntStream.range(0, switchesCount).forEach(i -> {
                    Button button = new Button(thing.getItemData().getName(), e -> {
                        Notification.show("done");
                    });
                    add(button);
                });
            }
        }
    }
}