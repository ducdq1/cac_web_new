package com.viettel.ws;

import java.util.List;

import com.viettel.ws.bo.HangHoaBO;

public class ProductResponse {
 
	 
		List<com.viettel.module.phamarcy.BO.Product> lstProduct;
		HangHoaBO tonKho;
		
		public HangHoaBO getTonKho() {
			return tonKho;
		}

		public void setTonKho(HangHoaBO tonKho) {
			this.tonKho = tonKho;
		}

		public List<com.viettel.module.phamarcy.BO.Product> getLstProduct() {
			return lstProduct;
		}

		public void setLstProduct(List<com.viettel.module.phamarcy.BO.Product> lstProduct) {
			this.lstProduct = lstProduct;
		}
		
	 
	
}