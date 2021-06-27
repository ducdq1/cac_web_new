/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.BO;

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
 * @author viettel
 */
@Entity
@Table(name = "OBJECTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objects.findAll", query = "SELECT o FROM Objects o"),
    @NamedQuery(name = "Objects.findByObjectId", query = "SELECT o FROM Objects o WHERE o.objectId = :objectId"),
    @NamedQuery(name = "Objects.findByStatus", query = "SELECT o FROM Objects o WHERE o.status = :status"),
    @NamedQuery(name = "Objects.findByOrd", query = "SELECT o FROM Objects o WHERE o.ord = :ord"),
    @NamedQuery(name = "Objects.findByObjectUrl", query = "SELECT o FROM Objects o WHERE o.objectUrl = :objectUrl"),
    @NamedQuery(name = "Objects.findByObjectName", query = "SELECT o FROM Objects o WHERE o.objectName = :objectName"),
    @NamedQuery(name = "Objects.findByDescription", query = "SELECT o FROM Objects o WHERE o.description = :description"),
    @NamedQuery(name = "Objects.findByObjectCode", query = "SELECT o FROM Objects o WHERE o.objectCode = :objectCode"),
    @NamedQuery(name = "Objects.findByObjectTypeId", query = "SELECT o FROM Objects o WHERE o.objectTypeId = :objectTypeId"),
    @NamedQuery(name = "Objects.findByObjectLevel", query = "SELECT o FROM Objects o WHERE o.objectLevel = :objectLevel")})
public class Objects implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "OBJECT_SEQ", sequenceName = "OBJECT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OBJECT_SEQ")
    @Column(name = "OBJECT_ID")
    private Long objectId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "ORD")
    private Integer ord;
    @Size(max = 500)
    @Column(name = "OBJECT_URL")
    private String objectUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "OBJECT_NAME")
    private String objectName;
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 100)
    @Column(name = "OBJECT_CODE")
    private String objectCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "OBJECT_TYPE_ID")
    private Long objectTypeId;
    @Size(max = 500)
    @Column(name = "OBJECT_LEVEL")
    private String objectLevel;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "APP_ID")
    private Long appId;

    public Objects() {
    }

    public Objects(Long objectId) {
        this.objectId = objectId;
    }

    public Objects(Long objectId, Long status, String objectName, Long objectTypeId) {
        this.objectId = objectId;
        this.status = status;
        this.objectName = objectName;
        this.objectTypeId = objectTypeId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public String getObjectUrl() {
        return objectUrl;
    }

    public void setObjectUrl(String objectUrl) {
        this.objectUrl = objectUrl;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public Long getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(Long objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getObjectLevel() {
        return objectLevel;
    }

    public void setObjectLevel(String objectLevel) {
        this.objectLevel = objectLevel;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objectId != null ? objectId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Objects)) {
            return false;
        }
        Objects other = (Objects) object;
        if ((this.objectId == null && other.objectId != null) || (this.objectId != null && !this.objectId.equals(other.objectId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "com.viettel.voffice.BO.Objects[ objectId=" + objectId + " ]";
        return objectName;
    }
}
