/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš Vahalík
 */
@Entity
@XmlRootElement
public class UnverifiedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "login", nullable = false, length = 255)
    private String login;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToMany()
    @JoinTable(
            name = "UnverifiedUser_Instrument",
            joinColumns = {
                @JoinColumn(name = "user_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "instrument_id")}
    )
    Set<Instrument> instruments = new HashSet<>();

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<Instrument> instruments) {
        this.instruments = instruments;
    }

}
