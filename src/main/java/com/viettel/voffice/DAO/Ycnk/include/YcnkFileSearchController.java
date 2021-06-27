/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Ycnk.include;

import com.viettel.utils.Constants;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.voffice.DAOHE.BookDAOHE;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import java.util.List;
import org.joda.time.LocalDate;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

/**
 *
 * @author ChucHV
 */
public class YcnkFileSearchController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire("#lbBookIn")
	private Listbox lbBookIn;
	@Wire("#lbDocType")
	private Listbox lbDocType;
	@Wire
	private Datebox dbFromDay, dbToDay;
	@Wire
	private Groupbox fullSearchGbx;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onLoadBookIn = #fullSearchGbx")
	public void onLoadBookIn() {
		// TODO: tối ưu sau
		BookDAOHE bookDAOHE = new BookDAOHE();
		List data = bookDAOHE.getBookIns(getUserId(), getDeptId(),
				isFileClerk());
		bookDAOHE.addOptionalBook(data);
		lbBookIn.setModel(new ListModelList(data));

		/*
		 * Tìm tất cả loại văn bản mà đơn vị sử dụng, đúng ra phải tìm tất cả
		 * loại văn bản của tất cả văn bản đến cá nhân, đơn vị.
		 */
		CategoryDAOHE categoryDAOHE = new CategoryDAOHE();
		data = categoryDAOHE.findAllCategoryByTypeAndDept(
				Constants.CATEGORY_TYPE.DOCUMENT_TYPE, getDeptId());
		categoryDAOHE.addOptionalCategory(data);
		ListModelList model = new ListModelList(data);
		lbDocType.setModel(model);
	}

	@Listen("onChange = #dbFromDay, #dbToDay")
	public void onChangeDate(InputEvent event) {
		LocalDate fromDate = LocalDate.fromDateFields(dbFromDay.getValue());
		LocalDate toDate = LocalDate.fromDateFields(dbToDay.getValue());
		if (toDate.isBefore(fromDate)) {
			if (event.getTarget().equals(dbFromDay)) {

				throw new WrongValueException(event.getTarget(),
						"Giá trị trong trường Từ ngày không được sau trường Đến ngày");
			} else if (event.getTarget().equals(dbToDay)) {
				throw new WrongValueException(event.getTarget(),
						"Giá trị trong trường Đến ngày không được trước trường Từ ngày");
			}
		}
	}
}
