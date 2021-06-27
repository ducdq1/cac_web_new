/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.OutsideOffice;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PseudoRootNode;
import com.viettel.core.base.model.TreeModel;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.model.DeptNode;

/**
 *
 * @author ChucHV
 */
@SuppressWarnings("serial")
public class OutsideOfficeSelectDept extends BaseComposer {

	@Wire
	private Tree tree;
	@Wire
	private Window windowSelectDept;

	private Window windowCRUDOffice;
	private Department deptSelected;

	@SuppressWarnings({ "unchecked" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> data = (HashMap<String, Object>) Executions
				.getCurrent().getArg();
		windowCRUDOffice = (Window) data.get("parentWindow");

		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		List<Long> lstParents = ddhe.getParents(getDeptId());
		TreeModel model;
		if (lstParents.isEmpty()) {
			model = new TreeModel(new PseudoRootNode(-1L, "",
					new com.viettel.core.user.model.DeptNode(getDeptId(),
							getDeptName())));
		} else {
			Department department = ddhe.findBOById(lstParents.get(0));
			model = new TreeModel(new PseudoRootNode(-1L, "",
					new com.viettel.core.user.model.DeptNode(
							department.getDeptId(), department.getDeptName(),
							DeptNode.TYPE.DEPT_ONLY)));
		}
		this.tree.setModel(model);
	}

	@Listen("onSelect = #tree")
	public void doSelected() throws Exception {
		Treeitem item = tree.getSelectedItem();
		DeptNode deptNode = item.getValue();
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		Department dept = ddhe.findBOById(deptNode.getId());
		setDeptSelected(dept);

		Events.sendEvent("onAfterSelectingDept", windowCRUDOffice,
				getDeptSelected());

		windowSelectDept.onClose();

	}

	public void setDeptSelected(Department dept) {
		this.deptSelected = dept;
	}

	public Department getDeptSelected() {
		return deptSelected;
	}
}
