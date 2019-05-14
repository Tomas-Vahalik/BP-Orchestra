/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import backend.BooleanWrapper;
import backend.Instrument;
import backend.Instruments;
import backend.User;
import backend.User_Group;
import backend.Users;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Tomáš Vahalík
 */
@Stateless
@Path("eu.cz.fit.vahalto1.orchestraapplication.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "OrchestraAppPersistence")
    private EntityManager em;

    @EJB
    private InstrumentFacadeREST ifr;

    public UserFacadeREST() {
        super(User.class);
    }

    public static String encodeSHA256(String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return DatatypeConverter.printBase64Binary(digest).toString();
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public void create(User user) {

        User_Group group = new User_Group();
        group.setLogin(user.getLogin());
        group.setGroupname(User_Group.USERS_GROUP);
        em.persist(user);
        em.persist(group);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public void edit(@PathParam("id") String id, User entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    public void remove(@PathParam("id") String id) {
        User_Group g = (User_Group) em.createQuery(
                "SELECT g FROM User_Group g WHERE g.login = :userId")
                .setParameter("userId", id).getSingleResult();
        em.remove(g);

        super.remove(super.find(id));
    }

    @GET
    @Path("createAdmin")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void createAdmin() {
        List<Long> result = em.createQuery(
                "SELECT COUNT(g) FROM User_Group g WHERE g.groupname = 'admin' GROUP BY g.groupname")
                .getResultList();

        if (result.size() > 0) {
            System.out.println("Admin alredy exists!!");
            return;
        }
        User user = new backend.User();
        user.setName("admin");
        user.setLogin("newAdmin@email.com");
        user.setPassword("hesloadmin");

        User_Group group = new User_Group();
        group.setLogin(user.getLogin());
        group.setGroupname(User_Group.ADMINS_GROUP);
        em.persist(user);
        em.persist(group);
    }

    @GET
    @Path("grantAdmin/{id}")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public void grantAdmin(@PathParam("id") String id) {
        User user = find(id);
        Instruments instruments = ifr.findAllInstruments();
        Set<Instrument> instr = new HashSet();
        instr.addAll(instruments.getInstruments());
        user.setInstruments(instr);

        User_Group g = (User_Group) em.createQuery(
                "SELECT g FROM User_Group g WHERE g.login = :userId")
                .setParameter("userId", id).getSingleResult();

        g.setGroupname(User_Group.ADMINS_GROUP);
        em.persist(g);
        em.persist(user);
    }

    @GET
    @Path("createUser")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void createUser() {
        User user = new User();
        user.setName("user");
        user.setLogin("novyUser@email.com");
        user.setPassword("heslouser");

        User_Group group = new User_Group();
        group.setLogin(user.getLogin());
        group.setGroupname(User_Group.USERS_GROUP);
        em.persist(user);
        em.persist(group);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

    public User find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Users findAllUsers() {
        Users users = new Users();
        users.setUsers(super.findAll());
        System.out.println("bla");
        return users;
    }

    @GET
    @Path("/isAdmin/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public BooleanWrapper isAdmin(@PathParam("id") String id) {
        BooleanWrapper wrapper = new BooleanWrapper();
        List<String> result = em.createQuery(
                "SELECT g.groupname FROM User_Group g WHERE g.login = :userId")
                .setParameter("userId", id)
                .getResultList();
        if (result.contains(User_Group.ADMINS_GROUP)) {
            wrapper.setValue(true);
        } else {
            wrapper.setValue(false);
        }

        return wrapper;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
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
