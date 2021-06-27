/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.BO;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.viettel.voffice.BO.Document.Attachs;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "USERS")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
		@NamedQuery(name = "Users.findByUserId", query = "SELECT u FROM Users u WHERE u.userId = :userId"),
		@NamedQuery(name = "Users.findByUserName", query = "SELECT u FROM Users u WHERE u.userName = :userName"),
		@NamedQuery(name = "Users.findByFullName", query = "SELECT u FROM Users u WHERE u.fullName = :fullName"),
		@NamedQuery(name = "Users.findByGender", query = "SELECT u FROM Users u WHERE u.gender = :gender"),
		@NamedQuery(name = "Users.findByDeptId", query = "SELECT u FROM Users u WHERE u.deptId = :deptId"),
		@NamedQuery(name = "Users.findByDeptName", query = "SELECT u FROM Users u WHERE u.deptName = :deptName"),
		@NamedQuery(name = "Users.findByPosId", query = "SELECT u FROM Users u WHERE u.posId = :posId"),
		@NamedQuery(name = "Users.findByPosName", query = "SELECT u FROM Users u WHERE u.posName = :posName"),
		@NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
		@NamedQuery(name = "Users.findByTelephone", query = "SELECT u FROM Users u WHERE u.telephone = :telephone"),
		@NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
		@NamedQuery(name = "Users.findByPasswordChanged", query = "SELECT u FROM Users u WHERE u.passwordChanged = :passwordChanged"),
		@NamedQuery(name = "Users.findByLastResetPassword", query = "SELECT u FROM Users u WHERE u.lastResetPassword = :lastResetPassword"),
		@NamedQuery(name = "Users.findByAvartarPath", query = "SELECT u FROM Users u WHERE u.avartarPath = :avartarPath"),
		@NamedQuery(name = "Users.findByStaffCode", query = "SELECT u FROM Users u WHERE u.staffCode = :staffCode"),
		@NamedQuery(name = "Users.findByIp", query = "SELECT u FROM Users u WHERE u.ip = :ip"),
		@NamedQuery(name = "Users.findByStatus", query = "SELECT u FROM Users u WHERE u.status = :status") })
public class Users implements Serializable {

	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
	@Basic(optional = false)
	@Column(name = "USER_ID")
	private Long userId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "USER_NAME")
	private String userName;
	@Size(max = 500)
	@Column(name = "FULL_NAME")
	private String fullName;
	@Column(name = "GENDER")
	private Long gender;
	@Column(name = "DEPT_ID")
	private Long deptId;
	@Size(max = 300)
	@Column(name = "DEPT_NAME")
	private String deptName;
	@Column(name = "POS_ID")
	private Long posId;
	@Size(max = 400)
	@Column(name = "POS_NAME")
	private String posName;
	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	// message="Invalid email")//if the field contains email address consider
	// using this annotation to enforce field validation
	@Size(max = 50)
	@Column(name = "EMAIL")
	private String email;
	@Size(max = 30)
	@Column(name = "TELEPHONE")
	private String telephone;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "PASSWORD_CHANGED")
	private Long passwordChanged;
	@Column(name = "LAST_RESET_PASSWORD")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastResetPassword;
	@Size(max = 500)
	@Column(name = "AVARTAR_PATH")
	private String avartarPath;
	@Size(max = 50)
	@Column(name = "STAFF_CODE")
	private String staffCode;
	@Size(max = 1000)
	@Column(name = "IP")
	private String ip;
	@Basic(optional = false)
	@NotNull
	@Column(name = "STATUS")
	private Long status;
	@Column(name = "BIRTHDAY")
	@Temporal(TemporalType.DATE)
	private Date birthday;
	@Column(name = "IDNUMBER")
	private String idNumber;
	@Transient
	private int random;
	// linhdx loai nguoi dung: Binh thuong hay quan tri, de co the xem duoc 1 so
	// danh muc hay toan bo danh muc
	@Column(name = "USER_TYPE")
	private Long userType;
	@Column(name = "BUSINESS_ID")
	private Long businessId;
	@Column(name = "BUSINESS_NAME")
	private String businessName;

//	@Lob
//	@Column(name = "SALT")
//	private byte[] salt;
	
	public Users() {
	}

	public Users(Long userId) {
		this.userId = userId;
	}

	public Users(Long deptId, String deptName) {
		this.deptId = deptId;
		this.deptName = deptName;
	}

	public Users(Long userId, String userName, Long deptId, String deptName, String posName) {
		this.userId = userId;
		this.userName = userName;
		this.deptId = deptId;
		this.deptName = deptName;
		this.posName = posName;
	}

	public Users(String fullName, Long userId) {
		this.fullName = fullName;
		this.userId = userId;
	}

	public Users(Long userId, String userName, String password, Long passwordChanged, Long status) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.passwordChanged = passwordChanged;
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getGender() {
		return gender;
	}

	public void setGender(Long gender) {
		this.gender = gender;
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

	public Long getPosId() {
		return posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getPasswordChanged() {
		return passwordChanged;
	}

	public void setPasswordChanged(Long passwordChanged) {
		this.passwordChanged = passwordChanged;
	}

	public Date getLastResetPassword() {
		return lastResetPassword;
	}

	public void setLastResetPassword(Date lastResetPassword) {
		this.lastResetPassword = lastResetPassword;
	}

	public String getAvartarPath() {
		return avartarPath;
	}

	public void setAvartarPath(String avartarPath) {
		this.avartarPath = avartarPath;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public int getRandom() {
		SecureRandom ran = new SecureRandom();
		return ran.nextInt(100);
	}

	public void setRandom(int random) {
		this.random = random;
	}

	public Long getUserType() {
		return userType;
	}

	public void setUserType(Long userType) {
		this.userType = userType;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (userId != null ? userId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Users)) {
			return false;
		}
		Users other = (Users) object;
		if ((this.userId == null && other.userId != null)
				|| (this.userId != null && !this.userId.equals(other.userId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.viettel.voffice.BO.Users[ userId=" + userId + " ]";
	}

//	public byte[] getSalt() {
//		return salt;
//	}
//
//	public void setSalt(byte[] salt) {
//		this.salt = salt;
//	}

}
