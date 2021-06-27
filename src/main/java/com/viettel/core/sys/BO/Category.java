/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.BO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author viettel
 */
@Entity
@Table(name = "CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c"),
    @NamedQuery(name = "Category.findByCategoryId", query = "SELECT c FROM Category c WHERE c.categoryId = :categoryId"),
    @NamedQuery(name = "Category.findByCode", query = "SELECT c FROM Category c WHERE c.code = :code"),
    @NamedQuery(name = "Category.findByName", query = "SELECT c FROM Category c WHERE c.name = :name"),
    @NamedQuery(name = "Category.findByExpiryDate", query = "SELECT c FROM Category c WHERE c.expiryDate = :expiryDate"),
    @NamedQuery(name = "Category.findByDescription", query = "SELECT c FROM Category c WHERE c.description = :description"),
    @NamedQuery(name = "Category.findByIsDefault", query = "SELECT c FROM Category c WHERE c.isDefault = :isDefault"),
    @NamedQuery(name = "Category.findBySortOrder", query = "SELECT c FROM Category c WHERE c.sortOrder = :sortOrder"),
    @NamedQuery(name = "Category.findByCategoryTypeCode", query = "SELECT c FROM Category c WHERE c.categoryTypeCode = :categoryTypeCode"),
    @NamedQuery(name = "Category.findByIsActive", query = "SELECT c FROM Category c WHERE c.isActive = :isActive"),
    @NamedQuery(name = "Category.findByValue", query = "SELECT c FROM Category c WHERE c.value = :value"),
    @NamedQuery(name = "Category.findByCreatedBy", query = "SELECT c FROM Category c WHERE c.createdBy = :createdBy"),
    @NamedQuery(name = "Category.findByCreatedDate", query = "SELECT c FROM Category c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "Category.findByUpdatedBy", query = "SELECT c FROM Category c WHERE c.updatedBy = :updatedBy"),
    @NamedQuery(name = "Category.findByUpdatedDate", query = "SELECT c FROM Category c WHERE c.updatedDate = :updatedDate"),
    @NamedQuery(name = "Category.findByDeptId", query = "SELECT c FROM Category c WHERE c.deptId = :deptId")
}
)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "CATEGORY_SEQ", sequenceName = "CATEGORY_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_SEQ")
    @Column(name = "CATEGORY_ID")
    private Long categoryId;
    @Size(max = 50)
    @Column(name = "CODE")
    private String code;
    @Size(max = 200)
    @Column(name = "NAME")
    private String name;
    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    @Size(max = 400)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_DEFAULT")
    private Long isDefault;
    @Column(name = "SORT_ORDER")
    private Long sortOrder;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CATEGORY_TYPE_CODE")
    private String categoryTypeCode;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Size(max = 2000)
    @Column(name = "VALUE")
    private String value;
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
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Column(name = "DEPT_NAME")
    private String deptName;
    
    @Transient
    private Long userId;
    
    public Category() {
    }

    public Category(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Category(Long categoryId, String name){
        this.categoryId = categoryId;
        this.name = name;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Long isDefault) {
        this.isDefault = isDefault;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCategoryTypeCode() {
        return categoryTypeCode;
    }

    public void setCategoryTypeCode(String categoryTypeCode) {
        this.categoryTypeCode = categoryTypeCode;
    }

    public Long getIsActive() {
        return isActive;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }    
    
    
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Category[ categoryId=" + categoryId + " ]";
    }
}
