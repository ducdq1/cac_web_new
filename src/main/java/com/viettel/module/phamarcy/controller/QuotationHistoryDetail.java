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
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.utils.Constants;

/**
 *
 * @author tuannt40
 */
public class QuotationHistoryDetail extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3722739384534389104L;
	@Wire
	private Listbox lbListSP;
	@Wire
	private Window detail;
	@Wire
	private Label tbTenBG, tbDiaChi, tbTongTien, tbNguoiBaoGia, lbNgayBaoGia, tbPhone;
	private List<QuotationDetail> lstQuotationDetail;
	private Quotation quotation;
	QuotationDetail objUpdate;
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		quotation = (Quotation) arguments.get("Quotation");
		lstQuotationDetail = (List<QuotationDetail>) arguments.get("lst");
		detail.setTitle("Chi tiết bản báo giá: " + quotation.getQuotationNumber());
		ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
		lbListSP.setModel(lstModel);
		BigDecimal total = new BigDecimal(0);
		for (QuotationDetail detail : lstQuotationDetail) {
			try {
				total = total.add(new BigDecimal(detail.getAmount()).multiply(new BigDecimal(detail.getPrice())));
			} catch (Exception e) {

			}
		}

		if (quotation != null) {
			tbTenBG.setValue(quotation.getCompanyName());
			tbDiaChi.setValue(quotation.getCompanyAdd());
			tbNguoiBaoGia.setValue(quotation.getCreateUserFullName());
			tbTongTien.setValue(formatNumber(total, "###,###,###") + " VNĐ");
			lbNgayBaoGia.setValue(getFormatDate(quotation.getQuotationDate()));
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
					new ExportFileDAO().exportBaoGia(quotation, lstQuotationDetail, 0);
				}else if(Messagebox.Button.NO.equals(event.getButton())){
					new ExportFileDAO().exportBaoGia(quotation, lstQuotationDetail, 1);
				}
				
			}
		};

		showDialogConfirmPrint("Bạn muốn hiển thị tên báo giá hay tên sản phẩm?", null, clickListener);

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

	@Listen("onDelete =#lbListSP")
	public void onDelete(Event event) {
		final QuotationDetail obj = (QuotationDetail) event.getData();

		String message = "Bạn muốn xóa sản phẩm này khỏi báo giá?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new QuotationDetailDao().deleteQuotationDetail(obj.getQuotationDetailId());
					showNotification("Xóa sản phẩm thành công", Constants.Notification.INFO, 2000);
					lstQuotationDetail.remove(obj);

					ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
					lbListSP.setModel(lstModel);
					
					BigDecimal total = new BigDecimal(0);
					for (QuotationDetail detail : lstQuotationDetail) {
						try {
							total = total.add(new BigDecimal(detail.getAmount()).multiply(new BigDecimal(detail.getPrice())));
						} catch (Exception e) {

						}
					}
					
					tbTongTien.setValue(formatNumber(total, "###,###,###") + " VNĐ");
					
				}

			}
		};

		showDialogConfirm(message, null, clickListener);
	}
	
	@Listen("onOpenUpdateSP =#lbListSP")
	public void onOpenUpdateSP(Event event) {
		objUpdate= (QuotationDetail) event.getData();
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", detail);
		arguments.put("QuotationDetail", objUpdate);
		createWindow("windowView", "/Pages/module/phamarcy/chooseProduct.zul", arguments, Window.MODAL);

	}
	@SuppressWarnings("rawtypes")
	@Listen("onChooseProduct =#detail")
	public void onChooseProduct(Event event) {
		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		objUpdate = (QuotationDetail) arguments.get("quotationDetail");
		ListModelArray lstModel = new ListModelArray(lstQuotationDetail);
		lbListSP.setModel(lstModel);
		new QuotationDetailDao().saveOrUpdate(objUpdate);
	}
	
}
