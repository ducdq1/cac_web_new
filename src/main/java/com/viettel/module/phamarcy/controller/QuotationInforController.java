package com.viettel.module.phamarcy.controller;

import java.util.Date;
import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.utils.DateTimeUtils;

/**
 *
 * @author tuannt40
 */
public class QuotationInforController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Textbox tbCompanyName;
	@Wire
	private Textbox tbCompanyAdd, tbGhiChu, tbCompanyPhone;
	@Wire
	private Datebox dbNgayBaoGia;
	@Wire
	private Datebox dbNgayHieuLuc;
	private Quotation quotation;
	Window parent;
	@Wire
	private Window resetPassDlg;
	@Wire
	private Radiogroup rg;
	@Wire
	private Radiogroup rgKhacHang;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {

		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		quotation = (Quotation) arguments.get("Quotation");
		parent = (Window) arguments.get("parent");
		
		if (quotation != null) {
			viewDate();
		}
	}

	private void viewDate() {
		tbCompanyAdd.setText(quotation.getCompanyAdd());
		tbCompanyName.setText(quotation.getCompanyName());
		dbNgayBaoGia.setValue(quotation.getQuotationDate() != null ? quotation.getQuotationDate() : new Date());
		dbNgayHieuLuc.setValue(
				quotation.getExpireDate() != null ? quotation.getExpireDate() : DateTimeUtils.addDay(new Date(), 7));
		tbCompanyPhone.setValue(quotation.getCompanyPhone());
		tbGhiChu.setValue(quotation.getNote());
		if(quotation.getNote()==null){
			tbGhiChu.setText("- Giá trên đã bao gồm VAT\n- Giá trên đã bao gồm vận chuyển đến chân công trình.");
		}
		rgKhacHang.setSelectedIndex(quotation.getType() == null ? 0 : quotation.getType().intValue());
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		if (!validateTextBox(tbCompanyName)) {
			return;
		}

		Date ngayBaoGia = dbNgayBaoGia.getValue();
		Date ngayHieuLuc = dbNgayHieuLuc.getValue();

		if (ngayBaoGia == null) {
			dbNgayBaoGia.setErrorMessage("Bắt buộc nhập");
			return;
		}

		if (ngayHieuLuc != null) {
			if (DateTimeUtils.compare2Date(ngayHieuLuc, ngayBaoGia) < 0) {
				dbNgayHieuLuc.setErrorMessage("Ngày hiệu lực không được nhỏ hơn ngày báo giá");
				return;
			}
		}

		quotation.setQuotationDate(ngayBaoGia);
		quotation.setExpireDate(ngayHieuLuc);
		quotation.setCompanyName(tbCompanyName.getText().trim());
		quotation.setCompanyAdd(tbCompanyAdd.getText().trim());
		quotation.setCompanyPhone(tbCompanyPhone.getText().trim());
		quotation.setNote(tbGhiChu.getText().trim());
		quotation.setType(Long.valueOf(rgKhacHang.getSelectedIndex()));

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("Quotation", quotation);
		arguments.put("isView", rg.getSelectedIndex());
		arguments.put("khacHang", rgKhacHang.getSelectedIndex());
		Events.sendEvent("onBaoGiaInfor", parent, arguments);
		resetPassDlg.onClose();
	}
}
