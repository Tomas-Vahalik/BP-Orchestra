/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.Instrument;
import backend.Instruments;
import backend.User;
import backend.Users;
import clients.InstrumentClient;
import clients.UserClient;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

/**
 *
 * @author HP
 */
public class ManageExistingUsersView {
    
    private InstrumentClient ic;
    private UserClient uc;
    private MyUI ui;    
    private String session;
    public ManageExistingUsersView(String session, MyUI ui){  
        this.ui = ui;
        this.session = session;
        ic = new InstrumentClient(session, ui.base);
        uc = new UserClient(session, ui.base);
    }  
    public VerticalLayout getLayout(){
               
        Users users = uc.findAll_XML(Users.class);
        VerticalLayout resultLayout = new VerticalLayout();
        
        resultLayout.addComponent(ui.getToolbar(session, ui));
        
        HorizontalLayout nameLayout = new HorizontalLayout();  
        
                
        Grid<User> userGrid = new Grid<>();
        userGrid.setSelectionMode(Grid.SelectionMode.NONE);        
        
        userGrid.setItems(uc.findAll_XML(Users.class).getUsers());        
        userGrid.addColumn(User::getName).setCaption("Users:");
        userGrid.addColumn(sheet -> "Detail",
                    new ButtonRenderer(clickEvent -> {
                     User user = (User) clickEvent.getItem();   
                     ui.setContent(new ProfileView(session, ui, user).getLayout());   

        }));
        userGrid.addColumn(sheet -> "Remove",
                    new ButtonRenderer(clickEvent -> {
                     User user = (User) clickEvent.getItem();   
                     uc.remove(user.getLogin());
                     userGrid.setItems(uc.findAll_XML(Users.class).getUsers());        

        }));
        
        resultLayout.addComponents(userGrid);
        
        return resultLayout;
    }
    
}

    

