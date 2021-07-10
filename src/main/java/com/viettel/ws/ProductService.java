package com.viettel.ws;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.Statistics;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportExcell;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.StatisticsDao;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.ws.bo.CreateQuotationBO;
import com.viettel.ws.bo.HangHoaBO;
import com.viettel.ws.bo.LoginRequest;
import com.viettel.ws.bo.NotificationBO;
import com.viettel.ws.bo.NotificationBO.Notification;
import com.viettel.ws.bo.SearchProductBO;
import com.viettel.ws.bo.SearchQuotationBO;

@Path("product")
public class ProductService {

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse login(final LoginRequest loginRequest) {
		LoginResponse resp = new LoginResponse();
		UserDAOHE udhe = new UserDAOHE();
		String strLogin = udhe.checkLogin(loginRequest.getUserName(), loginRequest.getPw());
		Users user = udhe.getUserByName(loginRequest.getUserName());
		try {

			if (strLogin.isEmpty() && user != null) {
				resp.setUser(user);
			} else {
				resp.setMessage("Tên đăng nhập hoặc mật khẩu không đúng");
				resp.setStatusCode(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@GET
	@Path("/getAllProducts")
	@Produces(MediaType.APPLICATION_JSON)
	public List<com.viettel.module.phamarcy.BO.Product> getProducts() {
		List<com.viettel.module.phamarcy.BO.Product> lstProduct = new ArrayList<>();
		lstProduct = new ProductDao().getAllProduct();
		return lstProduct;
	}

	@POST
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductResponse searchProduct(final SearchProductBO searchProductBO) {
		List<com.viettel.module.phamarcy.BO.Product> lstProduct = new ArrayList<>();
		lstProduct = new ProductDao().getProductByCode(searchProductBO);
		ProductResponse productResponse = new ProductResponse();

		Long productId = null;
		String maVT = "";
		if (lstProduct.size() > 0) {
			for (com.viettel.module.phamarcy.BO.Product p : lstProduct) {
				productId = p.getProductId();
				maVT = p.getMaHangHoa();
				List<Attachs> atts = new AttachDAOHE().findByObjectId(p.getProductId());
				p.setImages(atts);
			}
		}

		productResponse.setLstProduct(lstProduct);
		if (productId != null) {
			try {
				Statistics item = new Statistics();
				item.setCreateDate(new Date());
				item.setProductId(productId);
				item.setUserName(searchProductBO.getUserName());
				new StatisticsDao().saveOrUpdate(item);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		HangHoaBO tonKho = layThongTinTonKho(maVT);
		if (tonKho != null) {
			productResponse.setTonKho(tonKho);
		}
		return productResponse;
	}
	
	@POST
	@Path("/search-product")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductResponse findProduct(final SearchProductBO searchProductBO) {
		List<com.viettel.module.phamarcy.BO.Product> lstProduct = new ArrayList<>();
		lstProduct = new ProductDao().searchProductByCode(searchProductBO,searchProductBO.getOffset() * searchProductBO.getLimit(),searchProductBO.getLimit());
		ProductResponse productResponse = new ProductResponse();

		productResponse.setLstProduct(lstProduct);

		return productResponse;
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/quotations")
	@Produces(MediaType.APPLICATION_JSON)
	public ListQuotationResponse getQuotations(SearchQuotationBO searchBO) {

		List<Quotation> quotations = null;
		ListQuotationResponse quotationResponse = new ListQuotationResponse();
		quotationResponse.setStatusCode(200);
		quotationResponse.setMessage("success");
		try {
			PhamarcyFileModel model = new PhamarcyFileModel();
			model.setUserName(searchBO.getUserName());
			model.setTrangThai(searchBO.getStatus());
			model.setSearchText(searchBO.getSearch());
			model.setApproveAble(searchBO.getIsApproveAble()==null? false: searchBO.getIsApproveAble());
			model.setSaled(searchBO.getIsSaled() == null ? false : searchBO.getIsSaled());
			PagingListModel result = new QuotationDao().findFilesByReceiverAndDeptId(model, searchBO.getOffset() * searchBO.getLimit(),
					searchBO.getLimit());

			quotations = result.getLstReturn();

		} catch (Exception e) {
			e.printStackTrace();
			quotationResponse.setStatusCode(1000);
			quotationResponse.setMessage(e.getMessage());
		}

		quotationResponse.setListData(quotations);

		return quotationResponse;
	}

	@POST
	@Path("/quotation/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListQuotationDetailResponse getQuotations(@PathParam("id") Long id) {
		List<QuotationDetail> quotationDetails = null;
		ListQuotationDetailResponse quotationDetailResponse = new ListQuotationDetailResponse();
		try {
			quotationDetails = new QuotationDetailDao().getListQuotationDetail(id);
			quotationDetailResponse.setListData(quotationDetails);
		} catch (Exception e) {
			e.printStackTrace();
			quotationDetailResponse.setStatusCode(1000);
			quotationDetailResponse.setMessage(e.getMessage());
		}

		return quotationDetailResponse;
	}
	
	@POST
	@Path("/quotation/update-saledate")
	@Produces(MediaType.APPLICATION_JSON)
	public QuotationResponse updateSaleDate(CreateQuotationBO createQuotationBO) {
		QuotationResponse quotationResponse = new QuotationResponse();
		try {
			Quotation quotationUpdate = new QuotationDao().findById(createQuotationBO.getQuotation().getQuotationID());
			if (quotationUpdate != null) {
				if (quotationUpdate.getStatus() != Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA.intValue()) {
					quotationResponse.setMessage("Báo giá chưa được duyệt. Không thể cập nhật");
					quotationResponse.setStatusCode(1000);
					return quotationResponse;
				}
				
				if(createQuotationBO.getQuotationDate() !=null){
					try{
						Date saledDate = DateTimeUtils.convertStringToDate(createQuotationBO.getQuotationDate(), "dd/MM/yyyy");
						if(saledDate !=null){
							quotationUpdate.setSaledDate(saledDate);
							new QuotationDao().saveOrUpdate(quotationUpdate);
						}else{
							throw new Exception("Bạn chưa nhập Ngày bán");
						}
					}catch(Exception e){
						quotationResponse.setMessage("Bạn chưa nhập Ngày bán");
						quotationResponse.setStatusCode(1000);
						return quotationResponse;
					}
				}
				
				if(createQuotationBO.getIsInvalid()!=null && createQuotationBO.getIsInvalid()){
					quotationUpdate.setIsInvalid(1L);
					new QuotationDao().saveOrUpdate(quotationUpdate);
					quotationResponse.setMessage("Cập nhật hiệu lực thành công");
				}
				
				if(createQuotationBO.getQuotation().getNote() != null ){
					quotationUpdate.setNote(createQuotationBO.getQuotation().getNote());
					new QuotationDao().saveOrUpdate(quotationUpdate);
					quotationResponse.setMessage("Cập nhật tiến độ thành công");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			quotationResponse.setStatusCode(1000);
			quotationResponse.setMessage(e.getMessage());
		}

		return quotationResponse;
	}
	

	@POST
	@Path("/quotation/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ListQuotationDetailResponse deleteQuotation(@PathParam("id") Long id) {
		ListQuotationDetailResponse quotationDetailResponse = new ListQuotationDetailResponse();
		try {
			QuotationDao quotationDao = new QuotationDao();
			QuotationDetailDao detailDao = new QuotationDetailDao();
			Quotation quotationUpdate = new QuotationDao().findById(id);
			if (quotationUpdate != null && quotationUpdate.getStatus() == Constants.BAO_GIA_STATUS_TAO_MOI.intValue()) {
				quotationDao.delete(quotationUpdate);
				List<QuotationDetail> lstQuotationDetail = new QuotationDetailDao().getListQuotationDetail(id);
				for (QuotationDetail quotationDetail : lstQuotationDetail) {
					detailDao.delete(quotationDetail);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			quotationDetailResponse.setStatusCode(1000);
			quotationDetailResponse.setMessage(e.getMessage());
		}

		return quotationDetailResponse;
	}

	@POST
	@Path("/quotation")
	@Produces(MediaType.APPLICATION_JSON)
	public QuotationResponse createQuotation(final CreateQuotationBO createQuotationBO) {
		Long quotationId = null;
		QuotationResponse quotationResponse = new QuotationResponse();
		try {
			if (createQuotationBO == null) {
				return null;
			}

			// duyet bao gia
			if (createQuotationBO.getIsApproveAble() != null && createQuotationBO.getIsApproveAble() == true) {
				
				return approveQuotation(createQuotationBO);
			}

			Quotation quotationUpdate = null;

			Quotation quotationRequest = createQuotationBO.getQuotation();
			if (quotationRequest == null) {
				return null;
			}

			boolean isUpdate = false;
			if (quotationRequest.getQuotationID() != null) {
				quotationUpdate = new QuotationDao().findById(quotationRequest.getQuotationID());
				if (quotationUpdate != null) {
					int status = quotationUpdate.getStatus();
					if (status == Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA.intValue()) {
						quotationResponse.setMessage("Báo giá đã xử lý. Không thể cập nhật");
						quotationResponse.setStatusCode(1000);
						return quotationResponse;
					}
					isUpdate = true;
				}
				
			}

			Quotation quotation = getQuotation(quotationRequest, quotationUpdate);

			quotation.setCreateDate(new Date());
			quotation.setModifyDate(new Date());

			new QuotationDao().saveOrUpdate(quotation);
			quotationId = quotation.getQuotationID(); 
			
			List<QuotationDetail> lstQuotationDetailCurrent =  new QuotationDetailDao().getListQuotationDetail(quotationId);
			
			for (QuotationDetail obj : lstQuotationDetailCurrent) {
				Long id = obj.getQuotationDetailId();
				boolean exist = false;
				for (QuotationDetail detail : createQuotationBO.getLstQuotationDetail()) {
					if(detail.getQuotationDetailId() !=null &&
							detail.getQuotationDetailId().intValue() == id.intValue()){
						exist = true;
						break;
					}
				}
				
				if(!exist){
					new QuotationDetailDao().delete(obj);
				}
			}
			
			List<QuotationDetail> lstQuotationDetail = createQuotationBO.getLstQuotationDetail();
			for (QuotationDetail obj : lstQuotationDetail) {
				if(obj.getQuotationDetailId() == null){
					obj.setQuotationId(quotationId);
					new QuotationDetailDao().saveOrUpdate(obj);
				}else{
					QuotationDetail update = new QuotationDetailDao().findById(obj.getQuotationDetailId());
					if(update !=null){
						update.setAttachId(obj.getAttachId());
						update.setNote(obj.getNote());
						update.setAmount(obj.getAmount());
						new QuotationDetailDao().saveOrUpdate(update);
					}
				}
			}

			quotationResponse.setQuotationId(quotationId);
			if(isUpdate){
				sendNotification("create","Cập nhật báo giá", String.format("%s vừa cập nhật báo giá cho khách hàng %s", createQuotationBO.getQuotation().getCreateUserFullName().toUpperCase(),createQuotationBO.getQuotation().getCusName()));
			}else{
				sendNotification("create","Có báo giá mới", String.format("%s vừa tạo một báo giá mới cho khách hàng %s", createQuotationBO.getQuotation().getCreateUserFullName().toUpperCase(),createQuotationBO.getQuotation().getCusName()));
			}
			
			System.out.println("Return response");
				
		} catch (Exception e) {
			e.printStackTrace();
			quotationResponse.setMessage(e.getMessage());
			quotationResponse.setStatusCode(1000);
		} 
				
		return quotationResponse;
	}

	public static void sendNotification(final String topic,final String title, final String bodyStr){
		
		KThreadPoolExecutor.executeAccessLog((new Runnable() {
            @Override
            public void run() {
            	System.out.println("SendNotification");
            	NotificationBO body =new NotificationBO();
        		body.setTo("/topics/"+topic);
        		Notification notification = body.new Notification();

        		notification.setTitle(title);
        		notification.setBody(bodyStr);

        		body.setNotification(notification);
            	pushNotification(body);
            }
        }));
		 
	}
	private QuotationResponse approveQuotation(CreateQuotationBO createQuotationBO) {
		QuotationResponse quotationResponse = new QuotationResponse();
		quotationResponse.setStatusCode(1000);
		try {
			int status = createQuotationBO.getQuotation().getStatus();
			Quotation quotationBO = createQuotationBO.getQuotation();
			
			if (quotationBO == null || quotationBO.getQuotationID() == null ) {
				quotationResponse.setMessage("Không tìm thấy báo giá");
				return quotationResponse;
			}
			
			if (status == Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA.intValue()) {
				quotationResponse.setMessage("Báo giá đã xử lý. Không thể cập nhật");
				return quotationResponse;
			}

			BigDecimal totalPrice = new BigDecimal(0);
			for (QuotationDetail quotationDetail : createQuotationBO.getLstQuotationDetail()) {
				if (quotationDetail != null) {
					final Double amount = quotationDetail.getAmount() == null ? 0d : quotationDetail.getAmount();

					try {
						Long value = quotationDetail.getPrice();
						if (value == null || value.compareTo(0L) < 0) {
							quotationResponse
									.setMessage("Giá bán của sản phẩm " + quotationDetail.getProductCode() + "  không hợp lệ ");
							return quotationResponse;
						}

						quotationDetail.setPrice(value.longValue());
						totalPrice = totalPrice.add(new BigDecimal(amount * value));
					} catch (Exception e) {
						quotationResponse.setMessage("Giá bán của sản phẩm " + quotationDetail.getProductCode() + "  không hợp lệ ");
						return quotationResponse;
					}
				}
			}

			if (createQuotationBO.getExpiredDate() == null) {
				quotationResponse.setMessage("Bạn chưa nhập Ngày hết hạn");
				return quotationResponse;
			}
			Date expiredDate = null ;
			try{
				expiredDate = DateTimeUtils.convertStringToDate(createQuotationBO.getExpiredDate(), "dd/MM/yyyy");
				
			}catch(Exception e){
				quotationResponse.setMessage("Bạn chưa nhập Ngày hết hạn");
				return quotationResponse;
			}
			
			//QuotationDetailDao dao = new QuotationDetailDao();
			for (QuotationDetail quotationDetail : createQuotationBO.getLstQuotationDetail()) {				
				if (quotationDetail.getQuotationDetailId() != null ) {
					//System.out.println("chi tiet bao gia Id "+quotationDetail.getQuotationDetailId());
					QuotationDetail update = new QuotationDetailDao().findById(quotationDetail.getQuotationDetailId());
					update.setPrice(quotationDetail.getPrice());
					new QuotationDetailDao().saveOrUpdate(update);
				}
			}

			Quotation quotationUpdate = new QuotationDao().findById(quotationBO.getQuotationID());
			if (!createQuotationBO.getIsPreViewApprove()) {
				if(quotationUpdate.getQuotationNumber() == null || quotationUpdate.getQuotationNumber().isEmpty()){
					quotationUpdate.setQuotationNumber(new QuotationDao().getAutoPhaFileCode());
				}
				quotationUpdate.setStatus(Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA.intValue());
			}
			
			quotationUpdate.setQuotationDate(expiredDate);
			quotationUpdate.setModifyDate(new Date());
			quotationUpdate.setTotalPrice(totalPrice);
			
			List<QuotationDetail> listQuotationDetail = new QuotationDetailDao().getListQuotationDetail(quotationUpdate.getQuotationID());
			String filePath = printBaoGia(listQuotationDetail, quotationUpdate, createQuotationBO.getIsPreViewApprove());
			
			if(filePath == null){
				quotationResponse.setMessage("Không in được báo giá");
				return quotationResponse;
			} 
			
			new QuotationDao().saveOrUpdate(quotationUpdate);
			quotationResponse.setMessage(quotationUpdate.getFileName());
			quotationResponse.setStatusCode(200);
			
			if(createQuotationBO.getIsPreViewApprove()!=null
					&& !createQuotationBO.getIsPreViewApprove()){
				sendNotification(quotationUpdate.getCreateUserCode(),"Báo giá đã được duyệt: "+ quotationUpdate.getQuotationNumber(), String.format("Báo giá cho KH %s của bạn vừa được phê duyệt ", quotationUpdate.getCusName()));
			}
			return quotationResponse;
		} catch (Exception e) {
			quotationResponse.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return quotationResponse;
	}

	private String printBaoGia(List<QuotationDetail> lstQuotationDetail, Quotation quotation, boolean isPreview) {
		String filePath = new ExportExcell().exportBaoGia(lstQuotationDetail, quotation, isPreview, true);
		if (filePath != null) {
			return filePath;
		} else {
			return "Có lỗi xãy ra";
		}
	}

	private Quotation getQuotation(Quotation input, Quotation update) {
		if (update != null) {
			update.setCusAddress(input.getCusAddress());
			update.setCusPhone(input.getCusPhone());
			update.setCusName(input.getCusName());
			update.setType(input.getType());
			return update;
		} else {
			return input;
		}
	}

	public static HangHoaBO layThongTinTonKho(String maVT) {

		if (maVT == null || maVT.isEmpty()) {
			return null;
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			String url = ResourceBundleUtil.getString("pm_kho_url");
			HttpGet request = new HttpGet(url + URLEncoder.encode(maVT, StandardCharsets.UTF_8.toString()));

			response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200 && entity != null) {

				String result = EntityUtils.toString(entity);
				HangHoaBO tonKho = new Gson().fromJson(result, HangHoaBO.class);
				return tonKho;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static HangHoaBO pushNotification(NotificationBO body) { 

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			String url = "https://fcm.googleapis.com/fcm/send";
			HttpPost request = new HttpPost(url);
			request.setHeader("Authorization",
					"key=AAAAqfPZ1sQ:APA91bECNLIxhv2ZFNpVrNA_x33P7bK1el3jQBe3KbImmFjFxwRcM9vCsL7x6pf4Xx4rU0Nhi549sIAvsAtDS5ozHQRcZwlbT-nP-mCU1-vQbgihMXFydGMJxoLzzlGkPxotL3bm1nbY");
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept-Encoding", "UTF-8");
			
			request.setEntity(new StringEntity(new Gson().toJson(body),"UTF-8"));
			System.out.println("FireBase request: " + new Gson().toJson(body));
			response = httpClient.execute(request);
			System.out.println("FireBase response: "+ response.getStatusLine().getStatusCode());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		//pushNotification("BG001");
	}
	
	

}
