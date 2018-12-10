/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import backend.Sheet;
import static backend.Sheet_.name;
import backend.Sheets;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Stateless
@Path("eu.cz.fit.vahalto1.orchestraapplication.sheet")
public class SheetFacadeREST extends AbstractFacade<Sheet> {

    @PersistenceContext(unitName = "OrchestraAppPersistence")
    private EntityManager em;

    public SheetFacadeREST() {
        super(Sheet.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Sheet entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Sheet entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Sheet find(@PathParam("id") Long id) {        
        return super.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Sheets findAllSheets() {
        Sheets s = new Sheets();
        s.setSheets(super.findAll());
        return s;
    }

   @GET
   
@Path("/{id}/pdf")
@RolesAllowed("admin")
@Produces("application/pdf")
public Response getPdf(@PathParam("id") Long id, @Context HttpHeaders hh) throws Exception
{
    MultivaluedMap<String, String> headerParams = hh.getRequestHeaders();
    for (Map.Entry<String, List<String>> entry : headerParams.entrySet()) {
        String key = entry.getKey();
        List<String> value = entry.getValue();
        System.out.println(key + ":"+value);
        
    }
        //String nameParam = queryParams.getFirst("user");
       
    Sheet sheet = find(id);
    ByteArrayInputStream is = new ByteArrayInputStream(sheet.getPdfFile());
    Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.ok((Object) is);
    responseBuilder.type("application/pdf");
    responseBuilder.header("Content-Disposition", "filename=test.pdf");
    return responseBuilder.build();
}
    
@GET
    @Path("/byName/{name}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Sheet findByName(@PathParam("name") String name) {
       List<Sheet> result = em.createQuery(
        "SELECT s FROM Sheet s WHERE s.name = :sheetName")
        .setParameter("sheetName", name)
        .getResultList();
       if(!result.isEmpty()) return result.get(0);
       else return null;
    }
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Sheet> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
