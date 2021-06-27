/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.RapidTest;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */

@Entity
@Table(name = "V_FEE_FILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VFeeFile.findAll", query = "SELECT v FROM VFeeFile v"),
    @NamedQuery(name = "VFeeFile.findByCategoryId", query = "SELECT v FROM VFeeFile v WHERE v.categoryId = :categoryId"),
    @NamedQuery(name = "VFeeFile.findByCode", query = "SELECT v FROM VFeeFile v WHERE v.code = :code"),
    @NamedQuery(name = "VFeeFile.findByName", query = "SELECT v FROM VFeeFile v WHERE v.name = :name"),
    @NamedQuery(name = "VFeeFile.findByExpiryDate", query = "SELECT v FROM VFeeFile v WHERE v.expiryDate = :expiryDate"),
    @NamedQuery(name = "VFeeFile.findByDescription", query = "SELECT v FROM VFeeFile v WHERE v.description = :description"),
    @NamedQuery(name = "VFeeFile.findByIsDefault", query = "SELECT v FROM VFeeFile v WHERE v.isDefault = :isDefault"),
    @NamedQuery(name = "VFeeFile.findBySortOrder", query = "SELECT v FROM VFeeFile v WHERE v.sortOrder = :sortOrder"),
    @NamedQuery(name = "VFeeFile.findByCategoryTypeCode", query = "SELECT v FROM VFeeFile v WHERE v.categoryTypeCode = :categoryTypeCode"),
    @NamedQuery(name = "VFeeFile.findByIsActive", query = "SELECT v FROM VFeeFile v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VFeeFile.findByValue", query = "SELECT v FROM VFeeFile v WHERE v.value = :value"),
    @NamedQuery(name = "VFeeFile.findByCreatedBy", query = "SELECT v FROM VFeeFile v WHERE v.createdBy = :createdBy"),
    @NamedQuery(name = "VFeeFile.findByCreatedDate", query = "SELECT v FROM VFeeFile v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VFeeFile.findByUpdatedBy", query = "SELECT v FROM VFeeFile v WHERE v.updatedBy = :updatedBy"),
    @NamedQuery(name = "VFeeFile.findByUpdatedDate", query = "SELECT v FROM VFeeFile v WHERE v.updatedDate = :updatedDate"),
    @NamedQuery(name = "VFeeFile.findByDeptId", query = "SELECT v FROM VFeeFile v WHERE v.deptId = :deptId"),
    @NamedQuery(name = "VFeeFile.findByDeptName", query = "SELECT v FROM VFeeFile v WHERE v.deptName = :deptName"),
    @NamedQuery(name = "VFeeFile.findByFileCode", query = "SELECT v FROM VFeeFile v WHERE v.fileCode = :fileCode")})
public class VFeeFile implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2703164524111634931L;
	@Basic(optional = false)
    @NotNull
    @Id
    @Column(name = "CATEGORY_ID")
    private Long categoryId;
    @Size(max = 100)
    @Column(name = "CODE")
    private String code;
    @Size(max = 400)
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
    @Size(min = 1, max = 100)
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
    @Size(max = 500)
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Size(max = 100)
    @Column(name = "FILE_CODE")
    private String fileCode;

    public VFeeFile() {
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }
    
}
