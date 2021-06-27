/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

/**
 *
 * @author Linhdx
 */
public class RapidTestAttachModel {

    private Long id;
    private Long rapidTestId;
    private String attachTypeCode;
    private Long isDelete;
    private Long attachId;
    private String attachName;

    public RapidTestAttachModel() {

    }
    
    public RapidTestAttachModel(Long nid, Long nrapidTestId, String nattachTypeCode, 
            Long nattachId, String nattachName, Long nisDelete) {
        id = nid;
        rapidTestId= nrapidTestId;
        attachTypeCode = nattachTypeCode;
        isDelete= nisDelete;
        attachId = nattachId;
        attachName = nattachName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRapidTestId() {
        return rapidTestId;
    }

    public void setRapidTestId(Long rapidTestId) {
        this.rapidTestId = rapidTestId;
    }

    public String getAttachTypeCode() {
        return attachTypeCode;
    }

    public void setAttachTypeCode(String attachTypeCode) {
        this.attachTypeCode = attachTypeCode;
    }

    public Long getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Long isDelete) {
        this.isDelete = isDelete;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

}
