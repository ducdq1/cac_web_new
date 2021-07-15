package com.viettel.module.phamarcy.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem; 
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportExcell;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author tuannt40
 */
public class ProductController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire("#incSearchFullForm #fullSearchGbx") // Form search
	private Groupbox fullSearchGbx;
	@Wire("#incSearchFullForm #tbMaSP") 
	private Textbox tbMaSP;
	@Wire("#incSearchFullForm #tbMaHangHoa") 
	private Textbox tbMaHangHoa;
	// @Wire("#incSearchFullForm #tbTenSP") // Ten thuoc
	// private Textbox tbTenSP;
	@Wire
	private Paging userPagingBottom;
	@Wire("#incListSP #lbListSP")
	private Listbox lbListSP;
	@Wire
	private Window phamarcyAll;
	@Wire
	private Label lbTongtien;
	@Wire("#incSearchFullForm #cbLoaiSP")
	private Combobox cbLoaiSP;

	PhamarcyFileModel lastSearchModel;
	private List<Product> lstProducts;
	int indexUpdate;// cap nhat sp bao gia

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		lastSearchModel = new PhamarcyFileModel();
		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		onSearch();
	}

	@Listen("onClick = #incSearchFullForm #btnSearch")
	public void onSearch() {
		lastSearchModel.setProductCode(tbMaSP.getText().trim());
		lastSearchModel.setMaHangHoa(tbMaHangHoa.getText().trim());

		int index = cbLoaiSP.getSelectedIndex();
		lastSearchModel.setProductType(index);
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
			PagingListModel plm = dao.findProducts(searchModel, start, take);

			userPagingBottom.setTotalSize(plm.getCount());
			if (plm.getCount() == 0) {
				userPagingBottom.setVisible(false);
			} else {
				userPagingBottom.setVisible(true);
			}

			lstProducts = plm.getLstReturn();

			ListModelArray lstModel = new ListModelArray(lstProducts);
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

	@Listen("onClick = #incSearchFullForm #btnPrint")
	public void onPrint() {
		try {

			int index = cbLoaiSP.getSelectedIndex();
			if (index <= 0) {
				showNotification("Bạn phải chọn loại SP để in tem", Constants.Notification.WARNING, 2000);
				return;
			}
			File file;
			Set<Listitem> lstItemSelected = lbListSP.getSelectedItems();
			int size = lstItemSelected.size();
			if (size > 0) {
				List<Product> lstProductsSelected = new ArrayList<Product>();
				for (Listitem item : lstItemSelected) {
					Product p = (Product) item.getValue();
					lstProductsSelected.add(p);
				}

				file = new ExportExcell().exportExcell(lstProductsSelected, index - 1);

			} else {
				onSearch();

				file = new ExportExcell().exportExcell(lstProducts, index - 1);
			}
			Filedownload.save(file, "application/application/x-xls");

		} catch (Exception e) {
			e.printStackTrace();
			showNotification("Có lỗi xãy ra", Constants.Notification.INFO, 2000);
		}
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

	@Listen("onOpenUpdate = #incListSP #lbListSP")
	public void onOpenUpdate(Event event) {
		Product obj = (Product) event.getData();

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("product", obj);
		createWindow("windowView", "/Pages/module/phamarcy/addProduct.zul", arguments, Window.MODAL);

	}
	
}
