package com.viettel.module.phamarcy.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.GiayDan;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.module.phamarcy.DAO.PhaMedicine.GiayDanDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.UnitDao;
import com.viettel.utils.Constants;

public class ThemGiayDanController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbNhanVien,tbSoLuong;
	@Wire
	private Datebox dbNgayNhan;
	@Wire
	Window createDlg1;
	@Wire
	Listbox lbList;
	Window parent;
	int type, productType;
	boolean isUpdate = true;
	GiayDan unitUpdate;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");
		

		initListbox();
	}

	private void initListbox() {
		List<GiayDan> units = new GiayDanDao().findGiayDan("").getLstReturn();
		ListModelArray lstModel = new ListModelArray(units);
		lbList.setModel(lstModel);
	}

	@Listen("onOK=#createDlg1")
	public void onEnter() {
		onSave();
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		String nhanVien = tbNhanVien.getText().trim();

		if (nhanVien.isEmpty()) {
			tbNhanVien.setErrorMessage("Trường bắt buộc nhập");
			return;
		}
		
		String soLuong = tbSoLuong.getText().trim();
		
		if (soLuong.isEmpty() || getLongFromString(soLuong) == null) {
			tbSoLuong.setErrorMessage("Trường bắt buộc nhập");
			return;
		}
		
		Date ngayNhan = dbNgayNhan.getValue();
		
		if (ngayNhan == null) {
			dbNgayNhan.setErrorMessage("Trường bắt buộc nhập");
			return;
		} 
		
		
		if (unitUpdate == null) {
			unitUpdate = new GiayDan();
		}
		
		unitUpdate.setNhanVien(nhanVien);
		unitUpdate.setSoLuong(getLongFromString(soLuong));
		unitUpdate.setNgayNhan(ngayNhan);		
		
		new GiayDanDao().saveOrUpdate(unitUpdate);
		
		showNotification("Thêm thành công", Constants.Notification.INFO, 1000);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
	
		Events.sendEvent("onAddUnit", parent, map);
		// createDlg1.onClose();
		unitUpdate = null;
		initListbox();
		tbNhanVien.setText("");
		tbSoLuong.setText("");
		dbNgayNhan.setValue(new Date());
		
	}

	@Listen("onDeleteSP =  #lbList")
	public void onDeleteSP(Event event) {
		unitUpdate = (GiayDan) event.getData();
		String message = String.format("Xóa dữ liệu này?");
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new GiayDanDao().delete(unitUpdate);
					showNotification("Xóa thành công", Constants.Notification.INFO, 2000);
					initListbox();
					HashMap<String, Object> map = new HashMap<String, Object>();
					Events.sendEvent("onAddUnit", parent, map);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onOpenUpdate = #lbList")
	public void onOpenUpdate(Event event) {
		unitUpdate = (GiayDan) event.getData();
		tbNhanVien.setText(unitUpdate.getNhanVien());
		tbSoLuong.setText(unitUpdate.getSoLuong().toString());
		dbNgayNhan.setValue(unitUpdate.getNgayNhan());
	}

}
