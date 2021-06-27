/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.ZulEvents;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PseudoRootNode;
import com.viettel.core.base.model.TreeModel;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.model.DeptNode;
import com.viettel.utils.Constants;

/**
 *
 * @author HaVM2
 */
public class UserSelectDeptController extends BaseComposer {

	@Wire
	Textbox txtDisplayIdComponentId, txtDisplayNameComponentId;
	@Wire
	Window selectDeptDlg;
	@Wire
	Tree deptTree;
	@Wire
	Button btnSaveAndClose;
	private Long deptIdSelected;
	private Long treeMode;
	private Long sendToParent;
	private String parentPath;
	// truong hop sua thong tin phong ban(quan tri phong ban)
	private Long currentDeptId;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//
		// Truong hop set gia tri cho cac truong
		//
		String idOfDisplayNameComp = (String) Executions.getCurrent().getArg().get("idOfDisplayNameComp");
		txtDisplayNameComponentId.setValue(idOfDisplayNameComp);

		// cap nhat phong ban
		setCurrentDeptId((Long) Executions.getCurrent().getArg().get("currentDeptId"));

		//
		// truong hop lay dept va gui ve cho parent chu ko set value cho cac
		// truong
		//
		parentPath = (String) Executions.getCurrent().getArg().get("parentPath");
		sendToParent = (Long) Executions.getCurrent().getArg().get("sendToParent");
		//
		// end of position
		//
		String idOfDisplayIdComp = (String) Executions.getCurrent().getArg().get("idOfDisplayIdComp");
		txtDisplayIdComponentId.setValue(idOfDisplayIdComp);
		Long deptId = (Long) Executions.getCurrent().getArg().get("idOfDeptSelected");
		treeMode = (Long) Executions.getCurrent().getArg().get("treeMode");
		String treeFromDept = (String) Executions.getCurrent().getArg().get("treeFromDept");
		setDeptIdSelected(deptId);
		DepartmentDAOHE deptHe = new DepartmentDAOHE();
		Department root = deptHe.getDeptRoot(getDeptId());
		if (root == null) {
			return;
		}
		Long deptRootId = root.getDeptId();
		String deptRootName = root.getDeptName();
		if (treeFromDept != null && !treeFromDept.isEmpty() && !"".equals(treeFromDept)
				&& ("treeFromDept").equals(treeFromDept)) {
			deptRootId = getDeptId();
			deptRootName = getDeptName();
		}
		TreeModel depts = "admin".equals(getUserName())
				? new TreeModel(new DeptNode(-1L, null, DeptNode.TYPE.DEPT_ONLY))
				: new TreeModel(
						new PseudoRootNode(-1L, null, new DeptNode(deptRootId, deptRootName, DeptNode.TYPE.DEPT_ONLY)));
		// TreeModel depts = new TreeModel(new DeptNode(-1L,
		// null,DeptNode.TYPE.DEPT_ONLY));
		if (treeMode != null && treeMode.equals(Constants.TREE_MODE.MULTIPLE)) {
			deptTree.setCheckmark(true);
			depts.setMultiple(true);
			btnSaveAndClose.setVisible(true);
			btnSaveAndClose.addEventListener(Events.ON_CLICK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					String deptName = "";
					String deptIds = "";
					Set<Treeitem> treeitems = deptTree.getSelectedItems();
					for (Treeitem item : treeitems) {
						DeptNode dn = (DeptNode) item.getValue();
						deptName += "".equals(deptName) ? dn.getName() : ";" + dn.getName();
						deptIds += "".equals(deptIds) ? dn.getId() : ";" + dn.getId();
					}

					String idOfDisplayName = txtDisplayNameComponentId.getValue();
					String idOfDisplayId = txtDisplayIdComponentId.getValue();
					Textbox txtDeptName = (Textbox) Path.getComponent(idOfDisplayName);
					txtDeptName.setValue(deptName);

					Textbox txtDeptId = (Textbox) Path.getComponent(idOfDisplayId);
					txtDeptId.setValue(deptIds);
					selectDeptDlg.detach();
				}
			});
		} else {
			deptTree.addEventListener(Events.ON_SELECT, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					onSelectDept();
				}
			});
		}
		deptTree.setModel(depts);
		deptTree.addEventListener(ZulEvents.ON_AFTER_RENDER, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (deptIdSelected != null) {
					DepartmentDAOHE ddhe = new DepartmentDAOHE();
					List<Long> parentIds = ddhe.getParents(deptIdSelected);
					if (parentIds != null && parentIds.size() > 0) {
						doExpandTree(deptTree.getTreechildren().getItems(), parentIds, deptIdSelected, deptTree);
					}
				} else {

				}
			}
		});

	}

	public void onSelectDept() {
		Treeitem item = deptTree.getSelectedItem();
		DeptNode dn = (DeptNode) item.getValue();
		if (currentDeptId != null) {
			//
			// truong hop cap nhat don vi/chon don vi cap cha
			//
			Long deptId = dn.getId();
			if (deptId.equals(currentDeptId)) {
				showNotification("Không được chọn phòng ban này!");
				return;
			}
		}
		if (sendToParent != null && sendToParent == 1l) {
			Window wnd = (Window) Path.getComponent(parentPath);
			Events.sendEvent("onSelect", wnd, item);
		} else {

			String deptName = dn.getName();
			Long deptId = dn.getId();
			String idOfDisplayName = txtDisplayNameComponentId.getValue();
			String idOfDisplayId = txtDisplayIdComponentId.getValue();
			Textbox txtDeptName = (Textbox) Path.getComponent(idOfDisplayName);
			txtDeptName.setValue(deptName);
			Textbox txtDeptId = (Textbox) Path.getComponent(idOfDisplayId);
			txtDeptId.setValue(deptId.toString());
		}
		selectDeptDlg.detach();
	}

	public void setDeptIdSelected(Long deptId) {
		this.deptIdSelected = deptId;
	}

	public Long getDeptIdSelected() {
		return deptIdSelected;
	}

	public void setTreeMode(Long treeMode) {
		this.treeMode = treeMode;
	}

	public Long getTreeMode() {
		return treeMode;
	}

	public void setCurrentDeptId(Long currentDeptId) {
		this.currentDeptId = currentDeptId;
	}

	public Long getCurrentDeptId() {
		return currentDeptId;
	}
}
