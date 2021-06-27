/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Document;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.Constants;
import com.viettel.voffice.DAOHE.BookDAOHE;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

/**
 *
 * @author ChucHV
 */
public class DocOutSearchController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire("#lbBookIn")
	private Listbox lbBookIn;
	@Wire("#lbDocType")
	private Listbox lbDocType;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		CategoryDAOHE categoryDAOHE = new CategoryDAOHE();
		List data = categoryDAOHE.findAllCategoryByTypeAndDept(
				Constants.CATEGORY_TYPE.DOCUMENT_TYPE, getDeptId());
		categoryDAOHE.addOptionalCategory(data);
		ListModelList model = new ListModelList(data);
		lbDocType.setModel(model);

		BookDAOHE bookDAOHE = new BookDAOHE();
		// Lay tat cac cac so cua don vi tu danh muc so van ban den
		// "VOFFICE_CAT_BOOK_IN"
		data = bookDAOHE.getBookByType(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
		bookDAOHE.addOptionalBook(data);
		model = new ListModelList(data);
		lbBookIn.setModel(model);
	}
}
