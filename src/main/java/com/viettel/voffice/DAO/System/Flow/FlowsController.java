/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Flow;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.workflow.model.FlowModel;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class FlowsController extends BaseComposer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8303681205060375140L;
	@Wire
    Textbox txtSearchFlowName, txtSearchFlowCode, txtSearchDeptName, txtSearchDeptId;
    @Wire
    Listbox cbSearchObjects;
    @Wire
    Listbox lstFlows;
    @Wire
    Window createDlg,flowWindow,configWindow;

    @SuppressWarnings("unchecked")
	@Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        CategoryDAOHE cdhe = new CategoryDAOHE();
        List lstObjects = cdhe.getSelectCategoryByType(Constants.OBJECT_CONSTANT);
        ListModelArray lstModel = new ListModelArray(lstObjects);
        cbSearchObjects.setModel(lstModel);
        onSearch();
    }

    @Listen("onClick=#btnShowSearchDept")
    public void onOpenDeptSelect() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("idOfDisplayNameComp", "/flowWindow/txtSearchDeptName");
        args.put("idOfDisplayIdComp", "/flowWindow/txtSearchDeptId");
        Window showDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDept.zul", null, args);
        showDeptDlg.doModal();
        
    }

    @Listen("onClick=#btnSearch")
    public void onSearch() {
        FlowModel fm = new FlowModel();
        fm.setFlowName(txtSearchFlowName.getValue());
        fm.setFlowCode(txtSearchFlowCode.getValue());
        if (cbSearchObjects.getSelectedItem() != null) {
            fm.setObjectId((Long) cbSearchObjects.getSelectedItem().getValue());
        }
        if (!txtSearchDeptId.getValue().isEmpty()) {
            fm.setDeptId(Long.parseLong(txtSearchDeptId.getValue()));
        } else {
            if(!("admin").equals(getUserName())){
                fm.setDeptId(getDeptId());
            }
        }

        FlowDAOHE fdhe = new FlowDAOHE();
        List lst = fdhe.search(fm);
        ListModelArray lma = new ListModelArray(lst);
        lstFlows.setModel(lma);
    }
    
    @Listen("onReload=#flowWindow")
    public void onReloadGrid(){
        onSearch();
    }

    @Listen("onClick=#btnCreate")
    public void onCreate() {
        createDlg = (Window) Executions.createComponents("/Pages/admin/flow/flowCreate.zul", null, null);
        createDlg.doModal();
    }

    @Listen("onEdit=#lstFlows")
    public void onEdit() {
        Flow item = lstFlows.getSelectedItem().getValue();
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("item", item);
        createDlg = (Window) Executions.createComponents("/Pages/admin/flow/flowCreate.zul", null, args);
        createDlg.doModal();
    }

    @Listen("onDelete=#lstFlows")
    public void onDelete() {
        try {
            Flow item = lstFlows.getSelectedItem().getValue();
            FlowDAOHE fdhe = new FlowDAOHE();
            item = fdhe.findById(item.getFlowId());
            
            item.setIsActive(-1l);
            fdhe.update(item);
            showSuccessNotification("Xóa thành công");
            onSearch();
        } catch (NullPointerException en) {
            LogUtils.addLog(en);
        }
    }

    @Listen("onConfig=#lstFlows")
    public void onConfig() {
        Flow item = lstFlows.getSelectedItem().getValue();
        FlowDAOHE fdhe = new FlowDAOHE();
        FlowModel fm = fdhe.getFlowModel(item.getFlowId());
        HashMap<String,Object> args = new HashMap<String, Object>();
        args.put("parentWindow",flowWindow);
        args.put("flowModel", fm);
        //flowWindow.setHeight("0px");
        //flowWindow.setVisible(false);
        if(configWindow != null){
            configWindow.detach();
        }
        configWindow = (Window) Executions.createComponents("/Pages/admin/flow/flowConfig.zul", flowWindow.getParent(), args);
        configWindow.doModal();
    }
}
