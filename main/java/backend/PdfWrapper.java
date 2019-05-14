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
public class PdfWrapper {
    private byte[] pdfFile;    

    public byte[] getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(byte[] pdfFile) {
        this.pdfFile = pdfFile;
    }
}
