package eu.cz.fit.vahalto1.orchestraapplication;

import backend.Events;
import backend.MusicalPiece;
import backend.MusicalPieces;
import backend.Sheet;
import clients.EventClient;
import clients.MusicalPieceClient;
import clients.SheetClient;
import javax.servlet.annotation.WebServlet;



import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.io.File;
import java.util.HashSet;
import java.util.List;
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
    private EventClient ec;
    private MusicalPieceClient mpc = new MusicalPieceClient();
    private SheetClient sc;
    
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {        
       
        //*****************************************************888/
        String session = VaadinSession.getCurrent().getSession().getId();
        ec = new EventClient();
        sc = new SheetClient(VaadinSession.getCurrent().getSession().getId());
        System.out.println(vaadinRequest.getUserPrincipal());
        System.out.println(VaadinSession.getCurrent().getSession().getId());
        if(vaadinRequest.isUserInRole("admin")) System.out.println("admin here...");
        
       
        //******************************************************/
        final VerticalLayout layout = new VerticalLayout();              
        
        Button readEvents = new Button("Read all events");
        Button readPieces = new Button("Read all pieces");
        TextArea resArea = new TextArea();
        resArea.setWidth("500");
        resArea.setHeight("200");
        readEvents.addClickListener((event) -> {
            Events events = ec.findAllEvents_XML(Events.class);
            resArea.setValue("");
            for( backend.Event e : events.getEvents()){
                resArea.setValue(resArea.getValue() + e.getDescription() + "\n");
                //MusicalPieces mp = ec.getPieces(MusicalPieces.class, e);
                for(MusicalPiece p : e.getPieces()){
                    resArea.setValue(resArea.getValue() + "\n" + "Name: " + p.getName());                               
                    
                }
                
                resArea.setValue(resArea.getValue() + "\n" + "-----------------------------------\n");
            }});
        readPieces.addClickListener((event) -> {
            MusicalPieces pieces = mpc.findAll_XML(MusicalPieces.class);
            
            resArea.setValue("");
            for( MusicalPiece p: pieces.getPieces()){
                resArea.setValue(resArea.getValue() + p.getName() + "\n");               
                
                
            }
            resArea.setValue(resArea.getValue() + "\n" + "-----------------------------------");
        });
        
        
        Label l1 = new Label("piece name");
        Label l2 = new Label("event name");
        Button createPiece = new Button("create piece");
        Button createEvent = new Button("create event");
        TextField pieceName = new TextField();
        TextField eventName = new TextField();
        
        createPiece.addClickListener((event) -> {
            /*MusicalPiece piece = new MusicalPiece();
            piece.setName(pieceName.getValue());
            mpc.create_XML(piece);*/
            setContent(new MusicalPieceView(session).getLayout());
         });
        
        createEvent.addClickListener((event) -> {
           /* backend.Event e = new backend.Event();
            e.setDescription(eventName.getValue());
            ec.create_XML(e);*/
           setContent(new EventView().getLayout());
         });
        
        Button assignPiece = new Button("assign piece to event");
        
        assignPiece.addClickListener((event) -> {
            backend.Event e = ec.find_XML(backend.Event.class,"4");
            if(e == null){System.out.println("nenalezeno"); return;};
            MusicalPieces mp = ec.getPieces(MusicalPieces.class, e);
            List<MusicalPiece> pieces = mp.getPieces();
            MusicalPiece newPiece = mpc.findByName_XML(MusicalPiece.class, pieceName.getValue());
            
            if(newPiece != null){
               pieces.add(newPiece);
               Set<MusicalPiece> mySet = new HashSet<>();
               mySet.addAll(pieces);
               
               e.setPieces(mySet);
               System.out.println("blabla");
               ec.edit_XML(e, "1");
            }
         });
        
        
        //******************************************/
        FileUploadReceiver receiver = new FileUploadReceiver();
        Upload upload = new Upload("Upload Image Here", receiver);
        upload.addSucceededListener(receiver);
        //********************************************/
        Button createSheet = new Button("create sheet");
        
            createSheet.addClickListener((event) -> {
           /* Sheet sheet = new Sheet();
            sheet.setPdfFile(receiver.getStream().toByteArray());
            sheet.setName(pieceName.getValue());
            sc.create_XML(sheet);*/
           setContent(new SheetView(session, this).getLayout());
         });
            Button viewSheet = new Button("viewSheet");
        
        viewSheet.addClickListener((event) -> {
            Window window = new Window();
            window.setWidth("90%");
            window.setHeight("90%");
            window.addCloseShortcut(KeyCode.ESCAPE);
            BrowserFrame e = new BrowserFrame("PDF File", new FileResource(sc.getPdf(File.class,"3")));
           
            
            e.setWidth("90%");
            e.setHeight("90%");
            window.setContent(e);
            window.center();
            //window.setModal(true);
            addWindow(window);
         });

        Button instBut = new Button("instruments");
        instBut.addClickListener((event)->{
            setContent(new InstrumentView(session).getLayout());
        });
        layout.addComponents(readEvents, readPieces, resArea, l1,pieceName, l2,eventName, createPiece, createEvent, assignPiece, upload, createSheet, viewSheet, instBut);
        
        
        setContent(layout);

    }

    @WebServlet(urlPatterns = {"/ui/*", "/VAADIN/*"}, name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
     

    }
}
