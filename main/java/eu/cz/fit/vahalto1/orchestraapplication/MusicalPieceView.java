/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cz.fit.vahalto1.orchestraapplication;

import backend.MusicalPiece;
import backend.MusicalPieces;
import backend.Sheet;
import backend.Sheets;
import clients.MusicalPieceClient;
import clients.SheetClient;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author HP
 */
public class MusicalPieceView{
    private SheetClient sc;
    private MusicalPieceClient pc = new MusicalPieceClient();    
    public MusicalPieceView(String session){  
        
        sc = new SheetClient(session);
    }  
    public VerticalLayout getLayout(){
        MusicalPiece musicalPiece = new MusicalPiece();
        Set<Sheet> sheets = new HashSet<Sheet>();
        
        VerticalLayout resultLayout = new VerticalLayout();
        
        HorizontalLayout nameLayout = new HorizontalLayout();
        Label nameLabel = new Label("Enter piece name");        
        TextField nameField = new TextField();
        nameLayout.addComponents(nameLabel,nameField);
        
        HorizontalLayout sheetLayout = new HorizontalLayout();
        Label sheetNameLabel = new Label("Enter sheet name");        
        TextField sheetNameField = new TextField();
        Button addSheetButton = new Button("Add sheet");
        addSheetButton.addClickListener((e)->{
           MusicalPiece p = pc.findByName_XML(MusicalPiece.class, nameField.getValue());
           if(p==null){
                Notification.show("This piece does not exist",
                  "",
                  Notification.Type.WARNING_MESSAGE);
                return;
           }
           Sheet s = sc.findByName_XML(Sheet.class, sheetNameField.getValue());
           if(s==null){
                Notification.show("This sheet does not exist",
                  "",
                  Notification.Type.WARNING_MESSAGE);
                return;
           }
           s.setPiece(p);           
           sc.edit_XML(s, "" + s.getId());           
         
        });
        sheetLayout.addComponents(sheetNameLabel,sheetNameField, addSheetButton);
        
        
        Button createButton = new Button("Create");
        createButton.addClickListener((e)->{
           musicalPiece.setName(nameField.getValue());           
           pc.create_XML(musicalPiece);           
        });
        Button getAllButton = new Button("Get all");
        TextArea result = new TextArea();
        getAllButton.addClickListener((e)->{
            MusicalPieces pieces = pc.findAll_XML(MusicalPieces.class);
            for(MusicalPiece p : pieces.getPieces()){
                result.setValue(result.getValue() + "\n" + p.getName());                
                //System.out.println("bla");
                
                Sheets pieceSheets = pc.getSheets_XML(Sheets.class, "" + p.getId());
                
                if(pieceSheets!= null){
                    for(Sheet s : pieceSheets.getSheets()){
                        result.setValue(result.getValue() + "\n" + "instruments:");                
                            if(s != null){
                            result.setValue(result.getValue() + "\n" + s.getInstrument().getName());                
                        }
                    }
                }
            }
        });
        
        resultLayout.addComponents(nameLayout,sheetLayout, createButton, getAllButton, result);
        
        return resultLayout;
    }
    
}
