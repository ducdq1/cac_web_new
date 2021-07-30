package com.viettel.ws.bo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchProductBO {
	    @XmlElement public String productCode;
	    @XmlElement public String productName;
	    @XmlElement public String userName;
	    @XmlElement
		public int offset = 0;
		@XmlElement
		public int limit = 10;
		@XmlElement
		public Integer type;
		@XmlElement 
		public Boolean isAgent;
		@XmlElement
		public String code;		
		@XmlElement
		public Integer searchType = 0 ;
		
		
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
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
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Boolean getIsAgent() {
			return isAgent;
		}
		public void setIsAgent(Boolean isAgent) {
			this.isAgent = isAgent;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public Integer getSearchType() {
			return searchType;
		}
		public void setSearchType(Integer searchType) {
			this.searchType = searchType;
		}
		
		
}
