/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import backend.Event;
import backend.Events;
import backend.MusicalPiece;
import backend.MusicalPieces;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
 * @author Tomáš Vahalík
 */
@Stateless
@Path("eu.cz.fit.vahalto1.orchestraapplication.event")
public class EventFacadeREST extends AbstractFacade<Event> {

    @PersistenceContext(unitName = "OrchestraAppPersistence")
    private EntityManager em;

    @EJB
    private MusicalPieceFacadeREST pieceFacade;

    public EventFacadeREST() {
        super(Event.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Event entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Event entity) {

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
    public Event find(@PathParam("id") Long id) {
        return super.find(id);
    }

    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Events findAllEvents() {

        Events events = new Events();
        List<Event> result = em.createQuery(
                "SELECT e FROM Event e ORDER BY e.eventDate")
                .getResultList();        
        events.setEvents(result);
        return events;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Event> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("/byName/{name}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Event findByName(@PathParam("name") String name) {
        List<Event> result = em.createQuery(
                "SELECT e FROM Event e WHERE e.description = :eventName")
                .setParameter("eventName", name)
                .getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    //Slouzi k nalezeni skladeb, ktere se hraji na udalosti
    @GET
    @Path("/pieces/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MusicalPieces getPieces(@PathParam("id") Long id) {
        Event e = find(id);
        if (e == null) {
            return null;
        }
        MusicalPieces mp = new MusicalPieces();
        List<MusicalPiece> list = new ArrayList<>();
        Set<MusicalPiece> p = e.getPieces();
        if (p != null) {
            list.addAll(p);
        }
        mp.setPieces(list);
        return mp;
    }

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
