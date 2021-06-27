/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Books;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

/**
 *
 * @author HaVM2
 */
public class BookSelectDeptController extends SelectorComposer<Window> {

	@Wire
	Window selectDeptDlg;
	@Wire
	Tree deptTree;

	private String showMode;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		Execution execution = Executions.getCurrent();
		setShowMode((String) execution.getArg().get("showMode"));
	}

	@Listen("onSelect=#deptTree")
	public void onSelectDept() {

		Treeitem item = deptTree.getSelectedItem();
		String deptName = item.getLabel();
		Long deptId = Long.parseLong(item.getId());
		Textbox txtDeptName = (Textbox) Path
				.getComponent(("Form").equals(showMode) ? "/docBookWindow/txtDeptName" : "/docBookCRUD/txtDeptName");
		txtDeptName.setValue(deptName);
		Textbox txtDeptId = (Textbox) Path
				.getComponent(("Form").equals(showMode) ? "/docBookWindow/txtDeptId" : "/docBookCRUD/txtDeptId");
		txtDeptId.setValue(deptId.toString());
		selectDeptDlg.detach();
	}

	public String getShowMode() {
		return showMode;
	}

	public void setShowMode(String showMode) {
		this.showMode = showMode;
	}

}
