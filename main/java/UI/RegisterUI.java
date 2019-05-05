/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.Instrument;
import backend.Instruments;
import backend.UnverifiedUser;
import clients.InstrumentClient;
import clients.UnverifiedUserClient;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author HP
 */
public class RegisterUI extends UI {
    @Override    
    protected void init(VaadinRequest vaadinRequest) {      
        String base = "http://" + getPage().getLocation().getHost();
        base += ":" + getPage().getLocation().getPort();
        base += VaadinServlet.getCurrent().getServletContext().getContextPath();
        base += "/";
        UnverifiedUserClient uc = new UnverifiedUserClient(base);
        InstrumentClient ic = new InstrumentClient("", base);
        //*****************************************************888/
        VerticalLayout resultLayout = new VerticalLayout();
        
        //NAME
        HorizontalLayout nameLayout = new HorizontalLayout();        
        Label nameLabel = new Label("Enter your name");
        TextField nameField = new TextField();
        nameLayout.addComponents(nameLabel, nameField);
        
        //USERNAME
        HorizontalLayout userNameLayout = new HorizontalLayout();        
        Label userNameLabel = new Label("Pick a login name");
        TextField userNameField = new TextField();
        userNameLayout.addComponents(userNameLabel, userNameField);
        
        //PASSWORD
        HorizontalLayout passwordLayout = new HorizontalLayout();        
        Label passwordLabel = new Label("Enter password");
        PasswordField passwordField = new PasswordField();        
        passwordLayout.addComponents(passwordLabel, passwordField);
        
        //INSTRUMENTS
        Instruments instruments = ic.findAll_XML(Instruments.class);      
        Grid<Instrument> instrumentGrid = new Grid<>();
        instrumentGrid.setSelectionMode(Grid.SelectionMode.MULTI);        
        instrumentGrid.setItems(instruments.getInstruments());
        instrumentGrid.addColumn(Instrument::getName).setCaption("Choose instruments that you play");          
           
        //REGISTER
        // Content for the PopupView
VerticalLayout popupContent = new VerticalLayout();
popupContent.addComponent(new Label("Registation successfull. When administrator accepts your request, you will be able to login."));


PopupView popup = new PopupView(null, popupContent);

        
        Button registerButton = new Button("Register");
        registerButton.addClickListener((e)->{
            UnverifiedUser user = new UnverifiedUser();
            user.setLogin(userNameField.getValue());
            user.setName(nameField.getValue());
            user.setPassword(passwordField.getValue());
            user.setInstruments(instrumentGrid.getSelectedItems());
            
            uc.create_XML(user);
             popup.setPopupVisible(true);
            
        });
        
        Resource redirectResource = 
                new ExternalResource(".");
        
        Link link = new Link("Login", redirectResource);        
        
        resultLayout.addComponents(nameLayout, userNameLayout, passwordLayout, instrumentGrid, registerButton, link, popup);
        setContent(resultLayout);

    }

    @WebServlet(urlPatterns = {"/register/*"}, name = "RegisterServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = RegisterUI.class, productionMode = false, widgetset = "UI.AppWidgetSet")
    public static class MyUIServlet extends VaadinServlet {
     

    }
}


