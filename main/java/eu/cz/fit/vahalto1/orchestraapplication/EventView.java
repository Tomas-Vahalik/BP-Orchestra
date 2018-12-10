/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cz.fit.vahalto1.orchestraapplication;

import backend.Events;
import backend.MusicalPiece;
import backend.Sheet;
import backend.Sheets;
import clients.EventClient;
import clients.MusicalPieceClient;
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
public class EventView{
    private EventClient ec = new EventClient();
    private MusicalPieceClient pc = new MusicalPieceClient();
    public EventView(){  
    
    }  
    public VerticalLayout getLayout(){
        backend.Event event = new backend.Event();
        Set<MusicalPiece> pieces = new HashSet<MusicalPiece>();
        
        VerticalLayout resultLayout = new VerticalLayout();
        
        HorizontalLayout descriptionLayout = new HorizontalLayout();
        Label descriptionLabel = new Label("Enter event description");        
        TextField descriptionField = new TextField();
        descriptionLayout.addComponents(descriptionLabel,descriptionField);
        
        HorizontalLayout piecesLayout = new HorizontalLayout();
        Label piecesLabel = new Label("Enter piece name");        
        TextField piecesField = new TextField();
        TextArea piecesArea = new TextArea();
        Button addPieceButton = new Button("add piece to event");
        
        addPieceButton.addClickListener((e)->{
            MusicalPiece piece = pc.findByName_XML(MusicalPiece.class, piecesField.getValue());
            if(piece == null){
                Notification.show("This piece does not exist",
                  "",
                  Notification.Type.WARNING_MESSAGE);
                return;
            }
            pieces.add(piece);
            piecesArea.setValue(piecesArea.getValue() + "\n" + piece.getName());
            
        });
        piecesLayout.addComponents(piecesLabel, piecesField, addPieceButton);    
        
      
        
        Button createButton = new Button("Create");
        createButton.addClickListener((e)->{
           backend.Event ev = ec.findByName_XML(backend.Event.class, descriptionField.getValue());
            if(ev == null){
                event.setDescription(descriptionField.getValue());
                event.setPieces(pieces);
                ec.create_XML(event);           
           }
            else{
                Set<MusicalPiece> p = ev.getPieces();
                p.addAll(pieces);
                 ev.setPieces(p);
                ec.edit_XML(ev, "" + ev.getId());
            }
        });
        
        Button getAllButton = new Button("Get all");
        TextArea result = new TextArea();
        getAllButton.addClickListener((e)->{
            Events events = ec.findAll_XML(Events.class);
            for(backend.Event ev : events.getEvents()){
                result.setValue(result.getValue() + "\n" + ev.getDescription());                
                result.setValue(result.getValue() + "\n---Pieces:");                
                for(MusicalPiece p : ev.getPieces()){
                    result.setValue(result.getValue() + "\n" + p.getName());
                    Sheets pieceSheets = pc.getSheets_XML(Sheets.class, "" + p.getId());
                    result.setValue(result.getValue() + "\n---Sheet:");                                    
                    for(Sheet s : pieceSheets.getSheets()){
                        result.setValue(result.getValue() + "\n" + s.getInstrument().getName());
                    }
                }
            }
        });
        resultLayout.addComponents(descriptionLayout, piecesLayout, piecesArea, createButton, getAllButton, result);
        
        return resultLayout;
    }
    
}
