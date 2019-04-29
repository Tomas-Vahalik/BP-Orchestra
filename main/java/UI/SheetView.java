/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.Instrument;
import backend.Instruments;
import backend.Sheet;
import backend.Sheets;
import clients.InstrumentClient;
import clients.SheetClient;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author HP
 */
public class SheetView{
    private SheetClient sc;
    private InstrumentClient ic;
    private UI ui;
    public SheetView(String session, MyUI ui){  
        this.ui = ui;
        sc = new SheetClient(session, ui.base);
        ic = new InstrumentClient(session, ui.base);
        
    }  
    public VerticalLayout getLayout(){
        Sheet sheet = new Sheet();
        Instrument instrument = new Instrument();
        byte[] pdfFile;    
        
        VerticalLayout resultLayout = new VerticalLayout();
        
        HorizontalLayout nameLayout = new HorizontalLayout();
        Label nameLabel = new Label("Enter sheet name");        
        TextField nameField = new TextField();
        nameLayout.addComponents(nameLabel,nameField);
        
        HorizontalLayout instrumentLayout = new HorizontalLayout();
        
        Instruments ins = ic.findAll_XML(Instruments.class);
        List<Instrument>instruments = ins.getInstruments();
        
        ComboBox<Instrument> select = new ComboBox<>("Select instrument");
        select.setItems(instruments);
        select.setItemCaptionGenerator(Instrument::getName);
select.setNewItemProvider(inputString -> {

    Instrument newInstrument = new Instrument();
    newInstrument.setName(inputString);
    ic.create_XML(newInstrument);
    instruments.add(newInstrument);
    select.setItems(instruments);
            return Optional.of(newInstrument);

    
});
        select.addValueChangeListener(event -> {
           
            Instrument i = event.getValue();
            if(i != null){
                instrument.setName(i.getName());
                System.out.println("instrument exists");
            }
            else{
                System.out.println("instrument doesnt exist");
            }
       });
        

        
        
        Label instrumentNameLabel = new Label("Enter instrument");        
        TextField instrumentField = new TextField();        
        instrumentLayout.addComponents(/*instrumentNameLabel, instrumentField*/select);    
        
        FileUploadReceiver receiver = new FileUploadReceiver(new Label());
        Upload upload = new Upload("Upload pdf file", receiver);
        upload.addSucceededListener(receiver);
        
        Button createSheet = new Button("create sheet");
        
            createSheet.addClickListener((event) -> {
        
                //instrument = sc.findByName...
                //sheet.setInstrument(instrument);
            sheet.setPdfFile(receiver.getStream().toByteArray());
            sheet.setName(nameField.getValue());
            
            Instrument i = ic.findByName_XML(Instrument.class, select.getValue().getName());
            if(i == null){
                 Notification.show("This instrument does not exist",
                  "",
                  Notification.Type.WARNING_MESSAGE);
                return;
            }
            sheet.setInstrument(i);
            sc.create_XML(sheet);
            receiver.reset();
         });        
        
        Button viewSheet = new Button("viewSheet");
        
        viewSheet.addClickListener((event) -> {
           
            Sheet s = sc.findByName_XML(Sheet.class, nameField.getValue());
            if(s == null){
                Notification.show("This sheet does not exist",
                  "",
                  Notification.Type.WARNING_MESSAGE);
                return;
            }
            long id = s.getId();
            System.out.println("BLABLABLABLABLABLABLABLA");
            
            
            
            //NONONO//
            String url = "http://localhost:8080/OrchestraApplication/webresources/eu.cz.fit.vahalto1.orchestraapplication.sheet/" + id + "/pdf";
            ui.getPage().open(url, "Sheet", false);
         /*
            Window window = new Window();
            window.setWidth("90%");
            window.setHeight("90%");
            window.addCloseShortcut(ShortcutAction.KeyCode.ESCAPE);          
            
            
            File file = sc.getPdf(File.class,"" + id);
            System.out.println(file.getName());
            try {
                FileInputStream is = new FileInputStream(file);
                
                
                
            } catch (Exception ex) {
                Logger.getLogger(SheetView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           BrowserFrame e = new BrowserFrame("PDF File", new FileResource(sc.getPdf(File.class,"" + id)));           
          
            
            e.setWidth("90%");
            e.setHeight("90%");
            window.setContent(e);
            window.center();
            //window.setModal(true);*/
          //  ui.addWindow(window);
         });       
        
      
        
       
        
        Button getAllButton = new Button("Get all");
        TextArea result = new TextArea();
        getAllButton.addClickListener((e)->{
            Sheets sheets = sc.findAll_XML(Sheets.class);
            for (Sheet s : sheets.getSheets()) {
               result.setValue(result.getValue() + "\n" + s.getName());                
            }          
        });
        
        resultLayout.addComponents(nameLayout, instrumentLayout, upload, createSheet, viewSheet, getAllButton, result);
        
        return resultLayout;
    }
    
    
    
    
    
    
}
