package com.viettel.module.phamarcy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author tuannt40
 */
public class PhamarcyAllController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire("#incSearchFullForm #fullSearchGbx") // Form search
	private Groupbox fullSearchGbx;
	@Wire("#incSearchFullForm #tbMaSP") // Ma ho so
	private Textbox tbMaSP;
	@Wire("#incSearchFullForm #tbTenSP") // Ten thuoc
	private Textbox tbTenSP;
	@Wire
	private Paging userPagingBottom;
	@Wire("#incList #lbList")
	private Listbox lbList; // List ho so
	@Wire("#incListSP #lbListSP")
	private Listbox lbListSP;
	@Wire
	private Window phamarcyAll;
	@Wire
	private Label lbTongtien;
	PhamarcyFileModel lastSearchModel;
	private List<Product> lstProducts;
	private List<QuotationDetail> lstQuotationDetail;
	private Quotation quotation;
	int indexUpdate;// cap nhat sp bao gia

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		lastSearchModel = new PhamarcyFileModel();
		lstQuotationDetail = new ArrayList<>();
		if (quotation == null) {
			quotation = new Quotation();
			quotation.setCreateDate(new Date());
			quotation.setUserCreate(getUserId());

		}
		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		onSearch();
	}

	@Listen("onClick = #incSearchFullForm #btnDelete")
	public void deleteAll() {
		if (lstProducts == null || lstProducts.isEmpty()) {
			showDialogWithNoEvent("Không có sản phẩm nào để xóa", "Thông báo");
			return;
		}

		String message = "Bạn chắc chắn muốn xóa tất cả sản phẩm này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			@SuppressWarnings("unchecked")
			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					ProductDao dao = new ProductDao();
					PagingListModel plm = dao.findFilesByReceiverAndDeptId(lastSearchModel, -3, -3);
					List<Product> lstProducts = plm.getLstReturn();
					if (lstProducts != null && !lstProducts.isEmpty()) {
						for (Product obj : lstProducts) {
							new ProductDao().delete(obj);
						}
					}
					showNotification("Xóa thành công",Constants.Notification.INFO);
					onSearch();
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onClick = #incSearchFullForm #btnSearch")
	public void onSearch() {
//		lastSearchModel.setProductCode(tbMaSP.getText().trim());
//		lastSearchModel.setProductName(tbTenSP.getText().trim());
		try {
			userPagingBottom.setActivePage(0);
			reloadModel(lastSearchModel);
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
		}
	}

	@Listen("onOK=#phamarcyAll")
	public void onEnter() {
		onSearch();
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
			PagingListModel plm = dao.findFilesByReceiverAndDeptId(searchModel, start, take);

			userPagingBottom.setTotalSize(plm.getCount());
			if (plm.getCount() == 0) {
				userPagingBottom.setVisible(false);
			} else {
				userPagingBottom.setVisible(true);
			}

			lstProducts = plm.getLstReturn();

			ListModelArray lstModel = new ListModelArray(lstProducts);

			lbList.setModel(lstModel);

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
		tbTenSP.setText("");
	}

	@Listen("onClick = #incSearchFullForm #btnAdd")
	public void onAdd() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		createWindow("windowView", "/Pages/module/phamarcy/addProduct.zul", arguments, Window.MODAL);
	}

	@Listen("onRefreshALL =#phamarcyAll")
	public void onRefreshAll(Event event) {
		reloadModel(lastSearchModel);
	}

	@Listen("onDelete = #incList #lbList")
	public void onDelete(Event event) {
		final Product obj = (Product) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE) + " \""
				+ obj.getProductName() + "\" này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new ProductDao().delete(obj);
					showNotification("Xóa sản phẩm thành công", Constants.Notification.INFO, 2000);
					reloadModel(lastSearchModel);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onDeleteSP = #incListSP #lbListSP")
	public void onDeleteSP(Event event) {
		final QuotationDetail obj = (QuotationDetail) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE)
				+ " này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					if (obj.getQuotationDetailId() != null) {
						new QuotationDetailDao().delete(obj);
					}
					lstQuotationDetail.remove(obj);
					viewBaoGia();
					showNotification("Xóa sản phẩm thành công", Constants.Notification.INFO, 2000);

				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onOpenUpdate = #incList #lbList")
	public void onOpenUpdate(Event event) {
		Product obj = (Product) event.getData();

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("product", obj);
		createWindow("windowView", "/Pages/module/phamarcy/addProduct.zul", arguments, Window.MODAL);

	}

	@Listen("onChoose = #incList #lbList")
	public void onChoose(Event event) {

		final Product obj = (Product) event.getData();
		boolean isExist = checkExistQuotationDetail(obj.getProductId());

		if (isExist) {
			String message = "Bạn đã chọn sản phẩm này trước đó, chọn tiếp sản phẩm này?";
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

				public void onEvent(ClickEvent event) throws Exception {

					if (Messagebox.Button.YES.equals(event.getButton())) {
						showChooseProductView(obj);
					}

				}
			};
			showDialogConfirm(message, null, clickListener);
		} else {
			showChooseProductView(obj);
		}

	}

	private void showChooseProductView(Product obj) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("product", obj);
		createWindow("windowView", "/Pages/module/phamarcy/chooseProduct.zul", arguments, Window.MODAL);
	}

	@Listen("onOpenUpdateSP = #incListSP #lbListSP")
	public void onOpenUpdateSP(Event event) {
		QuotationDetail obj = (QuotationDetail) event.getData();
		indexUpdate = lstQuotationDetail.indexOf(obj);
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("QuotationDetail", obj);
		createWindow("windowView", "/Pages/module/phamarcy/chooseProduct.zul", arguments, Window.MODAL);

	}

	@SuppressWarnings({ "unchecked" })
	@Listen("onChooseProduct =#phamarcyAll")
	public void onChooseProduct(Event event) {
		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		Boolean isUpdate = (Boolean) arguments.get("isUpdate");
		QuotationDetail quotationDetail = (QuotationDetail) arguments.get("quotationDetail");

		if (isUpdate == null) {// create
			lstQuotationDetail.add(quotationDetail);
		} else {
			lstQuotationDetail.set(indexUpdate, quotationDetail);
		}

		viewBaoGia();
	}

	// kiem tra sp da duoc chon truoc do chua
	private boolean checkExistQuotationDetail(Long productId) {
		for (QuotationDetail quotationDetail : lstQuotationDetail) {
			if (quotationDetail.getProductId().equals(productId)) {
				return true;
			}
		}

		return false;
	}

	@Listen("onClick =#btnSave")
	public void onSave() {
		try {

			saveQuotation();
			showNotification("Lưu báo giá thành công", Constants.Notification.INFO, 2000);
		} catch (Exception e) {
			showNotification("Có lỗi xãy ra", Constants.Notification.ERROR, 2000);
		}

	}

	@Listen("onClick =#btnExport")
	public void onExportBaoGia() {

		try {

			HashMap<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("parent", phamarcyAll);
			arguments.put("Quotation", quotation);
			createWindow("windowView", "/Pages/module/phamarcy/quotationInfor.zul", arguments, Window.MODAL);

		} catch (Exception e) {
			showNotification("Có lỗi xãy ra", Constants.Notification.ERROR, 2000);
		}

	}

	private void saveQuotation() {

		quotation.setCreateUserFullName(getUserFullName());
		quotation.setCreateUserFullNameSearch(
				new BaseGenericForwardComposer().removeVietnameseChar(quotation.getCreateUserFullName()));

		if (quotation.getQuotationNumber() == null) {
			quotation.setQuotationNumber(new QuotationDao().getAutoPhaFileCode());
		}

		quotation.setModifyDate(new Date());
		quotation.setUserModify(getUserId());
		if (quotation.getQuotationDate() == null) {
			quotation.setQuotationDate(new Date());
		}
		new QuotationDao().saveOrUpdate(quotation);
		Long quotationId = quotation.getQuotationID();

		for (QuotationDetail obj : lstQuotationDetail) {
			obj.setQuotationId(quotationId);
			new QuotationDetailDao().saveOrUpdate(obj);
		}

		// quotation.setTotalPrice(totalPrice);
		new QuotationDao().saveOrUpdate(quotation);
	}

	@SuppressWarnings("unchecked")
	@Listen("onBaoGiaInfor =#phamarcyAll")
	public void onBaoGiaInfor(Event event) {

		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		quotation = (Quotation) arguments.get("Quotation");
		int isView = (int) arguments.get("isView");
		saveQuotation();

		new ExportFileDAO().exportBaoGia(quotation, lstQuotationDetail, isView);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void viewBaoGia() {
		ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
		lbListSP.setModel(lstModel);

		BigDecimal total = new BigDecimal(0);
		for (QuotationDetail obj : lstQuotationDetail) {
			if (obj != null && obj.getValue() != null) {
				total = total.add(obj.getValue());
			}
		}

		lbTongtien.setValue(formatNumber(total, "###,###,###") + "  VNĐ");
	}

	@Listen("onClick =#btnOrder")
	public void onChuyenDonHang(Event event) {

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("list", lstQuotationDetail);
		arguments.put("quotation", quotation);
		createWindow("windowView", "/Pages/module/phamarcy/xuat_don_hang.zul", arguments, Window.MODAL);
	}
}
