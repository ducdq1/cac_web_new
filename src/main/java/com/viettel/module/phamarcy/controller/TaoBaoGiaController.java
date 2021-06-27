package com.viettel.module.phamarcy.controller;

import java.io.File;
import java.io.IOException;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
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
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.model.UserToken;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.ws.ProductService;

/**
 *
 * @author tuannt40
 */
public class TaoBaoGiaController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbMaSP, tbMaNVBH, tbKhacHang, tbSDT, tbTenNVBH, tbDiaChi;
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
	private Button btnClose, btnSave;
	@Wire
	Radiogroup rdgLoaiBaoGia;

	PhamarcyFileModel lastSearchModel;
	private List<QuotationDetail> lstQuotationDetail;
	private List<Product> lstProductsCombobox;
	ListModelArray lstModel;
	int indexUpdate;// cap nhat sp bao gia
	QuotationDetail quotationDetailUpdate;

	Quotation quotation = new Quotation();

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		lastSearchModel = new PhamarcyFileModel();
		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// onSearch();
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();

		quotation = (Quotation) arguments.get("quotation");
		parrent = (Window) arguments.get("parrent");

		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		HttpSession httpSession = req.getSession(true);
		UserToken userToken = (UserToken) httpSession.getAttribute("userToken");
		if (userToken != null) {
			tbMaNVBH.setText(userToken.getUserName());
			tbTenNVBH.setText(userToken.getUserFullName());
		}

		if (quotation == null) {
			btnClose.setVisible(false);
			phamarcyAll.setClosable(false);
			phamarcyAll.setTitle("");
			quotation = new Quotation();
			lstQuotationDetail = new ArrayList<>();
		} else {
			btnClose.setVisible(true);
			phamarcyAll.setClosable(true);
			phamarcyAll.setTitle("Cập nhật báo giá");
			lstQuotationDetail = new QuotationDetailDao().getListQuotationDetail(quotation.getQuotationID());
			tbDiaChi.setText(quotation.getCusAddress());
			tbKhacHang.setText(quotation.getCusName());
			tbSDT.setText(quotation.getCusPhone());
			if (quotation.getCreateUserFullName() != null) {
				tbTenNVBH.setText(quotation.getCreateUserFullName());
			}
			if (quotation.getCreateUserCode() != null) {
				tbMaNVBH.setText(quotation.getCreateUserCode());
			}
			rdgLoaiBaoGia.setSelectedIndex(quotation.getType() == null ? 0 : quotation.getType());
			Quotation quotationUpdate = new QuotationDao().findById(quotation.getQuotationID());

			if (quotationUpdate.getStatus() == Constants.BAO_GIA_STATUS_DA_XUAT_BAO_GIA) {
				btnSave.setDisabled(true);
			}
		}

		ProductDao dao = new ProductDao();
		PagingListModel plm = dao.findProducts(new PhamarcyFileModel(), -2, 10);

		lstProductsCombobox = plm.getLstReturn();

		lstModel = new ListModelArray(lstQuotationDetail);
		lbListSP.setModel(lstModel);

	}

	private void addEventForCombobox(final Listitem item) {
		Listcell CELL2 = (Listcell) item.getChildren().get(2);
		final Label labelProductName = (Label) CELL2.getFirstChild();
		labelProductName.setValue("");

		Listcell CELL3 = (Listcell) item.getChildren().get(3);
		final Label labelDVT = (Label) CELL3.getFirstChild();
		labelDVT.setValue("");

		Listcell CELL6 = (Listcell) item.getChildren().get(6);
		final Image btnXoa = (Image) CELL6.getFirstChild();

		Listcell CELL1 = (Listcell) item.getChildren().get(1);
		final Combobox cb = (Combobox) CELL1.getFirstChild();
		cb.setModel(new ListModelArray(lstProductsCombobox));
		cb.setValue("");
		cb.addEventListener("onChange", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {

				Product p = (Product) cb.getSelectedItem().getValue();
				if (p != null) {
					labelProductName.setValue(p.getProductName());
					labelDVT.setValue(p.getUnit());
					btnXoa.setAttribute("productCode", p.getProductCode());
				} else {
					labelProductName.setValue("");
					labelDVT.setValue("");
				}
			}

		});

		btnXoa.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String productCode = (String) btnXoa.getAttribute("productCode");

				event.getTarget().getParent().getParent();
				String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE)
						+ " \"" + "\" này?";
				EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

					public void onEvent(ClickEvent event) throws Exception {
						if (Messagebox.Button.YES.equals(event.getButton())) {
							lbListSP.getItems().remove(item);
							lbListSP.renderAll();
							showNotification("Xóa sản phẩm thành công", Constants.Notification.INFO, 2000);

						}

					}
				};
				showDialogConfirm(message, null, clickListener);

			}
		});

		Listcell CELL4 = (Listcell) item.getChildren().get(4);
		final Textbox tbSoLuong = (Textbox) CELL4.getFirstChild();
		tbSoLuong.setValue("");
		tbSoLuong.addEventListener("onOK", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Listitem item = lbListSP.getItems().get(lbListSP.getItems().size() - 1);
				Listcell CELL1 = (Listcell) item.getChildren().get(1);
				final Combobox cb = (Combobox) CELL1.getFirstChild();
				if (!cb.getValue().isEmpty()) {
					addListItem();
				}
			}
		});

		Listcell CELL5 = (Listcell) item.getChildren().get(5);
		final Button btnChonAnh = (Button) CELL5.getFirstChild();

		btnChonAnh.addEventListener("onClick", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Listcell CELL1 = (Listcell) item.getChildren().get(1);
				final Combobox cb = (Combobox) CELL1.getFirstChild();
				if (!cb.getValue().isEmpty()) {
					Product p = (Product) cb.getSelectedItem().getValue();
					if (p != null) {
						showNotification(p.getProductCode());
					} else {

					}
				}
			}
		});

	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		try {
			if (tbMaNVBH.getValue().isEmpty()) {
				tbMaNVBH.setErrorMessage("Mã nhân viên không được để trống");
				return;
			}

			if (tbTenNVBH.getValue().isEmpty()) {
				tbTenNVBH.setErrorMessage("Tên nhân viên không được để trống");
				return;
			}

			if (tbKhacHang.getValue().isEmpty()) {
				tbKhacHang.setErrorMessage("Tên khách hàng không được để trống");
				return;
			}

			if (tbDiaChi.getValue().isEmpty()) {
				tbDiaChi.setErrorMessage("Địa chỉ khách hàng không được để trống");
				return;
			}

//			if (quotation.getQuotationNumber() == null) {
//				quotation.setQuotationNumber(new QuotationDao().getAutoPhaFileCode());
//			}

			quotation.setCreateDate(new Date());
			quotation.setModifyDate(new Date());
			// quotation.setQuotationDate(new Date());
			quotation.setCreateUserFullName(tbTenNVBH.getText().trim());
			quotation.setCreateUserCode(tbMaNVBH.getText().trim());
			quotation.setCusName(tbKhacHang.getText().trim());
			quotation.setCusAddress(tbDiaChi.getText().trim());
			quotation.setCusPhone(tbSDT.getText().trim());
			quotation.setCreateUserFullNameSearch(
					new BaseGenericForwardComposer().removeVietnameseChar(quotation.getCreateUserFullName()));
			quotation.setType(rdgLoaiBaoGia.getSelectedIndex());

			new QuotationDao().saveOrUpdate(quotation);
			Long quotationId = quotation.getQuotationID();

			for (QuotationDetail obj : lstQuotationDetail) {
				obj.setQuotationId(quotationId);
				new QuotationDetailDao().saveOrUpdate(obj);
			}

			showSuccessNotification("Lưu báo giá thành công");
			if (parrent != null) {
				Events.sendEvent("onReload", parrent, null);
			}
			
			ProductService.sendNotification("create", "Có báo giá mới", String.format("%s vừa tạo một báo giá mới cho khách hàng %s  ", quotation.getCreateUserFullName().toUpperCase(),quotation.getCusName()));
			/*WorkingThread thread =new WorkingThread();
			NotificationBO body =new NotificationBO();
			body.setTo("/topics/all");
			Notification notification = body.new Notification();

			notification.setTitle("Có báo giá mới");
			notification.setBody(String.format("%s vừa tạo một báo giá mới cho khách hàng %s  ", quotation.getCreateUserFullName().toUpperCase(),quotation.getCusName()));

			body.setNotification(notification);
			thread.setBody(body);
			thread.run();*/
			
		} catch (Exception e) {
			showNotification("Lưu không thành công: \n" + e.getMessage());
		}
	}

	@Listen("onClick = #btnThemSP")
	public void addListItem() {
		if (btnSave.isDisabled()) {
			showNotification("Không thể chỉnh sửa báo giá");
			return;
		}

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("lstQuotationDetail", lstQuotationDetail);
		createWindow("windowView", "/Pages/module/phamarcy/bao_gia_chon_sp.zul", arguments, Window.MODAL);
	}

	public ListModelArray comboboxModel() {
		return new ListModelArray(lstProductsCombobox);
	}

	@Listen("onAfterRender=#lbListSP")
	public void results_onAfterRender() throws IOException {
		List<Listitem> listitems = lbListSP.getItems();
		for (Listitem item : listitems) {
			QuotationDetail quotationDetail = item.getValue();
			Attachs media = quotationDetail.getImage();
			if (media != null) {
				Listcell cell = (Listcell) item.getChildren().get(3);
				org.zkoss.zul.Image image = (org.zkoss.zul.Image) cell.getFirstChild();
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

	public void onComboboxProductChange(Event event) {
		Object obj = event.getData();
		lstQuotationDetail.get(0);
	}

	@Listen("onOK=#phamarcyAll")
	public void onEnter() {
		// onSearch();
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

	@Listen("onClick = #incSearchFullForm #btnReset")
	public void onReset() {
		tbMaSP.setText("");
		// tbTenSP.setText("");
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
		if (btnSave.isDisabled()) {
			showNotification("Không thể chỉnh sửa báo giá");
			return;
		}

		QuotationDetail obj = (QuotationDetail) event.getData();
		quotationDetailUpdate = obj;
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("quotationDetail", obj);
		arguments.put("lstQuotationDetail", lstQuotationDetail);
		createWindow("windowView", "/Pages/module/phamarcy/bao_gia_chon_SP.zul", arguments, Window.MODAL);
	}

	@Listen("onDelete = #lbListSP")
	public void onDelete(Event event) {
		if (btnSave.isDisabled()) {
			showNotification("Không thể chỉnh sửa báo giá");
			return;
		}

		final QuotationDetail obj = (QuotationDetail) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE) + " \""
				+ obj.getProductCode() + "\" này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					if (obj.getQuotationId() != null) {
						new QuotationDetailDao().delete(obj);
					}
					lstQuotationDetail.remove(obj);
					lstModel = new ListModelArray(lstQuotationDetail);
					lbListSP.setModel(lstModel);
					showNotification("Xóa sản phẩm thành công", Constants.Notification.INFO, 2000);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onClick = #btnChonSP")
	public void onChonSp(Event event) {
		if (btnSave.isDisabled()) {
			showNotification("Không thể chỉnh sửa báo giá");
			return;
		}

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("lstQuotationDetail", lstQuotationDetail);
		createWindow("windowView", "/Pages/module/phamarcy/bao_gia_chon_sp.zul", arguments, Window.MODAL);

	}

}
