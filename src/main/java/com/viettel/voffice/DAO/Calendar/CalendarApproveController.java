package com.viettel.voffice.DAO.Calendar;

import com.google.gson.Gson;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Calendar.Calendar;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.voffice.DAOHE.Calendar.CalendarDAOHE;
import com.viettel.core.base.model.ToolbarModel;

import java.util.HashMap;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

/**
 *
 * @author havm2
 */
public class CalendarApproveController extends BaseComposer {

    @Wire
    Div viewDiv;
    @Wire
    Window calendarViewWnd;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
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
        CalendarDAOHE cdhe = new CalendarDAOHE();
        Calendar cal = new Calendar();
        cal.setTimeStart(tbModel.getFromDate());
        cal.setTimeEnd(tbModel.getToDate());
        cal.setSearchText(tbModel.getSearchText());
        List lstModel = cdhe.searchCalendar(cal,getDeptId(),null,null,null);
        String json = gson.toJson(lstModel);
        Clients.evalJavaScript("calendar.lstDateCalendar=" + json + ";");
        Clients.evalJavaScript("calendar.rendCalendar()");
    }

    @Listen("onView=#calendarManagerWnd")
    public void onView(Event e) {
        Long calendarId = Long.parseLong(e.getData().toString());
        CalendarDAOHE cdhe = new CalendarDAOHE();
        Calendar cal = cdhe.findById(calendarId);
        HashMap<String,Object> args = new HashMap<String,Object>();
        args.put("calendar", cal);
        args.put("viewType", Constants.CALENDAR_VIEWTYPE.DEPT_APPROVE);
        if (calendarViewWnd != null) {
            calendarViewWnd.detach();
        }
        calendarViewWnd = (Window) Executions.createComponents("/Pages/calendar/calendarViewWnd.zul", null, args);
        calendarViewWnd.doModal();
    }
}
