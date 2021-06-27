package com.viettel.ws;

import java.util.List;

import com.viettel.module.phamarcy.BO.Promotion;
import com.viettel.module.phamarcy.BO.Workers;

public class PromotionsResponse {

	List<Promotion> datas;

	public List<Promotion> getDatas() {
		return datas;
	}

	public void setDatas(List<Promotion> datas) {
		this.datas = datas;
	}

}