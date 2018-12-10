/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cz.fit.vahalto1.orchestraapplication;

import backend.Instrument;
import backend.Instruments;
import backend.MusicalPiece;
import backend.Sheet;
import clients.InstrumentClient;
import clients.SheetClient;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author HP
 */
public class InstrumentView{
    private InstrumentClient ic;
    
    public InstrumentView(String session){  
        
        ic = new InstrumentClient(session);
    }  
    public VerticalLayout getLayout(){
        Instrument instrument = new Instrument();      
        
        VerticalLayout resultLayout = new VerticalLayout();
        
        HorizontalLayout nameLayout = new HorizontalLayout();
        Label nameLabel = new Label("Enter instrument name");        
        TextField nameField = new TextField();
        nameLayout.addComponents(nameLabel,nameField);
        
        
        Button createButton = new Button("Create");
        createButton.addClickListener((e)->{
           instrument.setName(nameField.getValue());           
           ic.create_XML(instrument);           
        });
        Button getAllButton = new Button("Get all");
        TextArea result = new TextArea();
        getAllButton.addClickListener((e)->{
            Instruments instruments = ic.findAll_XML(Instruments.class);
            for(Instrument i : instruments.getInstruments()){
                result.setValue(result.getValue() + "\n" + i.getName());                
            }
        });
        
        resultLayout.addComponents(nameLayout, createButton, getAllButton, result);
        
        return resultLayout;
    }
    
}
