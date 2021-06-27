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
import com.viettel.module.phamarcy.BO.Order;

/**
 *
 * @author tuannt40
 */
public class OrderInforController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Textbox tbCompanyName;
	@Wire
	private Datebox dbNgayBan;
	@Wire
	private Textbox tbCompanyAdd, tbGhiChu, tbCompanyPhone;
	private Order quotation;
	Window parent;
	@Wire
	private Window resetPassDlg;
	@Wire
	private Radiogroup rg;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {

		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		quotation = (Order) arguments.get("Order");
		parent = (Window) arguments.get("parent");

		if (quotation != null) {
			viewDate();
		}
	}

	private void viewDate() {
		tbCompanyAdd.setText(quotation.getCompanyAdd());
		tbCompanyName.setText(quotation.getCompanyName());
		tbCompanyPhone.setValue(quotation.getCompanyPhone());
		tbGhiChu.setValue(quotation.getNote());
		dbNgayBan.setValue(quotation.getOrderDate());
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		if (!validateTextBox(tbCompanyName)) {
			return;
		}
		if(dbNgayBan.getValue()==null){
			dbNgayBan.setErrorMessage("Phải nhập ngày xuất hàng");
			return;
		}

		quotation.setOrderDate(dbNgayBan.getValue());
		quotation.setCompanyName(tbCompanyName.getText().trim());
		quotation.setCompanyAdd(tbCompanyAdd.getText().trim());
		quotation.setCompanyPhone(tbCompanyPhone.getText().trim());
		quotation.setNote(tbGhiChu.getText().trim());

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("Order", quotation);
		arguments.put("isView", rg.getSelectedIndex());
		Events.sendEvent("onBaoGiaInfor", parent, arguments);
		resetPassDlg.onClose();
	}
}
