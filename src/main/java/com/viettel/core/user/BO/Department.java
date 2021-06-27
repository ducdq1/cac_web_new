/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.BO;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author viettel
 */
@Entity
@Table(name = "DEPARTMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Department.findAll", query = "SELECT d FROM Department d"),
    @NamedQuery(name = "Department.findByDeptId", query = "SELECT d FROM Department d WHERE d.deptId = :deptId"),
    @NamedQuery(name = "Department.findByDeptName", query = "SELECT d FROM Department d WHERE d.deptName = :deptName"),
    @NamedQuery(name = "Department.findByDeptCode", query = "SELECT d FROM Department d WHERE d.deptCode = :deptCode"),
    @NamedQuery(name = "Department.findByDeptType", query = "SELECT d FROM Department d WHERE d.deptType = :deptType"),
    @NamedQuery(name = "Department.findByTelephone", query = "SELECT d FROM Department d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Department.findByFax", query = "SELECT d FROM Department d WHERE d.fax = :fax"),
    @NamedQuery(name = "Department.findByEmail", query = "SELECT d FROM Department d WHERE d.email = :email"),
    @NamedQuery(name = "Department.findByAddress", query = "SELECT d FROM Department d WHERE d.address = :address"),
    @NamedQuery(name = "Department.findByStatus", query = "SELECT d FROM Department d WHERE d.status = :status")})
public class Department implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "DEPARTMENT_SEQ", sequenceName = "DEPARTMENT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTMENT_SEQ")
    @Basic(optional = false)
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "DEPT_CODE")
    private String deptCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEPT_TYPE")
    private Long deptType;
    @Size(max = 20)
    @Column(name = "TELEPHONE")
    private String telephone;
    @Size(max = 20)
    @Column(name = "FAX")
    private String fax;
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 1000)
    @Column(name = "ADDRESS")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Transient
    private List<Department> children = new LinkedList<Department>();
    @Transient
    private String deptTypeName;

    public Department() {
    }

    public Department(Long deptId) {
        this.deptId = deptId;
    }

    public Department(Long deptId, String deptName, String deptCode, Long deptType, Long status) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.deptType = deptType;
        this.status = status;
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

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Long getDeptType() {
        return deptType;
    }

    public void setDeptType(Long deptType) {
        this.deptType = deptType;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
    }

    public String getDeptTypeName() {
        return deptTypeName;
    }

    public void setDeptTypeName(String deptTypeName) {
        this.deptTypeName = deptTypeName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deptId != null ? deptId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Department)) {
            return false;
        }
        Department other = (Department) object;
        if ((this.deptId == null && other.deptId != null) || (this.deptId != null && !this.deptId.equals(other.deptId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Department[ deptId=" + deptId + " ]";
    }
}
