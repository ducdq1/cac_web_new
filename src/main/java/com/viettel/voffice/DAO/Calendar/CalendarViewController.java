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
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.voffice.BO.Calendar.Calendar;
import com.viettel.voffice.BO.Calendar.CalendarParticipants;
import com.viettel.voffice.BO.Calendar.CalendarResource;
import com.viettel.voffice.DAOHE.Calendar.CalendarDAOHE;

/**
 *
 * @author havm2
 */
public class CalendarViewController extends BaseComposer {

    @Wire
    Textbox txtCalendarId;
    @Wire
    Label lbTitle, lbLocationName, lbDescription;
    @Wire
    Label lbStartDate, lbEndDate;
    @Wire
    Listbox lbParticipants, lbResources;
    @Wire
    Button btnApprove, btnReject, btnCheck;
    @Wire
    Window calendarViewWnd;
    @Wire
    Div notifyWnd;
    Long viewType;
    Boolean fromHome;
    Long calendarId;

    public CalendarViewController() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadInfoToForm();
    }

    public void loadInfoToForm() {
        Calendar calendar = (Calendar) Executions.getCurrent().getArg().get("calendar");
        calendarId = calendar.getCalendarId();
        //
        // Create notify
        //
        HashMap<String,Object> args = new HashMap<String,Object>();
        args.put("objectId", calendar.getCalendarId());
        args.put("objectType", Constants.OBJECT_TYPE.CALENDAR);
        Executions.createComponents("/Pages/notify/listNotify.zul", notifyWnd, args);
        //
        // load info to form
        //
        viewType = (Long) Executions.getCurrent().getArg().get("viewType");
        fromHome = (Boolean) Executions.getCurrent().getArg().get("fromHome");
        
            if (calendar.getCalendarId() != null) {
                txtCalendarId.setValue(calendar.getCalendarId().toString());

                CalendarDAOHE cdhe = new CalendarDAOHE();
                if (calendar.getTimeEnd().getTime() < (new Date()).getTime()) {
                    //
                    // neu thoi diem hien tai da qua gio ket thuc cua lich thi chi cho xem
                    //
                    viewType = Constants.CALENDAR_VIEWTYPE.VIEW_ONLY;
                }
                if (viewType == Constants.CALENDAR_VIEWTYPE.VIEW_ONLY) {
                    btnApprove.setVisible(false);
                    btnReject.setVisible(false);
                    btnCheck.setVisible(false);
                } else if (viewType == Constants.CALENDAR_VIEWTYPE.DEPT_APPROVE) {
                    CalendarParticipants par = cdhe.getParticipantOfDept(calendar.getCalendarId(), getDeptId());
                    if (par != null) {
                        if (par.getAcceptStatus().equals(Constants.CALENDAR_STATUS.APPROVE_ACCEPT)) {
                            btnApprove.setVisible(false);
                        } else if (par.getAcceptStatus().equals(Constants.CALENDAR_STATUS.APPROVE_REJECT)) {
                            btnReject.setVisible(false);
                        }
                    } else {
                        CalendarResource res = cdhe.getResourceOfDept(calendar.getCalendarId(), getDeptId());
                        if (res != null) {
                            if (res.getAcceptStatus().equals(Constants.CALENDAR_STATUS.APPROVE_ACCEPT)) {
                                btnApprove.setVisible(false);
                            } else if (res.getAcceptStatus().equals(Constants.CALENDAR_STATUS.APPROVE_REJECT)) {
                                btnReject.setVisible(false);
                            }
                        }

                    }
                } else if (viewType == Constants.CALENDAR_VIEWTYPE.USER_APPROVE) {
                    CalendarParticipants par = cdhe.getParticipantOfUser(calendar.getCalendarId(), getUserId());
                    if (par != null) {
                        if (par.getAcceptStatus().equals(Constants.CALENDAR_STATUS.APPROVE_ACCEPT)) {
                            btnApprove.setVisible(false);
                        } else if (par.getAcceptStatus().equals(Constants.CALENDAR_STATUS.APPROVE_REJECT)) {
                            btnReject.setVisible(false);
                        }
                    }
                }

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

            }
            lbTitle.setValue(calendar.getTitle());
            lbLocationName.setValue(calendar.getLocationName());
            lbDescription.setValue(calendar.getDescription());
            lbStartDate.setValue(DateTimeUtils.convertDateTimeToString(calendar.getTimeStart()));
            lbEndDate.setValue(DateTimeUtils.convertDateTimeToString(calendar.getTimeEnd()));
        
        onCheckDuplicate();
    }

    @Listen("onClick=#btnApprove")
    public void onApprove() {
        calendarId = Long.parseLong(txtCalendarId.getValue());
        CalendarDAOHE cdhe = new CalendarDAOHE();
        if (viewType == Constants.CALENDAR_VIEWTYPE.USER_APPROVE) {
            cdhe.userResponseCalendar(getUserId(), calendarId, Constants.CALENDAR_STATUS.APPROVE_ACCEPT);
        } else {
            cdhe.deptResponseCalendar(getDeptId(), calendarId, Constants.CALENDAR_STATUS.APPROVE_ACCEPT);
        }
        calendarViewWnd.detach();
        showNotification("Bạn đã chấp thuận lich :" + lbTitle.getValue(), Constants.Notification.INFO);
        if (fromHome == null || !fromHome.booleanValue()) {
            Clients.evalJavaScript("calendar.getCalendar();");
        }
    }

    @Listen("onClick=#btnReject")
    public void onReject() {
        calendarId = Long.parseLong(txtCalendarId.getValue());
        CalendarDAOHE cdhe = new CalendarDAOHE();
        if (viewType == Constants.CALENDAR_VIEWTYPE.USER_APPROVE) {
            cdhe.userResponseCalendar(getUserId(), calendarId, Constants.CALENDAR_STATUS.APPROVE_REJECT);
        } else {
            cdhe.deptResponseCalendar(getDeptId(), calendarId, Constants.CALENDAR_STATUS.APPROVE_REJECT);
        }
        calendarViewWnd.detach();
        showNotification("Bạn đã từ chối lich :" + lbTitle.getValue(), Constants.Notification.INFO);
        if (fromHome == null || !fromHome.booleanValue()) {
            Clients.evalJavaScript("calendar.getCalendar();");
        }
    }

    @Listen("onClick=#btnCheck")
    public void onCheckDuplicate() {
        List<Long> lstUserIds = new ArrayList();
        List<Long> lstResourceIds = new ArrayList();
        ListModelList lstUserModel = (ListModelList) lbParticipants.getModel();
        List<CalendarParticipants> lstUsers = lstUserModel.getInnerList();
        if (lstUsers != null && lstUsers.size() > 0) {
            for (CalendarParticipants cp : lstUsers) {
                if (cp.getUserId() != null && cp.getUserId() > 0l) {
                    lstUserIds.add(cp.getUserId());
                }
            }
        }

        ListModelList lstResourceModel = (ListModelList) lbResources.getModel();
        List<CalendarResource> lstResources = lstResourceModel.getInnerList();
        if (lstResources != null && lstResources.size() > 0) {
            for (CalendarResource cr : lstResources) {
                lstResourceIds.add(cr.getResourceId());
            }
        }
        boolean bUpdate = false;
        CalendarDAOHE cdhe = new CalendarDAOHE();
        Calendar cal = cdhe.findById(calendarId);
        if (!lstUserIds.isEmpty()) {
            lstUserIds = cdhe.checkCalendarUser(calendarId, lstUserIds, cal.getTimeStart(), cal.getTimeEnd());
            if (lstUserIds != null && !lstUserIds.isEmpty()) {
                for (Long id : lstUserIds) {
                    if (lstUsers != null) {
                        for (int i = 0; i < lstUsers.size(); i++) {
                            if (id.equals(lstUsers.get(i).getUserId())) {
                                lstUsers.get(i).setIsDuplicated(1l);
                                bUpdate = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (bUpdate) {
            lstUserModel = new ListModelList(lstUsers);
            lbParticipants.setModel(lstUserModel);
        }
        bUpdate = false;
        if (!lstResourceIds.isEmpty()) {
            lstResourceIds = cdhe.checkCalendarResource(calendarId, lstResourceIds, cal.getTimeStart(), cal.getTimeEnd());
            if (lstResourceIds != null && lstResourceIds.size() > 0) {
                for (Long id : lstResourceIds) {
                    if (lstResources != null) {
                        for (int i = 0; i < lstResources.size(); i++) {
                            if (id.equals(lstResources.get(i).getResourceId())) {
                                lstResources.get(i).setIsDuplicated(1l);
                                bUpdate = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (bUpdate) {
            lstResourceModel = new ListModelList(lstResources);
            lbResources.setModel(lstResourceModel);
        }

    }
}
