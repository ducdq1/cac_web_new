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

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "STATUS")
	private int status;

	@Column(name = "BUY_DATE")
	private Date buyDate;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(targetEntity = Street.class)
	@JoinColumn(name = "STREET_ID", referencedColumnName = "STREET_ID")
	private Street street;

	@Column(name = "NAME_SEARCH")
	private String nameSearch;

	@Column(name = "ASSIGN_NAME")
	private String assignName;
	
	@Column(name = "SURVEY_NAME")
	private String surveyName;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "NUM_CALL")
	private String numCall;

	@Column(name = "IS_BUY")
	private Integer isBuy;

	@Column(name = "PROCESS")
	private String process;
	
	@Column(name = "CONTENT")
	private String content;

	
	@Column(name = "REASION")
	private String reasion;//ly do khong lay hang 
	
	@Transient
	private String fullAdd;

	@Transient
	private String add;

	@Transient
	private String trangThai;

	@Transient
	private String layHang;

	@Transient
	private String kv;

	@Transient
	private String buyStr;

	@Transient
	private String dlh;
	
	@Transient
	private String ngaynhap;
	
	@Transient
	private String ngt;
	@Transient
	private String nks;
	
	@Transient
	private String a,b,c,d,qt;
	
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNameSearch() {
		return nameSearch;
	}

	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	public String getFullAdd() {
		fullAdd = address + " ";
		if (street != null) {
			fullAdd += street.getStreetName();

			if (street.getArea() != null) {
				fullAdd += ", " + street.getArea().getAreaName();
			}
		}

		return fullAdd;
	}

	public void setFullAdd(String fullAdd) {
		this.fullAdd = fullAdd;
	}

	public String getTrangThai() {

		switch (status) {
		case 0:
			trangThai = getLabel("sai_sdt");
			break;
		case 1:
			trangThai = getLabel("sai_dia_chi");
			break;
		case 2:
			trangThai = getLabel("xong_roi");
			break;
		case 3:
			trangThai = getLabel("da_qt");
			break;
		default:
			trangThai = "Đã xóa";
			break;
		}
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getNumCall() {
		return numCall;
	}

	public void setNumCall(String numCall) {
		this.numCall = numCall;
	}

	public Integer getIsBuy() {
		return isBuy;
	}

	public void setIsBuy(Integer isBuy) {
		this.isBuy = isBuy;
	}

	public String getLayHang() {
		if (isBuy != null) {
			if (isBuy == 1) {
				layHang = getLabel("da_lay_hang");
			} else if (isBuy == 0) {
				layHang = getLabel("chua_lay_hang");
			} else if (isBuy == 2) {
				layHang = getLabel("ko_lay_hang");
			}
		}
		return layHang;
	}

	public void setLayHang(String layHang) {
		this.layHang = layHang;
	}

	public String getDaLayHang() {
		if (isBuy != null && isBuy == 1) {
			dlh = "X";
		} else {
			dlh = "";
		}
		return dlh;
	}

	 


	public String getDlh() {
		if (isBuy != null && isBuy == 1) {
			dlh = "X";
		} else {
			dlh = "";
		}
		return dlh;
	}

	public void setDlh(String dlh) {
		this.dlh = dlh;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getKv() {
		Area area =street.getArea();
		kv="";
		if (area != null) {
			kv = area.getAreaName();
		}
		return kv;
	}

	public void setKv(String kv) {
		this.kv = kv;
	}

	public String getBuyStr() {
		buyStr = DateTimeUtils.convertDateToStringFormat(buyDate, "dd/MM/yyyy");
	   return buyStr;
	}

	public void setBuyStr(String buyStr) {
		this.buyStr = buyStr;
	}

	public void setData() {
		getBuyStr();
		getKv();
		getAdd();
		getFullAdd();
		getDlh();
		getTrangThai();
		getNgaynhap();
		getNgt();
		getA();
		getB();
		getQt();
		getNvks();
	}

	public String getAdd() {
		add = address;
		if (street != null) {
			add += " " + street.getStreetName();

		}

		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getNgaynhap() {
		ngaynhap = DateTimeUtils.convertDateToStringFormat(createDate, "dd/MM/yyyy");
		return ngaynhap;
	}

	public void setNgaynhap(String ngaynhap) {
		this.ngaynhap = ngaynhap;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNgt() {
		ngt=assignName;
		return ngt;
	}
	
	public String getNvks() {
		nks=surveyName;
		return ngt;
	}
	

	public void setNgt(String ngt) {
		this.ngt = ngt;
	}

	public String getReasion() {
		return reasion;
	}

	public void setReasion(String reasion) {
		this.reasion = reasion;
	}

	public String getA() {
		if(reasion!=null && (reasion.contains("giacao")||reasion.contains("layhangnguoiquen"))){
			a="X";
		}
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		if(reasion!=null && (reasion.contains("thaidopv")||reasion.contains("khongthichmau") )){
			b="X";
		}
		
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		if(reasion!=null && reasion.contains("layhangnguoiquen")){
			c="X";
		}
		
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		if(reasion!=null && reasion.contains("khongthichmau")){
			d="X";
		}
		
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getQt(){   
		if(status == 3){
			qt="X";
		}
		return qt;
	}

	public void setQt(String qt) {
		this.qt = qt;
	}
	
	
	
}
