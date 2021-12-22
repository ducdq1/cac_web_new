package com.viettel.module.phamarcy.controller;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.model.UserToken;
import com.viettel.module.phamarcy.BO.Customer;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.ThauThoKH;
import com.viettel.module.phamarcy.BO.ThauThoSearchModel;
import com.viettel.module.phamarcy.DAO.PhaMedicine.CustomerDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportKHExcell;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ThauThoKHDao;
import com.viettel.module.phamarcy.utils.EncryptionUtil;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.ws.ProductService;

/**
 *
 * @author tuannt40
 */
@SuppressWarnings("serial")
public class ThauThoKHController extends BaseComposer {

	@Wire
	private Textbox tbTimKiem;
	@Wire
	private Datebox dbCreateFromDay, dbCreateToDay;
	// End search form
	@Wire
	private Paging userPagingBottom;
	@Wire
	private Listbox lbList, lbLayHang; // List ho so

	@Wire
	private Window phamarcyAllDH; // View danh sach {phamarcyAllDH.zul)
	@Wire
	private Label lbTongtien;
	@Wire
	private Radiogroup rdg;
	ThauThoSearchModel lastSearchModel;
	private List<ThauThoKH> lstProducts;
	int indexUpdate;// cap nhat sp bao gia
	@Wire
	private Button btnClose;

	int userType = -1;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		onSearch();
		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		HttpSession httpSession = req.getSession(true);
		UserToken userToken = (UserToken) httpSession.getAttribute("userToken");
		if (userToken != null) {
			userType = userToken.getUserType() != null ? userToken.getUserType().intValue() : -1;
		}

	}

	@Listen("onClick =  #btnSearch")
	public void onSearch() {
		lastSearchModel = new ThauThoSearchModel();
		lastSearchModel.setFromDate(dbCreateFromDay.getValue());
		lastSearchModel.setToDate(dbCreateToDay.getValue());

		lastSearchModel.setSearchText(tbTimKiem.getText().trim());
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
	private void reloadModel(ThauThoSearchModel searchModel) {
		try {
			int take = userPagingBottom.getPageSize();
			int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
			PagingListModel plm = new ThauThoKHDao().findAll(searchModel, start, take);

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
		createWindow("windowView", "/Pages/module/phamarcy/addThauThoKH.zul", arguments, Window.MODAL);
	}

	@Listen("onRefreshALL =#phamarcyAllDH")
	public void onRefreshAll(Event event) {
		reloadModel(lastSearchModel);
	}

	@Listen("onDelete = #lbList")
	public void onDelete(Event event) {
		if (userType != 3) {// QLBH
			showNotification("Bạn không có quyền thực hiện", Constants.Notification.ERROR, 2000);
			return;
		}

		final ThauThoKH obj = (ThauThoKH) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, "Khách hàng") + " này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new ThauThoKHDao().delete(obj);
					showNotification("Xóa khách hàng thành công", Constants.Notification.INFO, 2000);
					reloadModel(lastSearchModel);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onOpenUpdate = #lbList")
	public void onOpenUpdate(Event event) {

		if (userType != 3) {// QLBH
			showNotification("Bạn không có quyền thực hiện", Constants.Notification.ERROR, 2000);
			return;
		}

		ThauThoKH obj = (ThauThoKH) event.getData();

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAllDH);
		arguments.put("thauThoKH", obj);
		createWindow("windowView", "/Pages/module/phamarcy/addThauThoKH.zul", arguments, Window.MODAL);

	}

	@Listen("onClick=#btnPrint")
	public void onPrint() {
		PagingListModel page = new ThauThoKHDao().findAll(lastSearchModel, -2, -2);
		List<ThauThoKH> lstProduct = page.getLstReturn();

		if (lstProduct.isEmpty()) {
			showNotification("Không có khách hàng nào để in", Constants.Notification.ERROR, 3000);
			return;
		}

		String filePath = new ExportKHExcell().exportData(lstProduct);
		if (filePath != null) {
			String URL;
			URL = "Pages/module/phamarcy/generalview/viewPDF.zul?filePath=";
			URL = URL + EncryptionUtil.encode(filePath);
			// URL = URL + (path);
			String link = String.format(
					"window.open('%s','','fullscreen=yes,scrollbars=1,resizable=1')", URL);
			Clients.evalJavaScript(link);

		} else {
			showNotification("Có lỗi xãy ra");
		}
	}

}
