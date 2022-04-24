package com.viettel.module.phamarcy.controller;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.Area;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.module.phamarcy.DAO.PhaMedicine.StreetDao;
import com.viettel.utils.Constants;

/**
 *
 * @author tuannt40
 */
public class AddDuongController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Listbox tbMaSP;
	@Wire
	private Textbox tbTenSP;

	@Wire
	private Window resetPassDlg;
	private Window parent;
	private Street street;
	boolean isUpdate = false;
	Boolean isImport;


	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");
		street = (Street) arguments.get("street");
		isImport = (Boolean) arguments.get("isImport");
		if (street != null) {
			viewData();
			isUpdate = true;
			resetPassDlg.setTitle("Cập nhật địa chỉ");
		}
	}

	private void viewData() {
		tbTenSP.setText(street.getStreetName());
	}

	@Listen("onClick = #btnSave")
	public void onSave() {

		if (!validateTextBox(tbTenSP)) {
			return;
		}

		Long streetId;
		if (street == null) {
			street = new Street();
		}

		streetId = street.getStreetId();


		street.setStreetName(tbTenSP.getText().trim());

		if (new StreetDao().checkExistStreet(tbTenSP.getText().trim().toLowerCase(), streetId)) {
			tbTenSP.setErrorMessage("Tên đường này đã tồn tại. Vui lòng nhập đường khác");
			return;
		}

		// String productNameSearch = new
		// BaseGenericForwardComposer().removeVietnameseChar(product.getProductName());

		if (isImport == null) {
			new StreetDao().saveOrUpdate(street);
		}

		String message = "Thêm mới địa chỉ thành công.";
		if (isUpdate) {
			message = "Cập nhật địa chỉ thành công.";
		}

		showNotification(message, Constants.Notification.INFO, 2000);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("product", street);
		Events.sendEvent("onRefreshALL", parent, map);
		resetPassDlg.onClose();
	}

	/**
	 * set chon item 0 trong combo loai tep dinh kem
	 */
	public void getSelectedIndexInModel() {
		 
	}

}