/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.BO;

import java.io.Serializable; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "PLACE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Place.findAll", query = "SELECT p FROM Place p"),
    @NamedQuery(name = "Place.findByPlaceId", query = "SELECT p FROM Place p WHERE p.placeId = :placeId"),
    @NamedQuery(name = "Place.findByCode", query = "SELECT p FROM Place p WHERE p.code = :code"),
    @NamedQuery(name = "Place.findByName", query = "SELECT p FROM Place p WHERE p.name = :name"),
    @NamedQuery(name = "Place.findByIsActive", query = "SELECT p FROM Place p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "Place.findByParentId", query = "SELECT p FROM Place p WHERE p.parentId = :parentId"),
    @NamedQuery(name = "Place.findByPlaceTypeCode", query = "SELECT p FROM Place p WHERE p.placeTypeCode = :placeTypeCode")})
public class Place implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "PLACE_SEQ", sequenceName = "PLACE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PLACE_SEQ")
    @Column(name = "PLACE_ID")
    private Long placeId;
    @Size(max = 50)
    @Column(name = "CODE")
    private String code;
    @Size(max = 200)
    @Column(name = "NAME")
    private String name;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Size(max = 50)
    @Column(name = "PLACE_TYPE_CODE")
    private String placeTypeCode;

    public Place() {
    }

    public Place(Long placeId) {
        this.placeId = placeId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPlaceTypeCode() {
        return placeTypeCode;
    }

    public void setPlaceTypeCode(String placeTypeCode) {
        this.placeTypeCode = placeTypeCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (placeId != null ? placeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Place)) {
            return false;
        }
        Place other = (Place) object;
        if ((this.placeId == null && other.placeId != null) || (this.placeId != null && !this.placeId.equals(other.placeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.core.sys.BO.Place[ placeId=" + placeId + " ]";
    }
    
}
