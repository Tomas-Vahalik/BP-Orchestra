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
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import java.util.List;

/**
 *
 * @author Tomáš Vahalík
 */
public class ManageSheetsView {

    private MusicalPieceClient pc;
    private SheetClient sc;
    private String session;
    private MyUI ui;
    private boolean isAdmin;
    private MusicalPiece selectedPiece = null;

    public ManageSheetsView(String session, MyUI ui, boolean isAdmin) {
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
        Sheets sh = sc.findAll_XML(Sheets.class);
        List<Sheet> allSheets = sh.getSheets();
        Grid<Sheet> sheetGrid = new Grid<>();
        sheetGrid.setItems(allSheets);
        sheetGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        sheetGrid.addColumn(Sheet::getName).setCaption("Name");
        sheetGrid.addColumn(sheet -> "View",
                new ButtonRenderer(clickEvent -> {
                    Sheet sheet = (Sheet) clickEvent.getItem();
                    ui.getPage().open("../webresources/eu.cz.fit.vahalto1.orchestraapplication.sheet/" + sheet.getId() + "/pdf", "Sheet", false);

                }));
        sheetGrid.addColumn(sheet -> "Edit",
                new ButtonRenderer(clickEvent -> {
                    Sheet sheet = (Sheet) clickEvent.getItem();
                    ui.setContent(new CreateSheetView(session, ui, sheet, isAdmin).getLayout());

                }));
        sheetGrid.addColumn(sheet -> "Remove",
                new ButtonRenderer(clickEvent -> {
                    Sheet sheet = (Sheet) clickEvent.getItem();
                    sc.remove("" + sheet.getId());
                    Sheets sheets = sc.findAll_XML(Sheets.class);
                    sheetGrid.setItems(sheets.getSheets());

                }));
        sheetGrid.setWidth("600px");

        resultLayout.addComponents(sheetGrid);

        return resultLayout;
    }
}
