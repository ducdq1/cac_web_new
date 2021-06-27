package com.viettel.ws.bo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchQuotationBO {
	@XmlElement
	public int status;
	@XmlElement
	public int offset = 0;
	@XmlElement
	public int limit = 10;
	@XmlElement
	public String userName;
	@XmlElement
	public String search;
	@XmlElement
	public Boolean isApproveAble;
	
	@XmlElement
	public Boolean isSaled;//da ban
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	} 
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
 

	public Boolean getIsApproveAble() {
		return isApproveAble;
	}

	public void setIsApproveAble(Boolean isApproveAble) {
		this.isApproveAble = isApproveAble;
	}

	public Boolean getIsSaled() {
		return isSaled;
	}

	public void setIsSaled(Boolean isSaled) {
		this.isSaled = isSaled;
	}
 
}
