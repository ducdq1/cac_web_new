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
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.google.gson.Gson;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.ToolbarModel;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Calendar.Calendar;
import com.viettel.voffice.BO.Calendar.CalendarParticipants;
import com.viettel.voffice.BO.Calendar.CalendarResource;
import com.viettel.voffice.DAOHE.Calendar.CalendarDAOHE;

/**
 *
 * @author havm2
 */
public class CalendarApprovedController extends BaseComposer {

	@Wire
	Div viewDiv, selectedDiv;
	@Wire
	Label lbSelectedDept, lbSelectedUser, lbSelectedResource;
	@Wire
	Window calendarViewWnd;
	List<Long> lstSelectedUsers;
	List<Long> lstSelectedResources;
	List<Long> lstSelectedDepts;
	String searchText;
	Date timeStart;
	Date timeEnd;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		lstSelectedDepts = new ArrayList();
		Long deptId = getDeptId();
		String deptName = getDeptName();
		lstSelectedDepts.add(deptId);
		lbSelectedDept.setValue(deptName);

	}

	@Listen("onChangeViewType=#calendarManagerWnd")
	public void onChangeViewType(Event e) {
		Long data = Long.parseLong(e.getData().toString());
		viewDiv.getChildren().clear();
		if (data == 0l) {
			Executions.createComponents("/Pages/calendar/calendarDay.zul", viewDiv, null);

		} else if (data == 1l) {
			Executions.createComponents("/Pages/calendar/calendarWeek.zul", viewDiv, null);

		} else if (data == 2l) {
			Executions.createComponents("/Pages/calendar/calendarMonth.zul", viewDiv, null);

		} else {
			Executions.createComponents("/Pages/calendar/calendarList.zul", viewDiv, null);

		}
	}

	@Listen("onSearch=#calendarManagerWnd")
	public void onSearch(Event e) {
		String data = e.getData().toString();
		Gson gson = new Gson();
		ToolbarModel tbModel = gson.fromJson(data, ToolbarModel.class);
		timeStart = tbModel.getFromDate();
		timeEnd = tbModel.getToDate();
		searchText = tbModel.getSearchText();
		searchAndLoadCalendar();
	}

	private void searchAndLoadCalendar() {
		CalendarDAOHE cdhe = new CalendarDAOHE();
		Calendar cal = new Calendar();
		cal.setTimeStart(timeStart);
		cal.setTimeEnd(timeEnd);
		cal.setSearchText(searchText);
		cal.setStatus(Constants.CALENDAR_STATUS.APPROVE_ACCEPT);
		List lstModel = cdhe.searchCalendar(cal, null, lstSelectedUsers, lstSelectedResources, lstSelectedDepts);
		Gson gson = new Gson();
		String json = gson.toJson(lstModel);
		Clients.evalJavaScript("calendar.lstDateCalendar=" + json + ";");
		Clients.evalJavaScript("calendar.rendCalendar()");

	}

	@Listen("onView=#calendarManagerWnd")
	public void onView(Event e) {
		Long calendarId = Long.parseLong(e.getData().toString());
		CalendarDAOHE cdhe = new CalendarDAOHE();
		Calendar cal = cdhe.findById(calendarId);
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("calendar", cal);
		args.put("viewType", Constants.CALENDAR_VIEWTYPE.VIEW_ONLY);

		if (calendarViewWnd != null) {
			calendarViewWnd.detach();
		}
		calendarViewWnd = (Window) Executions.createComponents("/Pages/calendar/calendarViewWnd.zul", null, args);
		calendarViewWnd.doModal();
	}

	@Listen("onOpenSelectUser=#calendarManagerWnd")
	public void onOpenSelectUser() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("parentPath", "/calendarManagerWnd");
		Window showDeptDlg = (Window) Executions.createComponents("/Pages/calendar/userDeptSelect.zul", null, args);
		showDeptDlg.doModal();
	}

	@Listen("onOpenSelectResource=#calendarManagerWnd")
	public void onOpenSelectResource() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("parentPath", "/calendarManagerWnd");
		Window showDeptDlg = (Window) Executions.createComponents("/Pages/calendar/resourceSelect.zul", null, args);
		showDeptDlg.doModal();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onSelectDeptUser=#calendarManagerWnd")
	public void onSelectDeptUser(Event event) {
		HashMap<String, Object> args = (HashMap) event.getData();
		List<CalendarParticipants> lstAdd = (List) args.get("add");
		if (lstSelectedUsers == null) {
			lstSelectedUsers = new ArrayList();
		} else {
			lstSelectedUsers.clear();
		}

		if (lstSelectedDepts == null) {
			lstSelectedDepts = new ArrayList();
		}

		StringBuilder userNames = new StringBuilder();
		StringBuilder deptNames = new StringBuilder();
		if (lstAdd != null && lstAdd.size() > 0) {
			for (CalendarParticipants cp : lstAdd) {
				if (cp.getUserId() != null && cp.getUserId() > 0l) {
					lstSelectedUsers.add(cp.getUserId());
					userNames.append(cp.getUserName()).append(",");
				} else {
					lstSelectedDepts.add(cp.getDeptId());
					deptNames.append(cp.getDeptName()).append(",");
				}
			}
		}
		lbSelectedUser.setValue(userNames.toString());
		lbSelectedDept.setValue(deptNames.toString());
		if ((lstSelectedResources != null && lstSelectedResources.size() > 0) || (lstSelectedUsers.size() > 0)
				|| (lstSelectedDepts.size() > 0)) {
			selectedDiv.setVisible(true);
		} else {
			selectedDiv.setVisible(false);
		}
		searchAndLoadCalendar();
	}

	@Listen("onSelectResource=#calendarManagerWnd")
	public void onSelectResource(Event event) {
		HashMap<String, Object> args = (HashMap<String, Object>) event.getData();
		List<CalendarResource> lstAdd = (List) args.get("add");
		if (lstSelectedResources == null) {
			lstSelectedResources = new ArrayList();
		} else {
			lstSelectedResources.clear();
		}
		StringBuilder resourceNames = new StringBuilder();
		if (lstAdd != null && lstAdd.size() > 0) {
			for (CalendarResource cp : lstAdd) {
				if (cp.getResourceId() != null && cp.getResourceId() > 0l) {
					lstSelectedResources.add(cp.getResourceId());
					resourceNames.append(cp.getResourceName()).append(",");
				}
			}
		}
		lbSelectedResource.setValue(resourceNames.toString());
		if ((lstSelectedResources.size() > 0) || (lstSelectedUsers != null && lstSelectedUsers.size() > 0)) {
			selectedDiv.setVisible(true);
		} else {
			selectedDiv.setVisible(false);
		}
		searchAndLoadCalendar();
	}

	@Listen("onClick=#imgDeleteSelected")
	public void onDeleteSelected() {
		if (lstSelectedResources != null) {
			lstSelectedResources.clear();
		}
		if (lstSelectedUsers != null) {
			lstSelectedUsers.clear();
		}
		if (lstSelectedDepts != null) {
			lstSelectedDepts.clear();
		}
		lbSelectedDept.setValue("");
		lbSelectedResource.setValue("");
		lbSelectedUser.setValue("");
		selectedDiv.setVisible(false);
		searchAndLoadCalendar();
	}
}
