/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tomáš Vahalík
 */
@XmlRootElement
public class BooleanWrapper {

    private boolean value;

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}
