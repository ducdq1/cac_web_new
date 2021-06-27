package com.viettel.voffice.DAO.Document;

import com.viettel.core.base.DAO.BaseComposer;

/**
 *
 * @author thanhvt10
 */
public class TaskManageController extends BaseComposer {/*

    @Wire
    Window newWindow;
    @Wire
    Tree treeParent;
    @Wire
    Listbox listTaskReceived;
    @Wire
    Tree treeTask;
    TaskManageDAOHE taskDAOHE = new TaskManageDAOHE();
    List<Task> listTaskRc;
    Task searchForm;
    TaskManagerTreeModel model;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        searchForm = new Task();
        searchForm.setUserPerformId(getUserId());
        model = new TaskManagerTreeModel(searchForm);
        treeTask.setModel(model);
    }

    @Listen("onClick = #btnNewTask")
    public void onOpenCreate() throws IOException {
        HashMap<String, Object> arguments =HashMap<String, Object>();
        arguments.put("parentWindow", newWindow);
        arguments.put("selectedRecord", new Task());
//        createEmbeddedWindow("newTask", "/Pages/document/task/createNewTask.zul", arguments);
        createWindow("newTask", "/Pages/document/task/createNewTask.zul", arguments, Window.MODAL);

        newWindow.setVisible(false);
    }

    @Listen("onClick = #treeMyJobReceived")
    public void doSelectMyJobReceived() {
        searchForm = new Task();
        searchForm.setUserPerformId(getUserId());
        model = new TaskManagerTreeModel(searchForm);
        treeTask.setModel(model);
    }

    @Listen("onClick = #treeMyJobSend")
    public void doSelectMyJobSend() {
        searchForm = new Task();
        searchForm.setUserCreateId(getUserId());
        model = new TaskManagerTreeModel(searchForm);
        treeTask.setModel(model);
    }
    
    @Listen("onClick = #jobFinish")
    public void doSelectJobFinish() {
        searchForm = new Task();
        searchForm.setUserPerformId(getUserId());
        searchForm.setStatus(Constants.Task.FINISH);
        model = new TaskManagerTreeModel(searchForm);
        treeTask.setModel(model);
    }
    
    @Listen("onClick = #jobUnFinish")
    public void doSelectJobUnFinish() {
        searchForm = new Task();
        searchForm.setUserPerformId(getUserId());
        searchForm.setStatus(Constants.Task.PROCESSING);
        model = new TaskManagerTreeModel(searchForm);
        treeTask.setModel(model);
    }
    
    @Listen("onClick = #jobDestroy")
    public void doSelectJobDestroy() {
        searchForm = new Task();
        searchForm.setUserPerformId(getUserId());
        searchForm.setStatus(Constants.Task.PROCESSING);
        model = new TaskManagerTreeModel(searchForm);
        treeTask.setModel(model);
    }

    @Listen("onOpenCreateTask = #treeTask")
    public void onOpenObject(Event event) {
        Task taskOne = (Task) event.getData();
//        Event origin = Events.getRealOrigin((ForwardEvent) event);
//        Image btn = (Image) origin.getTarget();
//        Task taskOne = (Task) btn.getParent().getParent();
        HashMap<String, Object> arguments =HashMap<String, Object>();
        arguments.put("taskOne", taskOne);
        createWindow("newTask", "/Pages/document/task/createNewTask.zul",
                arguments, Window.MODAL);

        treeTask.clearSelection();
    }
    
    @Listen("onViewUpdateProgress = #treeTask")
    public void onViewUpdateProgress(Event event) {
        Task taskProgress = (Task) event.getData();
//        Event origin = Events.getRealOrigin((ForwardEvent) event);
//        Image btn = (Image) origin.getTarget();
//        Task taskOne = (Task) btn.getParent().getParent();
        HashMap<String, Object> arguments =HashMap<String, Object>();
        arguments.put("taskProgress", taskProgress);
        arguments.put("viewUpdateProgress", "true");
        createWindow("widowUpdateTaskProgress", "/Pages/document/task/taskUpdateProgress.zul",
                arguments, Window.MODAL);

        treeTask.clearSelection();
    }

    @Listen("onUpdateProgress = #treeTask")
    public void onOpenUpdateTaskProgress(Event event) {
        Task taskProgress = (Task) event.getData();
//        Event origin = Events.getRealOrigin((ForwardEvent) event);
//        Image btn = (Image) origin.getTarget();
//        Task taskOne = (Task) btn.getParent().getParent();
        HashMap<String, Object> arguments =HashMap<String, Object>();
        arguments.put("taskProgress", taskProgress);
        createWindow("widowUpdateTaskProgress", "/Pages/document/task/taskUpdateProgress.zul",
                arguments, Window.MODAL);

        treeTask.clearSelection();
    }
*/}
