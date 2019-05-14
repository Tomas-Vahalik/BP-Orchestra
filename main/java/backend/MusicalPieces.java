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
 * @author Tomáš Vahalík
 */
@XmlRootElement
public class MusicalPieces {
    private List<MusicalPiece> pieces = new ArrayList<>();

    public List<MusicalPiece> getPieces() {
        return pieces;
    }

    public void setPieces(List<MusicalPiece> pieces) {
        this.pieces = pieces;
    }
    
}
