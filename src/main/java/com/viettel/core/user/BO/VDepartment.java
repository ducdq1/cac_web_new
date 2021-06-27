/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.BO;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "V_DEPARTMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VDepartment.findAll", query = "SELECT v FROM VDepartment v"),
    @NamedQuery(name = "VDepartment.findByDeptId", query = "SELECT v FROM VDepartment v WHERE v.deptId = :deptId"),
    @NamedQuery(name = "VDepartment.findByDeptName", query = "SELECT v FROM VDepartment v WHERE v.deptName = :deptName"),
    @NamedQuery(name = "VDepartment.findByDeptPath", query = "SELECT v FROM VDepartment v WHERE v.deptPath = :deptPath"),
    @NamedQuery(name = "VDepartment.findByDeptLevel", query = "SELECT v FROM VDepartment v WHERE v.deptLevel = :deptLevel")})
public class VDepartment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Size(max = 4000)
    @Column(name = "DEPT_PATH")
    private String deptPath;
    @Column(name = "DEPT_LEVEL")
    private Long deptLevel;

    public VDepartment() {
    }

    public VDepartment(Long deptId) {
        this.deptId = deptId;
    }

    public VDepartment(Long deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
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

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }

    public Long getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(Long deptLevel) {
        this.deptLevel = deptLevel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deptId != null ? deptId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VDepartment)) {
            return false;
        }
        VDepartment other = (VDepartment) object;
        if ((this.deptId == null && other.deptId != null) || (this.deptId != null && !this.deptId.equals(other.deptId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.VDepartment[ deptId=" + deptId + " ]";
    }
    
}
