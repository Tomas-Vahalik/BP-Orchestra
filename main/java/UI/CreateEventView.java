/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.MusicalPiece;
import backend.MusicalPieces;
import clients.EventClient;
import clients.MusicalPieceClient;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tomáš Vahalík
 */
public class CreateEventView {

    private EventClient ec;
    private MusicalPieceClient pc;
    private String session;
    private MyUI ui;
    private boolean isAdmin;
    private backend.Event event;
    private Set<MusicalPiece> piecesSet = new HashSet();

    public CreateEventView(String session, MyUI ui, backend.Event event, boolean isAdmin) {
        this.session = session;
        ec = new EventClient(ui.base);
        pc = new MusicalPieceClient(ui.base);
        this.ui = ui;
        this.event = event;
        if (event != null) {
            piecesSet = event.getPieces();
        }
        this.isAdmin = isAdmin;
    }

    public VerticalLayout getLayout() {
        VerticalLayout resultLayout = new VerticalLayout();

        resultLayout.addComponent(ui.getToolbar(session, ui));

        //Name layout
        HorizontalLayout nameLayout = new HorizontalLayout();
        Label nameLabel = new Label("Event name:");
        TextField nameField = new TextField();
        if (event != null) {
            nameField.setValue(event.getName());
        }
        nameLayout.addComponents(nameLabel, nameField);
        //Description
        TextArea descriptionField = new TextArea();
        if (event != null) {
            descriptionField.setValue(event.getDescription());
        }
        descriptionField.setWidth("500px");

        //Date layout
        HorizontalLayout dateLayout = new HorizontalLayout();
        Label dateLabel = new Label("Event date:");
        DateField dateField = new DateField();

        if (event != null) {
            if (event.getEventDate() != null) {
                LocalDate date = event.getEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dateField.setValue(date);
            }

        }

        dateLayout.addComponents(dateLabel, dateField);
        //Pieces grid
        Grid<MusicalPiece> pieceGrid = new Grid<>();
        pieceGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        pieceGrid.setItems(piecesSet);
        pieceGrid.removeAllColumns();
        pieceGrid.addColumn(MusicalPiece::getName).setCaption("Name");

        //pieces box layout
        HorizontalLayout addPieceLayout = new HorizontalLayout();
        MusicalPieces pieces = pc.findAll_XML(MusicalPieces.class);
        List<String> pieceNames = new ArrayList<String>();
        for (MusicalPiece p : pieces.getPieces()) {
            pieceNames.add(p.getName());
        }

        ComboBox<String> selectPiece = new ComboBox<>("Select musical piece");
        selectPiece.setItems(pieceNames);
        Button addPiece = new Button("Add this piece");
        addPiece.addClickListener((e) -> {
            //find piece
            MusicalPiece piece = pc.findByName_XML(MusicalPiece.class, selectPiece.getValue());
            if (piece == null) {
                Notification.show("This piece does not exist", Notification.Type.ERROR_MESSAGE);
                return;
            }

            piecesSet.add(piece);
            //update piece grid
            pieceGrid.setItems(piecesSet);

        });
        addPieceLayout.addComponents(selectPiece, addPiece);

        //remove selected pieces
        Button removeSelected = new Button("Remove selected");
        removeSelected.addClickListener((e) -> {
            Set<MusicalPiece> selected = pieceGrid.getSelectedItems();

            piecesSet.removeAll(selected);
            event.setPieces(piecesSet);            
            pieceGrid.setItems(piecesSet);

        });
        //save button
        Button addButton = new Button("Save this event");
        addButton.addClickListener((e) -> {

            //check valid input
            if (nameField.getValue() == null
                    || descriptionField.getValue() == null
                    || dateField.getValue() == null) {
                Notification.show("Please fill in all required fields", Notification.Type.ERROR_MESSAGE);
                return;
            }

            boolean newEvent = false;
            if (event == null) {
                event = new backend.Event();
                newEvent = true;
            }

            event.setName(nameField.getValue());
            event.setDescription(descriptionField.getValue());
            if (dateField.getValue() != null) {
                Date eventDate = Date.from(dateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                event.setEventDate(eventDate);
            }
            event.setPieces(piecesSet);
            if (newEvent == true) {
                ec.create_XML(event);
            } else {
                ec.edit_XML(event, "" + event.getId());
            }
            Notification.show("Event saved successfully", Notification.Type.HUMANIZED_MESSAGE);
        });

        resultLayout.addComponents(nameLayout, descriptionField, dateLayout, addPieceLayout, pieceGrid, removeSelected, addButton);
        return resultLayout;
    }

}
