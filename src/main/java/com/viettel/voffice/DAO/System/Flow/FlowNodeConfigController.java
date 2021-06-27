/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Flow;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.DAO.FlowDAOHE;

/**
 *
 * @author HaVM2
 */
public class FlowNodeConfigController extends BaseComposer {
    
    private static final long serialVersionUID = 1L;

    @Wire
    Textbox txtNodeId;
    @Wire
    Listbox lstDeptUser;
    @Wire
    Window selectDeptDlg, nodePositionDlg;
    @Wire
    Window nodeConfigDlg;
    
    @Wire
    Selectbox statusSelectbox;
    Category selectedStatus;
    
    ListModelList<Category> listCats;
    private ListModelList<NodeDeptUser> listNDUs = new ListModelList<>();

    public FlowNodeConfigController(){
    }
    
    private ListModelList<NodeDeptUser> loadAllNDUs(Long nodeId){
        FlowDAOHE fdhe = new FlowDAOHE();
        //Long nodeId = (Long) Executions.getCurrent().getArg().get("nodeId");
        List lstData = fdhe.getNodeDeptUser(nodeId);
        return (new ListModelList<>(lstData));
    }
    
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent,
            ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo);
    }
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
            Long nodeId = (Long) Executions.getCurrent().getArg().get("nodeId");
        txtNodeId.setValue(nodeId.toString());
        onLoadNodeData();
    }

    @Listen("onClick=#btnAddNodeConfig")
    public void onAddeptUser() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("nodeId", Long.parseLong(txtNodeId.getValue()));
        selectDeptDlg = (Window) Executions.createComponents("/Pages/admin/user/userDeptSelect.zul", null, args);
        selectDeptDlg.doModal();
    }

    @Listen("onLoadNodeData=#nodeConfigDlg")
    public void onLoadNodeData() {
        //FlowDAOHE fdhe = new FlowDAOHE();
        Long nodeId = Long.parseLong(txtNodeId.getValue());
        //List lstData = fdhe.getNodeDeptUser(nodeId);
        //ListModelArray lstModel = new ListModelArray(lstData);
        //listNDUs = new ListModelList<>(lstData);
        //lstDeptUser.setModel(lstModel);
        listNDUs = this.loadAllNDUs(nodeId);
        lstDeptUser.setModel(listNDUs);
        
        /*
        // comment 27/01/2015
        // viethd find all status from category tables
        String status_type = WorkflowAPI.CAT_FILE_STATUS;
        CategoryDAOHE catDAOHE = new CategoryDAOHE();
        listCats = new ListModelList<>(catDAOHE.findAllCategory(status_type));
        Node currentNode = fdhe.getNodeById(nodeId);
        int index;
        for(index = 0; index < listCats.size(); index++){
            Category temp = listCats.get(index);
            Long value = Long.parseLong(temp.getValue());
            if(value.equals(currentNode.getStatus())){
                selectedStatus = temp;
                break;
            }
        }
        if(selectedStatus!=null)
            listCats.addToSelection(selectedStatus);
        statusSelectbox.setModel(listCats);
        */
        //statusSelectbox.setSelectedIndex(index);
    }

    @Listen("onPosition=#lstDeptUser")
    public void onPosition() {
        NodeDeptUser ndu = lstDeptUser.getSelectedItem().getValue();
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("nodeId", Long.parseLong(txtNodeId.getValue()));
        args.put("deptId", ndu.getDeptId());
        args.put("deptLevel", ndu.getDeptLevel());

        nodePositionDlg = (Window) Executions.createComponents("/Pages/admin/flow/flowNodePosition.zul", null, args);
        nodePositionDlg.doModal();
    }

    @Listen("onClick=#btnAddPosConfig")
    public void onSelectAllPosition() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("type", 1l);
        args.put("parent", "/nodeConfigDlg");
        nodePositionDlg = (Window) Executions.createComponents("/Pages/admin/flow/flowNodePosition.zul", null, args);
        nodePositionDlg.doModal();
    }

    @Listen("onSelectPos=#nodeConfigDlg")
    public void onSelectPos(Event evt) {
        List<NodeDeptUser> listPos = (List)evt.getData();
        if (listPos == null || listPos.isEmpty()) {
            return;
        }

        ListModelList lstModel = (ListModelList) lstDeptUser.getListModel();
        if (lstModel == null) {
            return;
        }

        List<NodeDeptUser> lstNodeDeptUser = lstModel.getInnerList();

        if (lstNodeDeptUser == null || lstNodeDeptUser.isEmpty()) {
            return;
        }
        FlowDAOHE fdhe = new FlowDAOHE();
        for (NodeDeptUser pos : listPos) {
            for (NodeDeptUser dept : lstNodeDeptUser) {
                if(dept.getUserId() != null)
                    continue;
                if(dept.getPosId() != null)
                    continue;
                dept.setPosId(pos.getPosId());
                fdhe.saveNodeDeptUser(dept);
            }
        }
    }

    @Listen("onChangeProcessType=#lstDeptUser")
    public void onChangeProcessType() {
        NodeDeptUser ndu = lstDeptUser.getSelectedItem().getValue();
        if (ndu.getProcessType() == null) {
            ndu.setProcessType(0l);
        } else {
            if (ndu.getProcessType() < 4l) {
                ndu.setProcessType(ndu.getProcessType() + 1);
            } else {
                ndu.setProcessType(null);
            }
        }

        FlowDAOHE fdhe = new FlowDAOHE();
        fdhe.saveNodeDeptUser(ndu);

        onLoadNodeData();
    }
    
    @Listen("onCheckUseAllias=#lstDeptUser")
    public void onCheckUseAllias(ForwardEvent event) {
        System.out.println("Event:" + event.getName());
        Checkbox currentChk = (Checkbox)(event.getOrigin().getTarget());
        //Listbox currentListbox = (Listbox)(event.getTarget());
        Long nduId = currentChk.getValue();
        Textbox currentTxt = (Textbox)nodeConfigDlg.getFellowIfAny(nduId.toString());
        String newAllias = currentTxt.getValue();
        Long isUseAllias = (currentChk.isChecked() ? 1l : 0l);
        NodeDeptUser ndu = findNDUbyId(nduId, listNDUs);
        ndu.setAllias(newAllias);
        ndu.setUseAllias(isUseAllias);
        saveNDU(ndu);
        
        //onLoadNodeData();
    }
    
    private void saveNDU(NodeDeptUser ndu){
        FlowDAOHE fdhe = new FlowDAOHE();
        fdhe.saveNodeDeptUser(ndu);
    }
    
    private NodeDeptUser findNDUbyId(Long id, ListModelList<NodeDeptUser> list){
        if(!list.isEmpty()){
            for(NodeDeptUser ndu:list){
                if(id.equals(ndu.getNodeDeptUserId()))
                    return ndu;
            }
        }
        return null;
    }
    
    
    @Listen("onCheckOptionSelected=#lstDeptUser")
    public void onCheckOptionSelected(ForwardEvent event) {
        System.out.println("Event:" + event.getName());
        //Listbox currentListbox = (Listbox)(event.getTarget());
        Radio currentRadio = (Radio)(event.getOrigin().getTarget());
        String strNDUId = currentRadio.getRadiogroup().getId();
        Long nduId = 0l;
        Long optionSelected = 0l;
        
        try{
            nduId = Long.parseLong(strNDUId.replace("rd_",""));
            optionSelected = Long.parseLong(currentRadio.getValue().toString());
        }catch(NumberFormatException ex){
            System.out.println("--- casting NDU ID has error:" + ex);
        }
        NodeDeptUser ndu = findNDUbyId(nduId, listNDUs);
        ndu.setOptionSelected(optionSelected);
        saveNDU(ndu);

        //onLoadNodeData();
    }

    @Listen("onIncreaseDeptLevel=#lstDeptUser")
    public void onIncreaseDeptLevel() {
        NodeDeptUser ndu = lstDeptUser.getSelectedItem().getValue();
        if (ndu.getDeptLevel() == null) {
            ndu.setDeptLevel(1l);
        } else {
            ndu.setDeptLevel(ndu.getDeptLevel() + 1);
        }

        saveNDU(ndu);

        onLoadNodeData();
    }

    @Listen("onReduceDeptLevel=#lstDeptUser")
    public void onReduceDeptLevel() {
        NodeDeptUser ndu = lstDeptUser.getSelectedItem().getValue();
        if (ndu.getDeptLevel() == null) {
            ndu.setDeptLevel(1l);
        } else if (ndu.getDeptLevel() <= 1l) {
            ndu.setDeptLevel(1l);
        } else {
            ndu.setDeptLevel(ndu.getDeptLevel() - 1);
        }

        saveNDU(ndu);
        onLoadNodeData();
    }
    
    @Listen("onClick=#btnSave")
    public void onSave() {
        // viethd: comment 27/01/2015
        // move status setting function to the action configuration screen
        //int index = statusSelectbox.getSelectedIndex();
        //selectedStatus = listCats.get(index);
        //Long status;
        //status = Long.parseLong(selectedStatus.getValue());
        //currentNode.setStatus(status);
        nodeConfigDlg.onClose();
        //System.out.println("--- node: " + nodeId + " - selected item:" + listCats.get(index).getName());
    }

    /**
     * @return the listNDUs
     */
    public ListModelList<NodeDeptUser> getListNDUs() {
        return listNDUs;
    }

    /**
     * @param listNDUs the listNDUs to set
     */
    public void setListNDUs(ListModelList<NodeDeptUser> listNDUs) {
        this.listNDUs = listNDUs;
    }
}
