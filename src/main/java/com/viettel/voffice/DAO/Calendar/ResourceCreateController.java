/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Calendar;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Calendar.Resources;
import com.viettel.voffice.DAOHE.Calendar.ResourceDAOHE;

/**
 *
 * @author giangpn
 */
public class ResourceCreateController extends BaseComposer {

	@Wire
	Textbox txtResourceId, txtResourceName, txtResourceCode, txtDeptName, txtDeptId, txtResourceInf;
	@Wire
	Listbox lbResourceType;
	@Wire
	Window resourceCreateWnd;

	public ResourceCreateController() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		CategoryDAOHE cdhe = new CategoryDAOHE();
		List lstResourceType = cdhe.findAllCategorySearch(Constants.CATEGORY_TYPE.RESOURCE_TYPE, getDeptId());
		ListModelArray lstModel = new ListModelArray(lstResourceType);
		lbResourceType.setModel(lstModel);
		lbResourceType.renderAll();

		loadInfoToForm();
	}

	public void loadInfoToForm() {
		Long resourceId = (Long) Executions.getCurrent().getArg().get("resourceId");
		if (resourceId != null) {
			ResourceDAOHE rdhe = new ResourceDAOHE();
			Resources rs = rdhe.findById(resourceId);
			txtResourceId.setValue(rs.getResourceId().toString());
			txtResourceName.setValue(rs.getResourceName());
			txtResourceCode.setValue(rs.getResourceCode());
			if (rs.getDeptId() != null) {
				txtDeptId.setValue(rs.getDeptId().toString());
				txtDeptName.setValue(rs.getDeptName());
			}
			txtResourceInf.setValue(rs.getResourceInfo());
			if (rs.getResourceType() != null) {
				for (int i = 0; i < lbResourceType.getListModel().getSize(); i++) {
					Category ct = (Category) lbResourceType.getListModel().getElementAt(i);
					if (rs.getResourceType().equals(ct.getCategoryId())) {
						lbResourceType.setSelectedIndex(i);
						break;
					}
				}
			}
		} else {
			txtResourceId.setValue("");
			txtResourceName.setValue("");
			txtResourceCode.setValue("");
			txtDeptId.setValue("");
			txtDeptName.setValue("");
			txtResourceInf.setValue("");
			lbResourceType.setSelectedIndex(0);
		}
	}

	@Listen("onClick=#btnSave")
	public void onSave() {
		Resources rs = new Resources();
		if (txtResourceId.getValue() != null && !txtResourceId.getValue().isEmpty()) {
			rs.setResourceId(Long.parseLong(txtResourceId.getValue()));
		}
		if (txtResourceName.getValue() != null && txtResourceName.getValue().trim().length() == 0) {
			showNotification("Phải nhập tên tài nguyên", Constants.Notification.ERROR);
			txtResourceName.focus();
			return;
		}
		rs.setResourceName(txtResourceName.getValue().trim());

		if (txtResourceCode.getValue() != null && txtResourceCode.getValue().trim().length() == 0) {
			showNotification("Phải nhập mã tài nguyên", Constants.Notification.ERROR);
			txtResourceCode.focus();
			return;
		}
		rs.setResourceCode(txtResourceCode.getValue().trim());
		if (txtDeptId.getValue() != null && !txtDeptId.getValue().isEmpty()) {
			rs.setDeptId(Long.parseLong(txtDeptId.getValue()));
			rs.setDeptName(txtDeptName.getValue());
		} else {
			rs.setDeptId(null);
			rs.setDeptName(null);
		}
		rs.setResourceInfo(txtResourceInf.getValue().trim());
		if (lbResourceType.getSelectedItem() != null) {
			int idx = lbResourceType.getSelectedItem().getIndex();
			if (idx > 0l) {
				rs.setResourceType((Long) lbResourceType.getSelectedItem().getValue());
				rs.setResourceTypeName(lbResourceType.getSelectedItem().getLabel());
			} else {
				showNotification("Phải chọn loại tài nguyên", Constants.Notification.ERROR);
				lbResourceType.focus();
				return;

			}
		}
		rs.setStatus(1l);

		ResourceDAOHE rdhe = new ResourceDAOHE();
		if (rdhe.hasDuplicate(rs)) {
			showNotification("Trùng (tên hoặc mã) với tài nguyên đã tạo trên hệ thống");
			return;
		}
		rdhe.saveOrUpdate(rs);

		if (txtResourceId.getValue() != null && !txtResourceId.getValue().isEmpty()) {
			// update thì detach window
			resourceCreateWnd.detach();
		} else {
			// them moi thi clear window
			loadInfoToForm();
		}
		showNotification("Lưu thành công", Constants.Notification.INFO);

		Window parentWnd = (Window) Path.getComponent("/resourceWnd");
		Events.sendEvent(new Event("onReload", parentWnd, null));
	}

	@Listen("onClick=#btnShowDept")
	public void onOpenDeptSelect() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("idOfDisplayNameComp", "/resourceCreateWnd/txtDeptName");
		args.put("idOfDisplayIdComp", "/resourceCreateWnd/txtDeptId");
		Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
		showDeptDlg.doModal();
	}
}
