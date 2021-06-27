/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.BO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author viettel
 */
@Entity
@Table(name = "CATEGORY_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoryType.findAll", query = "SELECT c FROM CategoryType c"),
    @NamedQuery(name = "CategoryType.findByCategoryTypeId", query = "SELECT c FROM CategoryType c WHERE c.categoryTypeId = :categoryTypeId"),
    @NamedQuery(name = "CategoryType.findByName", query = "SELECT c FROM CategoryType c WHERE c.name = :name"),
    @NamedQuery(name = "CategoryType.findByCode", query = "SELECT c FROM CategoryType c WHERE c.code = :code"),
    @NamedQuery(name = "CategoryType.findByIsActive", query = "SELECT c FROM CategoryType c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "CategoryType.findByCreatedBy", query = "SELECT c FROM CategoryType c WHERE c.createdBy = :createdBy"),
    @NamedQuery(name = "CategoryType.findByCreatedDate", query = "SELECT c FROM CategoryType c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "CategoryType.findByUpdatedBy", query = "SELECT c FROM CategoryType c WHERE c.updatedBy = :updatedBy"),
    @NamedQuery(name = "CategoryType.findByUpdatedDate", query = "SELECT c FROM CategoryType c WHERE c.updatedDate = :updatedDate")})
public class CategoryType implements Serializable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "CATEGORY_TYPE_SEQ", sequenceName = "CATEGORY_TYPE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_TYPE_SEQ")
    @Column(name = "CATEGORY_TYPE_ID")
    private Long categoryTypeId;
    @Size(max = 200)
    @Column(name = "NAME")
    private String name;
    @Size(max = 50)
    @Column(name = "CODE")
    private String code;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "CREATED_BY")
    private Long createdBy;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "UPDATED_BY")
    private Long updatedBy;
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    
    @Column(name = "IS_SYSTEM")
    private Long isSystem;
    @Column(name = "IS_DEPT_DEPEND")
    private Long isDeptDepend;

    public CategoryType() {
    }

    public CategoryType(Long categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    public Long getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(Long categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Long isSystem) {
        this.isSystem = isSystem;
    }

    public Long getIsDeptDepend() {
        return isDeptDepend;
    }

    public void setIsDeptDepend(Long isDeptDepend) {
        this.isDeptDepend = isDeptDepend;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryTypeId != null ? categoryTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CategoryType)) {
            return false;
        }
        CategoryType other = (CategoryType) object;
        if ((this.categoryTypeId == null && other.categoryTypeId != null) || (this.categoryTypeId != null && !this.categoryTypeId.equals(other.categoryTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.CategoryType[ categoryTypeId=" + categoryTypeId + " ]";
    }
}
