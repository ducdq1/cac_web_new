/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.RapidTest;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "PROCEDURE_GROUP_FEE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcedureGroupFee.findAll", query = "SELECT r FROM ProcedureGroupFee r"),
    @NamedQuery(name = "ProcedureGroupFee.findByProcedureGroupFeeId", query = "SELECT r FROM ProcedureGroupFee r WHERE r.procedureGroupFeeId = :procedureGroupFeeId"),
    @NamedQuery(name = "ProcedureGroupFee.findByGroupId", query = "SELECT r FROM ProcedureGroupFee r WHERE r.groupId = :groupId"),
    @NamedQuery(name = "ProcedureGroupFee.findByProcedureId", query = "SELECT r FROM ProcedureGroupFee r WHERE r.procedureId = :procedureId"),
    @NamedQuery(name = "ProcedureGroupFee.findByIsActive", query = "SELECT r FROM ProcedureGroupFee r WHERE r.isActive = :isActive"),
    @NamedQuery(name = "ProcedureGroupFee.findByFeeId", query = "SELECT r FROM ProcedureGroupFee r WHERE r.feeId = :feeId")})
public class ProcedureGroupFee implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "PROCEDURE_GROUP_FEE_SEQ", sequenceName = "PROCEDURE_GROUP_FEE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCEDURE_GROUP_FEE_SEQ")
    @Column(name = "PROCEDURE_GROUP_FEE_ID")
    @Basic(optional = false)
    @NotNull
    
    private Long procedureGroupFeeId;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "PROCEDURE_ID")
    private Long procedureId;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "FEE_ID")
    private Long feeId;

    public ProcedureGroupFee() {
    }

    public ProcedureGroupFee(Long procedureGroupFeeId) {
        this.procedureGroupFeeId = procedureGroupFeeId;
    }

    public Long getProcedureGroupFeeId() {
        return procedureGroupFeeId;
    }

    public void setProcedureGroupFeeId(Long procedureGroupFeeId) {
        this.procedureGroupFeeId = procedureGroupFeeId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Long procedureId) {
        this.procedureId = procedureId;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (procedureGroupFeeId != null ? procedureGroupFeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcedureGroupFee)) {
            return false;
        }
        ProcedureGroupFee other = (ProcedureGroupFee) object;
        if ((this.procedureGroupFeeId == null && other.procedureGroupFeeId != null) || (this.procedureGroupFeeId != null && !this.procedureGroupFeeId.equals(other.procedureGroupFeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.RapidTest.ProcedureGroupFee[ procedureGroupFeeId=" + procedureGroupFeeId + " ]";
    }
    
}
