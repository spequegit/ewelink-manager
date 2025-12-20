package com.looxon;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("test")
public class TestView extends VerticalLayout {

    private EwelinkController controller;

    public TestView() throws Exception {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        controller = new EwelinkController();

//        List<Thing> things = controller.loginAndResolveDevices();
//        // 1. Tworzymy Grid
////        Grid<Person> grid = new Grid<>(Person.class, false); // false = nie używamy auto-konfiguracji
//        Grid<Thing> gridThings = new Grid<>(Thing.class, false); // false = nie używamy auto-konfiguracji
//
//        gridThings.addColumn(t -> t.getItemData().getName()).setHeader("name").setSortable(true);
//        gridThings.addColumn(t -> t.getItemData().getDeviceid()).setHeader("device id").setSortable(true);
//        gridThings.addColumn(t -> t.getItemData().getProductModel()).setHeader("model").setSortable(true);
//        gridThings.addColumn(t -> t.getItemData().getOnline()).setHeader("online").setSortable(true);
//        gridThings.addColumn(t -> t.getItemData().getParams().getSwitches()).setHeader("params").setSortable(true);

//
//        gridThings.setItems(things);
//
//        gridThings.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
//        gridThings.addItemClickListener(event ->
//                Notification.show("Wybrano "+event.getItem().getItemData().getName())
//        );
//
//        add(gridThings);
    }
}