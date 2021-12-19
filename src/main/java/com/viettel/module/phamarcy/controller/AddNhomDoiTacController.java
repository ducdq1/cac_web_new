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
import com.viettel.module.phamarcy.BO.ThauTho;
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ThauThoDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.UnitDao;
import com.viettel.utils.Constants;

public class AddNhomDoiTacController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbTen;
	@Wire
	Window createDlg1;
	@Wire
	Listbox lbList;
	Window parent;
	Long type;
	boolean isUpdate = true;
	ThauTho unitUpdate;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");
		type = (Long) arguments.get("type");
		if (type.equals(AddThauThoKHController.LOAI_DOI_TAC)) {
			createDlg1.setTitle("Thêm nhóm đối tác");
		}
		else if (type.equals(AddThauThoKHController.LOAI_QUA)) {
			createDlg1.setTitle("Thêm loại quà");
		}
		else if (type.equals(AddThauThoKHController.LOAI_QUA_CUOI_NAM)) {
			createDlg1.setTitle("Thêm loại quà cuối năm");
		}
		initListbox();
	}

	private void initListbox() {
		List<ThauTho> units = new ThauThoDao().findAll(type, -1, -1).getLstReturn();
		ListModelArray lstModel = new ListModelArray(units);
		lbList.setModel(lstModel);
	}

	@Listen("onOK=#createDlg1")
	public void onEnter() {
		onSave();
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		String value = tbTen.getText().trim();

		if (value.isEmpty()) {
			tbTen.setErrorMessage("Trường bắt buộc nhập");
			return;
		}

		if (unitUpdate == null) {
			unitUpdate = new ThauTho();
		}

		unitUpdate.setTen(tbTen.getText().trim());
		unitUpdate.setType(type);

		new ThauThoDao().saveOrUpdate(unitUpdate);
		showNotification("Thêm thành công", Constants.Notification.INFO, 1000);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("value", tbTen.getText().trim());
		Events.sendEvent("onAddUnit", parent, map);
		// createDlg1.onClose();
		unitUpdate = null;
		initListbox();
		tbTen.setText("");
	}

	@Listen("onDeleteSP =  #lbList")
	public void onDeleteSP(Event event) {
		final ThauTho thauTho = (ThauTho) event.getData();
		String message = String.format("Xóa dữ liệu này?");
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					if (unitUpdate != null && unitUpdate.getId().equals(thauTho.getId())) {
						unitUpdate = null;
					}

					new ThauThoDao().delete(thauTho);
					showNotification("Xóa thành công", Constants.Notification.INFO, 2000);
					initListbox();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("type", type);
					map.put("value", tbTen.getText().trim());
					Events.sendEvent("onAddUnit", parent, map);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onOpenUpdate = #lbList")
	public void onOpenUpdate(Event event) {
		unitUpdate = (ThauTho) event.getData();
		tbTen.setText(unitUpdate.getTen());
	}

}
