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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "RT_GROUP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RtGroup.findAll", query = "SELECT r FROM RtGroup r"),
    @NamedQuery(name = "RtGroup.findByGroupId", query = "SELECT r FROM RtGroup r WHERE r.groupId = :groupId"),
    @NamedQuery(name = "RtGroup.findByCode", query = "SELECT r FROM RtGroup r WHERE r.code = :code"),
    @NamedQuery(name = "RtGroup.findByName", query = "SELECT r FROM RtGroup r WHERE r.name = :name"),
    @NamedQuery(name = "RtGroup.findByIsActive", query = "SELECT r FROM RtGroup r WHERE r.isActive = :isActive"),
    @NamedQuery(name = "RtGroup.findByGroupType", query = "SELECT r FROM RtGroup r WHERE r.groupType = :groupType")})
public class RtGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "GROUP_SEQ", sequenceName = "GROUP_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GROUP_SEQ")
    @Column(name = "GROUP_ID")
    @Basic(optional = false)
    @NotNull
    private Long groupId;
    @Size(max = 100)
    @Column(name = "CODE")
    private String code;
    @Size(max = 400)
    @Column(name = "NAME")
    private String name;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "GROUP_TYPE")
    private Long groupType;

    public RtGroup() {
    }

    public RtGroup(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getGroupType() {
        return groupType;
    }

    public void setGroupType(Long groupType) {
        this.groupType = groupType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupId != null ? groupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RtGroup)) {
            return false;
        }
        RtGroup other = (RtGroup) object;
        if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.RapidTest.RtGroup[ groupId=" + groupId + " ]";
    }
    
}
