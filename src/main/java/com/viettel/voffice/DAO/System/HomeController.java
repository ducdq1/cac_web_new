/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System;

import com.viettel.core.base.DAO.BaseComposer;

/**
 *
 * @author havm2
 */
@SuppressWarnings("serial")
public class HomeController extends BaseComposer {
//
//    @Wire
//    Textbox txtStart, txtEnd;
//    @Wire
//    Window homeWnd,windowNotifyDetailView;
//    @Wire
//    Label lbDocOutNotProcess, lbDocOutPublish, lbDocOutInWeek, lbCalInDay,
//            lbCalInTomorow, lbCalInWeek, lbFileCoop, lbFileMain, lbFileAll;
//    @Wire
//    private Label lbDocInWaitingProcess, lbDocInExpired, lbDocInProcessing;
//    UserDAOHE userDAOHE = new UserDAOHE();
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp); // To change body of generated methods,
//    }
//
//    @Listen("onLoadNext=#homeWnd")
//    public void onNextNotify(Event event) {
//        Long start = 0l;
//        Long end = 0l;
//
//        if (txtStart != null) {
//            if (!txtStart.getValue().isEmpty()) {
//                start = Long.parseLong(txtStart.getValue());
//            }
//        }
//
//        if (txtEnd != null) {
//            if (!txtEnd.getValue().isEmpty()) {
//                end = Long.parseLong(txtEnd.getValue());
//            }
//        }
//
//        start += end;
//        txtStart.setValue(start.toString());
//
//        onLoadNotify(event);
//    }
//
//    @Listen("onReLoadNotify=#homeWnd")
//    public void onReLoadNotify(Event event) {
//        txtStart.setValue("0");
//        onLoadNotify(event);
//    }
//
//    @Listen("onSearchFullText=#homeWnd")
//    public void onSearch(Event event) {
//        txtStart.setValue("0");
//        String data = event.getData().toString();
//        Gson gson = new Gson();
//        ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
//
//        NotifyDAOHE ndhe = new NotifyDAOHE();
//        Notify searchNotify = null;
//        if (model != null) {
//            searchNotify = new Notify();
//            searchNotify.setContent(model.getSearchText());
//        }
//        List<Notify> lstNotify = ndhe.getNotifyOfUser(getUserId(), getDeptId(),
//                searchNotify, 0l, 20l);
//        String objects = gson.toJson(lstNotify);
//        Clients.evalJavaScript("clearNotify();");
//        Clients.evalJavaScript("page.lstNotify=" + objects + ";");
//        Clients.evalJavaScript("createNotifys()");
//    }
//
//    @Listen("onLoadNotify=#homeWnd")
//    public void onLoadNotify(Event event) {
//        Long start = 0l;
//        Long end = 0l;
//
//        if (txtStart != null) {
//            if (!txtStart.getValue().isEmpty()) {
//                start = Long.parseLong(txtStart.getValue());
//            }
//        }
//
//        if (txtEnd != null) {
//            if (!txtEnd.getValue().isEmpty()) {
//                end = Long.parseLong(txtEnd.getValue());
//            }
//        }
//
//        if (end == 0l) {
//            end = 20l;
//        }
//
//        Gson gson = new Gson();
//        Notify searchNotify = null;
//        if (event != null) {
//            searchNotify = gson.fromJson(event.getData().toString(),
//                    Notify.class);
//        }
//        NotifyDAOHE ndhe = new NotifyDAOHE();
//        // List<Notify> lstNotify = ndhe.getNotifyOfUser(getUserId(),
//        // searchNotify, start, end);
//        List<Notify> lstNotify = ndhe.getNotifyOfUser(getUserId(), getDeptId(),
//                searchNotify, start, end);
//        String objects = gson.toJson(lstNotify);
//        // Clients.evalJavaScript("clearNotify();");
//        Clients.evalJavaScript("page.lstNotify=" + objects + ";");
//        Clients.evalJavaScript("createNotifys()");
//        // Clients.evalJavaScript("createNotifys('" + objects + "')");
//        // Thông báo nội bô
////        onSearch();
//        List<Notify> lstNotifyAlert = ndhe.getNotifyAlertOfUser(getUserId(), getListVDepartment(),null, start, end);
//        String objectsNotifyAlert = gson.toJson(lstNotifyAlert);
//        // Clients.evalJavaScript("clearNotify();");
//        Clients.evalJavaScript("page.lstNotifyAlert=" + objectsNotifyAlert + ";");
//        Clients.evalJavaScript("createNotifysAlert()");
//    }
//    public List<String> getListVDepartment() {
//        Long deptId = getDeptId();
//        Users user = userDAOHE.findById(getUserId());
//        List<String> deptLst = new ArrayList<String>();
//        try {
//            
//            deptLst.add(user.getDeptId().toString());
//            VDepartment vDepartment = (new DepartmentDAOHE()).getVDepartmentDeptId(deptId);
//            if (vDepartment != null && vDepartment.getDeptPath() != null) {
//                String[] pathStr = vDepartment.getDeptPath().split("/");
//
//                if (pathStr.length != 0) {
//                    int deptIndex = 0;
//                    int len = pathStr.length;
//                    for (int i = 0; i < len; i++) {
//                        if (deptId.toString().equals(pathStr[i])) {
//                            deptIndex = i;
//                            break;
//                        }
//                    }
//                    for (int i = 0; i <= deptIndex; i++) {
//                        String a = pathStr[i];
//                        if (a != null && !"".equals(a)) {
//                            deptLst.add(a);
//                        }
//                    }
//                }
//            }
//        } catch (NumberFormatException ex) {
//        }
//        return deptLst;
//    }
//    @Listen("onOpenView = #homeWnd")
//    public void onOpenView(Event event) {
//        if (event != null) {
//            Gson gson = new Gson();
//            String data = event.getData().toString();
//            Notify notify = gson.fromJson(data, Notify.class);
//            HashMap<String, Object> arguments =new HashMap<String, Object>();
//            // Van ban den
//            if (Constants.OBJECT_TYPE.DOCUMENT_RECEIVE.equals(notify
//                    .getObjectType())) {
//                arguments.put("documentReceiveId", notify.getObjectId());
//                arguments.put("menuType", Constants.DOCUMENT_MENU.VIEW);
//                arguments.put("parentWindow", homeWnd);
//                createWindow("windowDocInView",
//                        "/Pages/document/docIn/viewDocIn.zul", arguments,
//                        Window.EMBEDDED);
////                DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
////                List listProcess = documentReceiveDAOHE.getProcessFromDocument(notify.getObjectId(), getUserId(), getDeptId(), isFileClerk());
//                
//                homeWnd.setVisible(false);
//            } else if (Constants.OBJECT_TYPE.DOCUMENT_PUBLISH.equals(notify
//                    .getObjectType())) {
//                // Van ban di
//                DocumentDAOHE docPublishDaoHe = new DocumentDAOHE();
//                DocumentPublish docPublish = docPublishDaoHe.getById(
//                        "documentPublishId", notify.getObjectId());
//                arguments.put("selectedRecord", docPublish);
//                arguments.put("recordMode", "Edit");
//                arguments.put("parentWindow", homeWnd);
//                arguments.put("urlType", Constants.DOCUMENT_MENU.ALL);
//                createWindow("docView", "/Pages/document/docOut/view.zul",
//                        arguments, Window.MODAL);
//            } else if (Constants.OBJECT_TYPE.CALENDAR.equals(notify
//                    .getObjectType())) {
//                CalendarDAOHE cdhe = new CalendarDAOHE();
//                Calendar cal = cdhe.findById(notify.getObjectId());
//                arguments.put("calendar", cal);
//                arguments.put("viewType",
//                        Constants.CALENDAR_VIEWTYPE.USER_APPROVE);
//                arguments.put("fromHome", true);
//                if (cal.getCreateUserId().equals(getUserId())) {
//                    Window createWnd = (Window) Executions.createComponents(
//                            "/Pages/calendar/calendarCreateWnd.zul", null,
//                            arguments);
//                    createWnd.doModal();
//                } else {
//                    Window viewWnd = (Window) Executions.createComponents(
//                            "/Pages/calendar/calendarViewWnd.zul", null,
//                            arguments);
//                    viewWnd.doModal();
//                }
//            } else if (Constants.OBJECT_TYPE.FILES.equals(notify
//                    .getObjectType())) {
//                arguments.put("fileId", notify.getObjectId());
//                Window viewWnd = (Window) Executions.createComponents(
//                        "/Pages/file/fileViewWnd.zul", null, arguments);
//                viewWnd.setWidth("1200px");
//                viewWnd.doModal();
//            }
//        }
//    }
//    /**
//     * ham nay load thong bao noi bo
//     * @param event 
//     */
//    @Listen("onOpenViewAlert = #homeWnd")
//    public void onOpenViewAlert(Event event) {
//       
//        if (event != null) {
//            Gson gson = new Gson();
//            String data = event.getData().toString();
//            Notify notify = gson.fromJson(data, Notify.class);
////            HashMap<String, Object> arguments =new HashMap<String, Object>();
//            HashMap<String, Object> arguments = new HashMap<String, Object>();
//            arguments.put("notify", notify);
////            arguments.put("parentWindow", homeWnd);
//            windowNotifyDetailView = (Window) Executions.createComponents("/Pages/notify/notifyDetailView.zul", null, arguments);
//            windowNotifyDetailView.doModal();
////            createWindow("windowNotifyDetailView",
////                    "/Pages/notify/notifyDetailView.zul", arguments,
////                    Window.EMBEDDED);
//
////            homeWnd.setVisible(false);
//        }
//    }
//    
//    public Long countDocOutHome(int criteria) {
//        DocumentDAOHE docDaoHe = new DocumentDAOHE();
//        DocumentSearchModel searchForm = new DocumentSearchModel();
//        if (criteria == 1) {
//            // count so van ban di cho xu ly
//            searchForm.setUrlType(Constants.DOCUMENT_MENU.WAITING_PROCESS);
//        } else if (criteria == 2) {
//            // count so van ban qua han
//            searchForm.setUrlType(Constants.DOCUMENT_MENU.MENU_PUBLISHED);
//            searchForm.setStatus(Constants.DOCUMENT_STATUS.PUBLISH);
//        } else {
//            // count so van ban trong tuan
//            searchForm.setUrlType(Constants.DOCUMENT_MENU.WAITING_PROCESS);
//            searchForm.setDateCreateFrom(DateTimeUtils.getWeekStart(null));
//            searchForm.setDateCreateTo(DateTimeUtils.getWeekEnd(null));
//        }
//
//        searchForm.setCreatorId(getUserId());
//        Long count = docDaoHe.countDocument(searchForm, 0, Integer.MAX_VALUE);
//        if (count == null) {
//            return 0L;
//        }
//        return count;
//    }
//
//    private void onLoadCountDocOut() {
//        lbDocOutNotProcess.setValue(countDocOutHome(1).toString());
//        lbDocOutPublish.setValue(countDocOutHome(2).toString());
//        lbDocOutInWeek.setValue(countDocOutHome(3).toString());
//    }
//
//    private void onLoadCountDocIn() {
//        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
//        DocumentReceiveSearchModel model = new DocumentReceiveSearchModel();
//        model.setMenuType(Constants.DOCUMENT_MENU.WAITING_PROCESS);
//        Long size = (Long) documentReceiveDAOHE.getDocumentReceiveOfUser(
//                getUserId(), getDeptId(), model, -1, -1, true, isFileClerk())
//                .get(0);
//        lbDocInWaitingProcess.setValue(size.toString());
//
//        lbDocInExpired.setValue(String.valueOf(documentReceiveDAOHE
//                .numberOfExpiredDoc(getDeptId(), getUserId(), isFileClerk())));
//
//        model.setMenuType(Constants.DOCUMENT_MENU.PROCESSING);
//        size = (Long) documentReceiveDAOHE.getDocumentReceiveOfUser(
//                getUserId(), getDeptId(), model, -1, -1, true, isFileClerk())
//                .get(0);
//        lbDocInProcessing.setValue(size.toString());
//    }
//
//    private void onLoadCountCalendar() {
//        CalendarDAOHE cdhe = new CalendarDAOHE();
//        Long userId = getUserId();
//        int cToday = cdhe.countCalendarInDay(userId);
//        int cTomorow = cdhe.countCalendarInTomorow(userId);
//        int cInWeek = cdhe.countCalendarInWeek(userId);
//        lbCalInDay.setValue(String.valueOf(cToday));
//        lbCalInTomorow.setValue(String.valueOf(cTomorow));
//        lbCalInWeek.setValue(String.valueOf(cInWeek));
//    }
//
//    private void onCountFiles() {
////        FilesDAOHE fdhe = new FilesDAOHE();
////        Long userId = getUserId();
////        int cAll = fdhe.countFiles(userId, null);
////        int cMain = fdhe.countFiles(userId, Constants.PROCESS_TYPE.MAIN);
////        int cCoope = fdhe.countFiles(userId, Constants.PROCESS_TYPE.COOPERATE);
////        lbFileAll.setValue(String.valueOf(cAll));
////        lbFileMain.setValue(String.valueOf(cMain));
////        lbFileCoop.setValue(String.valueOf(cCoope));
//    }
//
//    @Listen("onVisible = #homeWnd")
//    public void onVisible() {
//        homeWnd.setVisible(true);
//   }
}
