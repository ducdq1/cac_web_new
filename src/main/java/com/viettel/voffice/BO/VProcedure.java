/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO;

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
@Table(name = "V_PROCEDURE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VProcedure.findAll", query = "SELECT v FROM VProcedure v"),
    @NamedQuery(name = "VProcedure.findByProcedureId", query = "SELECT v FROM VProcedure v WHERE v.procedureId = :procedureId"),
    @NamedQuery(name = "VProcedure.findByCode", query = "SELECT v FROM VProcedure v WHERE v.code = :code"),
    @NamedQuery(name = "VProcedure.findByName", query = "SELECT v FROM VProcedure v WHERE v.name = :name"),
    @NamedQuery(name = "VProcedure.findByExpiryDate", query = "SELECT v FROM VProcedure v WHERE v.expiryDate = :expiryDate"),
    @NamedQuery(name = "VProcedure.findByDescription", query = "SELECT v FROM VProcedure v WHERE v.description = :description"),
    @NamedQuery(name = "VProcedure.findByIsDefault", query = "SELECT v FROM VProcedure v WHERE v.isDefault = :isDefault"),
    @NamedQuery(name = "VProcedure.findBySortOrder", query = "SELECT v FROM VProcedure v WHERE v.sortOrder = :sortOrder"),
    @NamedQuery(name = "VProcedure.findByCategoryTypeCode", query = "SELECT v FROM VProcedure v WHERE v.categoryTypeCode = :categoryTypeCode"),
    @NamedQuery(name = "VProcedure.findByIsActive", query = "SELECT v FROM VProcedure v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VProcedure.findByValue", query = "SELECT v FROM VProcedure v WHERE v.value = :value"),
    @NamedQuery(name = "VProcedure.findByCreatedBy", query = "SELECT v FROM VProcedure v WHERE v.createdBy = :createdBy"),
    @NamedQuery(name = "VProcedure.findByCreatedDate", query = "SELECT v FROM VProcedure v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VProcedure.findByUpdatedBy", query = "SELECT v FROM VProcedure v WHERE v.updatedBy = :updatedBy"),
    @NamedQuery(name = "VProcedure.findByUpdatedDate", query = "SELECT v FROM VProcedure v WHERE v.updatedDate = :updatedDate"),
    @NamedQuery(name = "VProcedure.findByDeptId", query = "SELECT v FROM VProcedure v WHERE v.deptId = :deptId"),
    @NamedQuery(name = "VProcedure.findByDeptName", query = "SELECT v FROM VProcedure v WHERE v.deptName = :deptName")})
public class VProcedure implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Id
    @Column(name = "PROCEDURE_ID")
    private long procedureId;
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

    public VProcedure() {
    }

    public long getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(long procedureId) {
        this.procedureId = procedureId;
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
    
}
