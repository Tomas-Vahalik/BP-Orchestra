/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.Event;
import backend.Events;
import clients.EventClient;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

/**
 *
 * @author Tomáš Vahalík
 */
public class HomeView {

    private EventClient ec;
    private String session;
    private MyUI ui;
    private boolean isAdmin;

    public HomeView(String session, MyUI ui, boolean isAdmin) {
        this.session = session;
        this.ui = ui;
        this.isAdmin = isAdmin;
        ec = new EventClient(ui.base);
    }

    public VerticalLayout getLayout() {
        VerticalLayout resultLayout = new VerticalLayout();
        resultLayout.addComponent(ui.getToolbar(session, ui));

        //GRID WITH EVENTS
        Grid<Event> grid = new Grid<>();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        Events events = ec.findAll_XML(Events.class);
        //Events events = new Events();
        grid.setItems(events.getEvents());
        grid.removeAllColumns();
        grid.addColumn(backend.Event::getName).setCaption("Name");
        grid.addColumn(backend.Event::getFormatedDate).setCaption("Date");
        grid.addColumn(event -> "Detail",
                new ButtonRenderer(clickEvent -> {
                    backend.Event event = (backend.Event) clickEvent.getItem();
                    ui.setContent(new EventDetailView(event, session, ui, isAdmin).getLayout());

                }));
        if (isAdmin) {
            grid.addColumn(event -> "Edit",
                    new ButtonRenderer(clickEvent -> {
                        backend.Event event = (backend.Event) clickEvent.getItem();
                        ui.setContent(new CreateEventView(session, ui, event, isAdmin).getLayout());

                    }));
            grid.addColumn(event -> "Remove",
                    new ButtonRenderer(clickEvent -> {
                        backend.Event event = (backend.Event) clickEvent.getItem();
                        ec.remove("" + event.getId());
                        Events ev = ec.findAll_XML(Events.class);
                        grid.setItems(ev.getEvents());

                    }));

        }
        grid.setWidth("800px");

        resultLayout.addComponents(grid);

        return resultLayout;
    }

}
