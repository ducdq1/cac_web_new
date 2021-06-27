package com.viettel.module.phamarcy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
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
import com.viettel.module.phamarcy.BO.Order;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.OrderDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.OrderDetailDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author tuannt40
 */
public class XuatDonHangController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire("#incSearchFullForm #fullSearchGbx") // Form search
	private Groupbox fullSearchGbx;
	@Wire("#incSearchFullForm #tbMaSP") // Ma ho so
	private Textbox tbMaSP;
	@Wire("#incSearchFullForm #tbTenSP") // Ten thuoc
	private Textbox tbTenSP;
	// End search form
	@Wire
	private Paging userPagingBottom;
	@Wire("#incList #lbList")
	private Listbox lbList; // List ho so
	@Wire("#incListSP #lbListSP")
	private Listbox lbListSP;
	@Wire
	private Window phamarcyAllDH; // View danh sach {phamarcyAllDH.zul)
	@Wire
	private Label lbTongtien;
	@Wire
	private Radiogroup rdg;
	PhamarcyFileModel lastSearchModel;
	private List<Product> lstProducts;
	private List<OrderDetail> lstQuotationDetail;
	private Order quotation;
	private Quotation quotationUpdate;//chuyen tu bao gia sang
	int indexUpdate;// cap nhat sp bao gia
	private List<QuotationDetail> lstQuotationDetailImport;
	@Wire
	private Button btnClose;
	@SuppressWarnings("unchecked")
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {

		Map<String, Object> arguments = (Map<String, Object>) Executions.getCurrent().getArg();
		lstQuotationDetailImport = (List<QuotationDetail>) arguments.get("list");
		quotationUpdate= (Quotation) arguments.get("quotation");
		lstQuotationDetail = new ArrayList<>();

		lastSearchModel = new PhamarcyFileModel();

		if (quotation == null) {
			quotation = new Order();
			quotation.setCreateDate(new Date());
			quotation.setUserCreate(getUserId());
		}

		if(quotationUpdate!=null){
			quotation.setCompanyAdd(quotationUpdate.getCompanyAdd());
			quotation.setCompanyName(quotationUpdate.getCompanyName());
			quotation.setCompanyPhone(quotationUpdate.getCompanyPhone());
		}
		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		onSearch();
		
		if (lstQuotationDetailImport != null) {
			for (QuotationDetail obj : lstQuotationDetailImport) {
				OrderDetail orderDetail=new OrderDetail();
				orderDetail.setAmount(obj.getAmount());
				orderDetail.setPrice(obj.getPrice());
				orderDetail.setProductId(obj.getProductId());
				orderDetail.setProductName(obj.getProductName());
				orderDetail.setName(obj.getName());
				orderDetail.setUnit(obj.getUnit());
				orderDetail.getValueString();
				orderDetail.getValue();
				orderDetail.setProductCode(obj.getProductCode());
				lstQuotationDetail.add(orderDetail);
			}
			phamarcyAllDH.setBorder("normal");
			phamarcyAllDH.setClosable(true);
			phamarcyAllDH.setTitle("Xuất đơn hàng");
			btnClose.setVisible(true);
			viewBaoGia();
		}
		
	}

	@Listen("onClick = #incSearchFullForm #btnSearch")
	public void onSearch() {
		lastSearchModel.setProductCode(tbMaSP.getText().trim());
		lastSearchModel.setProductName(tbTenSP.getText().trim());
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
			ProductDao dao = new ProductDao();
			//PagingListModel plm = dao.findFilesByReceiverAndDeptId(searchModel, start, take);

//			userPagingBottom.setTotalSize(plm.getCount());
//			if (plm.getCount() == 0) {
//				userPagingBottom.setVisible(false);
//			} else {
//				userPagingBottom.setVisible(true);
//			}
//
//			lstProducts = plm.getLstReturn();

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
		tbMaSP.setText("");
		tbTenSP.setText("");
	}

	@Listen("onClick = #incSearchFullForm #btnAdd")
	public void onAdd() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAllDH);
		createWindow("windowView", "/Pages/module/phamarcy/addProduct.zul", arguments, Window.MODAL);
	}

	@Listen("onRefreshALL =#phamarcyAllDH")
	public void onRefreshAll(Event event) {
		reloadModel(lastSearchModel);
	}

	@Listen("onDelete = #incList #lbList")
	public void onDelete(Event event) {
		final Product obj = (Product) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE)
				+ " này?";
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
		final OrderDetail obj = (OrderDetail) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE)
				+ " này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					if (obj.getOrderDetailId() != null) {
						new OrderDetailDao().delete(obj);
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
		arguments.put("parent", phamarcyAllDH);
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

	@SuppressWarnings({ "unchecked" })
	@Listen("onChooseProduct =#phamarcyAllDH")
	public void onChooseProduct(Event event) {
		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		Boolean isUpdate = (Boolean) arguments.get("isUpdate");
		OrderDetail quotationDetail = (OrderDetail) arguments.get("OrderDetail");

		if (isUpdate == null) {// create
			lstQuotationDetail.add(quotationDetail);
		} else {
			lstQuotationDetail.set(indexUpdate, quotationDetail);
		}

		viewBaoGia();
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
		
		//quotation.setOrderDate(new Date());

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void viewBaoGia() {
		ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
		lbListSP.setModel(lstModel);

		BigDecimal total = new BigDecimal(0);
		for (OrderDetail obj : lstQuotationDetail) {
			if (obj != null && obj.getValue() != null) {
				total = total.add(obj.getValue());
			}
		}

		lbTongtien.setValue(formatNumber(total, "###,###,###") + "  VNĐ");
	}

}
