package com.viettel.module.phamarcy.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.model.UserToken;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportExcell;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.module.phamarcy.utils.EncryptionUtil;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.ws.ProductService;

/**
 *
 * @author tuannt40
 */
public class BaoGiaController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire
	private Label tbMaSP, tbMaNVBH, tbKhacHang, tbSDT, tbTenNVBH, tbDiaChi;
	@Wire
	private Paging userPagingBottom;
	@Wire
	private Listbox lbListSP;
	@Wire
	private Window phamarcyAll;
	private Window parrent;
	@Wire
	private Label lbTongtien;
	@Wire
	private Button btnClose, btnSave,btnPreView;
	@Wire
	Radiogroup rdgLoaiBaoGia;
	@Wire
	Datebox dbExpireDate;

	PhamarcyFileModel lastSearchModel;
	private List<QuotationDetail> lstQuotationDetail;
	private List<Product> lstProductsCombobox;
	@SuppressWarnings("rawtypes")
	ListModelArray lstModel;
	int indexUpdate;// cap nhat sp bao gia
	QuotationDetail quotationDetailUpdate;

	Quotation quotation = new Quotation();

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		lastSearchModel = new PhamarcyFileModel();
		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// onSearch();
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();

		quotation = (Quotation) arguments.get("quotation");
		parrent = (Window) arguments.get("parrent");
		if (quotation == null) {
			btnClose.setVisible(false);
			phamarcyAll.setClosable(false);
			phamarcyAll.setTitle("");
			quotation = new Quotation();
			lstQuotationDetail = new ArrayList<>();
			HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			HttpSession httpSession = req.getSession(true);
			UserToken userToken = (UserToken) httpSession.getAttribute("userToken");
			if (userToken != null) {
				tbMaNVBH.setValue(userToken.getUserName());
				tbTenNVBH.setValue(userToken.getUserFullName());
			}
		} else {
			btnClose.setVisible(true);
			phamarcyAll.setClosable(true);
			phamarcyAll.setTitle("Báo giá");
			lstQuotationDetail = new QuotationDetailDao().getListQuotationDetail(quotation.getQuotationID());
			tbDiaChi.setValue(quotation.getCusAddress());
			tbKhacHang.setValue(quotation.getCusName());
			tbSDT.setValue(quotation.getCusPhone());
			tbTenNVBH.setValue(quotation.getCreateUserFullName());
			tbMaNVBH.setValue(quotation.getCreateUserCode());
			rdgLoaiBaoGia.setSelectedIndex(quotation.getType() == null ? 0 : quotation.getType());
			dbExpireDate.setValue(quotation.getQuotationDate());
			if (quotation.getStatus() == Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA) {
				btnSave.setDisabled(true);
			}
		}

		ProductDao dao = new ProductDao();
		PagingListModel plm = dao.findProducts(new PhamarcyFileModel(), -2, 10);

		lstProductsCombobox = plm.getLstReturn();

		lstModel = new ListModelArray(lstQuotationDetail);
		lbListSP.setModel(lstModel);
		
		if(getFileBaoGia() !=null ){
			btnPreView.setVisible(false);
		}else{
			btnPreView.setVisible(true);
		}
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		saveQuatation(null,true);
	}

	private boolean saveQuatation(Long status,boolean isPreview) {
		try {
			BigDecimal totalPrice = new BigDecimal(0);
			List<Listitem> listitems = lbListSP.getItems();
			for (Listitem item : listitems) {
				QuotationDetail quotationDetail = item.getValue();
				if (quotationDetail != null) {
					final Double amount = quotationDetail.getAmount() == null ? 0d : quotationDetail.getAmount();
					Listcell cell6 = (Listcell) item.getChildren().get(7);
					final Label lbTotal = (Label) cell6.getFirstChild();

					Listcell cell = (Listcell) item.getChildren().get(6);
					final Textbox tbPrice = (Textbox) cell.getFirstChild();

					try {
						Long value = Long.valueOf(tbPrice.getValue().toString().trim().replace(".", ""));
						if (value.compareTo(0L) <= 0) {
							tbPrice.setErrorMessage("Dữ liệu không hợp lệ ");
							return false;
						}
						lbTotal.setValue(formatNumber((amount * value), "###,###,###.####"));
						tbPrice.setValue(formatNumber(value, "###,###,###.####"));
						quotationDetail.setPrice(value.longValue());
						totalPrice = totalPrice.add(new BigDecimal(amount * value));
					} catch (Exception e) {
						tbPrice.setErrorMessage("Dữ liệu không hợp lệ ");
						return false;
					}
				}
			}

			if (dbExpireDate.getValue() == null) {
				dbExpireDate.setErrorMessage("Trường bắt buộc nhập");
				return false;
			}

			for (Listitem item : listitems) {
				QuotationDetail quotationDetail = item.getValue();
				if (quotationDetail != null) {
					new QuotationDetailDao().saveOrUpdate(quotationDetail);
				}
			}

			if (!isPreview) {
				quotation.setQuotationNumber(new QuotationDao().getAutoPhaFileCode());
			}

			quotation.setModifyDate(new Date());
			quotation.setTotalPrice(totalPrice);
			// ngay het han
			quotation.setQuotationDate(dbExpireDate.getValue());

			new QuotationDao().saveOrUpdate(quotation);
			Long quotationId = quotation.getQuotationID();

			for (QuotationDetail obj : lstQuotationDetail) {
				obj.setQuotationId(quotationId);
				new QuotationDetailDao().saveOrUpdate(obj);
			}

			if (status == null) {
				showSuccessNotification("Lưu báo giá thành công");
			}
			if (parrent != null) {
				Events.sendEvent("onReload", parrent, null);
			}
			return true;
		} catch (Exception e) {
			showNotification("Lưu không thành công: \n" + e.getMessage());
		}
		return false;
	}

	@Listen("onClick = #btnThemSP")
	public void addListItem() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("lstQuotationDetail", lstQuotationDetail);
		createWindow("windowView", "/Pages/module/phamarcy/bao_gia_chon_sp.zul", arguments, Window.MODAL);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ListModelArray comboboxModel() {
		return new ListModelArray(lstProductsCombobox);
	}

	@Listen("onAfterRender=#lbListSP")
	public void results_onAfterRender() throws IOException {
		List<Listitem> listitems = lbListSP.getItems();
		for (Listitem item : listitems) {
			QuotationDetail quotationDetail = item.getValue();
			if (quotationDetail != null) {
				final Double amount = quotationDetail.getAmount() == null ? 0d : quotationDetail.getAmount();
				Listcell cell6 = (Listcell) item.getChildren().get(7);
				final Label lbTotal = (Label) cell6.getFirstChild();

				Listcell cell = (Listcell) item.getChildren().get(6);
				final Textbox tbPrice = (Textbox) cell.getFirstChild();
				tbPrice.addEventListener("onChange", new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						try {
							Long value = Long.valueOf(tbPrice.getValue().toString().trim().replace(".", ""));
							lbTotal.setValue(formatNumber((amount * value), "###,###,###.####"));
							tbPrice.setValue(formatNumber(value, "###,###,###.####"));
						} catch (Exception e) {
							tbPrice.setErrorMessage("Dữ liệu không hợp lệ ");
						}

					}

				});

				Attachs media = quotationDetail.getImage();
				if (media != null) {
					Listcell cell3 = (Listcell) item.getChildren().get(3);
					org.zkoss.zul.Image image = (org.zkoss.zul.Image) cell3.getFirstChild();
					if (media.getAttachId() != null) {
						String dir_upload = ResourceBundleUtil.getString("dir_upload");
						File file = new File(dir_upload + media.getFullPathFile());
						if (file.exists()) {
							image.setContent(new org.zkoss.image.AImage(file));
						}
					}
				}

			}
		}
	}

	/**
	 * Lay danh sach tim kiem
	 * 
	 * @param searchModel
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void reloadModel(PhamarcyFileModel searchModel) {
		try {
			int take = userPagingBottom.getPageSize();
			int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
			ProductDao dao = new ProductDao();
			PagingListModel plm = dao.findProducts(searchModel, start, take);

			userPagingBottom.setTotalSize(plm.getCount());
			if (plm.getCount() == 0) {
				userPagingBottom.setVisible(false);
			} else {
				userPagingBottom.setVisible(true);
			}

			// lstProducts = plm.getLstReturn();

			ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
			lstModel.setMultiple(true);
			lbListSP.setModel(lstModel);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
			LogUtils.addLog(ex);
		}
	}

	@Listen("onPaging =  #userPagingBottom")
	public void onPaging(Event event) {
		reloadModel(lastSearchModel);
	}

	/**
	 * Open view xem chi tiet
	 * 
	 * @param event
	 */
	@Listen("onOpenView =#incList #lbList")
	public void onOpenView(Event event) {
		VPhaFileMedicine obj = (VPhaFileMedicine) event.getData();

		createWindowView(obj.getFileId(), obj.getSystemFileId(), Constants.DOCUMENT_MENU.ALL, phamarcyAll);

	}

	/**
	 * Tao view xem chi tiet
	 * 
	 * @param id
	 * @param systemFileId
	 * @param menuType
	 * @param parentWindow
	 */
	private void createWindowView(Long id, Long systemFileId, int menuType, Window parentWindow) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("id", id);
		createWindow("windowView", "/Pages/module/phamarcy/generalview/phamarcyView.zul", arguments, Window.EMBEDDED);
	}

	@Listen("onAddProduct =#phamarcyAll")
	public void onRefreshAll(Event event) {
		HashMap<String, Object> data = (HashMap<String, Object>) event.getData();
		if (data != null) {
			Boolean isUpdate = (Boolean) data.get("isUpdate");
			QuotationDetail quotationDetail = (QuotationDetail) data.get("quotationDetail");
			lstQuotationDetail = (List<QuotationDetail>) data.get("lstQuotationDetail");
			if (lstQuotationDetail != null) {

				quotationDetailUpdate = null;

				lstModel = new ListModelArray(lstQuotationDetail);
				lbListSP.setModel(lstModel);
				if (!isUpdate) {
					showSuccessNotification("Thêm thành công");
				} else {
					showSuccessNotification("Cập nhật thành công");
				}
			}
		}
	}

	@Listen("onOpenUpdate = #lbListSP")
	public void onOpenUpdate(Event event) {
		QuotationDetail obj = (QuotationDetail) event.getData();
		quotationDetailUpdate = obj;
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("quotationDetail", obj);
		arguments.put("lstQuotationDetail", lstQuotationDetail);
		createWindow("windowView", "/Pages/module/phamarcy/bao_gia_chon_SP.zul", arguments, Window.MODAL);
	}

	@Listen("onClick = #btnPrint")
	public void btnPrint(Event event) {
		String message = "Sau khi in báo giá sẽ không được phép chỉnh sửa. Bạn có muốn in báo giá?";
		String fileBaoGia = getFileBaoGia();
		if(fileBaoGia != null){
			String URL;
			URL = "Pages/module/phamarcy/generalview/viewPDF.zul?filePath=";
			URL = URL + EncryptionUtil.encode(fileBaoGia);
			// URL = URL + (path);
			String link = String.format(
					"window.open('%s','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')",
					URL);
			Clients.evalJavaScript(link);
			return;
		}
		
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					
					if (saveQuatation(Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA,false)) {
						String filePath = new ExportExcell().exportBaoGia(lstQuotationDetail, quotation,false,false);
						if (filePath != null) {
							String URL;
							URL = "Pages/module/phamarcy/generalview/viewPDF.zul?filePath=";
							URL = URL + EncryptionUtil.encode(filePath);
							// URL = URL + (path);
							String link = String.format(
									"window.open('%s','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')",
									URL);
							Clients.evalJavaScript(link);

							Quotation update = new QuotationDao().findById(quotation.getQuotationID());
							update.setStatus(Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA.intValue());
							update.setFileName(quotation.getFileName());
							update.setQuoationUserName(getUserName());
							new QuotationDao().saveOrUpdate(update);
							quotation.setStatus(Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA.intValue());
							if (parrent != null) {
								Events.sendEvent("onReload", parrent, null);
							}

							lstModel = new ListModelArray(lstQuotationDetail);
							lbListSP.setModel(lstModel);
							btnSave.setDisabled(true);
							
							ProductService.sendNotification(update.getCreateUserCode(), "Báo giá đã được duyệt", String.format("Báo giá của bạn vừa phê duyệt: %s", update.getQuotationNumber()));
							
//							NotificationBO body =new NotificationBO();
//							body.setTo("/topics/all");
//							Notification notification = body.new Notification();
//
//							notification.setTitle("Báo giá đã được duyệt");
//							notification.setBody(String.format("Báo giá của bạn vừa phê duyệt: %s", update.getQuotationNumber()));
//
//							body.setNotification(notification);
							
						} else {
							showNotification("Có lỗi xãy ra");
						}
					}

				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onClick = #btnPreView")
	public void btnPreView(Event event) {

		if (saveQuatation(Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA,true)) {
			String filePath = new ExportExcell().exportBaoGia(lstQuotationDetail, quotation,true,false); 
			if (filePath != null) {
				String URL;
				URL = "Pages/module/phamarcy/generalview/viewPDF.zul?filePath=";
				URL = URL + EncryptionUtil.encode(filePath);
				// URL = URL + (path);
				String link = String.format(
						"window.open('%s','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')", URL);
				Clients.evalJavaScript(link);
 
			} else {
				showNotification("Có lỗi xãy ra");
			}
		}

	}

	public boolean getStatusQuotation() {
		return quotation.getStatus() == 1 ? true : false;
	}
	
	private String getFileBaoGia(){
		if(quotation.getStatus() == Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA.intValue()){
			String fileName = quotation.getFileName();
			if(fileName!=null){
				String dir_upload = ResourceBundleUtil.getString("dir_upload");
				String filePathOut = dir_upload + "/bao_gia/" + fileName;
				File f= new File(filePathOut);
				if(f.exists()){ 
					return filePathOut;
				}
			}
		}
		return null;
	}
}
