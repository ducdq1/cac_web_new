package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.DateTimeUtils;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "CUSTOMER")
@XmlRootElement
public class Customer extends BaseComposer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "CUSTOMER_SEQ", sequenceName = "CUSTOMER_SEQ", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_SEQ")
	@Basic(optional = false)
	@Column(name = "CUSTOMER_ID")
	private Long customerId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "HOUSE_NUM")
	private String houseNum;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "PHONE")
	private String phone;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(targetEntity = Street.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "STREET_ID", referencedColumnName = "STREET_ID")
	private Street street;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(targetEntity = GiayDan.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "GIAY_DAN_ID", referencedColumnName = "GIAY_DAN_ID",insertable = false,updatable = false)
	private GiayDan giayDan;

	@Column(name = "GIAY_DAN_ID")
	private Long giayDanId;

	@Column(name = "NGAY_DI_DAN")
	private Date ngayDiDan;

	@Column(name = "NGAY_NHAP_PM")
	private Date ngayNhapPM;

	@Column(name = "GAP_CHU_NHA")
	private Long gapChuNha;

	@Column(name = "TIEN_DO_KHI_DAN_GIAY")
	private String tienDoKhiDanGiay;

	@Column(name = "NV_DAN_GIAY")
	private String nhanVienDanGiay;

	@Column(name = "NV_KIEM_TRA")
	private String nhanVienKiemTra;

	@Column(name = "XAC_NHAN_QUAN_LY")
	private String xacNhanQuanLy;

	@Column(name = "KQ_KIEM_TRA")
	private Long ketQuaKiemTra;
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public Long getGiayDanId() {
		return giayDanId;
	}

	public void setGiayDanId(Long giayDanId) {
		this.giayDanId = giayDanId;
	}

	public Date getNgayDiDan() {
		return ngayDiDan;
	}

	public void setNgayDiDan(Date ngayDiDan) {
		this.ngayDiDan = ngayDiDan;
	}

	public Date getNgayNhapPM() {
		return ngayNhapPM;
	}

	public void setNgayNhapPM(Date ngayNhapPM) {
		this.ngayNhapPM = ngayNhapPM;
	}

	public Long getGapChuNha() {
		return gapChuNha;
	}

	public void setGapChuNha(Long gapChuNha) {
		this.gapChuNha = gapChuNha;
	}

	public String getTienDoKhiDanGiay() {
		return tienDoKhiDanGiay;
	}

	public void setTienDoKhiDanGiay(String tienDoKhiDanGiay) {
		this.tienDoKhiDanGiay = tienDoKhiDanGiay;
	}

	public String getNhanVienDanGiay() {
		return nhanVienDanGiay;
	}

	public void setNhanVienDanGiay(String nhanVienDanGiay) {
		this.nhanVienDanGiay = nhanVienDanGiay;
	}

	public String getNhanVienKiemTra() {
		return nhanVienKiemTra;
	}

	public void setNhanVienKiemTra(String nhanVienKiemTra) {
		this.nhanVienKiemTra = nhanVienKiemTra;
	}

	public String getXacNhanQuanLy() {
		return xacNhanQuanLy;
	}

	public void setXacNhanQuanLy(String xacNhanQuanLy) {
		this.xacNhanQuanLy = xacNhanQuanLy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}

	public GiayDan getGiayDan() {
		return giayDan;
	}

	public void setGiayDan(GiayDan giayDan) {
		this.giayDan = giayDan;
	}

	public Long getKetQuaKiemTra() {
		return ketQuaKiemTra;
	}

	public void setKetQuaKiemTra(Long ketQuaKiemTra) {
		this.ketQuaKiemTra = ketQuaKiemTra;
	}

	
}
