/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cz.fit.vahalto1.orchestraapplication;

import backend.Instrument;
import backend.Sheet;
import backend.Sheets;
import clients.InstrumentClient;
import clients.SheetClient;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.File;

/**
 *
 * @author HP
 */
public class SheetView{
    private SheetClient sc;
    private InstrumentClient ic;
    private UI ui;
    public SheetView(String session, UI ui){  
        sc = new SheetClient(session);
        ic = new InstrumentClient(session);
        this.ui = ui;
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
        Label instrumentNameLabel = new Label("Enter instrument");        
        TextField instrumentField = new TextField();        
        instrumentLayout.addComponents(instrumentNameLabel, instrumentField);    
        
        FileUploadReceiver receiver = new FileUploadReceiver();
        Upload upload = new Upload("Upload pdf file", receiver);
        upload.addSucceededListener(receiver);
        
        Button createSheet = new Button("create sheet");
        
            createSheet.addClickListener((event) -> {
        
                //instrument = sc.findByName...
                //sheet.setInstrument(instrument);
            sheet.setPdfFile(receiver.getStream().toByteArray());
            sheet.setName(nameField.getValue());
            Instrument i = ic.findByName_XML(Instrument.class, instrumentField.getValue());
            if(i == null){
                 Notification.show("This instrument does not exist",
                  "",
                  Notification.Type.WARNING_MESSAGE);
                return;
            }
            sheet.setInstrument(i);
            sc.create_XML(sheet);
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
            
             Window window = new Window();
            window.setWidth("90%");
            window.setHeight("90%");
            window.addCloseShortcut(ShortcutAction.KeyCode.ESCAPE);
            BrowserFrame e = new BrowserFrame("PDF File", new FileResource(sc.getPdf(File.class,"" + id)));
           
            
            e.setWidth("90%");
            e.setHeight("90%");
            window.setContent(e);
            window.center();
            //window.setModal(true);
            ui.addWindow(window);
         });       
        
      
        
        Button createButton = new Button("Create");
        createButton.addClickListener((e)->{
          sheet.setPdfFile(receiver.getStream().toByteArray());
            sheet.setName(nameField.getValue());
            sc.create_XML(sheet);
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
