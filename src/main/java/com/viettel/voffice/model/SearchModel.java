/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author ChucHV
 */
public class SearchModel {

    protected Long bookId;
    protected int menuType;
    protected String menuTypeStr;
    protected Date createDateFrom;
    protected Date createDateTo;
    protected Date modifyDateFrom;
    protected Date modifyDateTo;

    protected String nSWFileCode;
    protected String rapidTestNo;
    protected Long status;
    protected Long documentTypeCode;
    private List<Long> statusList;

    public SearchModel() {
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Date getCreateDateFrom() {
        return createDateFrom;
    }

    public void setCreateDateFrom(Date createDateFrom) {
        this.createDateFrom = createDateFrom;
    }

    public Date getModifyDateFrom() {
        return modifyDateFrom;
    }

    public void setModifyDateFrom(Date modifyDateFrom) {
        this.modifyDateFrom = modifyDateFrom;
    }
    
    public Date getCreateDateTo() {
        return createDateTo;
    }

    public void setCreateDateTo(Date createDateTo) {
        this.createDateTo = createDateTo;
    }
    
     public Date getModifyDateTo() {
        return modifyDateTo;
    }

    public void setModifyDateTo(Date modifyDateTo) {
        this.modifyDateTo=modifyDateTo;
    }

    public String getnSWFileCode() {
        return nSWFileCode;
    }

    public void setnSWFileCode(String nSWFileCode) {
        this.nSWFileCode = nSWFileCode;
    }

    public String getRapidTestNo() {
        return rapidTestNo;
    }

    public void setRapidTestNo(String rapidTestNo) {
        this.rapidTestNo = rapidTestNo;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(Long documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getMenuTypeStr() {
        return menuTypeStr;
    }

    public void setMenuTypeStr(String menuTypeStr) {
        this.menuTypeStr = menuTypeStr;
    }

    public List<Long> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Long> statusList) {
        this.statusList = statusList;
    }

}
