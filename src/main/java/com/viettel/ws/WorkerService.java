package com.viettel.ws;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.zkoss.json.JSONObject;

import com.viettel.core.user.BO.Users;
import com.viettel.module.phamarcy.BO.Workers;
import com.viettel.module.phamarcy.DAO.PhaMedicine.WorkerDao;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.ws.bo.UpdateWorkerProcessorRequest;
import com.viettel.ws.bo.WorkerLoginRequest;

@Path("workers")
public class WorkerService {

	@GET
	@Path("/updateLastLogin")

	@Produces(MediaType.APPLICATION_JSON)
	public String updateLastLogin(@QueryParam(value = "workerId") String workerId) {

		if (workerId != null) {
			Workers worker = new WorkerDao().getWorkerByPhone(workerId);
			if (worker != null) {
				worker.setLastLogin(new Date());
				new WorkerDao().saveOrUpdate(worker);
			}
		}
		
		JSONObject json = new JSONObject();
		json.put("isLoginRequired",Boolean.valueOf(ResourceBundleUtil.getString("is_login_required")));
		
		return json.toJSONString();

	}

	@POST
	@Path("/updateProcessor")
	@Produces(MediaType.APPLICATION_JSON)
	public Workers updateProcessor(final UpdateWorkerProcessorRequest loginRequest) {
		Workers worker = new WorkerDao().getWorkerByPhone(loginRequest.getWorkerId());
		if (worker != null && worker.getProcessor() == null) {
			worker.setProcessor(loginRequest.getProcessor());
			new WorkerDao().saveOrUpdate(worker);
		}

		return worker;
	}

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
				user.setFullName(loginRequest.getName());
				user.setUserId(worker.getId());
				user.setUserName(worker.getPhone());
				user.setCusGroup(worker.getCusGroup());
				user.setBusinessName(worker.getProcessor());
				resp.setUser(user);

				if (worker.getInviterName() == null) {
					worker.setInviterName(loginRequest.getInviterName());
				}

				worker.setName(loginRequest.getName());
				worker.setLastLogin(new Date());
				worker.setCusGroup(loginRequest.getCusGroup());

				new WorkerDao().saveOrUpdate(worker);

			} else {
				Workers newWorker = new Workers();
				newWorker.setName(loginRequest.getName());
				newWorker.setPhone(loginRequest.getPhone());
				newWorker.setIsActive(1L);
				newWorker.setLastLogin(new Date());
				newWorker.setInviterName(loginRequest.getInviterName());
				newWorker.setCreateDate(new Date());
				newWorker.setCusGroup(loginRequest.getCusGroup());
				new WorkerDao().saveOrUpdate(newWorker);
				Users user = new Users();
				user.setUserId(newWorker.getId());
				user.setTelephone(newWorker.getPhone());
				user.setFullName(newWorker.getName());
				user.setUserName(newWorker.getPhone());
				user.setCusGroup(newWorker.getCusGroup());
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
		List<com.viettel.module.phamarcy.BO.Workers> lstWorkers = new WorkerDao().findCustomers(req.getPhone(), 1, -1)
				.getLstReturn();
		@SuppressWarnings("rawtypes")
		WorkersResponse productResponse = new WorkersResponse();

		productResponse.setDatas(lstWorkers);
		return productResponse;
	}

}
