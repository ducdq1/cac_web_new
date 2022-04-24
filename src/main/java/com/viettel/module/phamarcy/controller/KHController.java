package com.viettel.module.phamarcy.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.Category;
import com.viettel.module.phamarcy.BO.Area;
import com.viettel.module.phamarcy.BO.Customer;
import com.viettel.module.phamarcy.BO.Order;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.CustomerDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportExcell;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.StreetDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;


/**
 *
 * @author tuannt40
 */
public class KHController extends BaseComposer {

	@Wire
	private Textbox tbTenKH, tbSoDT, tbDiaChi, tbNhanVien;
	@Wire
	private Datebox dbCreateFromDay,dbCreateToDay;
	// End search form
	@Wire
	private Paging userPagingBottom;
	@Wire
	private Listbox lbList,lbLayHang; // List ho so

	@Wire
	private Window phamarcyAllDH; // View danh sach {phamarcyAllDH.zul)
	@Wire
	private Label lbTongtien;
	@Wire
	private Radiogroup rdg;
	PhamarcyFileModel lastSearchModel;
	private List<Customer> lstProducts;
	private List<OrderDetail> lstQuotationDetail;
	private Order quotation;
	int indexUpdate;// cap nhat sp bao gia
	@Wire
	private Button btnClose;
 

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		onSearch();

	}

	@Listen("onClick =  #btnSearch")
	public void onSearch() {
		lastSearchModel = new PhamarcyFileModel();
		lastSearchModel.setTenHK(tbTenKH.getText().trim());
		lastSearchModel.setDiaChi(tbDiaChi.getText().trim());
		lastSearchModel.setSoDT(tbSoDT.getText().trim());
 
		lastSearchModel.setCreateFromDate(dbCreateFromDay.getValue());
		lastSearchModel.setCreateToDate(dbCreateToDay.getValue());
		  
		
		lastSearchModel.setSurveyName(tbNhanVien.getText().trim());
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
			CustomerDao dao = new CustomerDao();
			PagingListModel plm = dao.findCustomers(searchModel, start, take);

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

	@Listen("onClick =  #btnAdd")
	public void onAdd() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAllDH);
		createWindow("windowView", "/Pages/module/phamarcy/addKH.zul", arguments, Window.MODAL);
	}

	@Listen("onRefreshALL =#phamarcyAllDH")
	public void onRefreshAll(Event event) {
		reloadModel(lastSearchModel);
	}

	@Listen("onDelete = #lbList")
	public void onDelete(Event event) {
		final Customer obj = (Customer) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, "địa chỉ") + " này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {					
					new CustomerDao().delete(obj);
					showNotification("Xóa khách hàng thành công", Constants.Notification.INFO, 2000);
					reloadModel(lastSearchModel);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onOpenUpdate = #lbList")
	public void onOpenUpdate(Event event) {
		Customer obj = (Customer) event.getData();

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAllDH);
		arguments.put("Customer", obj);
		createWindow("windowView", "/Pages/module/phamarcy/addKH.zul", arguments, Window.MODAL);

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

	/**
	 * set chon item 0 trong combo loai tep dinh kem
	 */
	public void getSelectedIndexInModel() {
		 
		 
	}
	
	public void getLayHangSelectedIndexInModel(){
		 lbLayHang.setSelectedIndex(0);
	 
}
	
	@Listen("onClick=#btnPrint")
	public void onPrint() throws FileNotFoundException {
		PagingListModel page=new CustomerDao().findCustomers(lastSearchModel, -2, -2);
		List<Customer> customers = page.getLstReturn();
		
		if (customers.isEmpty()) {
			showNotification("Không có khách hàng nào để in", Constants.Notification.ERROR, 3000);
			return;
		}
		
		File f = new ExportExcell().exportKhacHang(customers);
		
		Filedownload.save(f, "application/application/x-xls");
	}
	
	public void getStreetSelectedIndexInModel() { 
	}

}
