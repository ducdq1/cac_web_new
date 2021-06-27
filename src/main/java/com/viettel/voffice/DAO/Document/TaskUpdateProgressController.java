package com.viettel.voffice.DAO.Document;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.user.BO.Users;
import com.viettel.utils.Constants;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Task;
import com.viettel.voffice.BO.Document.TaskProgress;
import com.viettel.voffice.DAOHE.TaskManageDAOHE;
import com.viettel.voffice.DAOHE.TaskProgressDAOHE;

/**
 *
 * @author thanhvt10
 */
public class TaskUpdateProgressController extends BaseComposer {

    @Wire
    Window widowUpdateTaskProgress;
    @Wire
    Textbox taskName;
    @Wire
    Textbox commentTask;
    @Wire
    Textbox taskProgressPercent;
    @Wire
    Listbox nextStageId;
    @Wire
    Listbox listTaskProgress;
    @Wire
    Groupbox groupUpdateProgress;
    @Wire
    Div divUpdateProgress;
    @Wire
    Button btnSaveTaskProgress;
    @Wire
    Listbox listChild;
    @Wire
    Groupbox childDiv;
    private Vlayout flist;
    private List<Media> listMedia;
    private Task taskBO = new Task();
    private List<Category> listNextStage;
    private List<Category> listType;
    private List<Users> listUser;
    private TaskManageDAOHE taskDAOHE = new TaskManageDAOHE();
    private Task task = new Task();
    private List<TaskProgress> historyTaskProgress = new ArrayList<TaskProgress>();
    private TaskProgress taskpg;
    private List<Task> listTaskChild ;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        listMedia = new ArrayList<Media>();
        listNextStage = new TaskManageDAOHE().getListPriorityTask(Constants.CATEGORY_TYPE.VOFFICE_CAT_NEXTSTAGE);
        Category catFirst = new Category();
        catFirst.setCategoryId(-1L);
        catFirst.setName("-------------Chọn------------");
        listNextStage.add(0, catFirst);
        nextStageId.setModel(new ListModelList(listNextStage));
        historyTaskProgress = new TaskManageDAOHE().getListHistoryTaskProgress(task.getTaskId());
        listTaskProgress.setModel(new ListModelList(historyTaskProgress));
        String viewUpdateProgress = (String) Executions.getCurrent().getArg().get("viewUpdateProgress");
        if(viewUpdateProgress!= null && !"".equals(viewUpdateProgress)){
            widowUpdateTaskProgress.setTitle("Chi tiết công việc");
            groupUpdateProgress.setVisible(false);
            btnSaveTaskProgress.setVisible(false);
        }
        
            if (task.getStartTime() != null) {
                String starTimeStr[] = task.getStartTime().toString().split(" ");
                task.setStartTimeStr(starTimeStr[0].toString());
            }
            if (task.getDeadline() != null) {
                String deadlineStr[] = task.getDeadline().toString().split(" ");
                task.setDeadlineStr(deadlineStr[0].toString());
            }
        
        
        if(listTaskChild != null && !listTaskChild.isEmpty()){
            listChild.setModel(new ListModelList(listTaskChild));
            childDiv.setVisible(true);
        }else{
            childDiv.setVisible(false);
        }
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        task = (Task) Executions.getCurrent().getArg().get("taskProgress");
        listTaskChild = new TaskProgressDAOHE().getListTaskChild(task.getTaskId());

        
        return super.doBeforeCompose(page, parent, compInfo); //To change body of generated methods, choose Tools | Templates.
    }

    @Listen("onUpload = #btnAttach")
    public void onUpload(UploadEvent event) throws UnsupportedEncodingException {
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

    @Listen("onClick = #btnSaveTaskProgress")
    public void onSaveTaskProgress() {
        if (validateFormUpdateTaskProgress()) {
            Messagebox.show(String.format(Constants.Notification.SAVE_CONFIRM, "tiến độ công việc"),
                    "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event event) {
                            if (null != event.getName()) {
                                switch (event.getName()) {
                                    case Messagebox.ON_OK:
                                        //OK is clicked
                                        saveTaskProgress();
                                        afterSaveOrUpdate();
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
    @Listen("onClick = #btnCloseUpdateProgress")
    public void oncloseUpdateProgress() {
        afterSaveOrUpdate();
        listMedia = new ArrayList<>();
        widowUpdateTaskProgress.detach();
    }
    @Listen("onClick = #onViewUpdateProgress")
    public void onViewUpdateProgress() {
        groupUpdateProgress.setVisible(false);
        divUpdateProgress.setVisible(false);
    }
//    @Listen("onDetailChidTask = #listChild")
//    public void onDetailChidTask(Event event) {
//        Task taskProgress = (Task) event.getData();
////        Event origin = Events.getRealOrigin((ForwardEvent) event);
////        Image btn = (Image) origin.getTarget();
////        Task taskOne = (Task) btn.getParent().getParent();
//        HashMap<String, Object> arguments =HashMap<String, Object>();
//        arguments.put("taskProgress", taskProgress);
//        arguments.put("viewUpdateProgress", "true");
//        createWindow("widowUpdateTaskProgress", "/Pages/document/task/taskUpdateProgress.zul",
//                arguments, Window.MODAL);
//        listChild.clearSelection();
//    }
    

    private boolean validateFormUpdateTaskProgress() {
        if (commentTask.getValue() == null || "".equals(commentTask.getValue())) {
            showNotification(" Bạn chưa cập nhập nội dung tiến độ công việc !", Constants.Notification.WARNING);
            return false;
        }
        if (taskProgressPercent.getValue() == null) {
            showNotification(" Bạn chưa cập nhập % tiến độ hoàn thành công việc !", Constants.Notification.WARNING);
            return false;
        } else {
            Long percent = Long.parseLong(taskProgressPercent.getValue());
            if (percent > Constants.Task.MAX_PERCENT) {
                showNotification(" Tiến độ hoàn thành công việc không được vượt quá 100%!", Constants.Notification.WARNING);
                return false;
            }
        }
        return true;
    }
    private void afterSaveOrUpdate(){
        historyTaskProgress = new TaskManageDAOHE().getListHistoryTaskProgress(task.getTaskId());
        listTaskProgress.setModel(new ListModelList(historyTaskProgress));
        taskProgressPercent.setValue("");
        commentTask.setValue("");
//        nextStageId.setSelectedItems(0);
    }
    private void saveTaskProgress() {
        String notifyStr = "";
        try {
            taskpg = new TaskProgress();
            taskpg.setUserCreateId(getUserId());
            taskpg.setUserCreateName(getUserFullName());
            taskpg.setUserGroupCreateId(getDeptId());
            taskpg.setUserGroupCreateName(getDeptName());
            taskpg.setTaskId(task.getTaskId());
            taskpg.setCommentTask(commentTask.getValue());
            taskpg.setProgressPercent(Long.parseLong(taskProgressPercent.getValue()));
            taskpg.setCreateDate(new Date());
            taskpg.setIsActive(Constants.Status.ACTIVE);
            if (nextStageId.getSelectedItem().getValue() != null) {
                Category cat = (Category) nextStageId.getSelectedItem().getValue();
                if (!cat.getCategoryId().equals(-1L)) {
                    taskpg.setNextStageId(cat.getCategoryId());
                    taskpg.setNextStage(cat.getName());
                }
            }
            new TaskProgressDAOHE().saveOrUpdate(taskpg);
            if (taskpg.getProgressPercent().equals(Constants.Task.MAX_PERCENT)) {// Tiến độ công việc cập nhật 100%
                task.setStatus(Constants.Task.FINISH);
                task.setStatusStr(Constants.Task.FINISH_STR);
                new TaskManageDAOHE().update(task);
            }
            notifyStr = "Cập nhật tiến độ thành công!";
            showNotification(notifyStr, Constants.Notification.SAVE_SUCCESS);
        } catch (NumberFormatException | WrongValueException ex) {
            LogUtils.addLog(ex);
            showNotification(" Có lỗi xảy ra trong quá trình lưu lại!", Constants.Notification.WARNING);
        }
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

}
