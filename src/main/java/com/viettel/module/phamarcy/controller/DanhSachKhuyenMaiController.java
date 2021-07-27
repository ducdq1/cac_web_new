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
import org.zkoss.zul.Datebox;
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
import com.viettel.module.phamarcy.BO.Promotion;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportExcell;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.PromotionDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

public class DanhSachKhuyenMaiController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire // Form search
	private Groupbox fullSearchGbx;
	@Wire// Ma ho so
	private Textbox tbTenKM;
	// @Wire("#incSearchFullForm #tbTenSP") // Ten thuoc
	// private Textbox tbTenSP;
	@Wire
	private Paging userPagingBottom;
	@Wire
	private Datebox dbFromDay,dbToDay;
	
	@Wire
	private Listbox lbListSP;
	@Wire
	private Window phamarcyAll;
	@Wire
	private Label lbTongtien;
	@Wire
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

	@Listen("onClick =#btnSearch")
	public void onSearch() {

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
			PromotionDao dao = new PromotionDao();
			PagingListModel plm = dao.findPromotions(tbTenKM.getText().trim(), dbFromDay.getValue(), dbToDay.getValue());

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

	@Listen("onPaging =#userPagingBottom")
	public void onPaging(Event event) {
		reloadModel(lastSearchModel);
	}

	/**
	 * Open view xem chi tiet
	 * 
	 * @param event
	 */
	@Listen("onOpenView =#lbList")
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

	@Listen("onClick = #btnAdd")
	public void onAdd() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		createWindow("windowView", "/Pages/module/phamarcy/them_khuyen_mai.zul", arguments, Window.MODAL);
	}

	@Listen("onRefreshALL =#phamarcyAll")
	public void onRefreshAll(Event event) {
		reloadModel(lastSearchModel);
	}

	@Listen("onDelete =  #lbList")
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

	@Listen("onDeleteSP =#lbListSP")
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

	@Listen("onOpenUpdate =#lbListSP")
	public void onOpenUpdate(Event event) {
		Promotion obj = (Promotion) event.getData();

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("promotion", obj);
		createWindow("windowView", "/Pages/module/phamarcy/them_khuyen_mai.zul", arguments, Window.MODAL);

	}

}
