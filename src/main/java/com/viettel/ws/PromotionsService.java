package com.viettel.ws;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.viettel.module.phamarcy.DAO.PhaMedicine.PromotionDao;
import com.viettel.ws.bo.WorkerLoginRequest;

@Path("promotions")
public class PromotionsService {


	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public PromotionsResponse searchProduct(final WorkerLoginRequest req) {
		List<com.viettel.module.phamarcy.BO.Promotion> lstWorkers = new PromotionDao().findPromotions(req.getName()).getLstReturn();
		PromotionsResponse productResponse = new PromotionsResponse();

		productResponse.setDatas(lstWorkers);
		return productResponse;
	}
	 
}
