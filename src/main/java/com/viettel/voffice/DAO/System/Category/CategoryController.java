/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Category;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.BO.CategoryType;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.sys.DAO.CategoryTypeDAOHE;
import com.viettel.core.sys.model.CategorySearchForm;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.workflow.WorkflowAPI;
import com.viettel.utils.Constants;

/**
 *
 * @author giangpn
 */
public class CategoryController extends BaseComposer {

    //<editor-fold defaultstate="collapsed" desc="declare variables">
    @Wire
    Listbox catListbox;
    @Wire
    Listbox catTypeListbox;
    @Wire
    Listbox catListBox;
    @Wire
    Window catManageWindow;
    @Wire
    Window catInfoWindow;
    @Wire
    Hbox detailBox;
    @Wire
    Textbox txtSearchName;
    @Wire
    Textbox txtSearchCode;
    @Wire
    Listbox lboxStatus;
    @Wire
    Groupbox gbCat;
    
    protected Include incCatForm;
    protected CategoryType catTypeSelected;
    private Category catSelected;
    protected List<CategoryType> listCategoryType;
    
    private Boolean IS_SYSTEM_CAT;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="public methods">
    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        HashMap<String, Object> arguments = (HashMap<String,Object>) Executions.getCurrent().getArg();
        String catType = (String) arguments.get("type");
        IS_SYSTEM_CAT = ("SYS".equals(catType));
        doFillListBox();
    }

    public void doFillListBox() {
        CategoryTypeDAOHE catTypeDaoHe = new CategoryTypeDAOHE();
        //Fill danh sach loai danh muc
        //Boolean check = checkIsAdmin();
        //List<CategoryType> catTypes = catTypeDaoHe.getAllCategoryType(check);
        List<CategoryType> catTypes = catTypeDaoHe.getAllCategoryType(IS_SYSTEM_CAT);
        if (catTypes != null && catTypes.size() > 0) {
            ListModelArray model = new ListModelArray(catTypes);
            model.addToSelection(catTypeSelected==null? model.get(0): catTypeSelected);
            CategoryType catTypeFirst = catTypes.get(0);
            setCatTypeSelected(catTypeFirst);
            this.catTypeListbox.setModel(model);
            fillCatListbox();
        }
    }
    private Boolean checkIsAdmin(){
        Long userId = getUserId();
        UserDAOHE userDAOHE  = new UserDAOHE();
        return userDAOHE.checkIsAdmin(userId);
    }
    public CategoryType getCatTypeSelected() {
        return catTypeSelected;
    }

    public void setCatTypeSelected(CategoryType catType) {
        this.catTypeSelected = catType;
    }
    
     public Category getCatSelected() {
        return catSelected;
    }

    public void setCatSelected(Category catSelected) {
        this.catSelected = catSelected;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="events">
    @Listen("onClick=#btnSearch")
    public void searchCategory() {
        fillCatListbox();
    }

    @Listen("onCreateCatType=#catTypeListbox")
    public void showCreateCatType(Event ev) throws IOException {
        showCreateOrEditCatType(ev, false);
    }

    @Listen("onEditCatType=#catTypeListbox")
    public void showEditCatType(Event ev) throws IOException {
        showCreateOrEditCatType(ev, true);
    }

    @Listen("onDeleteCatType=#catTypeListbox")
    public void onDeleteCatType(Event ev) throws IOException {
        CategoryType ct = getCatTypeFromEvent(ev);
        setCatTypeSelected(ct);
        
        EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					 CategoryTypeDAOHE cdhe = new CategoryTypeDAOHE();
	                    catTypeSelected.setIsActive(-1L);
	                    cdhe.update(catTypeSelected);
	                    showNotify("Xoá thành công!");
	                    doFillListBox();
				}
			}
		};
		showDialogConfirm("Bạn có đồng ý xoá loại danh mục này1?", "Thông báo", clickListener);
		
      /*  Messagebox.show("Bạn có đồng ý xoá loại danh mục này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            public void onEvent(Event evt) throws InterruptedException {
                if (("onOK").equals(evt.getName())) {
                    CategoryTypeDAOHE cdhe = new CategoryTypeDAOHE();
                    catTypeSelected.setIsActive(-1L);
                    cdhe.update(catTypeSelected);
                    showNotify("Xoá thành công!");
                    doFillListBox();
                }
            }
        });*/
    }

    @Listen("onLockCatType=#catTypeListbox")
    public void onLockCatType(Event ev) throws IOException {
        CategoryType ct = getCatTypeFromEvent(ev);
        setCatTypeSelected(ct);
       /* Messagebox.show("Bạn có đồng ý khóa loại danh mục này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            public void onEvent(Event evt) throws InterruptedException {
            	 if (("onOK").equals(evt.getName())) {
                    CategoryTypeDAOHE cdhe = new CategoryTypeDAOHE();
                    catTypeSelected.setIsActive(Constants.Status.INACTIVE);
                    cdhe.update(catTypeSelected);
                    showNotify("Danh mục đã bị khóa!");
                    doFillListBox();
                }
            }
        });*/
        
        EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					 CategoryTypeDAOHE cdhe = new CategoryTypeDAOHE();
	                    catTypeSelected.setIsActive(Constants.Status.INACTIVE);
	                    cdhe.update(catTypeSelected);
	                    showNotify("Danh mục đã bị khóa!");
	                    doFillListBox();
				}
			}
		};
		showDialogConfirm("Bạn có đồng ý khóa loại danh mục này?", "Thông báo", clickListener);
	
    }
    
    @Listen("onUnLockCatType=#catTypeListbox")
    public void onUnLockCatType(Event ev) throws IOException {
        CategoryType ct = getCatTypeFromEvent(ev);
        setCatTypeSelected(ct);
        /*Messagebox.show("Bạn có đồng ý mở khóa loại danh mục này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            public void onEvent(Event evt) throws InterruptedException {
            	 if (("onOK").equals(evt.getName())) {
                    CategoryTypeDAOHE cdhe = new CategoryTypeDAOHE();
                    catTypeSelected.setIsActive(Constants.Status.ACTIVE);
                    cdhe.update(catTypeSelected);
                    showNotify("Danh mục đã được mở khóa!");
                    doFillListBox();
                }
            }
        });*/
        
        EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					CategoryTypeDAOHE cdhe = new CategoryTypeDAOHE();
                    catTypeSelected.setIsActive(Constants.Status.ACTIVE);
                    cdhe.update(catTypeSelected);
                    showNotify("Danh mục đã được mở khóa!");
                    doFillListBox();
				}
			}
		};
		showDialogConfirm("Bạn có đồng ý mở khóa loại danh mục này?", "Thông báo", clickListener);
	
		
    }
    
    @Listen("onSelectCatType = #catTypeListbox")
    public void showCatType() {
        Listitem item = catTypeListbox.getSelectedItem();
        CategoryType itemSelected = (CategoryType) item.getValue();
        setCatTypeSelected(itemSelected);
        fillCatListbox();
        //showNotify(itemSelected.getCode(), catManageWindow);
    }

    @Listen("onClick=#btnCreate")
    public void showCreateCat() throws IOException {
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        Category catCreate = new Category();
        catCreate.setIsActive(Constants.Status.ACTIVE);
        if (catCreate.getCode() == null){
            //neu la them moi thi tu dong sinh code
            String autoCode = getAutoCatCode(catTypeSelected.getCode());
            catCreate.setCode(autoCode);
        }
        arguments.put("catInfo", catCreate);
        arguments.put("catTypeInfo", catTypeSelected);
        arguments.put("recordMode", Constants.RECORD_MODE.CREATE);
        arguments.put("parentWindow", catManageWindow);
        arguments.put("catTypeName", getNameByCatCode());
        arguments.put("titleWindow", "Thêm mới danh mục " + getNameByCatCode());
        arguments.put("modifyCatOrCatType", "");
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/category/insertOrupdate.zul", null, arguments);
        window.doModal();
    }
    
    private String getAutoCatCode(String catTypeCode){
        CategoryDAOHE ca = new CategoryDAOHE();
        String code = ca.getCategorybyLastCode(catTypeCode);
        return code;        
    }

    @Listen("onEdit=#catListbox")
    public void showEdit(Event ev) throws IOException {
        showViewOrEdit(true, ev);
    }

    @Listen("onView=#catListbox")
    public void showView(Event ev) throws IOException {
        showViewOrEdit(false, ev);
    }

    @SuppressWarnings("unchecked")
	@Listen("onDelete=#catListbox")
    public void onDelete(Event ev) throws IOException {
        Category catBean = getCatFromEvent(ev);
        setCatSelected(catBean);
       /* Messagebox.show("Bạn có đồng ý xoá danh mục này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            public void onEvent(Event evt) throws InterruptedException {
            	 if (("onOK").equals(evt.getName())) {
                    CategoryDAOHE cdhe = new CategoryDAOHE();
                    catSelected.setIsActive(-1L);
                    cdhe.update(catSelected);
                    showNotify("Xoá thành công!");
                    fillCatListbox();
                    //viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
                    // 04/09/2015
                    WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);
                }
            }
        });*/
        
        EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					 CategoryDAOHE cdhe = new CategoryDAOHE();
	                    catSelected.setIsActive(-1L);
	                    cdhe.update(catSelected);
	                    showNotify("Xoá thành công!");
	                    fillCatListbox();
	                    //viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
	                    // 04/09/2015
	                    WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);
				}
			}
		};
		showDialogConfirm("Bạn có đồng ý xoá danh mục này?", "Thông báo", clickListener);
        
        
    }
    
    @Listen("onLockCat=#catListbox")
    public void onLockCat(Event ev) throws IOException {
        Category catBean = getCatFromEvent(ev);
        setCatSelected(catBean);
      /*  Messagebox.show("Bạn có đồng ý khóa danh mục này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            public void onEvent(Event evt) throws InterruptedException {
            	 if (("onOK").equals(evt.getName())) {
                    CategoryDAOHE cdhe = new CategoryDAOHE();
                    catSelected.setIsActive(Constants.Status.INACTIVE);
                    cdhe.update(catSelected);
                    showNotify("Danh mục đã bị khóa!");
                    fillCatListbox();
                    //viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
                    // 04/09/2015
                    WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);
                }
            }
        });*/
        
        EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					 CategoryDAOHE cdhe = new CategoryDAOHE();
	                    catSelected.setIsActive(Constants.Status.INACTIVE);
	                    cdhe.update(catSelected);
	                    showNotify("Danh mục đã bị khóa!");
	                    fillCatListbox();
	                    //viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
	                    // 04/09/2015
	                    WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);
				}
			}
		};
		showDialogConfirm("Bạn có đồng ý khóa danh mục này?", "Thông báo", clickListener);
        
		
    }
    
    @Listen("onUnLockCat=#catListbox")
    public void onUnLockCat(Event ev) throws IOException {
        Category catBean = getCatFromEvent(ev);
        setCatSelected(catBean);
      /*  Messagebox.show("Bạn có đồng ý mở khóa danh mục này?", "Thông báo", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
            public void onEvent(Event evt) throws InterruptedException {
                if (("onOK").equals(evt.getName())) {
                    CategoryDAOHE cdhe = new CategoryDAOHE();
                    catSelected.setIsActive(Constants.Status.ACTIVE);
                    cdhe.update(catSelected);
                    showNotify("Danh mục đã được mở khóa!");
                    fillCatListbox();
                    //viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
                    // 04/09/2015
                    WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);
                }
            }
        });*/
        
        EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			@Override
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					 CategoryDAOHE cdhe = new CategoryDAOHE();
	                    catSelected.setIsActive(Constants.Status.ACTIVE);
	                    cdhe.update(catSelected);
	                    showNotify("Danh mục đã được mở khóa!");
	                    fillCatListbox();
	                    //viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
	                    // 04/09/2015
	                    WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);
				}
			}
		};
		showDialogConfirm("Bạn có đồng ý mở khóa danh mục này?", "Thông báo", clickListener);
		
    }

    @SuppressWarnings({"unchecked", "static-access"})
    @Listen("onSaved =#catManageWindow")
    public void onSaved(Event event) {
        CategoryDAOHE catDaoHe = new CategoryDAOHE();
        HashMap<String, Object> args = (HashMap<String, Object>) event.getData();
        String recordMode = (String) args.get("recordMode");
        Category catEdit = (Category) args.get("selectedRecord");
        if (("Create").equals(recordMode)) {
            catEdit.setCategoryTypeCode(catTypeSelected.getCode());
            if (catDaoHe.onCreateOrUpdate(catEdit, false)) {
                showNotify("Thêm mới danh mục thành công!");
                //viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
                // 04/09/2015
                WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);      
            } else {
                showNotify("Thêm mới danh mục lỗi!");
            }
        }
        if (("Edit").equals(recordMode)) {
            if (catDaoHe.onCreateOrUpdate(catEdit, true)) {
                showNotify("Cập nhật danh mục thành công!");
                //viethd3: cap nhat trang thai toan cuc IS_UPDATE_STATUS
                // 04/09/2015
                WorkflowAPI.getInstance().setIS_UPDATE_STATUS(true);
            } else {
                showNotify("Cập nhật danh mục lỗi!");
            }
        }
        fillCatListbox();
    }

    @SuppressWarnings({"unchecked"})
    @Listen("onCatTypeSaved =#catManageWindow")
    public void onCatTypeSaved(Event event) {
         doFillListBox();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="private methods">
    private void showNotify(String msg) {
        showNotification(msg,
					Constants.Notification.INFO);
    }

    private String getNameByCatCode() {
        return catTypeSelected.getName().replace("Danh mục", "").toLowerCase();
    }

    private void fillCatListbox() {
        CategorySearchForm obj = new CategorySearchForm();
        obj.setCode(txtSearchCode.getValue());
        obj.setName(txtSearchName.getValue());
        obj.setType(catTypeSelected.getCode());
        String status = lboxStatus.getSelectedItem().getValue();
        if(!("---Chọn---").equals(status))
        {
            obj.setIsActive(Long.parseLong(status));
        }
        Caption gbCatCaption = gbCat.getCaption();
        gbCatCaption.setLabel("Danh sách " + catTypeSelected.getName().toLowerCase().replace("danh mục", ""));

        CategoryDAOHE catDaoHe = new CategoryDAOHE();
        List<Category> cats = catDaoHe.findCategory(obj);
        this.catListbox.setModel(new ListModelArray(cats));
    }

    private void showCreateOrEditCatType(Event ev, Boolean isEdit) {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        CategoryType ct = isEdit ? getCatTypeFromEvent(ev) : new CategoryType();
        if(!isEdit)
        {
            ct.setIsActive(Constants.Status.ACTIVE);
        }
        arguments.put("catTypeInfo",ct);
        arguments.put("recordMode", isEdit ? Constants.RECORD_MODE.EDIT : Constants.RECORD_MODE.CREATE);
        arguments.put("parentWindow", catManageWindow);
        arguments.put("modifyCatOrCatType", "modifyCatOrCatType");
        arguments.put("titleWindow", isEdit ? "Sửa loại danh mục" : "Thêm mới loại danh mục");
        Window window = (Window) Executions.createComponents(
                "/Pages/admin/category/insertOrupdate_catType.zul", null, arguments);
        window.doModal();
    }

    private void showViewOrEdit(Boolean isEdit, Event ev) {
        Category catBean = getCatFromEvent(ev);
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("catInfo", catBean);
        arguments.put("catTypeInfo", catTypeSelected);
        arguments.put("recordMode", isEdit ? "Edit" : "View");
        arguments.put("parentWindow", catManageWindow);
        arguments.put("modifyCatOrCatType", "");
        if (catBean.getDeptId() != null && catBean.getDeptId() > 0) {
            DepartmentDAOHE ddhe = new DepartmentDAOHE();
            Department dept = ddhe.findBOById(catBean.getDeptId());
            if (dept == null) {
                showNotify("Không tồn tại phòng ban");
                return;
            }
            arguments.put("department", dept);
        }
        arguments.put("titleWindow", isEdit ? "Cập nhật danh mục " + getNameByCatCode() : "Chi tiết danh mục " + getNameByCatCode());
        Window window = (Window) Executions.createComponents(
                isEdit ? "/Pages/admin/category/insertOrupdate.zul" : "/Pages/admin/category/view.zul", null, arguments);
        window.doModal();
    }

    private Category getCatFromEvent(Event ev) {
        Event origin;
        // get event target
        if (ev instanceof ForwardEvent) {
            origin = Events.getRealOrigin((ForwardEvent) ev);
        } else {
            origin = ev;
        }

        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        return (Category) litem.getValue();
    }

    private CategoryType getCatTypeFromEvent(Event ev) {
        Event origin;
        // get event target
        if (ev instanceof ForwardEvent) {
            origin = Events.getRealOrigin((ForwardEvent) ev);
        } else {
            origin = ev;
        }
        Image btn = (Image) origin.getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        return (CategoryType) litem.getValue();
    }
    //</editor-fold>
}
