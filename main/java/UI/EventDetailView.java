/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.Instrument;
import backend.Instruments;
import backend.MusicalPiece;
import backend.Sheet;
import backend.Sheets;
import backend.User;
import clients.EventClient;
import clients.InstrumentClient;
import clients.MusicalPieceClient;
import clients.SheetClient;
import clients.UserClient;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author HP
 */
public class EventDetailView {
    private backend.Event event;
    private MyUI ui;
    private String session;
    private MusicalPieceClient pc;
    private InstrumentClient ic;
    private UserClient uc;
    private EventClient ec;
    private SheetClient sc;
    private boolean isAdmin;
    
    public EventDetailView(backend.Event e,String session, MyUI ui, boolean isAdmin){
        this.event = e;
        this.session = session;
        this.ui = ui;
        this.isAdmin = isAdmin;
        pc = new MusicalPieceClient(ui.base);
        ic = new InstrumentClient(session,ui.base);
        ec = new EventClient(ui.base);
        sc = new SheetClient(session,ui.base);
        uc = new UserClient(session,ui.base);
    }
    public VerticalLayout getLayout(){
        VerticalLayout resultLayout = new VerticalLayout();
      
        resultLayout.addComponent(ui.getToolbar(session, ui));
        
        //GRID LAYOUT
        HorizontalLayout gridLayout = new HorizontalLayout();
        
        //GRID WITH PIECES
        VerticalLayout pieceGridLayout = new VerticalLayout();
        Label piecesLabel = new Label("Pieces played on this event:");
        Label dummy = new Label("");
         Grid<MusicalPiece> pieceGrid = new Grid<>();
        pieceGrid.setSelectionMode(Grid.SelectionMode.NONE);
                
        pieceGrid.setItems(event.getPieces());
        pieceGrid.removeAllColumns();
        pieceGrid.addColumn(MusicalPiece::getName).setCaption("Name");          
        pieceGridLayout.addComponents(piecesLabel,dummy, pieceGrid);
         //Grid for sheet
        VerticalLayout sheetGridLayout = new VerticalLayout();
        HorizontalLayout veryNameLayout = new HorizontalLayout();
        
        Label sheetLabel = new Label("Sheets for this event:");
        
        Grid<Sheet> sheetGrid = new Grid<>();
        sheetGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        Sheets sheets = sc.findByEvent_XML(Sheets.class, event.getId());
        List<Sheet> eventSheets = sheets.getSheets();
        //sheetGrid.setItems(eventSheets);
        sheetGrid.addColumn(Sheet::getName).setCaption("Name");          
        sheetGrid.addColumn(sheet -> "View",
                    new ButtonRenderer(clickEvent -> {
                        Sheet sheet = (Sheet) clickEvent.getItem();
                        ui.getPage().open("../webresources/eu.cz.fit.vahalto1.orchestraapplication.sheet/" + sheet.getId() + "/pdf", "Sheet", false);        

             }));
        
        
         //Combo box for instruments
        List<String> instrumentNames = new ArrayList<String>();
        
        if(isAdmin){
            Instruments instruments = ic.findAll_XML(Instruments.class);
            for (Instrument i : instruments.getInstruments()) {
                instrumentNames.add(i.getName());
            }
            instrumentNames.add("All");
            sheetGrid.setItems(eventSheets);
        }
        else{
            User user = uc.find_XML(User.class,ui.userName);
            for(Instrument i : user.getInstruments()){
                instrumentNames.add(i.getName());
            }
            List<Sheet> availableSheet = new ArrayList();
            for(Sheet s : eventSheets){
                if(user.getInstruments().contains(s.getInstrument())){
                    availableSheet.add(s);
                }
            }
            sheetGrid.setItems(availableSheet);            
            
        }           
        
        ComboBox<String> selectInstrument = new ComboBox<>("Choose instrument");
        selectInstrument.setItems(instrumentNames);
        selectInstrument.addValueChangeListener((e)->{          
           if("All".equals(selectInstrument.getValue()) && isAdmin){
               //Sheets s = sc.findAll_XML(Sheets.class);
               sheetGrid.setItems(eventSheets);
               return;
           }           
           Instrument instrument = ic.findByName_XML(Instrument.class, selectInstrument.getValue());           
           List<Sheet> availableSheet = new ArrayList();
           for(Sheet s : eventSheets){
               if(s.getInstrument().equals(instrument)){
                   availableSheet.add(s);
               }
           }
           sheetGrid.setItems(availableSheet);
        });
        veryNameLayout.addComponents(sheetLabel, selectInstrument);     
        sheetGridLayout.addComponents(veryNameLayout,sheetGrid);
        
        gridLayout.addComponents(pieceGridLayout, sheetGridLayout);      
        
        Label l = new Label(event.getName());
        TextArea descriptionArea = new TextArea();
        if(event.getDescription() != null) descriptionArea.setValue(event.getDescription());
        descriptionArea.setWidth("500px");
        descriptionArea.setEnabled(false);
        resultLayout.addComponents(l,descriptionArea,gridLayout);
        
        return resultLayout;
    }
}
