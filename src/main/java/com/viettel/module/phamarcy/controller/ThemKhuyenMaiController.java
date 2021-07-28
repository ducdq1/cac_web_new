package com.viettel.module.phamarcy.controller;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.Promotion;
import com.viettel.module.phamarcy.DAO.PhaMedicine.PromotionDao;
import com.viettel.utils.Constants;

/**
 *
 * @author tuannt40
 */
public class ThemKhuyenMaiController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Textbox tbTenKM,tbNoiDung;
	@Wire
	private Intbox tbSaleNum;
	
	@Wire
	private Datebox dbFromDate,dbToDate;
	
	@Wire
	private Window resetPassDlg;
	private Window parent;
	private Promotion area;
	boolean isUpdate = false;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");
		area = (Promotion) arguments.get("promotion");
		if (area != null) {
			viewData();
			isUpdate = true;
			resetPassDlg.setTitle("Cập nhật khuyến mãi");
		}
	}

	private void viewData() {
		tbTenKM.setText(area.getName());
		tbNoiDung.setText(area.getDescription());
		tbSaleNum.setText(area.getNumberSaleOff());
		dbFromDate.setValue(area.getFromDate());
		dbToDate.setValue(area.getToDate());
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		 
		if (!validateTextBox(tbTenKM)) {
			return;
		}
		
		 
		if (area == null) {
			area = new Promotion();
		}
		
		area.setName(tbTenKM.getText().trim());
		area.setDescription(tbNoiDung.getText().trim());
		area.setFromDate(dbFromDate.getValue());
		area.setToDate(dbToDate.getValue());
		area.setIsActive(1L);
		area.setNumberSaleOff(tbSaleNum.getText().trim());
		
		 
		
		new PromotionDao().saveOrUpdate(area);

		String message = "Thêm mới khuyến mãi thành công.";
		if (isUpdate) {
			message = "Cập nhật khuyến thành công.";
		}

		showNotification(message, Constants.Notification.INFO, 2000);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("promotion", area);
		Events.sendEvent("onRefreshALL", parent, map);
		resetPassDlg.onClose();
	}
}
