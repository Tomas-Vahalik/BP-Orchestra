/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.MusicalPiece;
import backend.MusicalPieces;
import backend.Sheet;
import backend.Sheets;
import clients.MusicalPieceClient;
import clients.SheetClient;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import java.util.ArrayList;

/**
 *
 * @author Tomáš Vahalík
 */
public class ManagePiecesView {

    private MusicalPieceClient pc;
    private SheetClient sc;
    private String session;
    private MyUI ui;
    private boolean isAdmin;
    private MusicalPiece selectedPiece = null;

    public ManagePiecesView(String session, MyUI ui, boolean isAdmin) {
        this.session = session;
        pc = new MusicalPieceClient(ui.base);
        sc = new SheetClient(session, ui.base);
        this.ui = ui;
        this.isAdmin = isAdmin;
    }

    public VerticalLayout getLayout() {
        VerticalLayout resultLayout = new VerticalLayout();

        resultLayout.addComponent(ui.getToolbar(session, ui));

        HorizontalLayout gridLayout = new HorizontalLayout();

        //GRID WITH SHEETS
        Grid<Sheet> sheetGrid = new Grid<>();
        sheetGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        sheetGrid.addColumn(Sheet::getName).setCaption("Sheet");
        sheetGrid.addColumn(sheet -> "View",
                new ButtonRenderer(clickEvent -> {
                    Sheet sheet = (Sheet) clickEvent.getItem();
                    ui.getPage().open("../webresources/eu.cz.fit.vahalto1.orchestraapplication.sheet/" + sheet.getId() + "/pdf", "Sheet", false);

                }));
        //GRID WITH PIECES
        Grid<MusicalPiece> pieceGrid = new Grid<>();
        pieceGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        MusicalPieces pieces = pc.findAll_XML(MusicalPieces.class);
        pieceGrid.setItems(pieces.getPieces());
        pieceGrid.removeAllColumns();
        pieceGrid.addColumn(MusicalPiece::getName).setCaption("Piece");
        pieceGrid.addColumn(piece -> "Sheets",
                new ButtonRenderer(clickEvent -> {
                    MusicalPiece piece = (MusicalPiece) clickEvent.getItem();
                    Sheets s = sc.findByPiece_XML(Sheets.class, piece.getId());
                    sheetGrid.setItems(s.getSheets());

                }));
        pieceGrid.addColumn(piece -> "edit",
                new ButtonRenderer(clickEvent -> {
                    MusicalPiece piece = (MusicalPiece) clickEvent.getItem();
                    ui.setContent(new CreatePieceView(session, ui, piece, isAdmin).getLayout());

                }));
        pieceGrid.addColumn(piece -> "remove",
                new ButtonRenderer(clickEvent -> {
                    MusicalPiece piece = (MusicalPiece) clickEvent.getItem();
                    pc.remove("" + piece.getId());
                    MusicalPieces p_ = pc.findAll_XML(MusicalPieces.class);
                    pieceGrid.setItems(p_.getPieces());
                    sheetGrid.setItems(new ArrayList<Sheet>());

                }));
        pieceGrid.setWidth("600px");
        
        gridLayout.addComponents(pieceGrid, sheetGrid);

        resultLayout.addComponents(gridLayout);

        return resultLayout;
    }

}
