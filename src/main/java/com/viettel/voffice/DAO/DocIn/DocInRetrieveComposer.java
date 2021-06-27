/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.voffice.BEAN.RetrieveBean;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;

/**
 *
 * @author ChucHV
 */
public class DocInRetrieveComposer extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Wire
    private Listbox lbDept;
    @Wire
    private Textbox tbReason;
    @Wire
    private Window windowRetrieve;

    private Process rootProcess;
    private Window windowParent;
    private List<RetrieveBean> listRetrieveBean;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        HashMap<String, Object> arguments = (HashMap<String,Object>) Executions.getCurrent().getArg();
        rootProcess = (Process) arguments.get("process");
        windowParent = (Window) arguments.get("windowParent");
        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        listRetrieveBean = processDAOHE.getProcessToRetrieve(rootProcess
                .getProcessId());
        ListModelList model = new ListModelList(listRetrieveBean);
        model.setMultiple(true);
        lbDept.setModel(model);
        lbDept.renderAll();
    }

    @Listen("onClick = #btnRetrieve")
    public void onRetrieve() {
        if (lbDept.getSelectedItems().isEmpty()) {
            showNotification("Bạn chưa chọn đơn vị thu hồi",
                    Constants.Notification.WARNING);
        } else if (("").equals(tbReason.getText().trim())) {
            showNotification("Bạn chưa nhập lí do thu hồi",
                    Constants.Notification.WARNING);
        } else {
            Messagebox.show("Bạn có chắc muốn thu hồi văn bản không?",
                    "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION, new EventListener<Event>() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            switch (event.getName()) {
                                case Messagebox.ON_OK:
                                  
                                        Set<Listitem> selectedItems = lbDept
                                        .getSelectedItems();

                                        List<RetrieveBean> selectedProcessId = new ArrayList<>();
                                        for (Listitem item : selectedItems) {
                                            selectedProcessId
                                            .add(((RetrieveBean) item
                                                    .getValue()));
                                        }

                                        List<Listitem> unSelectedItems = lbDept
                                        .getItems();
                                        unSelectedItems.removeAll(selectedItems);
                                        List<RetrieveBean> unSelectedProcessId = new ArrayList<>();
                                        for (Listitem item : unSelectedItems) {
                                            unSelectedProcessId
                                            .add(((RetrieveBean) item
                                                    .getValue()));
                                        }

                                        DocumentReceiveDAOHE docReceiveDAOHE = new DocumentReceiveDAOHE();
                                        docReceiveDAOHE.retrieve(rootProcess,
                                                selectedProcessId,
                                                unSelectedProcessId, tbReason
                                                .getText().trim());
                                        windowRetrieve.onClose();

                                        showNotification(
                                                "Thu hồi văn bản thành công",
                                                Constants.Notification.INFO);
                                        Events.sendEvent("onAfterProcessing",
                                                windowParent, null);
                                    
                                    break;
                                case Messagebox.ON_CANCEL:
                                    // do nothing
                                    break;
                            }
                        }
                    });
        }
    }

    public String loadProcessType(Long processType) {
        if (Constants.PROCESS_TYPE.MAIN.equals(processType)) {
            return "Xử lý chính";
        } else if (Constants.PROCESS_TYPE.COOPERATE.equals(processType)) {
            return "Phối hợp";
        } else if (Constants.PROCESS_TYPE.COMMENT.equals(processType)) {
            return "Xin ý kiến";
        } else if (Constants.PROCESS_TYPE.RECEIVE_TO_KNOW.equals(processType)) {
            return "Nhận để biết";
        } else {
            return "";
        }
    }
}
