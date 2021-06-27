package com.viettel.voffice.DAO.System.Department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.ZulEvents;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.base.model.PseudoRootNode;
import com.viettel.core.base.model.TreeModel;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.DeptNode;
import com.viettel.core.user.model.UserBean;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

public class DepartmentController extends BaseComposer {

    //<editor-fold defaultstate="collapsed" desc="declare variables">
    private static final long serialVersionUID = 1L;
    private Department deptSelected;
    private List<Department> lstDeptSelected;
    protected List<Department> lstDepts;
    protected Long itemSelected;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="declare controls">
    @Wire
    Window win;
    @Wire
    Tree tree;
    @Wire
    Button btnAdd;
    @Wire
    Button btnDelete;
    @Wire
    Listbox deptChildsListbox;
    @Wire
    Listbox deptUsersListbox;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public methods">
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        refreshForm();
    }

    private void refreshForm() {
        DepartmentDAOHE deptHe = new DepartmentDAOHE();
        Department root = deptHe.getDeptRoot(getDeptId());
        if (root == null) {
            return;
        }    
        TreeModel depts = "admin".equals(getUserName()) ? new TreeModel(new DeptNode(-1L, null, DeptNode.TYPE.DEPT_ONLY)) : new TreeModel(new PseudoRootNode(-1L, null, new DeptNode(root.getDeptId(), root.getDeptName(), DeptNode.TYPE.DEPT_ONLY)));
        this.tree.setModel(depts);
        //this.tree.setSelectedItem();
        lstDeptSelected = new LinkedList<>();
        //this.tree.addEventHandler(null, null);
        tree.addEventListener(ZulEvents.ON_AFTER_RENDER, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (itemSelected == null) {
                    Treeitem itemFirst = (Treeitem) tree.getTreechildren().getFirstChild();
                    tree.setSelectedItem(itemFirst);
                    itemFirst.setOpen(true);
                } else {
                    DepartmentDAOHE ddhe = new DepartmentDAOHE();
                    List<Long> parentIds = ddhe.getParents(itemSelected);
                    doExpandTree(tree.getTreechildren().getItems(), parentIds,itemSelected,tree);
                }
                doSelected();
            }
        });
    }

    public void setDeptSelected(Department dept) {
        this.deptSelected = dept;
    }

    public Department getDeptSelected() {
        return deptSelected;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="events">
    @Listen("onSelect = #tree")
    public void doSelected() throws Exception {
        Treeitem item = tree.getSelectedItem();
        DeptNode dn = (DeptNode) item.getValue();
        if (dn != null) {
            Long deptId = dn.getId();
            DepartmentDAOHE ddhe = new DepartmentDAOHE();
            Department dept = ddhe.findBOById(deptId);
            setDeptSelected(dept);
            lstDepts = ddhe.getAllChildIdByParentId(deptId, Constants.Status.ACTIVE, Constants.Status.INACTIVE);
            lstDepts.add(0, dept);
            ListModelList model = new ListModelList(lstDepts);
            model.setMultiple(true);
            this.deptChildsListbox.setModel(model);
            fillUserListbox(deptId);
            setItemSelected(deptId);
        }
    }

    @Listen("onClick = #btnCreate")
    public void addClick() {
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        Treeitem item = tree.getSelectedItem();
        DeptNode dn = (DeptNode) item.getValue();
        Long deptId = dn.getId();
        Department deptCreate = new Department();
        deptCreate.setParentId(deptId);
        arguments.put("titleWindow", "Thêm mới đơn vị");
        arguments.put("parentWindow", win);
        arguments.put("deptParent", deptSelected);
        arguments.put("deptInfo", deptCreate);
        arguments.put("recordMode", Constants.RECORD_MODE.CREATE);
        Window window = (Window) Executions.createComponents("/Pages/admin/department/insertOrupdate.zul", null, arguments);
        window.doModal();
    }

    @Listen("onView=#deptChildsListbox")
    public void viewClick(Event ev) throws Exception {
        Department deptBean = getDeptFromEvent(ev);
        CategoryDAOHE catDaoHe = new CategoryDAOHE();
        Category catDeptType = catDaoHe.getCategoryById(deptBean.getDeptType());
        if (catDeptType != null) {
            deptBean.setDeptTypeName(catDeptType.getName());
        }
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("deptInfo", deptBean);
        arguments.put("parentWindow", win);
        if (deptBean.getParentId() != null) {
            DepartmentDAOHE ddhe = new DepartmentDAOHE();
            Department deptParrent = ddhe.findBOById(deptBean.getParentId());
            if (deptParrent != null) {
                arguments.put("deptParent", deptParrent.getDeptName());
            }
        }
        Window window = (Window) Executions.createComponents("/Pages/admin/department/view.zul", null, arguments);
        window.doModal();
    }

    @Listen("onEdit = #deptChildsListbox")
    public void editClick(Event ev) {
        Department deptBean = getDeptFromEvent(ev);
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("deptInfo", deptBean);
        arguments.put("parentWindow", win);
        arguments.put("titleWindow", "Cập nhật thông tin đơn vị");
        arguments.put("recordMode", Constants.RECORD_MODE.EDIT);
        if (deptBean.getParentId() != null) {
            DepartmentDAOHE ddhe = new DepartmentDAOHE();
            Department deptParrent = ddhe.findBOById(deptBean.getParentId());
            if (deptParrent != null) {
                arguments.put("deptParent", deptParrent);
            }
        }
        Window window = (Window) Executions.createComponents("/Pages/admin/department/insertOrupdate.zul", null, arguments);
        window.doModal();
    }

    @Listen("onClick = #btnDelete")
    public void deleteClick() {
        Set<Listitem> ls = deptChildsListbox.getSelectedItems();
        if (ls.size() <= 0) {
            //Messagebox.show(String.format(Constants.Notification.SELECT_WARNING, "đơn vị"), "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
            showNotify(String.format(Constants.Notification.SELECT_WARNING, "đơn vị"), Constants.Notification.WARNING);
            return;
        }
        lstDeptSelected = new ArrayList();
        for (Listitem l : ls) {
            Department dept = (Department) l.getValue();
            lstDeptSelected.add(dept);
        }

        if (lstDeptSelected.size() == 1) {
            DepartmentDAOHE ddhe = new DepartmentDAOHE();
            Department first = lstDeptSelected.get(0);
            int childs = ddhe.getCountChildByParent(first.getDeptId());
            if (childs > 0) {
                showNotify("Đơn vị cha tồn tại đơn vị cấp dưới", Constants.Notification.ERROR);
                return;
            }
        }

        Messagebox.show(String.format(Constants.Notification.DELETE_CONFIRM, "đơn vị"),
                "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) {
                if (null != event.getName()) {
                    switch (event.getName()) {
                        case Messagebox.ON_OK:
                            //OK is clicked
                            try {
                                DepartmentDAOHE ddhe = new DepartmentDAOHE();
                                //Check exist user in dept or update alway
                                ddhe.onDelete(lstDeptSelected);
                                showNotify(String.format(Constants.Notification.DELETE_SUCCESS, "đơn vị"), Constants.Notification.INFO);
                                refreshForm();
                            } catch (NullPointerException e) {
                            	LogUtils.addLog(e);
                                showNotify(String.format(Constants.Notification.DELETE_ERROR, "đơn vị"), Constants.Notification.ERROR);
                            } finally {
                            }
                            break;
                        case Messagebox.ON_CANCEL:
                            break;
                    }
                }
            }
        });
    }

    @Listen("onViewUser=#deptChildsListbox")
    public void viewUsersClick(Event ev) {
        Department deptBean = getDeptFromEvent(ev);
        fillUserListbox(deptBean.getDeptId());
    }

    @Listen("onSaved = #win")
    public void onSaved(Event ev) throws Exception {
        DepartmentDAOHE ddhe = new DepartmentDAOHE();

        HashMap<String, Object> args = (HashMap<String, Object>) ev.getData();
        String recordMode = (String) args.get("recordMode");
        Department dept = (Department) args.get("selectedRecord");
        if (recordMode.equals(Constants.RECORD_MODE.CREATE)) {
            if (ddhe.onCreateOrUpdate(dept, false)) {
                showNotify(String.format(Constants.Notification.SAVE_SUCCESS, "đơn vị"), Constants.Notification.INFO);
            } else {
                showNotify(String.format(Constants.Notification.SAVE_ERROR, "đơn vị"), Constants.Notification.ERROR);
            }
        }

        if (recordMode.equals(Constants.RECORD_MODE.EDIT)) {
            if (ddhe.onCreateOrUpdate(dept, true)) {
                showNotify(String.format(Constants.Notification.UPDATE_SUCCESS, "đơn vị"), Constants.Notification.INFO);
            } else {
                showNotify(String.format(Constants.Notification.UPDATE_ERROR, "đơn vị"), Constants.Notification.ERROR);
            }
        }
        //doSelected();
        refreshForm();
    }

    @Listen("onCheckItem=#deptChildsListbox")
    public void onCheckItem(ForwardEvent ev) {
        Checkbox checkbox = (Checkbox) ev.getOrigin().getTarget();
        Listitem litem = (Listitem) checkbox.getParent().getParent();
        Department dept = (Department) litem.getValue();
        if (checkbox.isChecked()) {
            lstDeptSelected.add(dept);
        } else {
            lstDeptSelected.remove(dept);
        }
    }

    @Listen("onCheckAll=#deptChildsListbox")
    public void onCheckAll(ForwardEvent ev) {
        Checkbox checkbox = (Checkbox) ev.getOrigin().getTarget();
        List<Listitem> list = deptChildsListbox.getItems();

        if (list.size() > 0) {
            for (Listitem li : list) {
                if (li.getFirstChild().getNextSibling().getFirstChild() instanceof Checkbox) {
                    Checkbox cb = (Checkbox) li.getFirstChild().getNextSibling().getFirstChild();
                    cb.setChecked(checkbox.isChecked());
                    Department dept = (Department) li.getValue();
                    if (cb.isChecked() && !cb.isDisabled()) {
                        lstDeptSelected.add(dept);
                    } else {
                        lstDeptSelected.remove(dept);
                    }
                }
            }
        }
    }

    public boolean getCboxVisible(Long parentId) {
        boolean result ;

        if (parentId != 0) {
            result = true;
        } else {
            if (lstDepts.size() == 1) {
                result = true;
            } else {
                result = false;
            }
        }

        return result;
    }

    public Long getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(Long itemSelected) {
        this.itemSelected = itemSelected;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="priave methods">
    private void showNotify(String msg, String type) {
        Clients.showNotification(msg, type, null, "middle_center", 1500);
    }

    private Department getDeptFromEvent(Event ev) {
        Event origin;
        if (ev instanceof ForwardEvent) {
            origin = Events.getRealOrigin((ForwardEvent) ev);
        } else {
            origin = ev;
        }
        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        return (Department) litem.getValue();
    }

    private void fillUserListbox(Long deptId) {
        UserBean uBean = new UserBean();
        uBean.setDeptId(deptId);
        UserDAOHE udhe = new UserDAOHE();
        PagingListModel lstUserModel = udhe.search(uBean, 0, Integer.MAX_VALUE);
        if (lstUserModel != null) {
            this.deptUsersListbox.setModel(new ListModelArray(lstUserModel.getLstReturn()));
        }
    }

    //</editor-fold>
}
