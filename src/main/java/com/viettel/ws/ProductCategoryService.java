package com.viettel.ws;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.viettel.module.phamarcy.BO.ProductCategory;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductCategoryDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.WorkerDao;
import com.viettel.ws.bo.WorkerLoginRequest;

@Path("productCategory")
public class ProductCategoryService {


	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductCategoryResponse searchProduct(final WorkerLoginRequest req) {
		List<com.viettel.module.phamarcy.BO.ProductCategory> lstWorkers = new ProductCategoryDao().findProductCategorys(req.getName()).getLstReturn();
		ProductCategoryResponse productResponse = new ProductCategoryResponse();

		productResponse.setDatas(lstWorkers);
		return productResponse;
	}
	 
}
