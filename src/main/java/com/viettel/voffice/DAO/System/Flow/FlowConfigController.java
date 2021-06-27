/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Flow;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.google.gson.Gson;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.workflow.model.FlowModel;
import com.viettel.core.workflow.model.NodeToNodeModel;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class FlowConfigController extends BaseComposer {

    @Wire
    Textbox txtFlowId, txtFlowData;
    @Wire
    Window flowConfigDlg, nodeConfigDlg;
    @Wire
    Window actionConfigDlg;
    
    private Window parentWindow;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        FlowModel fm = (FlowModel) Executions.getCurrent().getArg().get("flowModel");
        Gson gson = new Gson();
        String json = gson.toJson(fm);
        Clients.evalJavaScript("page.flow=" + json + ";");
        Clients.evalJavaScript("loadFlowContent();");
    }

    @Listen("onBack=#flowConfigDlg")
    public void onBack() {
        Window flowWindow = (Window) Path.getComponent("/flowWindow");
        flowWindow.setHeight("");
        flowConfigDlg.detach();
    }

    public void validate(FlowModel model) throws Exception {
        if (model == null) {
            return;
        }
        List<Node> lstNodes = model.getLstNodes();
        List<NodeToNodeModel> lstNodeToNode = model.getLstNodeToNodes();
        if (lstNodes != null && lstNodes.size() > 0) {
            int count = 0;
            for (int i = 0; i < lstNodes.size(); i++) {
                Node n = lstNodes.get(i);
                if (Constants.NODE_TYPE.NODE_TYPE_START.equals(n.getType())) {
                    //
                    // Check xem co nhieu hon 1 node start ko
                    //
                    count++;
                    if (count > 1) {
                        //throw new Exception("Có nhiều hơn một nút start");
                    }
//                    if (lstNodeToNode != null && lstNodeToNode.size() > 0) {
//                        for (NodeToNodeModel ntn : lstNodeToNode) {
//                            if (ntn.getNextId().intValue() == i) {
//                                throw new Exception("Node start không được có đường đi vào");
//                            }
//                        }
//                    }

                } else if (Constants.NODE_TYPE.NODE_TYPE_FINISH.equals(n.getType())) {
//                    if (lstNodeToNode != null && lstNodeToNode.size() > 0) {
//                        for (NodeToNodeModel ntn : lstNodeToNode) {
//                            if (ntn.getPreviousId().intValue() == i) {
//                                throw new Exception("Node end không được có đường đi ra");
//                            }
//                        }
//                    }
                } else {
                    if (lstNodeToNode != null && lstNodeToNode.size() > 0) {
                        int countIn = 0;
                        int countOut = 0;
                        for (NodeToNodeModel ntn : lstNodeToNode) {
                            if (ntn.getPreviousId().intValue() == i) {
                                countOut++;
                            }
                            if (ntn.getNextId().intValue() == i) {
                                countIn++;
                            }
                        }
                        if (countIn == 0) {
                            throw new Exception("Có node không được có đường đi vào");
                        }
                        if (countOut == 0) {
                            //throw new Exception("Có node không được có đường đi ra");
                        }
                    }
                }
            }
        }
        //
        // Kiem tra cung 1 node co 2 action giong nhau ko
        //
        /*
         if (lstNodeToNode != null && lstNodeToNode.size() > 0) {
         for (int i = 0; i < lstNodeToNode.size() - 1; i++) {
         NodeToNodeModel n1 = lstNodeToNode.get(i);
         if (n1.getAction() != null && !n1.getAction().isEmpty()) {
         for (int j = i+1; j < lstNodeToNode.size(); j++) {
         NodeToNodeModel n2 = lstNodeToNode.get(j);
         if (n2.getPreviousId() == n1.getPreviousId() && n1.getAction().equals(n2.getAction())) {
         throw new Exception("Cùng một node không được có 2 action giống nhau");
         }
         }
         }
         }
         }
         */

        /*
         * Kiem tra co action nao null ko
         */
        if (lstNodeToNode != null && lstNodeToNode.size() > 0) {
            for (int i = 0; i < lstNodeToNode.size() - 1; i++) {
                NodeToNodeModel n = lstNodeToNode.get(i);
                if (n.getAction() == null || n.getAction().isEmpty()) {
                     throw new Exception("Có action chưa có tên");
                }
            }
        }

    }

    @Listen("onSave=#flowConfigDlg")
    public void onSave(Event event) {
        try {
            String data = event.getData().toString();
            Gson gson = new Gson();
            FlowModel flowModel = gson.fromJson(data, FlowModel.class);

            validate(flowModel);

            FlowDAOHE fdhe = new FlowDAOHE();
            flowModel = fdhe.saveFlowModel(flowModel);

            showSuccessNotification("Cập nhật thành công");

            flowModel = fdhe.getFlowModel(flowModel.getFlowId());
            String json = gson.toJson(flowModel);

            Clients.evalJavaScript("page.flow=" + json + ";");
            Clients.evalJavaScript("loadFlowContent();");
            
            Window flowWindow = (Window) Path.getComponent("/flowWindow");
            Events.sendEvent("onReload", flowWindow, null);
            //onBack();

        } catch (Exception en) {
            LogUtils.addLog(en);
            
            Logger.getLogger(FlowConfigController.class.getName()).log(Level.SEVERE, null, en);
            //System.out.println("---" + en.getMessage());
            showNotification("Cập nhật không thành công : ");
        }
    }

    @Listen("onLoadNodeData=#flowConfigDlg")
    public void onLoadNodeData(Event evt) {
        //evt.getData();
        String data = evt.getData().toString();
        Gson gson = new Gson();
        Node node = gson.fromJson(data, Node.class);

        if (node.getNodeId() == null) {
            node.setIsActive(1l);
            FlowDAOHE fdhe = new FlowDAOHE();
            Long nodeId = fdhe.saveNode(node);
            Clients.evalJavaScript("updateNodeId(" + node.getId() + "," + nodeId + ");");

        }

        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("nodeId", node.getNodeId());

        nodeConfigDlg = (Window) Executions.createComponents("/Pages/admin/flow/flowNodeConfig.zul", null, args);
        //nodeConfigDlg = (Window) Executions.createComponents("/Pages/admin/flow/flowNDUConfig.zul", null, args);
        nodeConfigDlg.doModal();
    }
    
    @Listen("onLoadLineData=#flowConfigDlg")
    public void onLoadLineData(Event evt) {
        System.out.println("-- double click on arrow --");
        String data = evt.getData().toString();
        Gson gson = new Gson();
        NodeToNodeModel node = gson.fromJson(data, NodeToNodeModel.class);

//        if (node.getNodeId() == null) {
//            node.setIsActive(1l);
//            FlowDAOHE fdhe = new FlowDAOHE();
//            Long nodeId = fdhe.saveNode(node);
//            Clients.evalJavaScript("updateNodeId(" + node.getId() + "," + nodeId + ");");
//
//        }

        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("prevId", node.getPreviousId());
        args.put("nextId", node.getNextId());
        args.put("actionName", node.getAction());
        args.put("flowId", node.getType());

        actionConfigDlg = (Window) Executions.createComponents("/Pages/admin/flow/flowNodeConfigAction.zul", null, args);
        actionConfigDlg.doModal();
    }
}
