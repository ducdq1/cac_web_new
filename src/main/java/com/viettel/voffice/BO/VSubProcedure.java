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
@Table(name = "V_SUB_PROCEDURE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VSubProcedure.findAll", query = "SELECT v FROM VSubProcedure v"),
    @NamedQuery(name = "VSubProcedure.findBySubProcedureId", query = "SELECT v FROM VSubProcedure v WHERE v.subProcedureId = :subProcedureId"),
    @NamedQuery(name = "VSubProcedure.findByCode", query = "SELECT v FROM VSubProcedure v WHERE v.code = :code"),
    @NamedQuery(name = "VSubProcedure.findByName", query = "SELECT v FROM VSubProcedure v WHERE v.name = :name"),
    @NamedQuery(name = "VSubProcedure.findByExpiryDate", query = "SELECT v FROM VSubProcedure v WHERE v.expiryDate = :expiryDate"),
    @NamedQuery(name = "VSubProcedure.findByDescription", query = "SELECT v FROM VSubProcedure v WHERE v.description = :description"),
    @NamedQuery(name = "VSubProcedure.findByIsDefault", query = "SELECT v FROM VSubProcedure v WHERE v.isDefault = :isDefault"),
    @NamedQuery(name = "VSubProcedure.findBySortOrder", query = "SELECT v FROM VSubProcedure v WHERE v.sortOrder = :sortOrder"),
    @NamedQuery(name = "VSubProcedure.findByCategoryTypeCode", query = "SELECT v FROM VSubProcedure v WHERE v.categoryTypeCode = :categoryTypeCode"),
    @NamedQuery(name = "VSubProcedure.findByIsActive", query = "SELECT v FROM VSubProcedure v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VSubProcedure.findByValue", query = "SELECT v FROM VSubProcedure v WHERE v.value = :value"),
    @NamedQuery(name = "VSubProcedure.findByCreatedBy", query = "SELECT v FROM VSubProcedure v WHERE v.createdBy = :createdBy"),
    @NamedQuery(name = "VSubProcedure.findByCreatedDate", query = "SELECT v FROM VSubProcedure v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VSubProcedure.findByUpdatedBy", query = "SELECT v FROM VSubProcedure v WHERE v.updatedBy = :updatedBy"),
    @NamedQuery(name = "VSubProcedure.findByUpdatedDate", query = "SELECT v FROM VSubProcedure v WHERE v.updatedDate = :updatedDate"),
    @NamedQuery(name = "VSubProcedure.findByDeptId", query = "SELECT v FROM VSubProcedure v WHERE v.deptId = :deptId"),
    @NamedQuery(name = "VSubProcedure.findByDeptName", query = "SELECT v FROM VSubProcedure v WHERE v.deptName = :deptName")})
public class VSubProcedure implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Id
    @Column(name = "SUB_PROCEDURE_ID")
    private long subProcedureId;
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

    public VSubProcedure() {
    }

    public long getSubProcedureId() {
        return subProcedureId;
    }

    public void setSubProcedureId(long subProcedureId) {
        this.subProcedureId = subProcedureId;
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
