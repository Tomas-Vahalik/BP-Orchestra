package UI;

import backend.Instrument;
import backend.User;
import clients.EventClient;
import clients.MusicalPieceClient;
import clients.SheetClient;
import clients.UserClient;
import javax.servlet.annotation.WebServlet;



import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import java.util.Set;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
   // private EventClient ec;
   // private MusicalPieceClient mpc = new MusicalPieceClient(base);
   // private SheetClient sc;
   // private UserClient uc;
    public boolean isAdmin = false;
    public String userName;
    public String base;
    
    public MenuBar getToolbar(String session, MyUI ui){
      //TOOLBAR
        MenuBar toolbar = new MenuBar();        
        MenuBar.Command myCommand = new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                String item = selectedItem.getText();
                switch(item){
                    case "Home":
                        ui.setContent(new HomeView(session, ui, isAdmin).getLayout());
                        break;
                    case "Logout":
                        VaadinService.getCurrentRequest().getWrappedSession().invalidate();
                        String url = Page.getCurrent().getLocation().toString();
                        url=url.substring(0,url.length() - 3);
                        getUI().getPage().setLocation(url/* + "logout.jsp"*/);
                        break;
                    case "Create event":
                        ui.setContent(new CreateEventView(session, ui,null,isAdmin).getLayout());
                        break;
                    case "Pieces overview":
                        ui.setContent(new ManagePiecesView(session, ui,isAdmin).getLayout());
                        break;
                    case "Create piece":
                        ui.setContent(new CreatePieceView(session, ui,null,isAdmin).getLayout());
                        break;
                    case "Sheet overview":
                        ui.setContent(new ManageSheetsView(session, ui,isAdmin).getLayout());
                        break;
                    case "Create sheet":
                        ui.setContent(new CreateSheetView(session, ui,null,isAdmin).getLayout());
                        break;
                    case "Instruments":
                        ui.setContent(new ManageInstrumentsView(session, ui).getLayout());
                        break;
                    case "Profile":
                        ui.setContent(new ProfileView(session, ui, null).getLayout());
                        break;
                    case "Waiting for approval":
                        ui.setContent(new ManagePendingUsersView(session, ui).getLayout());
                        break;
                    case "Existing users":
                        ui.setContent(new ManageExistingUsersView(session, ui).getLayout());
                        break;
                }
            }
        };
        MenuBar.MenuItem home  = toolbar.addItem("Home", myCommand);
        if (isAdmin) {
            MenuBar.MenuItem events = toolbar.addItem("Events", null);
            events.addItem("Create event", myCommand);
            MenuBar.MenuItem pieces = toolbar.addItem("Musical pieces", null);
            pieces.addItem("Pieces overview", myCommand);
            pieces.addItem("Create piece", myCommand);
            MenuBar.MenuItem sheets = toolbar.addItem("Sheets", null);
            sheets.addItem("Sheet overview", myCommand);
            sheets.addItem("Create sheet", myCommand);
            MenuBar.MenuItem instruments = toolbar.addItem("Instruments", myCommand);
            MenuBar.MenuItem users = toolbar.addItem("Users", null);
            users.addItem("Waiting for approval", myCommand);
            users.addItem("Existing users", myCommand);
        }
        MenuBar.MenuItem Profile = toolbar.addItem("Profile", myCommand);
        MenuBar.MenuItem logout = toolbar.addItem("Logout", myCommand);
        return toolbar;
    }
    @Override    
    protected void init(VaadinRequest vaadinRequest) {        
       
        //*****************************************************888/
        String session = VaadinSession.getCurrent().getSession().getId();
        this.base = "http://" + getPage().getLocation().getHost();
        this.base += ":" + getPage().getLocation().getPort();
        this.base += VaadinServlet.getCurrent().getServletContext().getContextPath();
        this.base += "/";
        
       // ec = new EventClient(base);
       // uc = new UserClient(session, base);
       // sc = new SheetClient(VaadinSession.getCurrent().getSession().getId(), base);
        if(vaadinRequest.isUserInRole("admin")){
            isAdmin = true;
        }
       // System.out.println(vaadinRequest.getUserPrincipal());
       // System.out.println(VaadinSession.getCurrent().getSession().getId());
       // if(vaadinRequest.isUserInRole("admin")) System.out.println("admin here...");
       // User user = uc.find_XML(User.class, vaadinRequest.getUserPrincipal().toString());        
       // Set<Instrument> userInstruments = user.getInstruments();
       // for(Instrument i : userInstruments){
       //     System.out.println("------ " + i.getName() + " ----------");
       // }
        
        //System.out.println(this.base);
        userName = vaadinRequest.getUserPrincipal().toString();
        
              
        setContent(new HomeView(session, this, isAdmin).getLayout());

    }

    @WebServlet(urlPatterns = {"/ui/*", "/VAADIN/*"}, name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false, widgetset = "UI.AppWidgetSet")
    public static class MyUIServlet extends VaadinServlet {
     

    }
}
