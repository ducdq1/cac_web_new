package com.viettel.ws;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.viettel.core.user.BO.Users;
import com.viettel.module.phamarcy.BO.Workers;
import com.viettel.module.phamarcy.DAO.PhaMedicine.WorkerDao;
import com.viettel.ws.bo.WorkerLoginRequest;

@Path("workers")
public class WorkerService {

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse login(final WorkerLoginRequest loginRequest) {
		LoginResponse resp = new LoginResponse();
		Workers worker = new WorkerDao().getWorkerByPhone(loginRequest.getPhone());
		
		try {

			if (worker != null) {
				Users user = new Users();
				user.setTelephone(worker.getPhone());
				user.setFullName(worker.getName());
				user.setUserId(worker.getId());
				resp.setUser(user);
			} else {
				Workers newWorker = new Workers();
				newWorker.setName(loginRequest.getName());
				newWorker.setPhone(loginRequest.getPhone());
				newWorker.setIsActive(1L);
				new WorkerDao().saveOrUpdate(newWorker);
				Users user = new Users();
				user.setUserId(newWorker.getId());
				user.setTelephone(newWorker.getPhone());
				user.setFullName(newWorker.getName());
				resp.setUser(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public WorkersResponse searchProduct(final WorkerLoginRequest req) {
		List<com.viettel.module.phamarcy.BO.Workers> lstWorkers = new WorkerDao().findCustomers(req.getPhone(),req.getName()).getLstReturn();
		@SuppressWarnings("rawtypes")
		WorkersResponse productResponse = new WorkersResponse();

		productResponse.setDatas(lstWorkers);
		return productResponse;
	}
	 
}
