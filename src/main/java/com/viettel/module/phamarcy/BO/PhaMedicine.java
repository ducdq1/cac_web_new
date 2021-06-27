package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "PHA_MEDICINE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhaMedicine.findAll", query = "SELECT c FROM PhaMedicine c WHERE c.isActive =1"),
    @NamedQuery(name = "PhaMedicine.findMedicinesByFileId", query = "SELECT c FROM PhaMedicine c WHERE c.fileId = :fileId AND c.isActive =1 ORDER BY c.medicineId asc")
})
public class PhaMedicine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "PHA_MEDICINE_SEQ", sequenceName = "PHA_MEDICINE_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHA_MEDICINE_SEQ")
	@Basic(optional = false)
	@Column(name = "MEDICINE_ID")
	private Long medicineId;
	@Column(name = "FILE_ID")
	private Long fileId;
	@Column(name = "NAME")
	@Size(max = 100)
	private String name;
	@Column(name = "REGISTRATION_NO")
	@Size(max = 20)
	private String registrationNo;
	@Column(name = "FORMAL_AND_OBJECT")
	@Size(max = 100)
	private String formalAndObject;
	@Column(name = "TIMES")
	private Long times;
	@Column(name = "HOAT_CHAT")
	@Size(max = 200)
	private String hoatchat;
	@Column(name = "DANG_BAO_CHE")
	@Size(max = 200)
	private String dangBaoChe;
	@Column(name = "HAM_LUONG")
	@Size(max = 200)
	private String hamLuong;
	@Column(name = "MANUFACTURER")
	@Size(max = 20)
	private String manufacturer;
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "CREATED_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdWhen;
	@Column(name = "CREATED_WHO")
	private Long createdWho;
	@Column(name = "MODIFIED_WHEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedWhen;
	@Column(name = "MODIFIED_WHO")
	private Long modifiedWho;
	@Column(name = "MEDICINE_TYPE")
	private Integer medicineType;
	@Column(name = "XUAT_XU")
	private Integer xuatXu;

	public PhaMedicine() {

	}

	public Long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(Long medicineId) {
		this.medicineId = medicineId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getFormalAndObject() {
		return formalAndObject;
	}

	public void setFormalAndObject(String formalAndObject) {
		this.formalAndObject = formalAndObject;
	}

	public Long getTimes() {
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}

	public String getHoatchat() {
		return hoatchat;
	}

	public void setHoatchat(String hoatchat) {
		this.hoatchat = hoatchat;
	}

	public String getDangBaoChe() {
		return dangBaoChe;
	}

	public void setDangBaoChe(String dangBaoChe) {
		this.dangBaoChe = dangBaoChe;
	}

	public String getHamLuong() {
		return hamLuong;
	}

	public void setHamLuong(String hamLuong) {
		this.hamLuong = hamLuong;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedWhen() {
		return createdWhen;
	}

	public void setCreatedWhen(Date createdWhen) {
		this.createdWhen = createdWhen;
	}

	public Long getCreatedWho() {
		return createdWho;
	}

	public void setCreatedWho(Long createdWho) {
		this.createdWho = createdWho;
	}

	public Date getModifiedWhen() {
		return modifiedWhen;
	}

	public void setModifiedWhen(Date modifiedWhen) {
		this.modifiedWhen = modifiedWhen;
	}

	public Long getModifiedWho() {
		return modifiedWho;
	}

	public void setModifiedWho(Long modifiedWho) {
		this.modifiedWho = modifiedWho;
	}

	public Integer getMedicineType() {
		return medicineType;
	}

	public void setMedicineType(Integer medicineType) {
		this.medicineType = medicineType;
	}

	public Integer getXuatXu() {
		return xuatXu;
	}

	public void setXuatXu(Integer xuatXu) {
		this.xuatXu = xuatXu;
	}

}
