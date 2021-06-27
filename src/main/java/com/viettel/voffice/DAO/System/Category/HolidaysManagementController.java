/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Category;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.DAO.HolidaysManagementDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.TimeHoliday;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

/**
 *
 * @author hungpv32
 */
public class HolidaysManagementController extends BaseComposer {

    @Wire
    Datebox dbDay;
    @Wire
    Listbox lbDayType, lbManageHolidays, lbArrange;
    @Wire
    Paging userPagingBottom;
    TimeHoliday timeHoliday;

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        onSearch();
    }
    
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo); //To change body of generated methods, choose Tools | Templates.
    }

    @Listen("onClick=#btnSearch")
    public void onSearch() {
        timeHoliday = new TimeHoliday();
        if (dbDay.getValue() != null && !"".equals(dbDay.getValue())) {
            timeHoliday.setTimeDate(dbDay.getValue());
        }
        if (!lbDayType.getSelectedItem().getValue().equals(Constants.HOLIDAY.COMBOBOX_HEADER_SELECT)) {
            Long dayType = Long.valueOf(lbDayType.getSelectedItem().getValue().toString());
            timeHoliday.setDateType(dayType);
        }
        if (!lbArrange.getSelectedItem().getValue().equals(Constants.HOLIDAY.COMBOBOX_HEADER_SELECT)) {
            Long arangge = Long.valueOf(lbArrange.getSelectedItem().getValue().toString());
            timeHoliday.setArrange(arangge);
        }
        //Fill danh sach loai danh muc
        fillDataToList();
    }

    private void fillDataToList() {
        HolidaysManagementDAOHE objhe = new HolidaysManagementDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
        PagingListModel plm = objhe.search(timeHoliday, start, take);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }
        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        lbManageHolidays.setModel(lstModel);
    }

    @Listen("onReload=#manageHolidaysWindow")
    public void onReload() {
        onSearch();
    }

    @Listen("onClick=#btnCreate")
    public void onCreate() throws IOException {
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/manageHolidays/createUpdateHolidays.zul", null, null);
        window.doModal();
    }

    @Listen("onEdit=#lbManageHolidays")
    public void onUpdate(Event ev) throws IOException {
        TimeHoliday pfs = (TimeHoliday) lbManageHolidays.getSelectedItem().getValue();
        Date checkDate = new Date();
        if (pfs.getTimeDate().after(checkDate)) {
        	HashMap<String,Object> args = new HashMap<String,Object>();
            args.put("id", pfs.getTimeHolidayId());
            Window window = (Window) Executions.createComponents("/Pages/admin/manageHolidays/createUpdateHolidays.zul", null, args);
            window.doModal();
        } else {
            showNotification(Constants.HOLIDAY.NOTI_NOT_EDIT, Constants.Notification.ERROR);
        }
    }

    @Listen("onDelete=#lbManageHolidays")
    public void doDeleteItem(ForwardEvent evt) {
        TimeHoliday rs = (TimeHoliday) lbManageHolidays.getSelectedItem().getValue();
        Date checkDate = new Date();
        if (rs.getTimeDate().after(checkDate)) {
            Messagebox.show(getLabelName("presentation_confirm_delete"), getLabelName("common_notification_title"),
                    Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
                @Override
                public void onEvent(Event event) throws InterruptedException {
                    try {
                        if (("onOK").equals(event.getName())) {
                            TimeHoliday rs = (TimeHoliday) lbManageHolidays.getSelectedItem().getValue();
                            HolidaysManagementDAOHE daohe = new HolidaysManagementDAOHE();
                            daohe.delete(rs.getTimeHolidayId());
                            onSearch();
                        }
                    } catch (NullPointerException e) {
                        LogUtils.addLog(e);
                    }
                }
            });
        } else {
            showNotification(Constants.HOLIDAY.NOTI_NOT_DEL, Constants.Notification.ERROR);
        }
    }

    @Override
    public String getConfigLanguageFile() {
        return "language_COSMETIC_vi";
    }
     
   @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }
}
