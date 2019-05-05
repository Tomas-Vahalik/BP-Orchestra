/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.Instrument;
import backend.Instruments;
import backend.MusicalPiece;
import backend.MusicalPieces;
import backend.PdfWrapper;
import backend.Sheet;
import clients.InstrumentClient;
import clients.MusicalPieceClient;
import clients.SheetClient;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class CreateSheetView {
    private SheetClient sc;
    private InstrumentClient ic;
    private MusicalPieceClient pc;
    private MyUI ui;
    private String session;
    private boolean isAdmin;
    private Sheet sheet;
    private byte[] pdfArray;
    
    public CreateSheetView(String session, MyUI ui, Sheet sheet,boolean isAdmin) {
        this.session = session;
        sc = new SheetClient(session, ui.base);        
        pc = new MusicalPieceClient(ui.base);
        ic = new InstrumentClient(session,ui.base);
        this.ui = ui;
        this.sheet = sheet;
        this.isAdmin = isAdmin;
    }
     public VerticalLayout getLayout(){
        VerticalLayout resultLayout = new VerticalLayout();
        
      resultLayout.addComponent(ui.getToolbar(session, ui));
      //Name layout
      HorizontalLayout nameLayout = new HorizontalLayout();
      Label nameLabel = new Label("Sheet name:");
      TextField nameField = new TextField();
      if(sheet != null){
          nameField.setValue(sheet.getName());
      }
      nameLayout.addComponents(nameLabel, nameField);
      //Piece layout
      
      //pieces box
      MusicalPieces mp = pc.findAll_XML(MusicalPieces.class);
      List<MusicalPiece> allPieces = mp.getPieces();
      List<String> pieceNames = new ArrayList();
      for(MusicalPiece p : allPieces){
          pieceNames.add(p.getName());
      }
      ComboBox<String> pieceBox = new ComboBox("Select piece for this sheet");
      pieceBox.setItems(pieceNames);
      if(sheet != null && sheet.getPiece() != null){
          pieceBox.setSelectedItem(sheet.getPiece().getName());
      }
      //instrumnet box      
        Instruments instruments = ic.findAll_XML(Instruments.class);
        List<String> instrumentNames = new ArrayList<String>();
        for (Instrument i : instruments.getInstruments()) {
            instrumentNames.add(i.getName());
        }
        
        ComboBox<String> instrumentBox = new ComboBox<>("Choose instrument");
        instrumentBox.setItems(instrumentNames);
        instrumentBox.addValueChangeListener((e)->{
            
         //  Notification.show(instrumentBox.getValue(),
          //        Notification.Type.HUMANIZED_MESSAGE); 
        });
        if(sheet != null){
          instrumentBox.setSelectedItem(sheet.getInstrument().getName());
        }
        //Upload layout
        HorizontalLayout uploadLayout = new HorizontalLayout();
        //Upload for pdf file
        Label uploadLabel = new Label("");
        FileUploadReceiver receiver = new FileUploadReceiver(uploadLabel);
        Upload upload = new Upload("Upload pdf file", receiver);
        upload.addSucceededListener(receiver);
        
        uploadLayout.addComponents(upload, uploadLabel);
                
        Button saveSheet = new Button("Save this sheet");
        
            saveSheet.addClickListener((event) -> {
                boolean newSheet = false;                
                if(sheet == null){
                    sheet = new Sheet();
                    newSheet = true;
                }
                
            sheet.setName(nameField.getValue());
            pdfArray = receiver.getStream().toByteArray();
            PdfWrapper wrapper = new PdfWrapper();
            if(pdfArray.length != 0){
              //  sheet.setPdfFile(pdfArray);
              wrapper.setPdfFile(pdfArray);
            }            
            
            Instrument i = ic.findByName_XML(Instrument.class, instrumentBox.getValue());
            if(i == null){
                 Notification.show("This instrument does not exist",
                  "",
                  Notification.Type.ERROR_MESSAGE);
                return;
            }
            sheet.setInstrument(i);
            
            MusicalPiece piece = pc.findByName_XML(MusicalPiece.class, pieceBox.getValue());
            if(piece == null){
                 Notification.show("This piece does not exist",
                  "",
                  Notification.Type.ERROR_MESSAGE);
                return;
            }
            sheet.setPiece(piece);
            if(newSheet == true){
                sc.create_XML(sheet);
                Sheet s = sc.findByName_XML(Sheet.class, sheet.getName());
                sc.setPdf("" + s.getId(), wrapper);                
            }
            else{
                sc.edit_XML(sheet, "" + sheet.getId());
                Sheet s = sc.findByName_XML(Sheet.class, sheet.getName());
                sc.setPdf("" + s.getId(), wrapper);       
            }
            receiver.reset();
         });        
        
            Button newSheet = new Button("Add next sheet");
            newSheet.addClickListener((event) -> {
                this.sheet = null;
                nameField.setValue("");
                pieceBox.setValue("");
                instrumentBox.setValue("");
                receiver.reset();
            });
            
      resultLayout.addComponents(nameLayout, pieceBox, instrumentBox, uploadLayout, saveSheet, newSheet);
      return resultLayout;
     }
}
