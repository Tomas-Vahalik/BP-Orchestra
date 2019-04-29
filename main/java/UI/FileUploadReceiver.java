/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 *
 * @author HP
 */
public class FileUploadReceiver implements Upload.Receiver, Upload.SucceededListener {
   private static final long serialVersionUID = 112358132134L;
    private String data;
    private String fileName;
    private String mType;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();    
    private Label label;
    public FileUploadReceiver(Label l){
        label = l;
    }
    
    public String getFileName(){
        return fileName;
    }
    public String getData(){
        return data;
    }
    public ByteArrayOutputStream getStream(){
        return baos;
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        this.fileName = filename;
        baos.reset();
        return baos;

        //return null;
    }

    void reset(){
       baos.reset();
        data = null;
    }
   

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        data = baos.toString();
       label.setCaption(fileName);
       // System.out.println(data);
       Notification.show("Uploaded successfully",
                  "",
                  Notification.Type.HUMANIZED_MESSAGE);
                
    }
}