/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.joda.time.LocalDate;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.workflow.DAO.NodeDeptUserDAOHE;
import com.viettel.core.workflow.DAO.NodeToNodeDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;

/**
 *
 * @author ChucHV
 */
public class DocInSendProcessFirstTimeController extends DocInSendProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long statusToAssignProcess;

    @SuppressWarnings({"unchecked"})
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
        actionType = (Long) arguments.get("actionType");

        listMedia = new ArrayList<>();
        listUserToSend = new ArrayList<>();
        listDeptToSend = new ArrayList<>();

        // Neu la action "Xin y kien"
        if ("xin ý kiến".equals(actionName.toLowerCase())) {
            windowComment.setTitle("Xin ý kiến");
            cbListNDU.setLabel("Danh sách người xin ý kiến");
            cbProcessContent.setLabel("Nội dung ý kiến");
            grbDeadline.setVisible(false);
        }// Neu la action "Cho y kien"
        else if ("cho ý kiến".equals(actionName.toLowerCase())) {
            // do nothing
            incSelectObjectsToSendProcess.setVisible(false);
            grbDeadline.setVisible(false);
            windowComment.setTitle("Cho ý kiến");
            cbProcessContent.setLabel("Nội dung ý kiến");
            btnChoose.setVisible(false);
            btnSend.setLabel("Cho ý kiến");
        } else {
            NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
            listNodeToNode = ntnDAOHE.getNodeToNode(process.getNodeId(),
                    null, actionType, actionName);

            FlowDAOHE flowDAOHE;
            /*
             * ListNodeToNode không thể rỗng được do đã load được action name và
             * action type qua đó hiển thị button action để người dùng click
             * vào.
             */
            if (!listNodeToNode.isEmpty()) {
                listAvailableNDU = new ArrayList<>();
                flowDAOHE = new FlowDAOHE();
                NodeDeptUserDAOHE nduDAOHE = new NodeDeptUserDAOHE();
                for (NodeToNode ntn : listNodeToNode) {
                    listAvailableNDU.addAll(nduDAOHE.getDetailedNodeDeptUser(
                            ntn.getNextId(), getDeptId()));
                }

                if (listAvailableNDU.isEmpty()) {
                    /*
                     * Nếu là hoàn thành loại 1
                     */
                    if (flowDAOHE.isFinishNode(listNodeToNode.get(0)
                            .getNextId())) {
                        // Ẩn các trường thông tin đi
                        incSelectObjectsToSendProcess.setVisible(false);
                        grbDeadline.setVisible(false);
                        ctDeadline.setLabel("Thời gian hoàn thành");
                        btnChoose.setVisible(false);
                        btnSend.setLabel("Hoàn thành");
                        windowComment.setTitle("Hoàn thành văn bản");
                    } else {
                        showNotification(
                                "Chưa định nghĩa người nhận trong luồng cho hành động "
                                + actionName,
                                Constants.Notification.WARNING);
                        return;
                    }
                }

                if (process.getDeadline() != null) {
                    LocalDate deadlineLD = new LocalDate(
                            process.getDeadline());
                    LocalDate now = LocalDate.now();
                    if (deadlineLD.isBefore(now)) {
                        dbDeadline.setValue(now.toDate());
                    } else {
                        dbDeadline
                                .setValue(documentReceive.getDeadlineByDate());
                    }
                }
            }
        }
    }

    @Listen("onClick = #btnChoose")
    public void openTree() {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("actionName", actionName);
        arguments.put("listAvailableNDU", listAvailableNDU);
        arguments.put("target", lbNodeDeptUser);
        arguments.put("processCurrent", process);
        arguments.put("listChoosedNDU", getListChoosedNDU());
        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        List listSentProcess = null;
        if (process != null && process.getProcessId() != null) {
            listSentProcess = processDAOHE.getSentProcess(process);
        }
        arguments.put("listSentProcess", listSentProcess);
        createWindow("wdTree",
                "/Pages/document/docIn/include/treeObjectsToSendProcess.zul",
                arguments, Window.MODAL);
    }

    /*
     * Gui comment Khi gui thi voi moi row trong danh sach xu li se tao ra 1
     * process tuong ung Set trang thai cho process hien tai thanh "Da xu li"
     */
    @Listen("onClick = #btnSend")
    public void onHandle() throws IOException {
        lbTopWarning.setValue("");
        lbBottomWarning.setValue("");
        try {
            // Neu la action "Xin y kien"
            if ("xin ý kiến".equals(actionName.toLowerCase())) {
                if (lbNodeDeptUser.getItemCount() == 0) {
                    // Set message warning
                    String message = "Bạn chưa chọn người nhận xử lý";
                    lbTopWarning.setValue(message);
                    lbBottomWarning.setValue(message);
                    return;
                }

                if ("".equals(tbcomment.getText().trim())) {
                    // Set message warning
                    String message = "Bạn chưa nhập ý kiến";
                    lbTopWarning.setValue(message);
                    lbBottomWarning.setValue(message);
                    return;
                }

                ProcessDAOHE processDAOHE = new ProcessDAOHE();
                NodeDeptUser ndu;
                for (Listitem item : lbNodeDeptUser.getItems()) {
                    ndu = (NodeDeptUser) item.getValue();
                    /*
                     * Xin ý kiến thì chỉ cá nhân trao đổi với nhau mà thôi, nên
                     * cần điền thêm thông tin người xin ý kiến
                     */
                    Process processClone = process.clone();
                    processClone.setReceiveUserId(getUserId());
                    processClone.setReceiveUser(getUserFullName());
                    processDAOHE.sendProcess(ndu, processClone, actionName,
                            actionType, dbDeadline.getValue(), new Date(),
                            listDeptToSend, listUserToSend);
                }
                statusToAssignProcess = null;
            }// Neu la action "Cho y kien"
            else if ("cho ý kiến".equals(actionName.toLowerCase())) {
                if ("".equals(tbcomment.getText().trim())) {
                    // Set message warning
                    String message = "Bạn chưa nhập ý kiến";
                    lbTopWarning.setValue(message);
                    lbBottomWarning.setValue(message);
                    return;
                }
                statusToAssignProcess = Constants.PROCESS_STATUS.DID;
                // Xin ý kiến thì chỉ có cá nhân xin thôi, ko có đơn vị
                listUserToSend.add(process.getSendUserId());
                listDeptToSend.add(process.getSendGroupId());
            } else {
                FlowDAOHE flowDAOHE = new FlowDAOHE();
                // Nếu là 'Hoàn thành' loại 1
                if (!listNodeToNode.isEmpty()) {

                    if (listAvailableNDU.isEmpty()) {
                        /*
                         * Nếu là hoàn thành loại 1
                         */
                        if (flowDAOHE.isFinishNode(listNodeToNode.get(0)
                                .getNextId())) {
                            statusToAssignProcess = Constants.PROCESS_STATUS.FINISH_1;
                        } else {
                            showNotification(
                                    "Chưa định nghĩa người nhận trong luồng cho hành động "
                                    + actionName,
                                    Constants.Notification.WARNING);
                            return;
                        }
                    } else {
                        List<Listitem> listChoosedItem = lbNodeDeptUser
                                .getItems();

                        if (listChoosedItem.isEmpty()) {
                            // Set message warning
                            String message = "Bạn chưa chọn người nhận xử lý";
                            lbTopWarning.setValue(message);
                            lbBottomWarning.setValue(message);
                            return;
                        }

                        List<NodeDeptUser> listChoosedNDU = convertListitemToListNDU(lbNodeDeptUser
                                .getItems());
                        List<NodeDeptUser> listNDU = getListNDUToSend(process, listChoosedNDU);
                        // render lại listbox
                        lbNodeDeptUser.setModel(new ListModelList(listNDU));
                        lbNodeDeptUser.renderAll();

                        // TODO
                        String message = isListNDUValidatedToSend(listNDU);
                        if (message != null) {
                            lbTopWarning.setValue(message);
                            lbBottomWarning.setValue(message);
                            return;
                        }

                        boolean hasFinish_1 = false;// TH "Hoan thanh" loai 1
                        boolean hasFinish_2 = false;// TH "Hoan thanh" loai 2
                        flowDAOHE = new FlowDAOHE();

                        NodeDeptUser ndu;
                        for (Listitem item : listChoosedItem) {
                            ndu = (NodeDeptUser) item.getValue();
                            if (Objects.equals(-1L, ndu.getNodeDeptUserId())) {
                                item.setDisabled(true);
                                continue;
                            }

                            if (!hasFinish_1 || !hasFinish_2) {
                                if (flowDAOHE.isFinishNode(ndu.getNodeId())) {
                                    if (!hasFinish_1) {
                                        if (flowDAOHE.getNodeDeptUser(
                                                ndu.getNodeId()).isEmpty()) {
                                            hasFinish_1 = true;
                                        } else {
                                            hasFinish_2 = true;
                                        }
                                    }

                                    if (!hasFinish_2) {
                                        if (flowDAOHE.getNodeDeptUser(
                                                ndu.getNodeId()).isEmpty()) {
                                            hasFinish_1 = true;
                                        } else {
                                            hasFinish_2 = true;
                                        }
                                    }
                                }
                            }

                            ProcessDAOHE processDAOHE = new ProcessDAOHE();
                            processDAOHE.sendProcess(ndu, process,
                                    actionName, actionType,
                                    dbDeadline.getValue(), new Date(),
                                    listDeptToSend, listUserToSend);
                        }

                        if (hasFinish_1) {
                            statusToAssignProcess = Constants.PROCESS_STATUS.FINISH_1;
                        } else {
                            if (hasFinish_2) {
                                statusToAssignProcess = Constants.PROCESS_STATUS.FINISH_2;
                            } else {
                                statusToAssignProcess = Constants.PROCESS_STATUS.DID;
                            }
                        }
                    }
                }
            }

            DocumentReceiveDAOHE docReceiveDAOHE = new DocumentReceiveDAOHE();
            docReceiveDAOHE.finishProcess(process.getProcessId(),
                    statusToAssignProcess, actionName, true);

            Notify notify = sendNotify(process, listDeptToSend,
                    listUserToSend);

            for (Media media : listMedia) {
                saveFileAttach((Media) media, notify);
            }

            showNotification(actionName + " thành công",
                    Constants.Notification.INFO);

            Events.sendEvent("onAfterProcessing", windowParent, null);

            windowComment.onClose();
        } catch (IOException|CloneNotSupportedException e) {
            showNotification("Chuyển xử lý thất bại",
                    Constants.Notification.ERROR);
            LogUtils.addLog(e);
        }
    }

    @Listen("onClick = #btnSelect")
    public void onSelectStrategist() {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("parentWindow", windowComment);
        createWindow("windowStrategist",
                "/Pages/document/docIn/strategist.zul", arguments, Window.MODAL);
    }
}
