package com.viettel.module.phamarcy.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.Order;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.OrderDetailDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.utils.Constants;

/**
 *
 * @author tuannt40
 */
public class OrderDetailController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3722739384534389104L;
	@Wire
	private Listbox lbListSP;
	@Wire
	private Window orderdetail;
	@Wire
	private Label tbTenBG, tbMaDH,tbDiaChi, tbTongTien, tbNguoiBaoGia, lbNgayBaoGia, tbPhone;
	private List<OrderDetail> lstQuotationDetail;
	private Order quotation;
	OrderDetail quotationDetailUpdate;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		quotation = (Order) arguments.get("Order");
		lstQuotationDetail = (List<OrderDetail>) arguments.get("lst");
		orderdetail.setTitle("Chi tiết đơn hàng: " + quotation.getOrderNumber());
		ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
		lbListSP.setModel(lstModel);
		BigDecimal total = new BigDecimal(0);
		
		for (OrderDetail detail : lstQuotationDetail) {
			try {
				total = total.add(new BigDecimal(detail.getAmount()).multiply(new BigDecimal(detail.getPrice())));
			} catch (Exception e) {

			}
		}

		if (quotation != null) {
			tbMaDH.setValue(quotation.getOrderNumber());
			tbTenBG.setValue(quotation.getCompanyName());
			tbDiaChi.setValue(quotation.getCompanyAdd());
			tbNguoiBaoGia.setValue(quotation.getCreateUserFullName());
			tbTongTien.setValue(formatNumber(total, "###,###,###") + " VNĐ");
			lbNgayBaoGia.setValue(getFormatDate(quotation.getOrderDate()));
			tbPhone.setValue(quotation.getCompanyPhone());
		}

	}

	@Listen("onClick =#btnExport")
	public void onBaoGiaInfor(Event event) {
		if (quotation == null) {
			return;
		}
		
		if (lstQuotationDetail == null || lstQuotationDetail.isEmpty()) {
			showDialogWithNoEvent("Không có sản phẩm nào", "Thông báo");
			return;
		}

		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {
				 
				if (Messagebox.Button.YES.equals(event.getButton())) {					 
					new ExportFileDAO().exportDonHang(quotation, lstQuotationDetail, 0);
				}else if(Messagebox.Button.NO.equals(event.getButton())){
					new ExportFileDAO().exportDonHang(quotation, lstQuotationDetail, 1);
				}

				
			}
		};

		showDialogConfirmPrint("Bạn muốn hiển thị tên báo giá hay tên xuất hàng?", null, clickListener);

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
	
	
	@Listen("onOpenUpdateSP =#lbListSP")
	public void onOpenUpdateSP(Event event) {
		quotationDetailUpdate = (OrderDetail) event.getData();
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", orderdetail);
		arguments.put("OrderDetail", quotationDetailUpdate);
		createWindow("windowView", "/Pages/module/phamarcy/chooseProductToOrder.zul", arguments, Window.MODAL);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onChooseProduct =#orderdetail")
	public void onChooseProduct(Event event) {
		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		quotationDetailUpdate = (OrderDetail) arguments.get("OrderDetail");
		new OrderDetailDao().saveOrUpdate(quotationDetailUpdate);
		ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
		lbListSP.setModel(lstModel);
	}
	
	@Listen("onDeleteSP =#lbListSP")
	public void onDeleteSP(Event event) {
		final OrderDetail obj = (OrderDetail) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE)
				+ " này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new OrderDetailDao().delete(obj);
					lstQuotationDetail.remove(obj);
					ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
					lbListSP.setModel(lstModel);
					showNotification("Xóa sản phẩm thành công", Constants.Notification.INFO, 2000);

				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	
}
