package com.viettel.module.phamarcy.controller;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;

/**
 *
 * @author tuannt40
 */
public class ChooseProductToOrderController extends BaseComposer {
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
	private Textbox tbDonViTinh;
	@Wire
	private Window resetPassDlg;
	private Window parent;
	private Product product;
	boolean isUpdate = false;
	OrderDetail quotationDetail;
	@Wire
	private Label lbMoneyChar;

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
		product = (Product) arguments.get("product");
		if (product != null) {// create
			viewData();
			isUpdate = false;
			resetPassDlg.setTitle("Chọn sản phẩm");
		} else {// update
			isUpdate = true;
			quotationDetail = (OrderDetail) arguments.get("OrderDetail");
			resetPassDlg.setTitle("Cập nhật sản phẩm");
			viewDataUpdate();
		}
	}

	private void viewData() {
		tbTen.setText(product.getProductName());
		tbTenSP.setText(product.getQuotationName());
		tbGia.setText(getStringFromLong(product.getPrice()).equals("") ? "0" : getStringFromLong(product.getPrice()));
		tbGia.setValue(convertStringToNumber(tbGia.getValue()));
		tbDonViTinh.setText(product.getUnit());
		
		Long temp = getLongFromString(tbGia.getValue().trim());

		if (temp == null) {
			lbMoneyChar.setValue("");
			tbGia.setText("0");
			return;
		} else {
			lbMoneyChar.setValue(String.format("(%s)", StringUtils.numberToString(temp)));
		}
		
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
		if (quotationDetail == null) {
			quotationDetail = new OrderDetail();
			quotationDetail.setUnit(product.getUnit());
			quotationDetail.setProductId(product.getProductId());
			quotationDetail.setName(product.getProductName());
			quotationDetail.setProductCode(product.getProductCode());
		} else {
			quotationDetail.setUnit(quotationDetail.getUnit());
			quotationDetail.setProductId(quotationDetail.getProductId());
		}

		String message = "";

		if (!validateTextBox(tbTenSP)) {
			return;
		}
		
		Double soluong = getDoubleFromString(tbSoLuong.getText().trim());
		if (soluong == null) {
			tbSoLuong.setErrorMessage("Số lượng nhập không đúng");
			return;
		}
		
		quotationDetail.setAmount(soluong);

		quotationDetail.setProductName(tbTenSP.getText().trim());

		quotationDetail.setPrice(
				getLongFromString(tbGia.getText().trim()) == null ? 0L : getLongFromString(tbGia.getText().trim()));
		quotationDetail.setWarning(false);
		quotationDetail.setUnit(tbDonViTinh.getText().trim());
		quotationDetail.setNote(tbNote.getText().trim());

		if (isUpdate) {
			if (new ProductDao().checkPriceHigher(quotationDetail.getProductId(), quotationDetail.getPrice())) {
				isAccept = false;
				message = "Giá bán thấp hơn giá nhập, vẫn bán giá này?";
			}
		} else {
			if (product.getPrice() != null) {
				if (quotationDetail.getPrice() < product.getPrice()) {
					isAccept = false;
					message = String.format("Giá bán thấp hơn giá nhập %s VNĐ, vẫn bán giá này?",
							(product.getPrice() - quotationDetail.getPrice()));
				} else if (quotationDetail.getPrice() <= product.getPrice()) {
					isAccept = false;
					message = "Giá bán bằng giá nhập, vẫn bán giá này?";
				}
			}
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
		// quotationDetail.setValue(new BigDecimal(0));

		/*
		 * if (price != null && amount != null) { BigDecimal total = new
		 * BigDecimal(price).multiply(new BigDecimal(amount));
		 * quotationDetail.setValue(total); }
		 */

		if (price != null) {
			quotationDetail.setPriceString(formatNumber(price, "###,###,###"));
		}

		if (amount != null) {
			quotationDetail.setSl(formatNumber(amount, "###,###,###.###"));
		}

		if (quotationDetail.getValue() != null) {
			quotationDetail.setValueString(quotationDetail.getValueString());
		}

		HashMap<String, Object> arg = new HashMap<>();
		String message = "Chọn sản phẩm thành công.";
		if (isUpdate) {
			message = "Cập nhật sản phẩm thành công.";
			arg.put("isUpdate", true);
		}

		showNotification(message, Constants.Notification.INFO, 1500);

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
