package com.viettel.module.phamarcy.controller;

import java.util.Calendar;
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
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.OrderDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author tuannt40
 */
public class QuotationHistory extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire("#incSearchFullForm #fullSearchGbx")
	private Groupbox fullSearchGbx;
	@Wire("#incSearchFullForm #tbMaBG")
	private Textbox tbMaBG;
	@Wire("#incSearchFullForm #tbNguoiBaoGia")
	private Textbox tbNguoiBaoGia;
	@Wire("#incSearchFullForm #dbFromDay")
	private Datebox dbFromDay;
	@Wire("#incSearchFullForm #dbToDay")
	private Datebox dbToDay;

	@Wire
	private Paging userPagingBottom;
	@Wire
	private Listbox lbList;

	@Wire
	private Listbox lbListSP;
	@Wire
	private Window phamarcyAll;

	PhamarcyFileModel lastSearchModel;
	private List<Product> lstProducts;
	int indexUpdate;// cap nhat sp bao gia
	@Wire
	private Label lbTongtien;

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
		lastSearchModel.setQuotationCode(tbMaBG.getText().trim());
		lastSearchModel.setQuotationUserFullName(tbNguoiBaoGia.getText().trim());
		lastSearchModel.setFromDate(dbFromDay.getValue());
		lastSearchModel.setToDate(dbToDay.getValue());
		try {
			userPagingBottom.setActivePage(0);
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
			int take = userPagingBottom.getPageSize();
			int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
			QuotationDao dao = new QuotationDao();
			PagingListModel plm = dao.findFilesByReceiverAndDeptId(searchModel, start, take);

			userPagingBottom.setTotalSize(plm.getCount());
			if (plm.getCount() == 0) {
				userPagingBottom.setVisible(false);
			} else {
				userPagingBottom.setVisible(true);
			}

			lstProducts = plm.getLstReturn();

			lbTongtien.setValue(formatNumber(plm.getTongTien(), "###,###,###") + " VNĐ");

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

	@Listen("onExport =#lbList")
	public void onExport(Event event) {
		final Quotation obj = (Quotation) event.getData();
		Long id = obj.getQuotationID();
		final List<QuotationDetail> lst = new QuotationDetailDao().getListQuotationDetail(id);

		if (!lst.isEmpty()) {
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

				public void onEvent(ClickEvent event) throws Exception {

					if (Messagebox.Button.YES.equals(event.getButton())) {

						new ExportFileDAO().exportBaoGia(obj, lst, 0);
					} else if (Messagebox.Button.NO.equals(event.getButton())) {
						new ExportFileDAO().exportBaoGia(obj, lst, 1);
					}
				}
			};

			showDialogConfirmPrint("Bạn muốn hiển thị tên báo giá hay tên xuất hàng?", null, clickListener);

		} else {
			showDialogWithNoEvent("Không có sản phẩm nào", "Thông báo");
		}
	}

	@Listen("onToOrder =#lbList")
	public void onToOrder(Event event) {
		final Quotation obj = (Quotation) event.getData();
		Long id = obj.getQuotationID();
		List<QuotationDetail> lst = new QuotationDetailDao().getListQuotationDetail(id);
		if (!lst.isEmpty()) {
			HashMap<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("parent", phamarcyAll);
			arguments.put("list", lst);
			arguments.put("quotation", obj);
			createWindow("windowView", "/Pages/module/phamarcy/xuat_don_hang.zul", arguments, Window.MODAL);
		} else {
			showDialogWithNoEvent("Không có sản phẩm nào để xem ", "Thông báo");
		}

	}

	/**
	 * hien thi dialog confirm
	 */
	public void showDialogConfirmPrint(String message, String title, EventListener<ClickEvent> clickListener) {
		if (title == null) {
			title = getLabel("confirm");
		}

		Messagebox.show(message, title, new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
				new String[] { "Tên báo giá", "Tên xuất hàng" }, Messagebox.QUESTION, null, clickListener);
	}

	@Listen("onClick = #incSearchFullForm #btnReset")
	public void onReset() {
		tbMaBG.setText("");
		tbNguoiBaoGia.setText("");
		Date fromDate = Calendar.getInstance().getTime();
		fromDate.setDate(1);
		dbFromDay.setValue(fromDate);
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

	@Listen("onDelete =#lbList")
	public void onDelete(Event event) {
		final Quotation obj = (Quotation) event.getData();

		String message = "Bạn muốn xóa báo giá này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					Long id = obj.getQuotationID();
					new QuotationDetailDao().deleteListQuotationDetail(id);
					new QuotationDao().delete(obj);
					showNotification("Xóa báo giá thành công", Constants.Notification.INFO, 2000);
					reloadModel(lastSearchModel);
				}

			}
		};

		showDialogConfirm(message, null, clickListener);
	}
	
	public String getValueOrder(Long orderId){
		Double value=new QuotationDao().getTotalValueOrder(orderId);
		
		return formatNumber(value, null);
	}
}
