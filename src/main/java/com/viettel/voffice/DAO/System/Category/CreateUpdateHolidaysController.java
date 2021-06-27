package com.viettel.voffice.DAO.System.Category;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.DAO.HolidaysManagementDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.TimeHoliday;
import java.io.IOException;
import java.util.Calendar;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author hungpv32
 */
public class CreateUpdateHolidaysController extends BaseComposer {

    @Wire
    Textbox tbId, tbName, tbDescription;
    @Wire
    Datebox dbTimeDate;
    @Wire
    Listbox lbDayType;
    @Wire
    Window createUpdateHolidays;

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadInfoToForm();
    }

    @Listen("onClick=#btnSave")
    public void onSave() throws IOException {
        Messagebox.show(getLabelName("common_comfirm_save"), getLabelName("common_notification_title"),
                Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
            @Override
            public void onEvent(Event event) throws InterruptedException {
                try {
                    if (("onOK").equals(event.getName())) {
                        TimeHoliday timeHoliday = new TimeHoliday();
                        if (tbId.getValue() != null && !tbId.getValue().isEmpty()) {
                            timeHoliday.setTimeHolidayId(Long.parseLong(tbId.getValue()));
                        }

                        if (tbName.getValue() != null && tbName.getValue().trim().length() == 0) {
                            showNotification(Constants.HOLIDAY.NOTI_EMPTY_HOLIDAY_NAME, Constants.Notification.ERROR);
                            tbName.focus();
                            return;
                        } else {
                            timeHoliday.setName(tbName.getValue());
                        }

                        if (dbTimeDate.getValue() == null) {
                            showNotification(Constants.HOLIDAY.NOTI_EMPTY_HOLIDAY_DATE, Constants.Notification.ERROR);
                            dbTimeDate.focus();
                            return;
                        } else {
                            timeHoliday.setTimeDate(dbTimeDate.getValue());
                        }

                        timeHoliday.setDateType(Long.valueOf(lbDayType.getSelectedItem().getValue().toString()));

                        Calendar checkDate = Calendar.getInstance();
                        checkDate.setTime(dbTimeDate.getValue());
                        if (("1").equals(lbDayType.getSelectedItem().getValue())) {
                            if (checkDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || checkDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                                showNotification(Constants.HOLIDAY.NOTI_HOLIDAY, Constants.Notification.ERROR);
                                dbTimeDate.focus();
                                return;
                            }
                        } else {
                            if (checkDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && checkDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                                showNotification(Constants.HOLIDAY.NOTI_WORK_HOLIDAY, Constants.Notification.ERROR);
                                dbTimeDate.focus();
                                return;
                            }
                        }

                        timeHoliday.setIsActive(1L);
                        HolidaysManagementDAOHE holidaysManagementDAOHE = new HolidaysManagementDAOHE();
                        if (holidaysManagementDAOHE.hasDuplicate(timeHoliday)) {
                            showNotification(Constants.HOLIDAY.NOTI_DATA_EXISTS, Constants.Notification.ERROR);
                            return;
                        }
                        holidaysManagementDAOHE.saveOrUpdate(timeHoliday);

                        if (tbId.getValue() != null && !tbId.getValue().isEmpty()) {
                            //update thì detach window
                            createUpdateHolidays.detach();
                        } else {
                            // them moi thi clear window
                            loadInfoToForm();
                        }
                        showNotification(Constants.HOLIDAY.NOTI_SAVE_FINISH, Constants.Notification.INFO);

                        Window parentWnd = (Window) Path.getComponent("/manageHolidaysWindow");
                        Events.sendEvent(new Event("onReload", parentWnd, null));
                    }
                } catch (NullPointerException e) {
                    LogUtils.addLog(e);
                }
            }
        });
    }

    public void loadInfoToForm() {
        Long id = (Long) Executions.getCurrent().getArg().get("id");
        if (id != null) {
            HolidaysManagementDAOHE objhe = new HolidaysManagementDAOHE();
            TimeHoliday rs = objhe.findById(id);
            if (rs.getTimeHolidayId() != null) {
                tbId.setValue(rs.getTimeHolidayId().toString());
            }
            if (rs.getName() != null) {
                tbName.setValue(rs.getName());
            }
            if (rs.getTimeDate() != null) {
                dbTimeDate.setValue(rs.getTimeDate());
            }

            if (rs.getDescription() != null) {
                tbDescription.setValue(rs.getDescription());
            }
            if (rs.getDateType() != null) {
                lbDayType.setSelectedIndex(Integer.valueOf(rs.getDateType().toString()));
            }
        } else {
            tbId.setValue("");
            tbName.setValue("");
            dbTimeDate.setValue(null);
            tbDescription.setValue("");
            lbDayType.setSelectedIndex(1);
        }
    }

    @Override
    public String getConfigLanguageFile() {
        return "language_COSMETIC_vi";
    }
}
