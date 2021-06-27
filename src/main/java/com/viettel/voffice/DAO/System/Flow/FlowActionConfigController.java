/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Flow;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.google.gson.Gson;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.workflow.WorkflowAPI;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.workflow.DAO.NodeToNodeDAOHE;
import com.viettel.core.workflow.model.ActionTypeModel;
import com.viettel.core.workflow.model.FlowModel;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class FlowActionConfigController extends BaseComposer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7662669786141161048L;
	@Wire
    Textbox txtNextId, txtPrevId;
    @Wire
    Textbox txtFlowId, txtActionId, txtActionName;
    @Wire
    Listbox lstDeptUser;
    @Wire
    Window actionConfigDlg;
    @Wire
    Selectbox listFormsSelectbox, listStatusSelectbox, listActionTypeSelectbox;
    
    Category selectedForm = null;
    Category selectedStatus = null;
    ActionTypeModel selectedActionType = null;
    
    ListModelList<Category> listCatForms;
    ListModelList<Category> listCatStatus;
    ListModelList<ActionTypeModel> listActionTypes;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Long prevId = (Long) Executions.getCurrent().getArg().get("prevId");
        Long nextId = (Long) Executions.getCurrent().getArg().get("nextId");
        String actionName = (String) Executions.getCurrent().getArg().get("actionName");
        Long flowId = (Long) Executions.getCurrent().getArg().get("flowId");
        txtNextId.setValue(nextId.toString());
        txtPrevId.setValue(prevId.toString());
        txtActionName.setValue(actionName);
        txtFlowId.setValue(flowId.toString());
        onLoadNodeData();
    }

    @Listen("onLoadNodeData=#actionConfigDlg")
    public void onLoadNodeData() {
        Long nextId = Long.parseLong(txtNextId.getValue());
        Long prevId = Long.parseLong(txtPrevId.getValue());
        // viethd find all forms from category tables
        listCatForms = getAllCatByTypeCode(WorkflowAPI.CAT_ACTION_FORM);
        listCatStatus = getAllCatByTypeCode(WorkflowAPI.CAT_FILE_STATUS);
        NodeToNode currentAction;
        Long actionFormId = null;
        Long actionStatus = null;
        Long actionType = null;
        NodeToNodeDAOHE actionDAOHE = new NodeToNodeDAOHE();
        List<NodeToNode> lstActions = actionDAOHE.getNodeToNodes(prevId, nextId);
        if(!lstActions.isEmpty()){
            currentAction = lstActions.get(0);
            actionFormId = currentAction.getFormId();
            actionStatus = currentAction.getStatus();
            actionType = currentAction.getType();
            txtActionId.setText(currentAction.getId().toString());
        }
        int index;
        // set value for model selectbox of Forms
        for(index = 0; index < listCatForms.size(); index++){
            Category temp = listCatForms.get(index);
            if(temp.getCategoryId().equals(actionFormId)){
                selectedForm = temp;
                break;
            }
        }
        // set default value for Form model
        if(selectedForm != null)
            listCatForms.addToSelection(selectedForm);
        listFormsSelectbox.setModel(listCatForms);
        
        // set value for model selectbox of Status
        for(index = 0; index < listCatStatus.size(); index++){
            Category temp = listCatStatus.get(index);
            if(actionStatus!=null &&
                    temp.getValue().equals(actionStatus.toString())){
                selectedStatus = temp;
                break;
            }
        }
        // set default value for Status model
        if(selectedStatus != null)
            listCatStatus.addToSelection(selectedStatus);
        listStatusSelectbox.setModel(listCatStatus);
        
        List<ActionTypeModel> actionTypes = getActionTypeList();
        // set value for selectbox of ActionType
        listActionTypes = new ListModelList<>(actionTypes);
        for(index = 0; index < listActionTypes.size(); index++){
            ActionTypeModel temp = listActionTypes.get(index);
            if(actionStatus!=null &&
                    temp.getId().equals(actionType)){
                selectedActionType = temp;
                break;
            }
        }
        // set default value for ActionType model
        if(selectedStatus != null)
            listActionTypes.addToSelection(selectedActionType);
        listActionTypeSelectbox.setModel(listActionTypes);
    }

    private List<ActionTypeModel> getActionTypeList() {
        List<ActionTypeModel> actionTypes = new ArrayList();
        //actionTypes.add(new ActionTypeModel(Constants.NODE_ASSOCIATE_TYPE.ALL,"Chung"));
        actionTypes.add(new ActionTypeModel(Constants.NODE_ASSOCIATE_TYPE.NORMAL,"Mặc định"));
        actionTypes.add(new ActionTypeModel(Constants.NODE_ASSOCIATE_TYPE.SUBMIT,"DN gửi HS"));
        actionTypes.add(new ActionTypeModel(Constants.NODE_ASSOCIATE_TYPE.RETURN_AUTHOR,"Gửi về DN"));
        actionTypes.add(new ActionTypeModel(Constants.NODE_ASSOCIATE_TYPE.RETURN_PREVIOUS,"Trả lại bước trước"));
        //actionTypes.add(new ActionTypeModel(Constants.NODE_ASSOCIATE_TYPE.START,"Khởi động luồng"));
        actionTypes.add(new ActionTypeModel(Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT,"Tự động gửi"));
        actionTypes.add(new ActionTypeModel(Constants.NODE_ASSOCIATE_TYPE.ALWAYS_SEND,"Luôn gửi"));
        return actionTypes;
    }

    private ListModelList<Category> getAllCatByTypeCode(String status_type) {
        CategoryDAOHE catDAOHE = new CategoryDAOHE();
        return new ListModelList<>(catDAOHE.findAllCategory(status_type));
    }
  
    @Listen("onClick=#btnSaveAction")
    public void onSave() {
        int index = listFormsSelectbox.getSelectedIndex();
        selectedForm = listCatForms.get(index);
        Long formId;
        Long actionId;
        actionId = Long.parseLong(txtActionId.getText());
        formId = selectedForm.getCategoryId();
        Long statusId;
        int statusIndex = listStatusSelectbox.getSelectedIndex();
        selectedStatus = listCatStatus.get(statusIndex);
        try{
            statusId = Long.parseLong(selectedStatus.getValue());
        }catch(NumberFormatException|NullPointerException e){
        	LogUtils.addLog(e);
            statusId = null;
        }
        
        Long typeId;
        int typeIndex = listActionTypeSelectbox.getSelectedIndex();
        selectedActionType = listActionTypes.get(typeIndex);
        try{
            typeId = selectedActionType.getId();
        }catch(NullPointerException e){
        	LogUtils.addLog(e);
            typeId = null;
        }
        
        NodeToNode currentAction;
        NodeToNodeDAOHE actionDAOHE = new NodeToNodeDAOHE();
        NodeToNode action = actionDAOHE.getActionById(actionId);
        if(action != null){
            currentAction = action;
            currentAction.setFormId(formId);
            String actionName = txtActionName.getText();
            if("".equals(actionName))
                actionName = "Chuyển bước tiếp theo";
            currentAction.setAction(actionName);
            currentAction.setStatus(statusId);
            currentAction.setType(typeId);
        }
        actionConfigDlg.onClose();
        // reload flow graph
        Gson gson = new Gson();
        FlowDAOHE fdhe = new FlowDAOHE();
        Long flowId = Long.parseLong(txtFlowId.getText());
        FlowModel flowModel = fdhe.getFlowModel(flowId);
        String json = gson.toJson(flowModel);

        Clients.evalJavaScript("page.flow=" + json + ";");
        Clients.evalJavaScript("loadFlowContent();");
        System.out.println("--- action node : " + actionId + 
                " - selected item:" + listCatForms.get(index).getName());
    }
}
