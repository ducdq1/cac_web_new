/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.voffice.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author viettel
 */

public class BookModel implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    private Long bookId;

    private String bookName;

    private Long bookOrder;

    private Long isDefault;

    private Long currentNumber;

    private String prefix;

    private Long prefixBy;

    private Long status;
    
    private String statusName;
    
    private Long deptId;

    private Long bookObjectTypeId;
    
    public BookModel() {
    }

    public BookModel(Long bookId) {
        this.bookId = bookId;
    }

    public BookModel(Long bookId, String bookName, Long isDefault, Long currentNumber, Long status) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.isDefault = isDefault;
        this.currentNumber = currentNumber;
        this.status = status;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Long getBookOrder() {
        return bookOrder;
    }

    public void setBookOrder(Long bookOrder) {
        this.bookOrder = bookOrder;
    }

    public Long getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Long isDefault) {
        this.isDefault = isDefault;
    }

    public Long getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(Long currentNumber) {
        this.currentNumber = currentNumber;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getPrefixBy() {
        return prefixBy;
    }

    public void setPrefixBy(Long prefixBy) {
        this.prefixBy = prefixBy;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
    
    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName() {
        this.statusName="Hoạt động"; //= status==1? "Hoạt động" : "Không hoạt động";
    }
    
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    
    public Long getBookObjectTypeId() {
        return bookObjectTypeId;
    }

    public void setBookObjectTypeId(Long bookObjectTypeId) {
        this.bookObjectTypeId = bookObjectTypeId;
    }
}
