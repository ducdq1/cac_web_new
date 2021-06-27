package com.viettel.module.phamarcy.controller;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.DAO.PhaMedicine.OrderDetailDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;

/**
 *
 * @author tuannt40
 */
public class UpdateVOrderDetailController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Textbox tbTenSP;
	@Wire
	private Textbox tbTen, tbNote;
	@Wire
	private Textbox tbSoLuong;
	@Wire
	private Textbox tbGia;
	@Wire
	private Label lbMoneyChar;
	@Wire
	private Textbox tbDonViTinh;
	@Wire
	private Window resetPassDlg;
	private Window parent;
	boolean isUpdate = false;
	OrderDetail quotationDetail;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {

		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");

		quotationDetail = (OrderDetail) arguments.get("OrderDetail");
		resetPassDlg.setTitle("Cập nhật sản phẩm");
		viewDataUpdate();

	}

	private void viewDataUpdate() {
		if (quotationDetail == null) {
			return;
		}

		tbTen.setText((quotationDetail.getName()));

		tbTenSP.setText(quotationDetail.getProductName());
		tbGia.setText(getStringFromLong(quotationDetail.getPrice()).equals("") ? "0"
				: getStringFromLong(quotationDetail.getPrice()));
		tbGia.setValue(convertStringToNumber(tbGia.getValue()));
		tbDonViTinh.setText(quotationDetail.getUnit());
		tbSoLuong.setText(getStringFromDouble(quotationDetail.getAmount()));
		tbNote.setText(quotationDetail.getNote());
		
		Long temp = getLongFromString(tbGia.getValue().trim());

		if (temp == null) {
			lbMoneyChar.setValue("");
			tbGia.setText("0");
			return;
		} else {
			lbMoneyChar.setValue(String.format("(%s)", StringUtils.numberToString(temp)));
		}

	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		boolean isAccept = true;

		String message = "";

		if (!validateTextBox(tbTenSP)) {
			return;
		}

		Double amount = getDoubleFromString(tbSoLuong.getText().trim());
		if (amount == null) {
			tbSoLuong.setErrorMessage("Số lượng nhập không đúng");
			return;
		}
		quotationDetail.setAmount(amount);
		quotationDetail.setProductName(tbTenSP.getText().trim());

		quotationDetail.setPrice(
				getLongFromString(tbGia.getText().trim()) == null ? 0L : getLongFromString(tbGia.getText().trim()));
		quotationDetail.setWarning(false);
		quotationDetail.setUnit(tbDonViTinh.getText().trim());
		quotationDetail.setNote(tbNote.getText().trim());

		if (new ProductDao().checkPriceHigher(quotationDetail.getProductId(), quotationDetail.getPrice())) {
			isAccept = false;
			message = "Giá bán thấp hơn giá nhập, vẫn bán giá này?";
		}

		if (!isAccept) {

			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

				public void onEvent(ClickEvent event) throws Exception {

					if (Messagebox.Button.YES.equals(event.getButton())) {
						quotationDetail.setWarning(true);
						chooseProduct(quotationDetail);
					}

				}
			};
			showDialogConfirm(message, null, clickListener);
		} else {
			chooseProduct(quotationDetail);
		}

	}

	private void chooseProduct(OrderDetail quotationDetail) {
		Long price = quotationDetail.getPrice();
		Double amount = quotationDetail.getAmount();

		if (price != null) {
			quotationDetail.setPriceString(formatNumber(price, "###,###,###"));
		}

		if (amount != null) {
			quotationDetail.setSl(formatNumber(amount, "###,###,###.####"));
		}

		quotationDetail.getValueString();

		HashMap<String, Object> arg = new HashMap<>();
		String message = "Cập nhật sản phẩm thành công.";

		showNotification(message, Constants.Notification.INFO, 1500);
		new OrderDetailDao().saveOrUpdate(quotationDetail);

		arg.put("OrderDetail", quotationDetail);

		Events.sendEvent("onChooseProduct", parent, arg);

		resetPassDlg.onClose();
	}
	
	@Listen("onChanging = #tbGia")
	public void onChanging(org.zkoss.zk.ui.event.InputEvent event) {

		String input = (String) event.getValue();

		if (input.trim().length() > 0) {

			Long temp = getLongFromString(input);

			if (temp == null) {

				lbMoneyChar.setValue("");
				tbGia.setText("0");
				return;

			}

			lbMoneyChar.setValue(String.format("(%s)", StringUtils.numberToString(temp)));
			if (input.length() > 3) {
				if (input.contains(".")) {
					String strTemp = input.substring(input.lastIndexOf("."), input.length());
					if (strTemp.length() != 4) {
						String converted = convertStringToNumber(input);
						tbGia.setValue(converted);
						return;
					}
				}else{
					String converted = convertStringToNumber(input);
					tbGia.setValue(converted);
				}
			}

		} else {
			lbMoneyChar.setValue("");
			tbGia.setText("");

		}

	}
	
}
