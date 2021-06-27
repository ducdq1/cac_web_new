/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.TreeItem;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Calendar.Calendar;
import com.viettel.voffice.BO.Calendar.CalendarParticipants;
import com.viettel.voffice.BO.Calendar.CalendarResource;
import com.viettel.voffice.DAOHE.Calendar.CalendarDAOHE;

/**
 *
 * @author giangpn
 */
public class CalendarCreateController extends BaseComposer {

    @Wire
    Textbox txtCalendarId, txtTitle, txtLocationName, txtLocationId, txtDescription;
    @Wire
    Datebox dbStartDate, dbEndDate;
    @Wire
    Timebox tbStartTime, tbEndTime;
    @Wire
    Listbox lbParticipants, lbResources;
    @Wire
    Button btnDelete;
    @Wire
    Window calendarCreateWnd;

    public CalendarCreateController() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadInfoToForm();
    }

    public void loadInfoToForm() {
        Calendar calendar = (Calendar) Executions.getCurrent().getArg().get("calendar");
        if (calendar != null) {
            if (calendar.getCalendarId() != null) {
                txtCalendarId.setValue(calendar.getCalendarId().toString());

                CalendarDAOHE cdhe = new CalendarDAOHE();
                List lstParticipants = cdhe.getCalendarParticipant(calendar.getCalendarId());
                if (lstParticipants != null) {
                    ListModelList lstModel = new ListModelList(lstParticipants);
                    lbParticipants.setModel(lstModel);
                }
                List lstResource = cdhe.getCalendarResource(calendar.getCalendarId());
                if (lstResource != null) {
                    ListModelList lstModel = new ListModelList(lstResource);
                    lbResources.setModel(lstModel);
                }
            } else {
                btnDelete.setVisible(false);
            }

            txtTitle.setValue(calendar.getTitle());
            txtLocationName.setValue(calendar.getLocationName());
            if (calendar.getLocationId() != null) {
                txtLocationId.setValue(calendar.getLocationId().toString());
            }
            txtDescription.setValue(calendar.getDescription());
            dbStartDate.setValue(calendar.getTimeStart());
            tbStartTime.setValue(calendar.getTimeStart());
            dbEndDate.setValue(calendar.getTimeEnd());
            tbEndTime.setValue(calendar.getTimeEnd());
        } else {
        }
    }

    @Listen("onClick=#btnSave")
    public void onSave() {
        Calendar cal = new Calendar();
        CalendarDAOHE cdhe = new CalendarDAOHE();
        if (txtCalendarId.getValue() != null && txtCalendarId.getValue().trim().length() > 0) {
            cal.setCalendarId(Long.parseLong(txtCalendarId.getValue()));
            cal = cdhe.findById(cal.getCalendarId());
        }
        if (txtTitle.getValue() == null || txtTitle.getValue().trim().length() == 0) {
            showNotification("Bạn phải nhập tiêu đề", Constants.Notification.ERROR);
            txtTitle.focus();
            return;
        }
        cal.setTitle(txtTitle.getValue().trim());
        cal.setLocationName(txtLocationName.getValue().trim());
        Date startDate = dbStartDate.getValue();
        Date startTime = tbStartTime.getValue();
        if (startDate == null || startTime == null) {
            showNotification("Bạn phải nhập thời gian bắt đầu", Constants.Notification.ERROR);
            dbStartDate.focus();
            return;
        }
        startDate.setHours(startTime.getHours());
        startDate.setMinutes(startTime.getMinutes());
        Date endDate = dbEndDate.getValue();
        Date endTime = tbEndTime.getValue();
        if (endDate == null || endTime == null) {
            showNotification("Bạn phải nhập thời gian kết thúc", Constants.Notification.ERROR);
            dbEndDate.focus();
            return;
        }

        endDate.setHours(endTime.getHours());
        endDate.setMinutes(endTime.getMinutes());
        cal.setTimeStart(startDate);
        cal.setTimeEnd(endDate);

        if (cal.getTimeStart().getTime() >= cal.getTimeEnd().getTime()) {
            showNotification("Thời điểm bắt đầu phải diễn ra trước thời điểm kết thúc", Constants.Notification.ERROR);
            tbEndTime.focus();
            return;
        }
        if (cal.getCalendarId() == null) {
            if (startDate.getTime() <= new Date().getTime()) {
                showNotification("Thời điểm bắt đầu phải sau thời điểm hiện tại", Constants.Notification.ERROR);
                tbStartTime.focus();
                return;
            }
        }

        if (endDate.getYear() - (new Date()).getYear() >= 5) {
            showNotification("Thời điểm kết thúc lịch diễn ra quá lâu so với thời điểm hiện tại(chỉ cho phép tạo lịch diễn ra trong 5 năm)", Constants.Notification.ERROR);
            dbEndDate.focus();
            return;

        }

        cal.setDescription(txtDescription.getValue().trim());
        cal.setCreateUserId(getUserId());
        cal.setStatus(0l);
        ListModelList lstModelPar = (ListModelList) lbParticipants.getListModel();
        List<CalendarParticipants> lstParticipants = null;
        if (lstModelPar != null) {
            lstParticipants = lstModelPar.getInnerList();
        }
        ListModelList lstModelRes = (ListModelList) lbResources.getListModel();
        List<CalendarResource> lstResources = null;
        if (lstModelRes != null) {
            lstResources = lstModelRes.getInnerList();
        }
        cdhe.saveCalendar(cal, lstParticipants, lstResources);
        calendarCreateWnd.detach();
        showNotification("Cập nhật thành công", Constants.Notification.INFO);
        Clients.evalJavaScript("calendar.getCalendar();");
    }

    @Listen("onClick=#btnDelete")
    public void btnDelete() {
        CalendarDAOHE cdhe = new CalendarDAOHE();
        if (txtCalendarId.getValue() != null && txtCalendarId.getValue().trim().length() > 0) {
            Long calendarId = Long.parseLong(txtCalendarId.getValue());
            Calendar cal = cdhe.findById(calendarId);
            cal.setStatus(-1l);
            cdhe.update(cal);
        }
        calendarCreateWnd.detach();
        showNotification("Xóa thành công", Constants.Notification.INFO);
        Clients.evalJavaScript("calendar.getCalendar();");
    }

    @Listen("onClick=#btnShowDept")
    public void onOpenDeptSelect() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        ListModelList lstModel = (ListModelList) lbParticipants.getListModel();
        List<CalendarParticipants> lstItems;
        if (lstModel != null) {
            lstItems = lstModel.getInnerList();
            List<TreeItem> lstTreeItem = new ArrayList();
            if (lstItems != null) {
                for (CalendarParticipants par : lstItems) {
                    TreeItem item ;
                    if (par.getUserId() == null) {
                        item = new TreeItem(par.getDeptId(), par.getDeptName(), 0l, 1l);
                    } else {
                        item = new TreeItem(par.getUserId(), par.getUserName(), 1l, 1l);
                    }
                    lstTreeItem.add(item);
                }
            }
            args.put("lstItem", lstTreeItem);
        }
        args.put("parentPath", "/calendarCreateWnd");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/calendar/userDeptSelect.zul", null, args);
        showDeptDlg.doModal();
    }

    @Listen("onClick=#btnShowResource")
    public void onOpenResourceSelect() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        ListModelList lstModel = (ListModelList) lbResources.getListModel();
        List<CalendarResource> lstItems;
        if (lstModel != null) {
            lstItems = lstModel.getInnerList();
            List<Long> lstSelectedItem = new ArrayList();
            if (lstItems != null) {
                for (CalendarResource res : lstItems) {
                    lstSelectedItem.add(res.getResourceId());
                }
            }
            args.put("lstItem", lstSelectedItem);
        }
        args.put("parentPath", "/calendarCreateWnd");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/calendar/resourceSelect.zul", null, args);
        showDeptDlg.doModal();
    }

    @Listen("onSelectDeptUser=#calendarCreateWnd")
    public void onSelectDeptUser(Event event) {
        HashMap<String,Object> args = (HashMap<String,Object>) event.getData();
        List<CalendarParticipants> lstAdd = (List) args.get("add");
        List<CalendarParticipants> lstDelete = (List) args.get("delete");
        ListModelList lstModel = (ListModelList) lbParticipants.getListModel();
        List<CalendarParticipants> lstItems;
        if (lstModel != null) {
            lstItems = lstModel.getInnerList();
        } else {
            lstItems = new ArrayList();
        }
        lstItems.addAll(lstAdd);
        if (lstDelete != null) {
            for (CalendarParticipants par : lstDelete) {
                for (int i = 0; i < lstItems.size(); i++) {
                    CalendarParticipants selectedItem = lstItems.get(i);
                    if (par.getUserId() == null) {
                        if (par.getDeptId().equals(selectedItem.getDeptId())) {
                            lstItems.remove(i);
                            break;
                        }
                    } else if (par.getUserId().equals(selectedItem.getUserId())) {
                        lstItems.remove(i);
                        break;
                    }
                }
            }
        }
        lstModel = new ListModelList(lstItems);
        lbParticipants.setModel(lstModel);
    }

    @Listen("onSelectResource=#calendarCreateWnd")
    public void onSelectResource(Event event) {
        HashMap<String,Object> args = (HashMap<String,Object>) event.getData();
        List<CalendarResource> lstAdd = (List) args.get("add");
        List<CalendarResource> lstDelete = (List) args.get("delete");
        ListModelList lstModel = (ListModelList) lbResources.getListModel();
        List<CalendarResource> lstItems;
        if (lstModel != null) {
            lstItems = lstModel.getInnerList();
        } else {
            lstItems = new ArrayList();
        }
        lstItems.addAll(lstAdd);
        if (lstDelete != null) {
            for (CalendarResource res : lstDelete) {
                for (int i = 0; i < lstItems.size(); i++) {
                    CalendarResource selectedItem = lstItems.get(i);
                    if (res.getResourceId().equals(selectedItem.getResourceId())) {
                        lstItems.remove(i);
                        break;
                    }
                }
            }
        }
        lstModel = new ListModelList(lstItems);
        lbResources.setModel(lstModel);

    }

    @Listen("onChangeType=#lbParticipants")
    public void onChangeType() {
        ListModelList lstModel = (ListModelList) lbParticipants.getListModel();
        List<CalendarParticipants> lstItems = lstModel.getInnerList();
        CalendarParticipants selectedItem = lstItems.get(lbParticipants.getSelectedIndex());
        if (selectedItem.getParticipantRole() < 3l) {
            selectedItem.setParticipantRole(selectedItem.getParticipantRole() + 1);
        } else {
            selectedItem.setParticipantRole(0l);
        }
        lstItems.set(lbParticipants.getSelectedIndex(), selectedItem);
        lstModel = new ListModelList(lstItems);
        lbParticipants.setModel(lstModel);

    }
    
    @Listen ("onDelete=#lbParticipants")
    public void onDeleteParticipants(){
        int index = lbParticipants.getSelectedIndex();
        ListModelList lstModel = (ListModelList)lbParticipants.getModel();
        List lstParticipants = lstModel.getInnerList();
        lstParticipants.remove(index);
        lstModel = new ListModelList(lstParticipants);
        lbParticipants.setModel(lstModel);
    }
    
    @Listen ("onDelete=#lbResources")
    public void onDeleteResource(){
        int index = lbResources.getSelectedIndex();
        ListModelList lstModel = (ListModelList)lbResources.getModel();
        List lstResource = lstModel.getInnerList();
        lstResource.remove(index);
        lstModel = new ListModelList(lstResource);
        lbResources.setModel(lstModel);
    }
    
}
