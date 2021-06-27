/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn.include;

import com.viettel.utils.Constants;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.voffice.DAOHE.BookDAOHE;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import java.util.List;
import org.joda.time.LocalDate;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.DateConstraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

/**
 *
 * @author ChucHV
 */
public class DocInSearchController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Wire
    private Listbox lbBookIn;
    @Wire
    private Listbox lbDocType;
    @Wire
    private Datebox dbFromDay, dbToDay;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dbFromDay.setConstraint(new DateConstraint(dbToDay, "before"));
        dbToDay.setConstraint(new DateConstraint(dbFromDay, "after"));
    }

    @Listen("onLoadBookIn = #fullSearchGbx")
    public void onLoadBookIn(Event event) {
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
        int menuType = (int) event.getData();
        data = categoryDAOHE.getDocTypeInMenu(getUserId(), getDeptId(), isFileClerk(), menuType);
        categoryDAOHE.addOptionalCategory(data);
        lbDocType.setModel(new ListModelList(data));
        
        /*
        Nếu là menu "Tiếp nhận văn bản" thì disable trường tìm kiếm theo "Sổ văn bản"
        */
        if(Constants.DOCUMENT_MENU.ALL == menuType && lbBookIn != null){
            lbBookIn.setDisabled(true);
        }
    }

    @Listen("onAfterRender = #lbBookIn")
    public void onAfterRenderListboxBookIn() {
        if (lbBookIn != null && lbBookIn.getItems().size() > 0) {
            lbBookIn.setSelectedIndex(0);
        }
    }

    @Listen("onAfterRender = #lbDocType")
    public void onAfterRenderListboxDocType() {
        if (lbDocType != null && lbDocType.getItems().size() > 0) {
            lbDocType.setSelectedIndex(0);
        }
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
