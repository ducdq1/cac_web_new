package com.viettel.module.phamarcy.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.VOrderDetail;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.OrderDetailDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.VOrderDetailDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author tuannt40
 */
public class DoanhThuController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire("#incSearchFullForm #fullSearchGbx")
	private Groupbox fullSearchGbx;
	@Wire("#incSearchFullForm #dbFromDay")
	private Datebox dbFromDay;
	@Wire("#incSearchFullForm #dbToDay")
	private Datebox dbToDay;


	@Wire
	private Listbox lbListSP;
	@Wire
	private Window phamarcyAll;

	PhamarcyFileModel lastSearchModel;
	private List<VOrderDetail> lstProducts;
	int indexUpdate;// cap nhat sp bao gia
	@Wire
	private Label lbTongtien, lbSoDuDK, lbSoDuCK;
	BigDecimal sodudauky;
	BigDecimal phatsinh;
	BigDecimal soducuoiky;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		lastSearchModel = new PhamarcyFileModel();
		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		onReset();
		onSearch();
	}

	@Listen("onOK=#phamarcyAll")
	public void onEnter() {
		onSearch();
	}

	@Listen("onClick = #incSearchFullForm #btnSearch")
	public void onSearch() {
		lastSearchModel.setFromDate(dbFromDay.getValue());
		lastSearchModel.setToDate(dbToDay.getValue());
		try {
			reloadModel(lastSearchModel);
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
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

			if (searchModel.getFromDate() == null) {
				dbFromDay.setErrorMessage("Thời gian không được để trống");
				return;
			}

			if (searchModel.getToDate() == null) {
				dbToDay.setErrorMessage("Thời gian không được để trống");
				return;
			}

			PagingListModel plm = new VOrderDetailDao().getDoanhThu(searchModel);

			lstProducts = plm.getLstReturn();

			sodudauky = plm.getSoDuDauKy();
			phatsinh = plm.getTongTien();
			soducuoiky = sodudauky.add(phatsinh);

			lbSoDuCK.setValue(formatNumber(soducuoiky, "###,###,###") + " VNĐ");

			lbTongtien.setValue(formatNumber(phatsinh, "###,###,###") + " VNĐ");
			lbSoDuDK.setValue(formatNumber(sodudauky, "###,###,###") + " VNĐ");

			ListModelArray lstModel = new ListModelArray(lstProducts);

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

	@Listen("onClick =#btnExport")
	public void onExport(Event event) {
		if (lstProducts != null && !lstProducts.isEmpty()) {
			boolean isSuccess = new ExportFileDAO().exportDoanhThu(lstProducts, dbFromDay.getValue(),
					dbToDay.getValue(), sodudauky, phatsinh, soducuoiky);
			if (!isSuccess) {
				showDialogWithNoEvent("Xuất file không được", "Thông báo");
			}
		} else {
			showDialogWithNoEvent("Không có sản phẩm nào để xem", "Thông báo");
		}
	}


	@Listen("onClick = #incSearchFullForm #btnReset")
	public void onReset() {
		dbFromDay.setValue(new Date());
		dbToDay.setValue(new Date());
	}

	@Listen("onOpenView =#lbList")
	public void onOpenView(Event event) {
		final Quotation obj = (Quotation) event.getData();
		Long id = obj.getQuotationID();
		List<QuotationDetail> lst = new QuotationDetailDao().getListQuotationDetail(id);

		if (!lst.isEmpty()) {
			HashMap<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("Quotation", obj);
			arguments.put("lst", lst);
			createWindow("windowView", "/Pages/module/phamarcy/listQuotationDetail.zul", arguments, Window.MODAL);
		} else {
			showDialogWithNoEvent("Không có sản phẩm nào để xem ", "Thông báo");
		}
	}

	@Listen("onOpenUpdateSP = #lbListSP")
	public void onOpenUpdateSP(Event event) {
		VOrderDetail obj = (VOrderDetail) event.getData();
		indexUpdate = lstProducts.indexOf(obj);
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		OrderDetail orderDetail = new OrderDetailDao().getOrderDetail(obj.getOrderDetailId());
		if (orderDetail == null) {
			return;
		}
		arguments.put("OrderDetail", orderDetail);
		createWindow("windowView", "/Pages/module/phamarcy/updateVOrderDetail.zul", arguments, Window.MODAL);

	}

	@Listen("onChooseProduct =#phamarcyAll")
	public void onChooseProduct(Event event) {
		reloadModel(lastSearchModel);
	}

	@Listen("onDeleteSP =  #lbListSP")
	public void onDeleteSP(Event event) {
		final VOrderDetail obj = (VOrderDetail) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE)
				+ " này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new OrderDetailDao().deleteById(obj.getOrderDetailId());
					showNotification("Xóa sản phẩm thành công", Constants.Notification.INFO, 2000);
					reloadModel(lastSearchModel);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

}
