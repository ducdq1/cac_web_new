/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Books;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Books;
import com.viettel.voffice.DAOHE.BookDAOHE;

/**
 *
 * @author giangpn
 */
@SuppressWarnings({"rawtypes", "serial"})
public class BookController extends BaseGenericForwardComposer {

    //<editor-fold defaultstate="collapsed" desc="declare controls">
    @WireVariable
    private BookDAOHE bookDaoHe;
    private Books searchForm;
    @Wire
    Listbox bookListBox;
    @Wire
    Window docBookWindow;
    private Books bookSelected;
    protected Listbox lboxStatus;
    @Wire
    protected Listbox lboxDocBookType;

    private AnnotateDataBinder binder;
    private BindingListModelList<Category> catList;
    private Category catSelected;
    private BindingListModelList<Books> booksList;
    private Long statusUpdate;

    //</editor-fold>
    public BookController() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

    }

    public void onCreate$docBookWindow(Event event) throws Exception {

        this.binder = (AnnotateDataBinder) event.getTarget().getAttribute(
                "binder", true);
        //doFillListbox();                

        Books bookDefault = new Books();
        //bookDefault.setCurrentNumber(0L);
        bookDefault.setDeptId(getDeptId());

        setSearchForm(bookDefault);

        List lstDocType = getObjectType();
        ListModelArray model = new ListModelArray(lstDocType);
        if (model.getSize() > 0) {
            model.addToSelection(model.get(0));
        }
 
        this.lboxDocBookType.setModel(model);
        doFillListbox();
        this.binder.loadAll();
    }

    public BindingListModelList<Category> getCatList() {
        return catList;
    }

    public void seCatList(BindingListModelList<Category> catList) {
        this.catList = catList;
    }

    //<editor-fold defaultstate="collapsed" desc="events">
    public void onClick$btnSearch(Event ev) throws IOException {

        binder.saveAll();
        searchForm.setBookObjectTypeId((Long)lboxDocBookType.getSelectedItem().getValue());
        String status = lboxStatus.getSelectedItem().getValue();
        searchForm.setStatus(Long.parseLong(status));
        doFillListbox();
        //showNotify(searchForm.getCurrentNumber().toString(), docBookWindow);
    }

    public void onClick$btnClearDept(Event ev) {
        binder.saveAll();
        searchForm.setDeptId(getDeptId());
        searchForm.setDeptName("");
    }

    public void onEdit$bookListBox(ForwardEvent evt) throws IOException {
        viewOrEdit(evt, true);

    }

    public void onView$bookListBox(ForwardEvent evt) throws IOException {
        viewOrEdit(evt, false);
    }

    public void onClick$btnCreate() throws IOException {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("selectedRecord", new Books());
        arguments.put("recordMode", Constants.RECORD_MODE.CREATE);
        arguments.put("parentWindow", docBookWindow);
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/books/insertOrupdate.zul", null, arguments);
        window.doModal();
    }

    public void onClick$btnShowDept() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("idOfDisplayNameComp", "/docBookWindow/txtDeptName");
        args.put("idOfDisplayIdComp", "/docBookWindow/txtDeptId");
        args.put("idOfDeptSelected", getDeptId());
        args.put("treeFromDept", "treeFromDept");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
    }

    @SuppressWarnings({"unchecked"})
    public void onSaved(Event event) {
        bookDaoHe = new BookDAOHE();

        HashMap<String, Object> args = (HashMap<String, Object>) event.getData();
        String recordMode = (String) args.get("recordMode");
        Books book = (Books) args.get("selectedRecord");

        if (recordMode.equals(Constants.RECORD_MODE.CREATE)) {
            if (bookDaoHe.onCreate(book)) {
                showNotify("Thêm mới thành công!");
            } else {
                showNotify("Thêm mới lỗi!");
            }
        }

        if (recordMode.equals(Constants.RECORD_MODE.EDIT)) {
            if (bookDaoHe.onUpdate(book)) {
                showNotify("Cập nhật thành công!");
            } else {
                showNotify("Cập nhật lỗi!");
            }

        }
        doFillListbox();
    }

    public void onDelete$bookListBox(ForwardEvent evt) throws IOException {
        onDLULBook(evt, Constants.Status.DELETE);
    }

    public void onLockBook$bookListBox(ForwardEvent evt) throws IOException {
        onDLULBook(evt, Constants.Status.INACTIVE);
    }

    public void onUnLockBook$bookListBox(ForwardEvent evt) throws IOException {
        onDLULBook(evt, Constants.Status.ACTIVE);
    }

    @SuppressWarnings("unchecked")
	public void onDLULBook(ForwardEvent evt, Long status) {
        //bookDaoHe = new BookDAOHE();
        //setSelectedRole(getRoleFromEvent(ev));
        statusUpdate = status;
        Event origin = Events.getRealOrigin(evt);
        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Books curBook = (Books) litem.getValue();
        setBookSelected(curBook);
        if (statusUpdate.equals(Constants.Status.DELETE)) {
//            List<String> key = new ArrayList();
//            List<Object> valueOfKey = new ArrayList();
//            key.add("bookId");
//            valueOfKey.add(curBook.getBookId());
//            BookDocumentDAOHE bookDocumentDAOHE = new BookDocumentDAOHE();
//            if (bookDocumentDAOHE.isExistIDInDb(key, valueOfKey, null, null)) {
//                showErrorNotify("Sổ văn bản này đã dùng, bạn không được phép xóa!");
//                return;
//            }
        }
        String mesConfirm ;
        if (statusUpdate.equals(Constants.Status.DELETE)) {
            mesConfirm = getDeleteConfirm("sổ văn bản");
        } else if (statusUpdate.equals(Constants.Status.ACTIVE)) {
            mesConfirm = getUnLockConfirm("sổ văn bản");
        } else {
            mesConfirm = getLockConfirm("sổ văn bản");
        }
        Messagebox.show(mesConfirm, "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            public void onEvent(Event evt) throws InterruptedException {
                if (("onOK").equals(evt.getName())) {
                    BookDAOHE bookDaoHe1 = new BookDAOHE();
                    bookSelected.setStatus(statusUpdate);
                    String msg = "";
                    try {
                        bookDaoHe1.update(bookSelected);
                        if (statusUpdate.equals(Constants.Status.DELETE)) {
                            msg = getDeleteSuccess("sổ văn bản");
                        } else if (statusUpdate.equals(Constants.Status.ACTIVE)) {
                            msg = getUnLockSuccess("sổ văn bản");
                        } else {
                            msg = getLockSuccess("sổ văn bản");
                        }
                        showNotify(msg);
                        doFillListbox();
                    } catch (NullPointerException e) {
                    	LogUtils.addLog(e);
                        if (statusUpdate.equals(Constants.Status.DELETE)) {
                            getDeleteError("sổ văn bản");
                        } else if (statusUpdate.equals(Constants.Status.ACTIVE)) {
                            msg = getUnLockError("sổ văn bản");
                        } else {
                            msg = getLockError("sổ văn bản");
                        }
                        showErrorNotify(msg);
                    }
                }
            }
        });
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public methods">
    public void doFillListbox() {

        bookDaoHe = new BookDAOHE();
        List<Books> books = bookDaoHe.search(this.searchForm);
        //List<BookModel> bms = MapperUtils.BookListToModelList(books);
        booksList = new BindingListModelList<>(books, true);
        setBookList(booksList);
        this.bookListBox.setModel(booksList);
    }

    public Books getSearchForm() {
        return searchForm;
    }

    public void setSearchForm(Books searchForm) {
        this.searchForm = searchForm;
    }

    public BindingListModelList<Books> getBooksList() {
        return booksList;
    }

    public void setBookList(BindingListModelList<Books> lstBooks) {
        this.booksList = lstBooks;
    }

    public void setCatSelected(Category catSelected) {
        this.catSelected = catSelected;
    }

    public Category getCatSelected() {
        return catSelected;
    }

    public void setBookSelected(Books bookSelected) {
        this.bookSelected = bookSelected;
    }

    public Books getBookSelected() {
        return bookSelected;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="priave methods">
    private void viewOrEdit(ForwardEvent evt, boolean isEdit) {
        Event origin = Events.getRealOrigin(evt);
        Listitem litem;
        if (isEdit) {
            Image btn = (Image) origin.getTarget();
            litem = (Listitem) btn.getParent().getParent();
        } else {
            Listcell cell = (Listcell) origin.getTarget();
            litem = (Listitem) cell.getParent();
        }

        Books curBook = (Books) litem.getValue();
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("selectedRecord", curBook);
        arguments.put("recordMode", isEdit ? Constants.RECORD_MODE.EDIT : Constants.RECORD_MODE.VIEW);
        arguments.put("parentWindow", docBookWindow);
        Window window = (Window) Executions.createComponents(
                isEdit ? "/Pages/admin/books/insertOrupdate.zul" : "/Pages/admin/books/view.zul", null, arguments);
        window.doModal();
    }
    //</editor-fold>
}
