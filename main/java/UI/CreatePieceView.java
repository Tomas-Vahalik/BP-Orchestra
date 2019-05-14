/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.MusicalPiece;
import backend.Sheet;
import backend.Sheets;
import clients.MusicalPieceClient;
import clients.SheetClient;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tomáš Vahalík
 */
public class CreatePieceView {

    private SheetClient sc;
    private MusicalPieceClient pc;
    private String session;
    private MyUI ui;
    private boolean isAdmin;
    private MusicalPiece piece;
    private Set<Sheet> sheetSet = new HashSet();

    public CreatePieceView(String session, MyUI ui, MusicalPiece piece, boolean isAdmin) {
        this.session = session;
        sc = new SheetClient(session, ui.base);
        pc = new MusicalPieceClient(ui.base);
        this.ui = ui;
        this.piece = piece;

        this.isAdmin = isAdmin;
    }

    public VerticalLayout getLayout() {
        VerticalLayout resultLayout = new VerticalLayout();

        resultLayout.addComponent(ui.getToolbar(session, ui));

        //Name layout
        HorizontalLayout nameLayout = new HorizontalLayout();
        Label nameLabel = new Label("Piece name:");
        TextField nameField = new TextField();
        if (piece != null) {
            nameField.setValue(piece.getName());
        }
        nameLayout.addComponents(nameLabel, nameField);

        //Sheet grid
        Grid<Sheet> sheetGrid = new Grid<>();
        sheetGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        if (piece != null) {
            Sheets _sheets = sc.findByPiece_XML(Sheets.class, piece.getId());
            sheetSet.addAll(_sheets.getSheets());
            sheetGrid.setItems(sheetSet);
        }

        sheetGrid.removeAllColumns();
        sheetGrid.addColumn(Sheet::getName).setCaption("Name");

        //sheet box layout
        HorizontalLayout addSheetLayout = new HorizontalLayout();
        Sheets _sheets = sc.findAll_XML(Sheets.class);
        List<String> sheetNames = new ArrayList<String>();
        for (Sheet s : _sheets.getSheets()) {
            sheetNames.add(s.getName());
        }

        ComboBox<String> selectSheet = new ComboBox<>("Select Sheet");
        selectSheet.setItems(sheetNames);
        Button addSheet = new Button("Add this sheet");
        addSheet.addClickListener((e) -> {
            //find piece
            Sheet sheet = sc.findByName_XML(Sheet.class, selectSheet.getValue());
            if (sheet == null) {
                Notification.show("This sheet does not exist", Notification.Type.ERROR_MESSAGE);
                return;
            }

            sheetSet.add(sheet);
            //update piece grid
            sheetGrid.setItems(sheetSet);

        });
        addSheetLayout.addComponents(selectSheet, addSheet);

        //remove selected pieces
        Button removeSelected = new Button("Remove selected");
        removeSelected.addClickListener((e) -> {
            if (piece == null) {
                return;
            }
            Set<Sheet> selected = sheetGrid.getSelectedItems();
            sheetSet.removeAll(selected);
            piece.setSheets(sheetSet);
            sheetGrid.setItems(sheetSet);

        });
        //save button
        Button addButton = new Button("Save this piece");
        addButton.addClickListener((e) -> {
            boolean newPiece = false;
            if (piece == null) {
                piece = new MusicalPiece();
                newPiece = true;
            }

            piece.setName(nameField.getValue());
            //piece.setSheets(sheetSet);
            if (newPiece == true) {
                pc.create_XML(piece);
            } else {
                pc.edit_XML(piece, "" + piece.getId());
            }

            //updateSheets
            for (Sheet s : sheetSet) {
                piece = pc.findByName_XML(MusicalPiece.class, nameField.getValue());
                s.setPiece(piece);
                sc.edit_XML(s, "" + s.getId());
            }
            Notification.show("Piece saved successfully", Notification.Type.HUMANIZED_MESSAGE);
        });

        Button newPiece = new Button("Add next piece");
        newPiece.addClickListener((event) -> {
            this.piece = null;
            nameField.setValue("");
            sheetSet = new HashSet();

        });

        resultLayout.addComponents(nameLayout, addSheetLayout, sheetGrid, addButton, newPiece);
        return resultLayout;
    }
}
