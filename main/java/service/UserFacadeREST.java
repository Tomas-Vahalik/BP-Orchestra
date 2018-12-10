/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import backend.User;
import backend.User_Group;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
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
 * @author HP
 */
@Stateless
@Path("eu.cz.fit.vahalto1.orchestraapplication.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "OrchestraAppPersistence")
    private EntityManager em;

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
    public void create(User user) {
       try {
			user.setPassword(encodeSHA256(user.getPassword()));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		User_Group group = new User_Group();
		group.setEmail(user.getEmail());
		group.setGroupname(User_Group.USERS_GROUP);
		em.persist(user);
		em.persist(group);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, User entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    
    @GET
    @Path("createAdmin")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void createAdmin() {
        User user = new User();
        user.setName("admin2");
        user.setEmail("admin2@email.com");
        user.setPassword("passwordAdmin2");
        
          try {
			user.setPassword(encodeSHA256(user.getPassword()));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		User_Group group = new User_Group();
		group.setEmail(user.getEmail());
		group.setGroupname(User_Group.ADMINS_GROUP);
		em.persist(user);
		em.persist(group);
    }
    
    @GET
    @Path("createUser")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void createUser() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@email.com");
        user.setPassword("passwordUser");
        
          try {
			user.setPassword(encodeSHA256(user.getPassword()));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		User_Group group = new User_Group();
		group.setEmail(user.getEmail());
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
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        return super.findAll();
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
