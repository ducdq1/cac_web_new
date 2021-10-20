package com.viettel.ws.bo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.module.phamarcy.BO.CKBaoGia;
import com.viettel.module.phamarcy.BO.CKBaoGiaDetail;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;

@XmlRootElement
public class CreateCKBaoGiaBO {
	@XmlElement
	public CKBaoGia ckBaoGia;
	@XmlElement
	public List<CKBaoGiaDetail> ckBaoGiaDetail;

	@XmlElement
	public String quotationDate;
	
	@XmlElement
	public String expiredDate;
	
	@XmlElement
	public String userName;
	
	@XmlElement
	public Boolean isApproveAble;
	
	@XmlElement
	public Boolean isPreViewApprove;
	
	@XmlElement
	public Boolean isInvalid;
	

	public CKBaoGia getCkBaoGia() {
		return ckBaoGia;
	}

	public void setCkBaoGia(CKBaoGia ckBaoGia) {
		this.ckBaoGia = ckBaoGia;
	}

	public List<CKBaoGiaDetail> getCkBaoGiaDetail() {
		return ckBaoGiaDetail;
	}

	public void setCkBaoGiaDetail(List<CKBaoGiaDetail> ckBaoGiaDetail) {
		this.ckBaoGiaDetail = ckBaoGiaDetail;
	}

	public String getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getIsApproveAble() {
		return isApproveAble;
	}

	public void setIsApproveAble(Boolean isApproveAble) {
		this.isApproveAble = isApproveAble;
	}

	public Boolean getIsPreViewApprove() {
		return isPreViewApprove;
	}

	public void setIsPreViewApprove(Boolean isPreViewApprove) {
		this.isPreViewApprove = isPreViewApprove;
	}

	public Boolean getIsInvalid() {
		return isInvalid;
	}

	public void setIsInvalid(Boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

}
