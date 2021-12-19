package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "THAU_THO_KH")
@XmlRootElement
public class ThauThoKH extends BaseComposer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "THAU_THO_KH_SEQ", sequenceName = "THAU_THO_KH_SEQ", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "THAU_THO_KH_SEQ")
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TEN")
	private String ten;

	@Column(name = "DIA_CHI")
	private String diaChi;
	
	@Column(name = "DIA_CHI_THI_CONG")
	private String diaChiThiCong;
	
	@Column(name = "SDT")
	private String sdt;

	@Column(name = "NGAY_NHAP")
	private Date ngayNhap;

	@Column(name = "NGAY_TANG_QUA")
	private Date ngayTangQua;

	@Column(name = "QUA")
	private String qua;

	@Column(name = "NGAY_TANG_HH")
	private Date ngayTangHH;

	@Column(name = "TIEN_HH")
	private Long tienHoahong;

	@Column(name = "NGAY_TANG_QUA_CN")
	private Date ngayTangQuaCN;

	@Column(name = "QUA_CN")
	private String quaCN;

	@Column(name = "NHOM_THAU_THO")
	private String nhomThauTho;
	
	@Column(name = "NGUOI_NHAP")
	private String nguoiNhap;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public Date getNgayNhap() {
		return ngayNhap;
	}

	public void setNgayNhap(Date ngayNhap) {
		this.ngayNhap = ngayNhap;
	}

	public Date getNgayTangQua() {
		return ngayTangQua;
	}

	public void setNgayTangQua(Date ngayTangQua) {
		this.ngayTangQua = ngayTangQua;
	}

	public String getQua() {
		return qua;
	}

	public void setQua(String qua) {
		this.qua = qua;
	}

	public Date getNgayTangHH() {
		return ngayTangHH;
	}

	public void setNgayTangHH(Date ngayTangHH) {
		this.ngayTangHH = ngayTangHH;
	}

	public Long getTienHoahong() {
		return tienHoahong;
	}

	public void setTienHoahong(Long tienHoahong) {
		this.tienHoahong = tienHoahong;
	}

	public Date getNgayTangQuaCN() {
		return ngayTangQuaCN;
	}

	public void setNgayTangQuaCN(Date ngayTangQuaCN) {
		this.ngayTangQuaCN = ngayTangQuaCN;
	}

	public String getQuaCN() {
		return quaCN;
	}

	public void setQuaCN(String quaCN) {
		this.quaCN = quaCN;
	}

	public String getNhomThauTho() {
		return nhomThauTho;
	}

	public void setNhomThauTho(String nhomThauTho) {
		this.nhomThauTho = nhomThauTho;
	}

	public String getNguoiNhap() {
		return nguoiNhap;
	}

	public void setNguoiNhap(String nguoiNhap) {
		this.nguoiNhap = nguoiNhap;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDiaChiThiCong() {
		return diaChiThiCong;
	}

	public void setDiaChiThiCong(String diaChiThiCong) {
		this.diaChiThiCong = diaChiThiCong;
	}

	

	
}