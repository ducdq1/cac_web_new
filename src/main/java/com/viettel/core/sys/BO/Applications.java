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
@Table(name = "APPLICATIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Applications.findAll", query = "SELECT a FROM Applications a"),
    @NamedQuery(name = "Applications.findByAppId", query = "SELECT a FROM Applications a WHERE a.appId = :appId"),
    @NamedQuery(name = "Applications.findByStatus", query = "SELECT a FROM Applications a WHERE a.status = :status"),
    @NamedQuery(name = "Applications.findByAppCode", query = "SELECT a FROM Applications a WHERE a.appCode = :appCode"),
    @NamedQuery(name = "Applications.findByAppName", query = "SELECT a FROM Applications a WHERE a.appName = :appName"),
    @NamedQuery(name = "Applications.findByDescription", query = "SELECT a FROM Applications a WHERE a.description = :description"),
    @NamedQuery(name = "Applications.findByLockDescription", query = "SELECT a FROM Applications a WHERE a.lockDescription = :lockDescription")})
public class Applications implements Serializable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "APPLICATION_SEQ", sequenceName = "APPLICATION_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPLICATION_SEQ")
    @Basic(optional = false)
    @Column(name = "APP_ID")
    private Long appId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private short status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "APP_CODE")
    private String appCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "APP_NAME")
    private String appName;
    @Size(max = 200)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 200)
    @Column(name = "LOCK_DESCRIPTION")
    private String lockDescription;

    public Applications() {
    }

    public Applications(Long appId) {
        this.appId = appId;
    }
    
    //hoangnv28
    public Applications(short status, String appCode, String appName, String desc){
        this.status = status;
        this.appCode = appCode;
        this.appName = appName;
        this.description = desc;
    }

    public Applications(Long appId, short status, String appCode, String appName) {
        this.appId = appId;
        this.status = status;
        this.appCode = appCode;
        this.appName = appName;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLockDescription() {
        return lockDescription;
    }

    public void setLockDescription(String lockDescription) {
        this.lockDescription = lockDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appId != null ? appId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Applications)) {
            return false;
        }
        Applications other = (Applications) object;
        if ((this.appId == null && other.appId != null) || (this.appId != null && !this.appId.equals(other.appId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "com.viettel.voffice.BO.Applications[ appId=" + appId + " ]";
        return appName;
    }
}
