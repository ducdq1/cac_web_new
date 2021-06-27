package com.viettel.ws.bo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;

@XmlRootElement
public class CreateQuotationBO {
	@XmlElement
	public Quotation quotation;
	@XmlElement
	public List<QuotationDetail> lstQuotationDetail;

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
	
	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}
 
	public List<QuotationDetail> getLstQuotationDetail() {
		return lstQuotationDetail;
	}

	public void setLstQuotationDetail(List<QuotationDetail> lstQuotationDetail) {
		this.lstQuotationDetail = lstQuotationDetail;
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

}
