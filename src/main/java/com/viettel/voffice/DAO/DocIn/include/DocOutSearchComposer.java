package com.viettel.voffice.DAO.DocIn.include;

import java.util.Calendar;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import com.viettel.utils.Constants;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.DAO.CategoryDAOHE;

public class DocOutSearchComposer extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7140689022434653938L;

	@Wire
	private Listbox lbDocType;
	@Wire
	private Textbox tbDocumentAbstract, tbDocumentCode;
	@Wire
	private Datebox dbCreateFrom, dbCreateTo;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		CategoryDAOHE catDaoHe = new CategoryDAOHE();
		List<Category> catTypes = catDaoHe
				.findAllByType(new Constants().toListCatType());
		List<Category> lstDocType = catDaoHe.findAllByType(catTypes,
				Constants.CATEGORY_TYPE.DOCUMENT_TYPE);
		lbDocType.setModel(new ListModelList(lstDocType));

		Calendar cal = Calendar.getInstance();
		dbCreateTo.setValue(cal.getTime());
		cal.add(Calendar.DAY_OF_YEAR, -7);
		dbCreateFrom.setValue(cal.getTime());
	}
}
