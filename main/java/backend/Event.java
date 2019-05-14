/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private String description;
    private Date eventDate;

    @ManyToMany()
    @JoinTable(
            name = "Event_Piece",
            joinColumns = {
                @JoinColumn(name = "event_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "piece_id")}
    )
    Set<MusicalPiece> pieces = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getFormatedDate() {
        SimpleDateFormat format = new SimpleDateFormat("d.MM.YYYY");
        return format.format(eventDate);
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Set<MusicalPiece> getPieces() {
        return pieces;
    }

    public void setPieces(Set<MusicalPiece> pieces) {
        this.pieces = pieces;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.cz.fit.vahalto1.orchestrasheetapplication.Event[ id=" + id + " ]";
    }

}
