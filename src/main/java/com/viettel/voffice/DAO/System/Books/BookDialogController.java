/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Books;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Document.Books;

/**
 *
 * @author giangpn
 */
@SuppressWarnings({"rawtypes", "serial"})
public class BookDialogController extends BaseGenericForwardComposer {

    @WireVariable
    private String recordMode;
    Window docBookCRUD; // autowired
    protected Listbox lboxStatus;
    private Window parentWindow;
    private Books bookSelected;
    private AnnotateDataBinder binder;
    private BindingListModelList<Category> catList;
    private Category catSelected;
    @Wire
    private Listbox lboxDocBookType;

//    public BookDialogController(){
//        super();
//    }
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        this.self.setAttribute("controller", this, false);
        Execution execution = Executions.getCurrent();
        setRecordMode((String) execution.getArg().get("recordMode"));
        setBookSelected((Books) execution.getArg().get("selectedRecord"));
        setParentWindow((Window) execution.getArg().get("parentWindow"));

//        List lstDocType = getObjectType();
//        
//        catList = new BindingListModelList<>(lstDocType, true);
        List<Category> lstDocType = getObjectType();
        lstDocType.remove((Category) lstDocType.get(0));
        ListModelArray model = new ListModelArray(lstDocType);

        

        if (recordMode.equals(Constants.RECORD_MODE.CREATE)) {
            if (model.getSize() > 0) {
                model.addToSelection(model.get(0));
            }
            this.bookSelected = new Books();
            bookSelected.setStatus(Constants.Status.ACTIVE);
            bookSelected.setDeptId(getDeptId());
            bookSelected.setDeptName(getDeptName());
            setCatSelected((Category) lstDocType.get(0));
            lboxStatus.setSelectedIndex(0);
        }
        if (recordMode.equals(Constants.RECORD_MODE.EDIT) || recordMode.equals(Constants.RECORD_MODE.VIEW)) {
            this.bookSelected = getBookSelected();
//            if (Objects.equals(bookSelected.getBookObjectTypeId(), Constants.OBJECT_TYPE.DOCUMENT_RECEIVE)) {
//                setCatSelected((Category) lstDocType.get(0));
//            } else if (Objects.equals(bookSelected.getBookObjectTypeId(), Constants.OBJECT_TYPE.DOCUMENT_PUBLISH)) {
//                setCatSelected((Category) lstDocType.get(1));
//            } else if (Objects.equals(bookSelected.getBookObjectTypeId(), Constants.OBJECT_TYPE.FILES_RAPIDTEST)) {
//                setCatSelected((Category) lstDocType.get(2));
//            }
            for (Category docType : lstDocType) {
                if (docType.getCategoryId().equals(bookSelected.getBookObjectTypeId())) {
                    model.addToSelection(docType);
                    break;
                }
            }
            if (recordMode.equals(Constants.RECORD_MODE.EDIT)) {
                if (bookSelected.getStatus().equals(Constants.Status.ACTIVE)) {
                    lboxStatus.setSelectedIndex(0);
                } else {
                    lboxStatus.setSelectedIndex(1);
                }
            }
        }
        
        this.lboxDocBookType.setModel(model);
        //seCatList(catList);
    }

    public void onCreate$docBookCRUD(Event event) {
        this.binder = (AnnotateDataBinder) event.getTarget().getAttribute(
                "binder", true);
        this.binder.loadAll();
    }

    public void onClick$btnSave() {
        HashMap<String, Object> args =new HashMap<String, Object>();
        binder.saveAll();

        if (!validateForm()) {
            return;
        }

        docBookCRUD.detach();

        String status = lboxStatus.getSelectedItem().getValue();

        bookSelected.setStatus(Long.parseLong(status));
        bookSelected.setIsDefault(Long.parseLong("1"));

        bookSelected.setBookObjectTypeId((Long)lboxDocBookType.getSelectedItem().getValue());

        args.put("selectedRecord", this.bookSelected);
        args.put("recordMode", this.recordMode);
        Events.sendEvent(new Event("onSaved", parentWindow, args));

    }

    public void onClick$btnShowDept() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("idOfDisplayNameComp", "/docBookCRUD/txtDeptName");
        args.put("idOfDisplayIdComp", "/docBookCRUD/txtDeptId");
        args.put("idOfDeptSelected", bookSelected.getDeptId());
        args.put("treeFromDept", "treeFromDept");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }

    public String getRecordMode() {
        return recordMode;
    }

    public void setRecordMode(String recordMode) {
        this.recordMode = recordMode;
    }

    public void setBookSelected(Books bookSelected) {
        this.bookSelected = bookSelected;
    }

    public Books getBookSelected() {
        return bookSelected;
    }

    public Window getParentWindow() {
        return parentWindow;
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    public BindingListModelList<Category> getCatList() {
        return catList;
    }

    public void seCatList(BindingListModelList<Category> catList) {
        this.catList = catList;
    }

    public void setCatSelected(Category catSelected) {
        this.catSelected = catSelected;
    }

    public Category getCatSelected() {
        return catSelected;
    }

    private boolean validateForm() {
        if (bookSelected.getBookName().isEmpty()) {
            showNotify("Bạn chưa nhập tên sổ");
            return false;
        }
        if (bookSelected.getPrefix().isEmpty()) {
            showNotify("Bạn chưa nhập mã sổ");
            return false;
        }
        if (bookSelected.getCurrentNumber() == null) {
            showNotify("Bạn chưa nhập số sổ");
            return false;
        }
        if (bookSelected.getCurrentNumber() < 0) {
            showNotify("Số sổ phải là số lớn hơn 0");
            return false;
        }
        if (bookSelected.getDeptName().isEmpty()) {
            showNotify("Bạn chưa nhập đơn vị");
            return false;
        }
        return true;
    }
}
