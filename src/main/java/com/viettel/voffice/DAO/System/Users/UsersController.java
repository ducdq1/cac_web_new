/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.PositionDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserBean;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.utils.Constants_XNN;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;

/**
 *
 * @author HaVM2
 */
public class UsersController extends BaseComposer {

	@Wire
	Textbox txtSearchUserName, txtSearchFullName, txtSearchStaffCode, txtSearchDept, txtSearchDeptId,
			txtSearchTelephone;
	ListModelArray lstUser;
	@Wire
	Listbox lstItems;
	@Wire
	Listbox cbSearchPosition, cbUserType;
	ListModelArray lstPosition;
	@Wire
	Popup popup;
	@Wire
	Window createDlg, roleDlg, resetPassDlg;
	@Wire
	Paging userPagingBottom;
	UserBean searchForm;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		PositionDAOHE pdhe = new PositionDAOHE();
		List lstPosData = pdhe.getSelectPosition(null);
		lstPosition = new ListModelArray(lstPosData);
		cbSearchPosition.setModel(lstPosition);

		ListModel infos = new ListModelArray(
				new String[][] { { Constants_XNN.USER_TYPE.ADMIN.toString(), "Quản trị hệ thống" },
					{"0", "Bộ phận tem" },
						{"2", "Nhân viên bán hàng" },
						{ "3", "Quản lý bán hàng" },{ "4", "Nhân viên bán hàng đại lý" }});
		cbUserType.setModel(infos);
		cbUserType.renderAll();
		cbUserType.setSelectedIndex(2);

		userPagingBottom.setActivePage(0);
		onSearch();
	}

	private void fillDataToList() {
		UserDAOHE udhe = new UserDAOHE();
		int take = userPagingBottom.getPageSize();
		int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
		PagingListModel plm = udhe.search(searchForm, start, take);
		userPagingBottom.setTotalSize(plm.getCount());
		if (plm.getCount() == 0) {
			userPagingBottom.setVisible(false);
		} else {
			userPagingBottom.setVisible(true);
		}
		ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
		lstItems.setModel(lstModel);

	}

	@Listen("onAfterRender=#lstItems")
	public void results_onAfterRender() throws IOException {
		List<Listitem> listitems = lstItems.getItems();
		for (Listitem item : listitems) {
			Users quotationDetail = item.getValue();
			String media = quotationDetail.getAvartarPath();
			if (media != null && !media.isEmpty()) {
				Listcell cell = (Listcell) item.getChildren().get(1);
				org.zkoss.zul.Image image = (org.zkoss.zul.Image) cell.getFirstChild();
					String dir_upload = ResourceBundleUtil.getString("dir_upload");
					File file = new File(dir_upload + media);
					if (file.exists()) {
						image.setContent(new org.zkoss.image.AImage(file));
					}
			}
		}
	}

	@Listen("onPaging = #userPagingBottom")
	public void onPaging(Event event) {
		fillDataToList();
	}

	@Listen("onClick=#btnSearch")
	public void onSearch() throws IOException {
		searchForm = new UserBean();
		searchForm.setUserName(txtSearchUserName.getValue());
		searchForm.setFullName(txtSearchFullName.getValue());
		searchForm.setTelephone(txtSearchTelephone.getValue());
		searchForm.setStaffCode(txtSearchStaffCode.getValue());
		searchForm.setDeptName(txtSearchDept.getValue());
		if (txtSearchDeptId.getValue() != null && txtSearchDeptId.getValue().length() > 0) {
			searchForm.setDeptId(Long.parseLong(txtSearchDeptId.getValue()));
		} else {
			if (("admin").equals(getUserName())) {
			} else {
				searchForm.setDeptId(getDeptId());
			}
		}

		if (cbSearchPosition.getSelectedItem() != null) {
			Long posId = cbSearchPosition.getSelectedItem().getValue();
			searchForm.setPosId(posId);
		}
		if (cbUserType.getSelectedItem() != null) {
			Long userType = Long.parseLong(cbUserType.getSelectedItem().getValue().toString());
			searchForm.setUserType(userType);
		}
		fillDataToList();
	}

	@Listen("onReload=#userManWindow")
	public void onReload() throws IOException {
		onSearch();
	}

	@Listen("onClick=#btnOpenCreate")
	public void onOpenCreate() {
		createDlg = (Window) Executions.createComponents("/Pages/admin/user/userCreate.zul", null, null);
		createDlg.doModal();
	}

	@Listen("onClick=#btnClose")
	public void onClose() {
		createDlg.detach();
	}

	@Listen("onEdit=#lstItems")
	public void onEdit() throws IOException {
		Listitem item = lstItems.getSelectedItem();
		Users u = item.getValue();
		UserDAOHE udhe = new UserDAOHE();
		u = udhe.findById(u.getUserId());
		if (u != null) {
			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("user", u);
			createDlg = (Window) Executions.createComponents("/Pages/admin/user/userCreate.zul", null, args);
			createDlg.setTitle("Chỉnh sửa thông tin người dùng");
			createDlg.doModal();
		}
	}

	@Listen("onRole=#lstItems")
	public void onUserRole() throws IOException {
		Listitem item = lstItems.getSelectedItem();
		Users u = item.getValue();
		UserDAOHE udhe = new UserDAOHE();
		u = udhe.findById(u.getUserId());
		if (u != null) {
			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("user", u);
			roleDlg = (Window) Executions.createComponents("/Pages/admin/user/userRole.zul", null, args);
			roleDlg.doModal();
		}
	}

	@Listen("onDelete=#lstItems")
	public void onDelete() throws IOException {
		Listitem item = lstItems.getSelectedItem();
		final Users u = item.getValue();
		if (u.getUserId().equals(getUserId())) {
			showNotification("Không được phép xóa chính account của mình");
			return;
		}

		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					UserDAOHE udhe = new UserDAOHE();
					Users user = udhe.findById(u.getUserId());
					if (user != null) {

						user.setStatus(-1l);
						udhe.update(user);
						onSearch();
						showSuccessNotification("Xóa thành công");
					}
				}
			}
		};
		showDialogConfirm("Bạn có đồng ý xóa tài khoản này?", "Xác nhận", clickListener);

	}

	@Listen("onLock=#lstItems")
	public void onLock() throws IOException {
		Listitem item = lstItems.getSelectedItem();
		Users u = item.getValue();
		UserDAOHE udhe = new UserDAOHE();
		u = udhe.findById(u.getUserId());
		if (u != null) {
			u.setStatus(0l);
			udhe.update(u);
			onSearch();
			showSuccessNotification("Khóa thành công");

		}
	}

	@Listen("onUnlock=#lstItems")
	public void onUnLock() throws IOException {
		Listitem item = lstItems.getSelectedItem();
		Users u = item.getValue();
		UserDAOHE udhe = new UserDAOHE();
		u = udhe.findById(u.getUserId());
		if (u != null) {
			u.setStatus(1l);
			udhe.update(u);
			onSearch();
			showSuccessNotification("Mở khóa thành công");

		}

	}

	@Listen("onResetPass=#lstItems")
	public void onResetPass() throws IOException {
		Listitem item = lstItems.getSelectedItem();
		Users u = item.getValue();
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("userId", u.getUserId());
		if (u.getUserId() == getUserId()) {
			args.put("type", 1l);
			resetPassDlg = (Window) Executions.createComponents("/Pages/admin/user/userChangePass.zul", null, args);
			resetPassDlg.doModal();
		} else {
			resetPassDlg = (Window) Executions.createComponents("/Pages/admin/user/userResetPass.zul", null, args);
			resetPassDlg.doModal();
		}
	}

	@Listen("onClick=#btnShowSearchDept")
	public void onOpenDeptSelect() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("idOfDisplayNameComp", "/userManWindow/txtSearchDept");
		args.put("idOfDisplayIdComp", "/userManWindow/txtSearchDeptId");
		Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
		showDeptDlg.doModal();
	}

	public ListModelArray getLstUser() {
		return lstUser;
	}

	public void setLstUser(ListModelArray lstUser) {
		this.lstUser = lstUser;
	}
}
