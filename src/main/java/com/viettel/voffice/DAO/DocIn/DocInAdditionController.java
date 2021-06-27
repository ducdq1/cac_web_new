/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.NodeDeptUserDAOHE;
import com.viettel.core.workflow.DAO.NodeToNodeDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;

/**
 *
 * @author ChucHV
 */
public class DocInAdditionController extends DocInSendProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private List<Process> listSentProcess;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
                .getCurrent().getArg();
        Long documentReceiveId = (Long) arguments.get("documentReceiveId");
        DocumentReceiveDAOHE docDAOHE = new DocumentReceiveDAOHE();
        documentReceive = docDAOHE.findById(documentReceiveId);
        process = (Process) arguments.get("process");
        windowParent = (Window) arguments.get("windowParent");
        actionName = (String) arguments.get("actionName");

        listMedia = new ArrayList<>();
        listUserToSend = new ArrayList<>();
        listDeptToSend = new ArrayList<>();

        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        listSentProcess = processDAOHE.getSentProcess(process);
        actionName = listSentProcess.get(0).getActionName();
        actionType = Constants.NODE_ASSOCIATE_TYPE.NORMAL;

        NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
        listNodeToNode = ntnDAOHE.getNodeToNode(
                process.getNodeId(), null, actionType, actionName);

        NodeDeptUserDAOHE nduDAOHE = new NodeDeptUserDAOHE();
        listAvailableNDU = new ArrayList<>();
        for (NodeToNode ntn : listNodeToNode) {
            listAvailableNDU.addAll(nduDAOHE.getDetailedNodeDeptUser(
                    ntn.getNextId(), getDeptId())); 
        }

        processDAOHE = new ProcessDAOHE();
        listSentProcess = processDAOHE.getSentProcess(process);
        List<NodeDeptUser> listSentNDU = processDAOHE.convertProcessToNDU(listSentProcess);
        lbNodeDeptUser.setModel(new ListModelList<>(listSentNDU));
    }

    @Listen("onClick = #btnChoose")
    public void openTree() {
        HashMap<String, Object> arguments = new HashMap<String, Object> ();
        arguments.put("actionName", actionName);
        arguments.put("processCurrent", process);

        arguments.put("listSentProcess", listSentProcess);

        arguments.put("listAvailableNDU", listAvailableNDU);
        arguments.put("target", lbNodeDeptUser);
        arguments.put("listChoosedNDU", getListChoosedNDU());
        createWindow("wdTree",
                "/Pages/document/docIn/include/treeObjectsToSendProcess.zul",
                arguments, Window.MODAL);
    }

    @Listen("onClick = #btnSend")
    public void onHandler() {
        lbTopWarning.setValue("");
        lbBottomWarning.setValue("");

        List<NodeDeptUser> listSelectedNDU = convertListitemToListNDU(lbNodeDeptUser
                .getItems());

        if (listSelectedNDU.isEmpty()) {
            showNotification("Bạn chưa chọn đơn vị, cá nhân để gửi bổ sung",
                    Constants.Notification.WARNING);
            return;
        }

        // TODO
        String message = isListNDUValidatedToSend(listSelectedNDU);
        if (message != null) {
            lbTopWarning.setValue(message);
            lbBottomWarning.setValue(message);
            return;
        }

       
            ProcessDAOHE processDAOHE = new ProcessDAOHE();
            for (NodeDeptUser ndu : listSelectedNDU) {
                // ndu đã gửi rồi có id -1L.
                if (!Objects.equals(ndu.getNodeDeptUserId(), -1L)) {
                    processDAOHE.sendProcess(ndu, process, actionName,
                            actionType, dbDeadline.getValue(), new Date(),
                            listDeptToSend, listUserToSend);
                }
            }

            documentReceive.setStatus(Constants.DOCUMENT_STATUS.PROCESSING);
            DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
            documentReceiveDAOHE.saveOrUpdate(documentReceive);

            /*
             * Tạo notify
             */
            showNotification("Chuyển xử lý thành công",
                    Constants.Notification.INFO);

            Events.sendEvent("onAfterSendProcess", windowParent, null);

            windowComment.onClose();
       
    }
}
