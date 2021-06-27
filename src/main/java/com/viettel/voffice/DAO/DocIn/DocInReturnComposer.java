/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.DAO.NodeDeptUserDAOHE;
import com.viettel.core.workflow.DAO.NodeToNodeDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;

/**
 *
 * @author hoangnv28
 */
public class DocInReturnComposer extends DocInSendProcess {

    private List<NodeDeptUser> listObjectToReturn; //cá nhân hoặc đơn vị

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
        listNodeToNode = ntnDAOHE.getNodeToNode(process.getNodeId(),
                null, Constants.NODE_ASSOCIATE_TYPE.NORMAL, Constants.ACTION.NAME.RETURN);

        //Lấy danh sách đối tượng trả lại được định nghĩa trong luồng
        listObjectToReturn = new ArrayList<>();
        NodeDeptUserDAOHE nduDAOHE = new NodeDeptUserDAOHE();
        for (NodeToNode ntn : listNodeToNode) {
            listObjectToReturn.addAll(nduDAOHE.getDetailedNodeDeptUser(
                    ntn.getNextId(), getDeptId()));
        }

        /*
         Lấy danh sách đối tượng có thể được trả lại: nghĩa là các đối tượng tham
         gia vào luồng văn bản.
         */
        List listObjectCanBeReturn = new ArrayList<>();
        for (NodeDeptUser ndu : listObjectToReturn) {
            if (Objects.equals(ndu.getDeptId(), process.getSendGroupId())
                    && Objects.equals(ndu.getUserId(), process.getSendUserId())) {
                listObjectCanBeReturn.add(ndu);
            }
        }
        lbNodeDeptUser.setModel(new ListModelList<>(listObjectCanBeReturn));
        lbNodeDeptUser.renderAll();

        btnSend.setLabel("Gửi trả lại");
        btnChoose.setVisible(false);
        grbListNDU.setVisible(true);
        incSelectObjectsToSendProcess.setVisible(true);
        grbDeadline.setVisible(false);
        lhDelete.setVisible(false);
    }

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
        Long documentReceiveId = (Long) arguments.get("documentReceiveId");
        DocumentReceiveDAOHE documentReceiveDAOHE = new DocumentReceiveDAOHE();
        documentReceive = documentReceiveDAOHE.findById(documentReceiveId);

        Long processId = (Long) arguments.get("processId");
        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        process = processDAOHE.findById(processId);

        windowParent = (Window) arguments.get("windowParent");

        listMedia = new ArrayList<>();
        return super.doBeforeCompose(page, parent, compInfo);
    }

    public String loadProcessTypeName(Long processType) {
        return ProcessDAOHE.loadProcessTypeName(processType);
    }

    @Listen("onClick = #btnSend")
    public void onReturn() {
        try {
            if (lbNodeDeptUser.getItems().isEmpty()) {
                Messagebox.show("Định nghĩa sai đơn vị, cá nhân trả lại. Liên hệ quản trị hệ thống để khắc phục.",
                        "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            DocumentReceiveDAOHE docReceiveDAOHE = new DocumentReceiveDAOHE();
            docReceiveDAOHE.returnDoc(process.getProcessId(), process.getParentId());

            //Gui notify
            Notify notify = sendNotify(super.process, listDeptToSend,
                    listUserToSend);

            //Luu file dinh kem
            for (Media media : listMedia) {
                saveFileAttach((Media) media, notify);
            }

            showNotification("Trả lại thành công", Constants.Notification.INFO);

            Events.sendEvent("onAfterProcessing", windowParent, null);

            windowComment.onClose();
        } catch (IOException e) {
            LogUtils.addLog(e);
            showNotification("Trả lại thất bại", Constants.Notification.ERROR);
        }
    }
}
