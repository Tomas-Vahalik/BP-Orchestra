/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import backend.Event;
import backend.Instrument;
import backend.MusicalPiece;
import backend.MusicalPieces;
import backend.Sheet;
import backend.Sheets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
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
import javax.ws.rs.core.MediaType;

/**
 *
 * @author HP
 */
@Stateless
@Path("eu.cz.fit.vahalto1.orchestraapplication.musicalpiece")
public class MusicalPieceFacadeREST extends AbstractFacade<MusicalPiece> {

    @PersistenceContext(unitName = "OrchestraAppPersistence")
    private EntityManager em;

    @EJB
    private SheetFacadeREST sfr;
    @EJB
    private EventFacadeREST efr;
    public MusicalPieceFacadeREST() {
        super(MusicalPiece.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(MusicalPiece entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, MusicalPiece entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        MusicalPiece piece = find(id);
        //edit events
        List<Event> allEvents = efr.findAll();
        List<Event> eventsToUpdate = new ArrayList();
        for(Event e : allEvents){
            if(e.getPieces().contains(piece))
                eventsToUpdate.add(e);
        }
        for(Event e : eventsToUpdate){
            e.getPieces().remove(piece);
            efr.edit(e);
        }
        //edit sheets
        List<Sheet> sheetsToUpdate = sfr.findByPiece(id).getSheets();
        for(Sheet s : sheetsToUpdate){
            s.setPiece(null);
            sfr.edit(s);
        }
        //remove piece
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MusicalPiece find(@PathParam("id") Long id) {
        return super.find(id);
    }

    /*@GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<MusicalPiece> findAll() {
        return super.findAll();
    }*/
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MusicalPieces findAllPieces() {
        MusicalPieces mp = new MusicalPieces();
        mp.setPieces(super.findAll());
        return mp;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<MusicalPiece> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }
     //vyhledani podle jmena
    @GET
    @Path("/byName/{name}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MusicalPiece findByName(@PathParam("name") String name) {
       List<MusicalPiece> result = em.createQuery(
        "SELECT p FROM MusicalPiece p WHERE p.name = :pieceName")
        .setParameter("pieceName", name)
        .getResultList();
       if(!result.isEmpty()) return result.get(0);
       else return null;
    }
    /*@GET
    @Path("/sheets/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Sheets getSheets(@PathParam("id") Long id) {
      Sheets s = sfr.findAllSheets();
      List<Sheet> res = new ArrayList<Sheet>();
      
      for(Sheet sheet : s.getSheets()){    
          MusicalPiece p = sheet.getPiece();
          
          if(p != null){
              if(Objects.equals(p.getId(), id)) res.add(sheet);
          }
      }
      Sheets resultSheets = new Sheets();
        System.out.println("resSize: "+res.size());  
      resultSheets.setSheets(res);
      return resultSheets;
    }*/
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
