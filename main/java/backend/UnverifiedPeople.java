/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HP
 */
@XmlRootElement
public class UnverifiedPeople {
    private List<UnverifiedUser> users = new ArrayList<UnverifiedUser>();

    public List<UnverifiedUser> getUsers() {
        return users;
    }

    public void setUsers(List<UnverifiedUser> users) {
        this.users = users;
    }

    
}
