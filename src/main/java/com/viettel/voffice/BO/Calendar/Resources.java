/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Calendar;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "RESOURCES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resources.findAll", query = "SELECT r FROM Resources r"),
    @NamedQuery(name = "Resources.findByResourceId", query = "SELECT r FROM Resources r WHERE r.resourceId = :resourceId"),
    @NamedQuery(name = "Resources.findByResourceName", query = "SELECT r FROM Resources r WHERE r.resourceName = :resourceName"),
    @NamedQuery(name = "Resources.findByResourceType", query = "SELECT r FROM Resources r WHERE r.resourceType = :resourceType"),
    @NamedQuery(name = "Resources.findByResourceInfo", query = "SELECT r FROM Resources r WHERE r.resourceInfo = :resourceInfo"),
    @NamedQuery(name = "Resources.findByStatus", query = "SELECT r FROM Resources r WHERE r.status = :status")})
public class Resources implements Serializable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "RESOURCE_SEQ", sequenceName = "RESOURCE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESOURCE_SEQ")
    @Column(name = "RESOURCE_ID")
    private Long resourceId;
    @Size(max = 250)
    @Column(name = "RESOURCE_NAME")
    private String resourceName;
    @Column(name = "RESOURCE_CODE")
    private String resourceCode;
    @Column(name = "RESOURCE_TYPE")
    private Long resourceType;
    @Column(name = "RESOURCE_TYPE_NAME")
    private String resourceTypeName;
    @Size(max = 250)
    @Column(name = "RESOURCE_INFO")
    private String resourceInfo;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Column(name = "STATUS")
    private Long status;

    public Resources() {
    }

    public Resources(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public Long getResourceType() {
        return resourceType;
    }

    public void setResourceType(Long resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceInfo() {
        return resourceInfo;
    }

    public void setResourceInfo(String resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resourceId != null ? resourceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Resources)) {
            return false;
        }
        Resources other = (Resources) object;
        if ((this.resourceId == null && other.resourceId != null) || (this.resourceId != null && !this.resourceId.equals(other.resourceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Calendar.Resources[ resourceId=" + resourceId + " ]";
    }
}
