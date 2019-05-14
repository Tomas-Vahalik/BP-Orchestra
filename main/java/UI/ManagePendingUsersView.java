/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import backend.UnverifiedUser;
import backend.UnverifiedPeople;
import backend.User;
import clients.UnverifiedUserClient;
import clients.UserClient;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import java.util.List;

/**
 *
 * @author Tomáš Vahalík
 */
public class ManagePendingUsersView {

    private UserClient uc;
    private UnverifiedUserClient uuc;
    private String session;
    private MyUI ui;

    public ManagePendingUsersView(String session, MyUI ui) {
        uc = new UserClient(session, ui.base);
        uuc = new UnverifiedUserClient(ui.base);
        this.session = session;
        this.ui = ui;

    }

    public VerticalLayout getLayout() {
        VerticalLayout resultLayout = new VerticalLayout();

        resultLayout.addComponent(ui.getToolbar(session, ui));

        //GRID WITH USERS
        UnverifiedPeople allUnverifiedUsers = uuc.findAll_XML(UnverifiedPeople.class);
        List<UnverifiedUser> unverifiedUsers = allUnverifiedUsers.getUsers();
        Grid<UnverifiedUser> userGrid = new Grid<>();
        userGrid.setWidth("800px");
        userGrid.setItems(unverifiedUsers);
        userGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        userGrid.addColumn(UnverifiedUser::getName).setCaption("Name");
        userGrid.addColumn(UnverifiedUser::getLogin).setCaption("User name");
        userGrid.addColumn(user -> "Approve",
                new ButtonRenderer(clickEvent -> {
                    UnverifiedUser user = (UnverifiedUser) clickEvent.getItem();

                    User newUser = new User();
                    newUser.setLogin(user.getLogin());
                    newUser.setName(user.getName());
                    newUser.setPassword(user.getPassword());
                    newUser.setInstruments(user.getInstruments());

                    uc.create_XML(newUser);
                    uuc.remove(user.getLogin());
                    unverifiedUsers.remove(user);
                    userGrid.setItems(unverifiedUsers);

                }));
        userGrid.addColumn(user -> "Reject",
                new ButtonRenderer(clickEvent -> {
                    UnverifiedUser user = (UnverifiedUser) clickEvent.getItem();

                    uuc.remove(user.getLogin());
                    unverifiedUsers.remove(user);
                    userGrid.setItems(unverifiedUsers);

                }));

        resultLayout.addComponents(userGrid);

        return resultLayout;
    }
}
