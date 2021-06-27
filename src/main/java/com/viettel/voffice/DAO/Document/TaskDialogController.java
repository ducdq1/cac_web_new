package com.viettel.voffice.DAO.Document;

import com.viettel.core.base.DAO.BaseComposer;

/**
 *
 * @author thanhvt10
 */
public class TaskDialogController extends BaseComposer {/*

    @Wire
    Window newTask;
    @Wire
    Window usersInDeptTree;
    @Wire
    Textbox taskName;
    @Wire
    Combobox taskType;
    @Wire
    Combobox taskPriorityId;
    @Wire
    Listbox taskParentId;
    @Wire
    Listbox userHelp;
    @Wire
    Listbox userPerform;
    @Wire
    Datebox startTime, deadline;
    @Wire
    Textbox description;
    @Wire
    private Vlayout flist;
    private List<Media> listMedia;
    private Task taskBO = new Task();
    private List<Category> listPriority;
    private List<Category> listType;
    private List<Users> listUser;
    private TaskManageDAOHE taskDAOHE = new TaskManageDAOHE();
    private Task taskParent = new Task();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        listMedia = new ArrayList<Media>();
        listUser = new ArrayList<Users>();
        listPriority = taskDAOHE.getListPriorityTask(Constants.CATEGORY_TYPE.VOFFICE_CAT_PRIORITY);
        Comboitem item;
//        Start set list do uu tien
        for (Category cat : listPriority) {
            item = new Comboitem();
            item.setValue(cat.getCategoryId());
            item.setLabel(cat.getName());
            taskPriorityId.getItems().add(item);
            if (Constants.CATEGORY_TYPE.PRIORITY_NORMAL.equals(cat.getCategoryId())) {
                taskPriorityId.setSelectedItem(item);
            }
        }
        //End set list do uu tien

        // set list loai cong viec
        listType = taskDAOHE.getListPriorityTask(Constants.CATEGORY_TYPE.VOFFICE_CAT_TYPE);
        for (Category cat : listType) {
            item = new Comboitem();
            item.setValue(cat.getCategoryId());
            item.setLabel(cat.getName());
            taskType.getItems().add(item);
        }
        Comboitem itemTop = new Comboitem();
        itemTop.setValue(1L);
        itemTop.setLabel("Chọn");
        taskType.getItems().add(itemTop);
        taskType.setSelectedItem(itemTop);
        taskBO.setStartTime(new Date());
        // end list loai cong viec
        
        //get TaskParent
        taskParent = (Task) Executions.getCurrent().getArg().get("taskOne");
        if(taskParent != null){
            List<Task> lstTask = new ArrayList<Task>();
            lstTask.add(taskParent);
            taskParentId.setModel(new ListModelList(lstTask));
            if(taskParent.getStartTime() != null){
                startTime.setValue(taskParent.getStartTime());
            }
            if(taskParent.getDeadline()!= null){
                deadline.setValue(taskParent.getDeadline());
            }
        }
        
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo); //To change body of generated methods, choose Tools | Templates.
    }

    @Listen("onUpload = #btnAttach")
    public void onUpload(UploadEvent event)  throws UnsupportedEncodingException {
        // final Media media = event.getMedia();
        final Media[] medias = event.getMedias();
        for (int i = 0; i < medias.length; i++) {
            final Media media = medias[i];

            String extFile = FileUtil.getFileExtention(media.getName());
			if (!FileUtil.validFileType(extFile.replace(".", ""))) {
                showNotification("Định dạng file không được phép tải lên",
                        Constants.Notification.WARNING);
                continue;
            }

            // luu file vao danh sach file
            listMedia.add(media);

            // layout hien thi ten file va nut "Xóa"
            final Hlayout hl = new Hlayout();
            hl.setSpacing("6px");
            hl.setClass("newFile");
            hl.appendChild(new Label(media.getName()));
            A rm = new A("Xóa");
            rm.addEventListener(Events.ON_CLICK,
                    new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            hl.detach();
                            // xoa file khoi danh sach file
                            listMedia.remove(media);
                        }
                    });
            hl.appendChild(rm);
            flist.appendChild(hl);
        }
    }

    @Listen("onClick = #btnSave")
    public void onSave() {
        if (validateFormTask()) {
            Messagebox.show(String.format(Constants.Notification.SAVE_CONFIRM, "công việc"),
                    "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event event) {
                            if (null != event.getName()) {
                                switch (event.getName()) {
                                    case Messagebox.ON_OK:
                                        //OK is clicked
                                        saveTask();
                                        closeAndBack();
                                        break;
                                    case Messagebox.ON_CANCEL:
                                        break;
                                }
                            }
                        }
                    });
        }
        listMedia = new ArrayList<>();
    }

    @Listen("onClick = #btnSelectUserPerform")
    public void openTree() {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
//        arguments.put("roleInfo", new Roles());
//        arguments.put("recordMode", "Create");
        arguments.put("idOfUserHelp", "/newTask/userHelp");
//            arguments.put("newTask", "/newTask/");
        arguments.put("parentWindow", newTask);
        Window window = (Window) Executions.createComponents(
                "/Pages/document/task/usersTree.zul", null, arguments);
        window.setTitle("Chọn người thực hiện công việc");
        window.doModal();
    }
    
    @Listen("onClick = #btnBack")
    public void closeAndBack() {
        cleanAfterSaveOrUpdate();
        newTask.detach();
    }
    
    public void cleanAfterSaveOrUpdate(){
        taskName.setValue("");
        description.setValue("");
        startTime.setValue(new Date());
    }

    @Listen("onClick = #btnSelectUserHept")
    public void openTreeUserHelp() {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
//        arguments.put("roleInfo", new Roles());
//        arguments.put("recordMode", "Create");
        arguments.put("idOfUserHelp", "/newTask/userHelp");
//            arguments.put("newTask", "/newTask/");
        arguments.put("parentWindow", newTask);
        arguments.put("type", "callForUserHelp");
        Window window = (Window) Executions.createComponents(
                "/Pages/document/task/usersHelpTree.zul", null, arguments);
        window.setTitle("Chọn người phối hợp thực hiện công việc");
        window.doModal();
    }

    @Listen("onSelectUsersHelp = #newTask")
    public void saveUsersHelp(Event event) {
        List<Users> lstUserHelp = (ArrayList<Users>) event.getData();
        ListModelList lstModel = new ListModelList(lstUserHelp);
        userHelp.setModel(lstModel);
    }

    @Listen("onSelectUsersPerform = #newTask")
    public void saveUsersPerForm(Event event) {
        Users oneUserPerForm = (Users) event.getData();
        List<Users> lstUserPerform = new ArrayList<Users>();
        lstUserPerform.add(oneUserPerForm);
        ListModelList lstModel = new ListModelList(lstUserPerform);
        userPerform.setModel(lstModel);
    }

    @Listen("onDelete=#userHelp")
    public void onDeleteUserHelp(Event ev) throws IOException {
        Event origin = Events.getRealOrigin((ForwardEvent) ev);
        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        userHelp.getItems().remove(litem);
    }

    private void saveTask() {
        try {
            String notifyStr = "";
            taskBO = new Task();
            taskBO.setCreateDate(DateTimeUtils.getDate());
            taskBO.setUserCreateId(getUserId());
            taskBO.setUserCreateName(getUserFullName());
            taskBO.setIsActive(Constants.Status.ACTIVE);
            taskBO.setUserGroupId(getDeptId());
            taskBO.setUserGroupName(getDeptName());
            taskBO.setStatus(Constants.Task.PROCESSING);
            taskBO.setStatusStr(Constants.Task.PROCESSING_STR);
            taskBO.setTaskName(taskName.getValue());
            Users performUser;
            if (!userPerform.getItems().isEmpty()) {
                performUser = (Users) userPerform.getItems().get(0).getValue();
                taskBO.setUserPerformId(performUser.getUserId());
                taskBO.setUserPerformName(performUser.getUserName());
            }
            if (!userHelp.getItems().isEmpty()) {
                String idsHept = "";
                String namesHept = "";
                List<Listitem> lstIdHept = userHelp.getItems();
                for (Listitem item : lstIdHept) {
                    performUser = (Users) item.getValue();
                    idsHept = idsHept + ";" + performUser.getUserId();
                    namesHept = namesHept + ";" + (String) performUser.getUserName();
                }
                taskBO.setUsersHelpId(idsHept);
                taskBO.setUserHeptName(namesHept);
            }
            if (description.getValue() != null && !"".equals(description.getValue())) {
                taskBO.setDescription(description.getValue());
            }
            if (startTime.getValue() != null) {
                taskBO.setStartTime(startTime.getValue());
            }
            if (deadline.getValue() != null) {
                taskBO.setDeadline(deadline.getValue());
            }
            if (taskPriorityId.getSelectedItem().getId() != null && !"".equals(taskPriorityId.getSelectedItem().getId())) {
                taskBO.setTaskPriorityId(Long.parseLong("" + taskPriorityId.getSelectedItem().getId()));
                taskBO.setTaskPriorityName(taskPriorityId.getSelectedItem().getValue().toString());
            }
            if (taskType.getSelectedItem().getId() != null && !"".equals(taskType.getSelectedItem().getId())) {
                taskBO.setTaskType(Long.parseLong("" + taskType.getSelectedItem().getId()));
                taskBO.setTaskTypeName(taskType.getSelectedItem().getValue().toString());
            }
            if(!taskParentId.getItems().isEmpty()){
                Task parentTask = (Task) taskParentId.getItems().get(0).getValue();
                taskBO.setTaskParentId(parentTask.getTaskId());
                taskBO.setTaskParentName(parentTask.getTaskName());
            }
//            taskBO.setPath(notifyStr);
            taskBO.setProgressPercent(0L);
            new TaskManageDAOHE().saveOrUpdate(taskBO);
            notifyStr = "Giao việc thành công!";
            for (Object media : listMedia) {
                new AttachDAO().saveFileAttach((Media) media, taskBO.getTaskId(), Constants.OBJECT_TYPE.TASK, null);
            }
            showNotification(notifyStr, Constants.Notification.SAVE_SUCCESS);
        } catch (WrongValueException | NumberFormatException | IOException ex) {
            LogUtils.addLog(ex);
            showNotification(" Có lỗi xảy ra trong quá trình lưu lại!", Constants.Notification.WARNING);
        }
    }

    private boolean validateFormTask() {
        if (taskName.getValue() == null || "".equals(taskName.getValue())) {
            showNotification(" Bạn chưa nhập tên công việc!", Constants.Notification.WARNING);
            return false;
        }
        if (userPerform.getItems().isEmpty()) {
            showNotification(" Bạn chưa chọn người thực hiện công việc!", Constants.Notification.WARNING);
            return false;
        }
        if(startTime.getValue() != null){
//            startTime.getValue().setHours(0);
            startTime.getValue().setHours(23);
            startTime.getValue().setMinutes(59);
        }
        if(startTime.getValue() == null){
            showNotification(" Ngày bắt đầu không được bỏ trống!", Constants.Notification.WARNING);
            return false;
        }else {
            Calendar cal = Calendar.getInstance() ;
            if(startTime.getValue().before(cal.getTime())){
                showNotification(" Ngày bắt đầu không được trước ngày hôm nay!", Constants.Notification.WARNING);
                return false;
            }else{
                if(taskParent != null){
                    if(taskParent.getStartTime()!= null){
                        if(startTime.getValue().before(taskParent.getStartTime())){
                            String dataStr = taskParent.getStartTime().toString();
                            String timeStr[] = dataStr.split(" ");
                            showNotification(" Ngày bắt đầu không được trước ngày " + timeStr[0].toString(), Constants.Notification.WARNING);
                            return false;
                        }
                    }
                }
            }
        }
        if(deadline.getValue() != null){
            deadline.getValue().setHours(23);
            deadline.getValue().setMinutes(59);
            if(taskParent != null){
                if(taskParent.getDeadline()!= null){
                    if(deadline.getValue().after(taskParent.getDeadline())){
                        String dataEndStr = taskParent.getDeadline().toString();
                        String endtimeStr[] = dataEndStr.split(" ");
                        showNotification(" Ngày hoàn thành không được sau ngày " + endtimeStr[0].toString(), Constants.Notification.WARNING);
                        return false;
                    }
                }
            }
        }
        return true;
    }
*/}
