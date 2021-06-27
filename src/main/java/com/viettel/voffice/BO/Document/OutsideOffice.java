/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Document;

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
 * @author HaVM2
 */
@Entity
@Table(name = "OUTSIDE_OFFICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OutsideOffice.findAll", query = "SELECT o FROM OutsideOffice o"),
    @NamedQuery(name = "OutsideOffice.findByOfficeId", query = "SELECT o FROM OutsideOffice o WHERE o.officeId = :officeId"),
    @NamedQuery(name = "OutsideOffice.findByOfficeCode", query = "SELECT o FROM OutsideOffice o WHERE o.officeCode = :officeCode"),
    @NamedQuery(name = "OutsideOffice.findByOfficeName", query = "SELECT o FROM OutsideOffice o WHERE o.officeName = :officeName"),
    @NamedQuery(name = "OutsideOffice.findByAddress", query = "SELECT o FROM OutsideOffice o WHERE o.address = :address"),
    @NamedQuery(name = "OutsideOffice.findByEmail", query = "SELECT o FROM OutsideOffice o WHERE o.email = :email"),
    @NamedQuery(name = "OutsideOffice.findByMobile", query = "SELECT o FROM OutsideOffice o WHERE o.mobile = :mobile"),
    @NamedQuery(name = "OutsideOffice.findByFax", query = "SELECT o FROM OutsideOffice o WHERE o.fax = :fax"),
    @NamedQuery(name = "OutsideOffice.findByServicesUrl", query = "SELECT o FROM OutsideOffice o WHERE o.servicesUrl = :servicesUrl"),
    @NamedQuery(name = "OutsideOffice.findByLeader", query = "SELECT o FROM OutsideOffice o WHERE o.leader = :leader"),
    @NamedQuery(name = "OutsideOffice.findByStatus", query = "SELECT o FROM OutsideOffice o WHERE o.status = :status"),
    @NamedQuery(name = "OutsideOffice.findByDeptId", query = "SELECT o FROM OutsideOffice o WHERE o.deptId = :deptId")})
public class OutsideOffice implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "OUTSIDE_OFFICE_SEQ", sequenceName = "OUTSIDE_OFFICE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OUTSIDE_OFFICE_SEQ")
    @Column(name = "OFFICE_ID")
    private Long officeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "OFFICE_CODE")
    private String officeCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "OFFICE_NAME")
    private String officeName;
    @Size(max = 500)
    @Column(name = "ADDRESS")
    private String address;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 20)
    @Column(name = "MOBILE")
    private String mobile;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "FAX")
    private String fax;
    @Size(max = 500)
    @Column(name = "SERVICES_URL")
    private String servicesUrl;
    @Size(max = 200)
    @Column(name = "LEADER")
    private String leader;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Column(name = "DEPT_NAME")
    private String deptName;

    public OutsideOffice() {
    }

    public OutsideOffice(Long officeId) {
        this.officeId = officeId;
    }

    public OutsideOffice(Long officeId, String officeCode, String officeName, Long status, Long deptId) {
        this.officeId = officeId;
        this.officeCode = officeCode;
        this.officeName = officeName;
        this.status = status;
        this.deptId = deptId;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getServicesUrl() {
        return servicesUrl;
    }

    public void setServicesUrl(String servicesUrl) {
        this.servicesUrl = servicesUrl;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (officeId != null ? officeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OutsideOffice)) {
            return false;
        }
        OutsideOffice other = (OutsideOffice) object;
        if ((this.officeId == null && other.officeId != null) || (this.officeId != null && !this.officeId.equals(other.officeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "com.viettel.voffice.BO.Document.OutsideOffice[ officeId=" + officeId + " ]";
    	return officeName;
    }

}
