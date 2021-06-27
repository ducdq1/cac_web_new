package com.viettel.ws;

import java.util.List;

import com.viettel.module.phamarcy.BO.ProductCategory;
import com.viettel.module.phamarcy.BO.Workers;

public class ProductCategoryResponse {

	List<ProductCategory> datas;

	public List<ProductCategory> getDatas() {
		return datas;
	}

	public void setDatas(List<ProductCategory> datas) {
		this.datas = datas;
	}

}