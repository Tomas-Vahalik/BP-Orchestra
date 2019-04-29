/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import com.vaadin.server.StreamResource.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author HP
 */
public class MyPdfResource implements StreamSource{

    private ByteArrayInputStream stream;

    public MyPdfResource(ByteArrayInputStream stream) {
        this.stream = stream;
    }
    @Override
    public InputStream getStream() {
        return stream;
    }
    
}
