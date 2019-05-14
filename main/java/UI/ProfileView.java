/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.BooleanWrapper;
import backend.Instrument;
import backend.Instruments;
import backend.User;
import clients.InstrumentClient;
import clients.UserClient;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tomáš Vahalík
 */
public class ProfileView {

    private InstrumentClient ic;
    private UserClient uc;
    private MyUI ui;
    private String session;
    private User user;
    private List<Instrument> userInstruments = new ArrayList();

    public ProfileView(String session, MyUI ui, User u) {
        this.ui = ui;
        this.session = session;
        ic = new InstrumentClient(session, ui.base);
        uc = new UserClient(session, ui.base);
        user = u;
    }

    public VerticalLayout getLayout() {

        if (user == null) {
            user = uc.find_XML(User.class, ui.userName);
        }
        VerticalLayout resultLayout = new VerticalLayout();

        resultLayout.addComponent(ui.getToolbar(session, ui));

        HorizontalLayout nameLayout = new HorizontalLayout();

        Label nameLabel = new Label("Name: ");
        TextField nameField = new TextField();
        nameField.setEnabled(false);
        nameField.setValue(user.getName());

        nameLayout.addComponents(nameLabel, nameField);

        HorizontalLayout loginLayout = new HorizontalLayout();

        Label loginLabel = new Label("Login: ");
        TextField loginField = new TextField();
        loginField.setEnabled(false);
        loginField.setValue(user.getLogin());

        loginLayout.addComponents(loginLabel, loginField);

        PasswordField oldPasswordField = new PasswordField("Old password");
        PasswordField newPasswordField1 = new PasswordField("New password");
        PasswordField newPasswordField2 = new PasswordField("New password");

        Button confirmPasswordButton = new Button("Confirm new password");
        confirmPasswordButton.addClickListener((e) -> {
            if (!(user.getPassword().equals(oldPasswordField.getValue()))) {
                Notification.show("Wrong old password", Notification.Type.ERROR_MESSAGE);
                return;
            }
            if (!(newPasswordField1.getValue().equals(newPasswordField2.getValue()))) {
                Notification.show("New passwords do not match", Notification.Type.ERROR_MESSAGE);
                return;
            }
            user.setPassword(newPasswordField1.getValue());
            uc.edit_XML(user, user.getLogin());
        });

        
        VerticalLayout passwordLayout = new VerticalLayout();
        passwordLayout.addComponents(oldPasswordField, newPasswordField1, newPasswordField2, confirmPasswordButton);
        passwordLayout.setVisible(false);

        Button changePasswordButton = new Button("Change password");
        changePasswordButton.addClickListener((e) -> {
            passwordLayout.setVisible(true);
        });
        if (!(ui.userName.equals(user.getLogin()))) {        
            changePasswordButton.setVisible(false);
        }
        HorizontalLayout addingLayout = new HorizontalLayout();

        List<String> instrumentNames = new ArrayList<String>();
        Instruments instruments = ic.findAll_XML(Instruments.class);
        for (Instrument i : instruments.getInstruments()) {
            instrumentNames.add(i.getName());
        }
        ComboBox<String> selectInstrument = new ComboBox<>("Choose instrument");
        selectInstrument.setItems(instrumentNames);

        Grid<Instrument> instrumentGrid = new Grid<>();
        if (ui.isAdmin) {
            instrumentGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        } else {
            instrumentGrid.setSelectionMode(Grid.SelectionMode.NONE);
        }

        BooleanWrapper bw = uc.isAdmin(BooleanWrapper.class, user.getLogin());

        if (bw.getValue()) {
            userInstruments = ic.findAll_XML(Instruments.class).getInstruments();
        } else {
            userInstruments.addAll(user.getInstruments());
        }
        instrumentGrid.setItems(userInstruments);
        instrumentGrid.addColumn(Instrument::getName).setCaption("My instruments:");

        Button addInstrument = new Button("Add instrument to the user");
        addInstrument.addClickListener((e) -> {
            Instrument i = ic.findByName_XML(Instrument.class, selectInstrument.getValue());
            if (i == null) {
                Notification.show("This instrument", Notification.Type.ERROR_MESSAGE);
                return;
            }
            userInstruments.add(i);
            instrumentGrid.setItems(userInstruments);

        });

        Button removeSelected = new Button("Remove selected");
        removeSelected.addClickListener((e) -> {
            Set<Instrument> selected = instrumentGrid.getSelectedItems();
            userInstruments.removeAll(selected);
            instrumentGrid.setItems(userInstruments);

        });
        Button saveButton = new Button("Save");
        saveButton.addClickListener((e) -> {
            Set<Instrument> insSet = new HashSet();
            insSet.addAll(userInstruments);
            user.setInstruments(insSet);
            uc.edit_XML(user, user.getLogin());

        });
        Button giveAdmin = new Button("Set this user admin");
        giveAdmin.addClickListener((e) -> {
            uc.grantAdmin(user.getLogin());

        });
        addingLayout.addComponents(selectInstrument, addInstrument);
        resultLayout.addComponents(nameLayout, loginLayout, passwordLayout, changePasswordButton);
        if (ui.isAdmin) {
            resultLayout.addComponent(addingLayout);
        }
        resultLayout.addComponents(instrumentGrid);
        if (ui.isAdmin) {
            resultLayout.addComponents(removeSelected, giveAdmin, saveButton);
        }

        return resultLayout;
    }

}
