/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * Jersey REST client generated for REST resource:SheetFacadeREST
 * [eu.cz.fit.vahalto1.orchestrasheetapplication.sheet]<br>
 * USAGE:
 * <pre>
 *        SheetClient client = new SheetClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author HP
 */
public class SheetClient {

    private WebTarget webTarget;
    private Client client;
    private static String BASE_URI;// = "http://localhost:8080/OrchestraApplication/webresources";
    //private static final String BASE_URI = "http://185.88.73.72:8080/Orchestra";
    private String sessionid;
    public SheetClient(String id, String baseURI) {
        /*HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin@email.com", "hesloadmin");        

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(feature) ;*/
        BASE_URI = baseURI;
        sessionid=id;
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("webresources/eu.cz.fit.vahalto1.orchestraapplication.sheet");
    }

    public String countREST() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public <T> T getPdf(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;        
        String toPath = id + "/pdf";
        resource = resource.path(toPath);  
        //resource = resource.path(java.text.MessageFormat.format("{0}/pdf", new Object[]{id}));
        Invocation.Builder ib = resource.request("application/pdf");
        return ib.cookie("JSESSIONID", sessionid).get(responseType);
    }
    public <T> T findByName_XML(Class<T> responseType, String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("byName/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
     public <T> T findByPiece_XML(Class<T> responseType, Long id) throws ClientErrorException {
        WebTarget resource = webTarget;        
        //resource = resource.path(java.text.MessageFormat.format("byPiece/{0}", new Object[]{id}));        
        String toPath = "byPiece/" + id;
        resource = resource.path(toPath);        
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
      public <T> T findByInstrument_XML(Class<T> responseType, Long id) throws ClientErrorException {
        WebTarget resource = webTarget;
        String toPath = "byInstrument/" + id;
        resource = resource.path(toPath);  
        //resource = resource.path(java.text.MessageFormat.format("byInstrument/{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
      public <T> T findByEvent_XML(Class<T> responseType, Long id) throws ClientErrorException {
        WebTarget resource = webTarget;
        String toPath = "byEvent/" + id;
        resource = resource.path(toPath);          
        //resource = resource.path(java.text.MessageFormat.format("byEvent/{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    public void edit_XML(Object requestEntity, String id) throws ClientErrorException {
        String toPath = "" + id;        
        //webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        webTarget.path(toPath).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void edit_JSON(Object requestEntity, String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        String toPath = "byEvent/" + id;
        resource = resource.path(toPath);   
        //resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void create_XML(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public <T> T findAll_XML(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void remove(String id) throws ClientErrorException {
        String toPath = "" + id;        
        //webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
        webTarget.path(toPath).request().delete();
    }

    public void close() {
        client.close();
    }
    
}
