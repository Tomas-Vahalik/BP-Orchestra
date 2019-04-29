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
import clients.InstrumentClient;
import clients.SheetClient;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author HP
 */
public class ManageInstrumentsView{
    private InstrumentClient ic;
    private SheetClient sc;
    private MyUI ui;
    //private Instrument selectedInstrument;
    private String session;
    public ManageInstrumentsView(String session, MyUI ui){  
        this.ui = ui;
        this.session = session;
        ic = new InstrumentClient(session, ui.base);
        sc = new SheetClient(session, ui.base);
    }  
    public VerticalLayout getLayout(){
               
        VerticalLayout resultLayout = new VerticalLayout();
        
        resultLayout.addComponent(ui.getToolbar(session, ui));
        
        HorizontalLayout gridLayout = new HorizontalLayout();
        //Instrument grid
        Instruments instruments = ic.findAll_XML(Instruments.class);      
        Grid<Instrument> instrumentGrid = new Grid<>();
        instrumentGrid.setSelectionMode(Grid.SelectionMode.SINGLE);        
        instrumentGrid.setItems(instruments.getInstruments());
        instrumentGrid.addColumn(Instrument::getName).setCaption("Instrument");          
        //Sheet grid        
        Grid<Sheet> sheetGrid = new Grid<>();
        sheetGrid.setSelectionMode(Grid.SelectionMode.SINGLE);                
        sheetGrid.addColumn(Sheet::getName).setCaption("Sheet");       
                sheetGrid.addColumn(sheet -> "View",
                    new ButtonRenderer(clickEvent -> {
                        Sheet sheet = (Sheet) clickEvent.getItem();
                        ui.getPage().open("../webresources/eu.cz.fit.vahalto1.orchestraapplication.sheet/" + sheet.getId() + "/pdf", "Sheet", false);        

             }));
        
         instrumentGrid.addSelectionListener(event -> {
        if(event.getFirstSelectedItem().isPresent() == false)return;
        Instrument selectedInstrument = event.getFirstSelectedItem().get();             
            Sheets s = sc.findByInstrument_XML(Sheets.class, selectedInstrument.getId());
            sheetGrid.setItems(s.getSheets());
    
        });
        gridLayout.addComponents(instrumentGrid, sheetGrid);
        
        HorizontalLayout nameLayout = new HorizontalLayout();
        TextField nameField = new TextField();       
                
        Button createButton = new Button("Add new instrument");
        createButton.addClickListener((e)->{
            
            Instrument i = ic.findByName_XML(Instrument.class, nameField.getValue());            
            if(i != null){
                Notification.show("This instrument alredy exists", Notification.Type.ERROR_MESSAGE);
                return;
            }
            i = new Instrument();
           i.setName(nameField.getValue());            
           ic.create_XML(i); 
           Instruments instr = ic.findAll_XML(Instruments.class);      
           instrumentGrid.setItems(instr.getInstruments());
           
        });        
        
        nameLayout.addComponents(nameField, createButton);
        
        resultLayout.addComponents(nameLayout, gridLayout);
        
        return resultLayout;
    }
    
}
