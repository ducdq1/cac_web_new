package com.viettel.module.phamarcy.controller;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.Area;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.module.phamarcy.DAO.PhaMedicine.AreaDao;
import com.viettel.utils.Constants;

/**
 *
 * @author tuannt40
 */
public class AddKVController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Textbox tbTenSP;
	 
	@Wire
	private Window resetPassDlg;
	private Window parent;
	private Area area;
	boolean isUpdate = false;
	List<Area> lstArea;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");
		area = (Area) arguments.get("area");
		if (area != null) {
			viewData();
			isUpdate = true;
			resetPassDlg.setTitle("Cập nhật địa chỉ");
		}
	}

	private void viewData() {
		tbTenSP.setText(area.getAreaName());
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		 
		if (!validateTextBox(tbTenSP)) {
			return;
		}

		 
		if (area == null) {
			area = new Area();
		}
		
		area.setAreaName(tbTenSP.getText().trim());

		if (new AreaDao().checkExistStreet(tbTenSP.getText().trim().toLowerCase(),area.getAreaId())) {
			tbTenSP.setErrorMessage("Khu vực này đã tồn tại. Vui lòng nhập Khu vực khác");
			return;
		}
		
		new AreaDao().saveOrUpdate(area);

		String message = "Thêm mới Khu vực thành công.";
		if (isUpdate) {
			message = "Cập nhật Khu vực thành công.";
		}

		showNotification(message, Constants.Notification.INFO, 2000);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("product", area);
		Events.sendEvent("onRefreshALL", parent, map);
		resetPassDlg.onClose();
	}
}
