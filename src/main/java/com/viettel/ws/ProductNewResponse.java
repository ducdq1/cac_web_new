package com.viettel.ws;

import java.util.List;

import com.viettel.ws.bo.HangHoaBO;

public class ProductNewResponse {
 
		List<com.viettel.module.phamarcy.BO.ProductNew> lstProduct;
		HangHoaBO tonKho;
		public List<com.viettel.module.phamarcy.BO.ProductNew> getLstProduct() {
			return lstProduct;
		}
		public void setLstProduct(List<com.viettel.module.phamarcy.BO.ProductNew> lstProduct) {
			this.lstProduct = lstProduct;
		}
		public HangHoaBO getTonKho() {
			return tonKho;
		}
		public void setTonKho(HangHoaBO tonKho) {
			this.tonKho = tonKho;
		}
		
		 
}