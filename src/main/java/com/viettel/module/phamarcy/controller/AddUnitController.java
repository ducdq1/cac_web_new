package com.viettel.module.phamarcy.controller;

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
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.UnitDao;
import com.viettel.utils.Constants;

public class AddUnitController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbValue;
	@Wire
	Window createDlg1;
	@Wire
	Listbox lbList;
	Window parent;
	int type, productType;
	boolean isUpdate = true;
	Unit unitUpdate;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		type = (int) arguments.get("type");
		parent = (Window) arguments.get("parent");
		productType = (int) arguments.get("productType");
		if (type == AddProductController.TYPE_XUAT_XU) {
			createDlg1.setTitle("Thêm xuất xứ");
		} else if (type == AddProductController.TYPE_DVT) {
			createDlg1.setTitle("Thêm đơn vị tính");
		} else if (type == AddProductController.TYPE_DONG_GOI_VIEN_THUNG) {
			createDlg1.setTitle("Thêm đóng gói viên/thùng");
		} else if (type == AddProductController.TYPE_KICK_THUOC) {
			createDlg1.setTitle("Thêm kích thước");
		} else if (type == AddProductController.TYPE_NOI_SX) {
			createDlg1.setTitle("Thêm nơi sản xuất");
		} else if (type == AddProductController.TYPE_TINH_NANG) {
			createDlg1.setTitle("Thêm tính năng");
		} else if (type == AddProductController.TYPE_TRONG_LUONG_THUNG) {
			createDlg1.setTitle("Thêm trọng lượng thùng");
		} else if (type == AddProductController.TYPE_THONG_SO_KT) {
			createDlg1.setTitle("Thêm thông số kỹ thuật");
		} else if (type == AddProductController.TYPE_BAO_HANH) {
			createDlg1.setTitle("Thêm thông tin bảo hành");
		} else if (type == AddProductController.TYPE_MAU_SAC) {
			createDlg1.setTitle("Thêm mô tả màu sắc");
		}

		initListbox(type);
	}

	private void initListbox(int type) {
		List<Unit> units = new UnitDao().findUnit("", type, productType).getLstReturn();
		ListModelArray lstModel = new ListModelArray(units);
		lbList.setModel(lstModel);
	}

	@Listen("onOK=#createDlg1")
	public void onEnter() {
		onSave();
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		String value = tbValue.getText().trim();

		if (value.isEmpty()) {
			showNotification("Chưa nhập giá trị", Constants.Notification.WARNING, 1000);
			return;
		}
		if (type == AddProductController.TYPE_TINH_NANG
				|| type == AddProductController.TYPE_THONG_SO_KT) {
			String values[] = value.split(";");
			for (int i = 0; i < values.length; i++) {
				String temp = values[i];
				if (temp.length() > 80) {
					showNotification("Vượt quá 80 ký tự cho phép trên 1 dòng. Nhập ; để xuống dòng", Constants.Notification.WARNING, 2000);
					return;
				}
			}
		}
		if (unitUpdate == null) {
			unitUpdate = new Unit();
			unitUpdate.setType(Long.valueOf(type));
		}
		unitUpdate.setProductType(Long.valueOf(productType));
		unitUpdate.setValue(tbValue.getText().trim());
		new UnitDao().saveOrUpdate(unitUpdate);
		showNotification("Thêm thành công", Constants.Notification.INFO, 1000);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("value", tbValue.getText().trim());
		Events.sendEvent("onAddUnit", parent, map);
		// createDlg1.onClose();
		unitUpdate = null;
		initListbox(type);
		tbValue.setText("");
	}

	@Listen("onDeleteSP =  #lbList")
	public void onDeleteSP(Event event) {
		unitUpdate = (Unit) event.getData();
		String message = String.format("Xóa dữ liệu này?");
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new UnitDao().delete(unitUpdate);
					showNotification("Xóa thành công", Constants.Notification.INFO, 2000);
					initListbox(type);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("type", type);
					map.put("value", tbValue.getText().trim());
					Events.sendEvent("onAddUnit", parent, map);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onOpenUpdate = #lbList")
	public void onOpenUpdate(Event event) {
		unitUpdate = (Unit) event.getData();
		tbValue.setText(unitUpdate.getValue());
	}

}
