/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;

/**
 * Cau hinh mat khau
 * 
 * @author ducdq1
 */
public class ConfigPassWordController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 650067525887309477L;
	@Wire
	private Listbox lbMenu;
	private List<Category> lstCategory;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		lstCategory = new CategoryDAOHE().findAllCategoryByType("XNQC_Cauhinhmatkhau");
		lbMenu.setModel(new ListModelArray<>(lstCategory));

	}

	@Listen("onClick=#btnSave")
	public void onSave() {
		List<Listitem> lstItem = lbMenu.getItems();
		int i = 0;
		for (Listitem item : lstItem) {

			Listcell cell = (Listcell) item.getLastChild();
			Checkbox cb = (Checkbox) cell.getLastChild();
			Category cate = lstCategory.get(i);
			if (cb.isChecked()) {
				cate.setValue("1");
			} else {
				cate.setValue("0");
			}
			new CategoryDAOHE().saveOrUpdate(cate);
			i++;
		}
		showSuccessNotification(getLabel("luu_thanh_cong"));
	}
}
