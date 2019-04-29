/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author HP
 */
@Entity
public class User_Group implements Serializable {

    private static final long serialVersionUID = 1L;
    
	public static final String USERS_GROUP = "user";
        public static final String ADMINS_GROUP = "admin";
	
	@Id
	@Column(name="login", nullable=false, length=255)
	private String login;
	
	@Column(name="groupname", nullable=false, length=32)
	private String groupname;
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
}