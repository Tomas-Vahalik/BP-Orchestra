/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import backend.Event;
import backend.Instrument;
import backend.MusicalPiece;
import backend.PdfWrapper;
import backend.Sheet;
import backend.Sheets;
import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tomáš Vahalík
 */
@Stateless
@Path("eu.cz.fit.vahalto1.orchestraapplication.sheet")
public class SheetFacadeREST extends AbstractFacade<Sheet> {

    @PersistenceContext(unitName = "OrchestraAppPersistence")
    private EntityManager em;

    @EJB
    private MusicalPieceFacadeREST pfr;
    @EJB
    private InstrumentFacadeREST ifr;
    @EJB
    private EventFacadeREST efr;

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
    @Produces("application/pdf")
    public Response getPdf(@PathParam("id") Long id, @Context HttpHeaders hh) throws Exception {

        byte[] result = (byte[]) em.createQuery(
                "SELECT s.pdfFile FROM Sheet s WHERE s.id = :id")
                .setParameter("id", id)
                .getSingleResult();

        ByteArrayInputStream is = new ByteArrayInputStream(result);
        Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.ok((Object) is);
        responseBuilder.type("application/pdf");
        responseBuilder.header("Content-Disposition", "filename=test.pdf");
        return responseBuilder.build();
    }

    @Path("/setPdf/{id}")
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void setPdf(@PathParam("id") Long id, PdfWrapper wrapper) {
        Sheet sheet = super.find(id);
        sheet.setPdfFile(wrapper.getPdfFile());
        super.edit(sheet);
    }

    @GET
    @Path("/byName/{name}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Sheet findByName(@PathParam("name") String name) {
        List<Sheet> result = em.createQuery(
                "SELECT s FROM Sheet s WHERE s.name = :sheetName")
                .setParameter("sheetName", name)
                .getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @GET
    @Path("/byPiece/{piece}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Sheets findByPiece(@PathParam("piece") Long piece_id) {
        MusicalPiece p = pfr.find(piece_id);
        Sheets s = new Sheets();
        List<Sheet> result = em.createQuery(
                "SELECT s FROM Sheet s WHERE s.piece = :piece")
                .setParameter("piece", p)
                .getResultList();
        s.setSheets(result);
        return s;
    }

    @GET
    @Path("/byInstrument/{instrument}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Sheets findByInstrument(@PathParam("instrument") Long instrument_id) {

        Instrument i = ifr.find(instrument_id);

        Sheets s = new Sheets();
        List<Sheet> result = em.createQuery(
                "SELECT s FROM Sheet s WHERE s.instrument = :instrument")
                .setParameter("instrument", i)
                .getResultList();
        s.setSheets(result);

        return s;
    }

    @GET
    @Path("/byEvent/{event}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Sheets findByEvent(@PathParam("event") Long event_id) {
        Event e = efr.find(event_id);
        Set<MusicalPiece> p = e.getPieces();
        Set<Long> l = new HashSet();
        for (MusicalPiece piece : p) {
            l.add(piece.getId());
        }

        Sheets s = new Sheets();
        if (p.size() == 0) {
            return s;
        }
        List<Sheet> result = em.createQuery(
                "SELECT s FROM Sheet s WHERE s.piece IN :pieces")
                .setParameter("pieces", p)
                .getResultList();
        s.setSheets(result);

        return s;
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
