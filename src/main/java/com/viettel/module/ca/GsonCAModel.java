/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.module.ca;

/**
 *
 * @author HaVM2
 */
public class GsonCAModel {
    String base64Cert;
    Long fileId;
    String certChain;
    //Certificate[] certChain;
    public String getBase64Cert() {
        return base64Cert;
    }

    public void setBase64Cert(String base64Cert) {
        this.base64Cert = base64Cert;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getCertChain() {
        return certChain;
    }

    public void setCertChain(String certChain) {
        this.certChain = certChain;
    }
//    public Certificate[] getCertChain() {
//        return certChain;
//    }
//
//    public void setCertChain(Certificate[] certChain) {
//        this.certChain = certChain;
//    }
   
}
