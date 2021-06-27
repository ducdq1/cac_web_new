/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "V_FEE_PROCEDURE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VFeeProcedure.findAll", query = "SELECT v FROM VFeeProcedure v"),
    @NamedQuery(name = "VFeeProcedure.findByFeeId", query = "SELECT v FROM VFeeProcedure v WHERE v.feeId = :feeId"),
    @NamedQuery(name = "VFeeProcedure.findByCode", query = "SELECT v FROM VFeeProcedure v WHERE v.code = :code"),
    @NamedQuery(name = "VFeeProcedure.findByName", query = "SELECT v FROM VFeeProcedure v WHERE v.name = :name"),
    @NamedQuery(name = "VFeeProcedure.findByExpiryDate", query = "SELECT v FROM VFeeProcedure v WHERE v.expiryDate = :expiryDate"),
    @NamedQuery(name = "VFeeProcedure.findByDescription", query = "SELECT v FROM VFeeProcedure v WHERE v.description = :description"),
    @NamedQuery(name = "VFeeProcedure.findByIsDefault", query = "SELECT v FROM VFeeProcedure v WHERE v.isDefault = :isDefault"),
    @NamedQuery(name = "VFeeProcedure.findBySortOrder", query = "SELECT v FROM VFeeProcedure v WHERE v.sortOrder = :sortOrder"),
    @NamedQuery(name = "VFeeProcedure.findByCategoryTypeCode", query = "SELECT v FROM VFeeProcedure v WHERE v.categoryTypeCode = :categoryTypeCode"),
    @NamedQuery(name = "VFeeProcedure.findByIsActive", query = "SELECT v FROM VFeeProcedure v WHERE v.isActive = :isActive"),
    @NamedQuery(name = "VFeeProcedure.findByCost", query = "SELECT v FROM VFeeProcedure v WHERE v.cost = :cost"),
    @NamedQuery(name = "VFeeProcedure.findByCreatedBy", query = "SELECT v FROM VFeeProcedure v WHERE v.createdBy = :createdBy"),
    @NamedQuery(name = "VFeeProcedure.findByCreatedDate", query = "SELECT v FROM VFeeProcedure v WHERE v.createdDate = :createdDate"),
    @NamedQuery(name = "VFeeProcedure.findByUpdatedBy", query = "SELECT v FROM VFeeProcedure v WHERE v.updatedBy = :updatedBy"),
    @NamedQuery(name = "VFeeProcedure.findByUpdatedDate", query = "SELECT v FROM VFeeProcedure v WHERE v.updatedDate = :updatedDate"),
    @NamedQuery(name = "VFeeProcedure.findByDeptId", query = "SELECT v FROM VFeeProcedure v WHERE v.deptId = :deptId"),
    @NamedQuery(name = "VFeeProcedure.findByDeptName", query = "SELECT v FROM VFeeProcedure v WHERE v.deptName = :deptName"),
    @NamedQuery(name = "VFeeProcedure.findByProcedureId", query = "SELECT v FROM VFeeProcedure v WHERE v.procedureId = :procedureId")})
public class VFeeProcedure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "FEE_ID")
    private Long feeId;
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
    @Size(max = 100)
    @Column(name = "CATEGORY_TYPE_CODE")
    private String categoryTypeCode;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Size(max = 2000)

    @Column(name = "COST")
    private String cost;
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
    @Column(name = "PROCEDURE_ID")
    private Long procedureId;

    @Column(name = "PHASE")
    private Long phase;

    public VFeeProcedure() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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

    public Long getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Long procedureId) {
        this.procedureId = procedureId;
    }

    public Long getPhase() {
        return phase;
    }

    public void setPhase(Long phase) {
        this.phase = phase;
    }

}
