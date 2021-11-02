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
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.module.phamarcy.BO.CKBaoGia;
import com.viettel.module.phamarcy.BO.CKBaoGiaDetail;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.Statistics;
import com.viettel.module.phamarcy.DAO.PhaMedicine.CKBaoGiaDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.CKBaoGiaDetailDao;
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
import com.viettel.ws.bo.CreateCKBaoGiaBO;
import com.viettel.ws.bo.CreateQuotationBO;
import com.viettel.ws.bo.HangHoaBO;
import com.viettel.ws.bo.LoginRequest;
import com.viettel.ws.bo.NotificationBO;
import com.viettel.ws.bo.NotificationBO.Notification;
import com.viettel.ws.bo.SearchProductBO;
import com.viettel.ws.bo.SearchQuotationBO;

@Path("ckbg")
public class CKBaoGiaService {

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
			model.setApproveAble(searchBO.getIsApproveAble() == null ? false : searchBO.getIsApproveAble());
			model.setSaled(searchBO.getIsSaled() == null ? false : searchBO.getIsSaled());
			PagingListModel result = new QuotationDao().findFilesByReceiverAndDeptId(model,
					searchBO.getOffset() * searchBO.getLimit(), searchBO.getLimit());

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
	@Path("/{id}")
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
	@Path("/delete/{id}")
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
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public QuotationResponse createCKBaoGia(final CreateCKBaoGiaBO createQuotationBO) {
		Long quotationId = null;
		QuotationResponse quotationResponse = new QuotationResponse();
		try {
			if (createQuotationBO == null) {
				return null;
			}

			CKBaoGia quotationRequest = createQuotationBO.getCkBaoGia();

			if (quotationRequest == null) {
				return null;
			}

			CKBaoGia quotationUpdate = new CKBaoGiaDao().findById(quotationRequest.getCkId());
			
			boolean isUpdate = quotationUpdate != null;

			CKBaoGia quotation = getQuotation(quotationRequest, quotationUpdate);
			if (quotation.getCkNumber() == null || quotation.getCkNumber().isEmpty()) {
				quotation.setCkNumber(new CKBaoGiaDao().getAutoPhaFileCode());
			}
			
			
			quotation.setCreateDate(new Date());
			quotation.setModifyDate(new Date());

			new CKBaoGiaDao().saveOrUpdate(quotation);
			quotationId = quotation.getCkId();

			List<CKBaoGiaDetail> lstQuotationDetailCurrent = new CKBaoGiaDetailDao()
					.getListQuotationDetail(quotationId);

			for (CKBaoGiaDetail obj : lstQuotationDetailCurrent) {
				Long id = obj.getCkDetailId();
				boolean exist = false;
				for (CKBaoGiaDetail detail : createQuotationBO.getCkBaoGiaDetail()) {
					if (detail.getCkDetailId() != null && detail.getCkDetailId().intValue() == id.intValue()) {
						exist = true;
						break;
					}
				}

				if (!exist) {
					new CKBaoGiaDetailDao().delete(obj);
				}
			}

			List<CKBaoGiaDetail> lstQuotationDetail = createQuotationBO.getCkBaoGiaDetail();
			for (CKBaoGiaDetail obj : lstQuotationDetail) {
				if (obj.getCkbgId() == null) {
					obj.setCkbgId(quotationId);
					new CKBaoGiaDetailDao().saveOrUpdate(obj);
				} else {
					CKBaoGiaDetail update = new CKBaoGiaDetailDao().findById(obj.getCkDetailId());
					if (update != null) {
						update.setAmount(obj.getAmount());
						update.setPrice(obj.getPrice());
						update.setPickDate(obj.getPickDate());
						update.setDeposit(obj.getPrice()/2);
						new CKBaoGiaDetailDao().saveOrUpdate(update);
					}
				}
			}

			quotationResponse.setQuotationId(quotationId);
			if (isUpdate) {
				sendNotification("create", "Cập nhật cam kết đặt giữ hàng",
						String.format("%s vừa cập nhật cam kết đặt giữ hàng cho khách hàng %s",
								createQuotationBO.getCkBaoGia().getCreateUserFullName().toUpperCase(),
								createQuotationBO.getCkBaoGia().getCusName()));
			} else {
				sendNotification("create", "Cam kết đặt giữ hàng mới",
						String.format("%s vừa tạo một cam kết đặt giữ hàng mới cho khách hàng %s",
								createQuotationBO.getCkBaoGia().getCreateUserFullName().toUpperCase(),
								createQuotationBO.getCkBaoGia().getCusName()));
			}

			System.out.println("Return response");

		} catch (Exception e) {
			e.printStackTrace();
			quotationResponse.setMessage(e.getMessage());
			quotationResponse.setStatusCode(1000);
		}

		return quotationResponse;
	}

	public static void sendNotification(final String topic, final String title, final String bodyStr) {

		KThreadPoolExecutor.executeAccessLog((new Runnable() {
			@Override
			public void run() {
				System.out.println("SendNotification");
				NotificationBO body = new NotificationBO();
				body.setTo("/topics/" + topic);
				Notification notification = body.new Notification();

				notification.setTitle(title);
				notification.setBody(bodyStr);

				body.setNotification(notification);
				//ProductService.pushNotification(body);
			}
		}));

	}

	private QuotationResponse approveQuotation(CreateQuotationBO createQuotationBO) {
		QuotationResponse quotationResponse = new QuotationResponse();
		quotationResponse.setStatusCode(1000);
		try {
			int status = createQuotationBO.getQuotation().getStatus();
			Quotation quotationBO = createQuotationBO.getQuotation();

			if (quotationBO == null || quotationBO.getQuotationID() == null) {
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
							quotationResponse.setMessage(
									"Giá bán của sản phẩm " + quotationDetail.getProductCode() + "  không hợp lệ ");
							return quotationResponse;
						}

						quotationDetail.setPrice(value.longValue());
						totalPrice = totalPrice.add(new BigDecimal(amount * value));
					} catch (Exception e) {
						quotationResponse.setMessage(
								"Giá bán của sản phẩm " + quotationDetail.getProductCode() + "  không hợp lệ ");
						return quotationResponse;
					}
				}
			}

			if (createQuotationBO.getExpiredDate() == null) {
				quotationResponse.setMessage("Bạn chưa nhập Ngày hết hạn");
				return quotationResponse;
			}
			Date expiredDate = null;
			try {
				expiredDate = DateTimeUtils.convertStringToDate(createQuotationBO.getExpiredDate(), "dd/MM/yyyy");

			} catch (Exception e) {
				quotationResponse.setMessage("Bạn chưa nhập Ngày hết hạn");
				return quotationResponse;
			}

			// QuotationDetailDao dao = new QuotationDetailDao();
			for (QuotationDetail quotationDetail : createQuotationBO.getLstQuotationDetail()) {
				if (quotationDetail.getQuotationDetailId() != null) {
					// System.out.println("chi tiet bao gia Id
					// "+quotationDetail.getQuotationDetailId());
					QuotationDetail update = new QuotationDetailDao().findById(quotationDetail.getQuotationDetailId());
					update.setPrice(quotationDetail.getPrice());
					new QuotationDetailDao().saveOrUpdate(update);
				}
			}

			Quotation quotationUpdate = new QuotationDao().findById(quotationBO.getQuotationID());
			if (!createQuotationBO.getIsPreViewApprove()) {
				if (quotationUpdate.getQuotationNumber() == null || quotationUpdate.getQuotationNumber().isEmpty()) {
					quotationUpdate.setQuotationNumber(new QuotationDao().getAutoPhaFileCode());
				}
				quotationUpdate.setStatus(Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA.intValue());
				quotationUpdate.setQuotationUserName(createQuotationBO.getUserName());
			}

			quotationUpdate.setQuotationDate(expiredDate);
			quotationUpdate.setModifyDate(new Date());
			quotationUpdate.setTotalPrice(totalPrice);

			List<QuotationDetail> listQuotationDetail = new QuotationDetailDao()
					.getListQuotationDetail(quotationUpdate.getQuotationID());
			String filePath = printBaoGia(listQuotationDetail, quotationUpdate,
					createQuotationBO.getIsPreViewApprove());

			if (filePath == null) {
				quotationResponse.setMessage("Không in được báo giá");
				return quotationResponse;
			}

			new QuotationDao().saveOrUpdate(quotationUpdate);
			quotationResponse.setMessage(quotationUpdate.getFileName());
			quotationResponse.setStatusCode(200);

			if (createQuotationBO.getIsPreViewApprove() != null && !createQuotationBO.getIsPreViewApprove()) {
				sendNotification(quotationUpdate.getCreateUserCode(),
						"Báo giá đã được duyệt: " + quotationUpdate.getQuotationNumber(),
						String.format("Báo giá cho KH %s của bạn vừa được phê duyệt ", quotationUpdate.getCusName()));
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

	private CKBaoGia getQuotation(CKBaoGia input, CKBaoGia update) {
		if (update != null) {
			update.setCusAddress(input.getCusAddress());
			update.setCusPhone(input.getCusPhone());
			update.setCusName(input.getCusName());
			update.setType(input.getType());
			update.setProductType(input.getProductType());
			update.setCkContent(update.getCkContent());
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

			RequestConfig.Builder requestConfig = RequestConfig.custom();
			requestConfig.setConnectTimeout(20 * 1000);
			requestConfig.setConnectionRequestTimeout(20 * 1000);
			requestConfig.setSocketTimeout(20 * 1000);

			request.setConfig(requestConfig.build());

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

  

}
