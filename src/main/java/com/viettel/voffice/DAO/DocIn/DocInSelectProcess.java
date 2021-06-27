/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.workflow.BO.Process;

/**
 *
 * @author ChucHV
 */
public class DocInSelectProcess extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Wire
    private Window windowSelectProcess;
    @Wire
    private Listbox lbProcess;
    private Window windowParent;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
                .getCurrent().getArg();
        List<Process> listProcess = (List<Process>) arguments
                .get("listProcess");
        windowParent = (Window) arguments.get("windowParent");
        ListModelList modelList = new ListModelList(listProcess);
        lbProcess.setModel(modelList);
    }

    @Listen("onSelect = #lbProcess")
    public void onSelectProcess() {
        Process process = lbProcess.getSelectedItem().getValue();
        Events.sendEvent("onSelectedProcess", windowParent, process);
        windowSelectProcess.onClose();
    }

}
