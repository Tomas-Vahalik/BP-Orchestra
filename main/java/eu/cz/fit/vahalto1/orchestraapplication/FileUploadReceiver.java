/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cz.fit.vahalto1.orchestraapplication;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author HP
 */
public class FileUploadReceiver implements Upload.Receiver, Upload.SucceededListener {
   private static final long serialVersionUID = 112358132134L;
    private String data;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    
    
    public String getData(){
        return data;
    }
    public ByteArrayOutputStream getStream(){
        return baos;
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        baos.reset();
        return baos;

        //return null;
    }

   

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        data = baos.toString();
       
       // System.out.println(data);
       Notification.show("Uploaded successfully",
                  "",
                  Notification.Type.HUMANIZED_MESSAGE);
                
    }
}