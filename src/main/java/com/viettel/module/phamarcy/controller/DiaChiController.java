package com.viettel.module.phamarcy.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.Area;
import com.viettel.module.phamarcy.BO.Order;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportExcell;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.OrderDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.OrderDetailDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.StreetDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author tuannt40
 */
public class DiaChiController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire("#incSearchFullForm #fullSearchGbx") // Form search
	private Groupbox fullSearchGbx;
	@Wire("#incSearchFullForm #tbMaSP") // Ma ho so
	private Listbox tbMaSP;
	@Wire("#incSearchFullForm #tbTenDuong") // Ma ho so
	private Textbox tbTenDuong;
	// End search form
	@Wire
	private Paging userPagingBottom;
	@Wire
	private Listbox lbList; // List ho so

	@Wire
	private Window phamarcyAllDH; // View danh sach {phamarcyAllDH.zul)
	@Wire
	private Label lbTongtien;
	@Wire
	private Radiogroup rdg;
	PhamarcyFileModel lastSearchModel;
	private List<Street> lstProducts;
	private List<OrderDetail> lstQuotationDetail;
	private Order quotation;
	int indexUpdate;// cap nhat sp bao gia
	private List<QuotationDetail> lstQuotationDetailImport;
	@Wire
	private Button btnClose;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {

		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		List lstArea = new StreetDao().getListArea();
		Area a = new Area();
		a.setAreaId(-1L);
		a.setAreaName("-- Tất cả --");
		lstArea.add(0, a);
		ListModelList lstModel = new ListModelList(lstArea);
		tbMaSP.setModel(lstModel);

		onSearch();

	}

	@Listen("onClick = #incSearchFullForm #btnSearch")
	public void onSearch() {
		lastSearchModel = new PhamarcyFileModel();
		if(tbMaSP.getSelectedItem() !=null){
			lastSearchModel.setAreaId((Long) tbMaSP.getSelectedItem().getValue());
		}else{
			lastSearchModel.setAreaId(-1L);
		}
		
		lastSearchModel.setDiaChi(tbTenDuong.getText().trim());
		
		try {
			userPagingBottom.setActivePage(0);
			reloadModel(lastSearchModel);
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
		}
	}

	@Listen("onOK=#phamarcyAllDH")
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
			StreetDao dao = new StreetDao();
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

		createWindowView(obj.getFileId(), obj.getSystemFileId(), Constants.DOCUMENT_MENU.ALL, phamarcyAllDH);

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

	}

	@Listen("onClick = #incSearchFullForm #btnAdd")
	public void onAdd() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAllDH);
		createWindow("windowView", "/Pages/module/phamarcy/addDuong.zul", arguments, Window.MODAL);
	}

	@Listen("onRefreshALL =#phamarcyAllDH")
	public void onRefreshAll(Event event) {
		reloadModel(lastSearchModel);
	}

	@Listen("onDelete = #lbList")
	public void onDelete(Event event) {
		final Street obj = (Street) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, "địa chỉ") + " này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new StreetDao().delete(obj);
					showNotification("Xóa địa chỉ thành công", Constants.Notification.INFO, 2000);
					reloadModel(lastSearchModel);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onOpenUpdate = #lbList")
	public void onOpenUpdate(Event event) {
		Street obj = (Street) event.getData();

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAllDH);
		arguments.put("street", obj);
		createWindow("windowView", "/Pages/module/phamarcy/addDuong.zul", arguments, Window.MODAL);

	}

	private void showChooseProductView(Product obj) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAllDH);
		arguments.put("product", obj);
		createWindow("windowView", "/Pages/module/phamarcy/chooseProductToOrder.zul", arguments, Window.MODAL);
	}

	@Listen("onOpenUpdateSP = #incListSP #lbListSP")
	public void onOpenUpdateSP(Event event) {
		OrderDetail obj = (OrderDetail) event.getData();
		indexUpdate = lstQuotationDetail.indexOf(obj);
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAllDH);
		arguments.put("OrderDetail", obj);
		createWindow("windowView", "/Pages/module/phamarcy/chooseProductToOrder.zul", arguments, Window.MODAL);

	}

	// kiem tra sp da duoc chon truoc do chua
	private boolean checkExistQuotationDetail(Long productId) {
		for (OrderDetail quotationDetail : lstQuotationDetail) {
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
			arguments.put("parent", phamarcyAllDH);
			arguments.put("Order", quotation);
			createWindow("windowView", "/Pages/module/phamarcy/orderInfor.zul", arguments, Window.MODAL);

		} catch (Exception e) {
			showNotification("Có lỗi xãy ra", Constants.Notification.ERROR, 2000);
		}

	}

	private void saveQuotation() {
		quotation.setType(Long.valueOf(rdg.getSelectedIndex()));

		quotation.setCreateUserFullName(getUserFullName());
		quotation.setCreateUserFullNameSearch(
				new BaseGenericForwardComposer().removeVietnameseChar(quotation.getCreateUserFullName()));

		if (quotation.getOrderNumber() == null) {
			quotation.setOrderNumber(new OrderDao().getAutoPhaFileCode());
		}

		quotation.setModifyDate(new Date());
		quotation.setUserModify(getUserId());

		// quotation.setOrderDate(new Date());

		new OrderDao().saveOrUpdate(quotation);
		Long quotationId = quotation.getOrderId();

		Long price;
		int type = rdg.getSelectedIndex();
		for (OrderDetail obj : lstQuotationDetail) {

			obj.setOrderId(quotationId);
			price = obj.getPrice();

			if (type == 0) {// ban hang
				if (price < 0) {
					price = price * (-1);
				}
			} else {// xuat hang
				if (price > 0) {
					price = price * (-1);
				}
			}

			obj.setPrice(price);

			new OrderDetailDao().saveOrUpdate(obj);

		}

		new OrderDao().saveOrUpdate(quotation);
	}

	@SuppressWarnings("unchecked")
	@Listen("onBaoGiaInfor =#phamarcyAllDH")
	public void onBaoGiaInfor(Event event) {

		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		quotation = (Order) arguments.get("Order");
		int isView = (int) arguments.get("isView");
		saveQuotation();

		new ExportFileDAO().exportDonHang(quotation, lstQuotationDetail, isView);
	}

	/**
	 * set chon item 0 trong combo loai tep dinh kem
	 */
	public void getSelectedIndexInModel() {
		tbMaSP.setSelectedIndex(0);

	}
	@Listen("onClick = #incSearchFullForm #btnPrint")
	public void onPrint() {
	try {
		onSearch();
		PagingListModel plm = new StreetDao().findFilesByReceiverAndDeptId(lastSearchModel, -2, 0);
		
		File file = new ExportExcell().exportExcellKhuVuc(plm.getLstReturn());
		Filedownload.save(file, "application/application/x-xls");
	} catch (Exception e) {
		e.printStackTrace();
		showNotification("Có lỗi xãy ra", Constants.Notification.INFO, 2000);
	}
	}
}