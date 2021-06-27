/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.TreeItem;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class FlowNodeDeptUserController extends BaseComposer {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2956352403832232931L;
	@Wire
    Textbox txtNodeId;
    @Wire
    Tree deptUserTree;
    @Wire
    Window deptUserSelectWindow;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Long nodeId = (Long) Executions.getCurrent().getArg().get("nodeId");
        txtNodeId.setValue(nodeId.toString());
    }

    @Listen("onClick=#btnSaveDeptUser")
    public void onSaveDeptUser() {
        try {
            Set<Treeitem> lstItems = deptUserTree.getSelectedItems();
            List<Treeitem> lstDeleteItems = new ArrayList(deptUserTree.getItems());
            Long nodeId = Long.parseLong(txtNodeId.getValue());
            ArrayList deleteDeptIds = new ArrayList();
            ArrayList deleteUserIds = new ArrayList();
            FlowDAOHE fdhe = new FlowDAOHE();
            UserDAOHE mUserDAO = new UserDAOHE();

            List<NodeDeptUser> lstNDU = fdhe.getNodeDeptUser(nodeId);
            boolean bAdd;
            for (Treeitem item : lstItems) {
                TreeItem value = item.getValue();
                bAdd = true;
                if (value.getType() == 0l) {
                    if (lstNDU != null && lstNDU.size() > 0) {
                        for (NodeDeptUser ndu : lstNDU) {
                            if (ndu.getDeptId().equals(value.getId())) {
                                bAdd = false;
                                break;
                            }
                        }
                    }
                } else {
                    if (lstNDU != null && lstNDU.size() > 0) {
                        for (NodeDeptUser ndu : lstNDU) {
                            if (value.getId().equals(ndu.getUserId())) {
                                bAdd = false;
                                break;
                            }
                        }
                    }
                }
                if (bAdd) {
                    NodeDeptUser ndu = new NodeDeptUser();
                    
                    ndu.setNodeId(nodeId);
                    if (value.getType() == 0l) {
                        ndu.setDeptId(value.getId());
                        ndu.setDeptName(value.getName());
                    } else {
                        ndu.setUserId(value.getId());
                        ndu.setUserName(value.getName());
                        TreeItem parent = item.getParentItem().getValue();
                        ndu.setDeptId(parent.getId());
                        ndu.setDeptName(parent.getName());
                        ndu.setPosName(mUserDAO.getUserById(value.getId()).getPosName());
                        
                    }
                    fdhe.saveNodeDeptUser(ndu);
                }
                lstDeleteItems.remove(item);
            }

            for (Treeitem item : lstDeleteItems) {
                TreeItem value = item.getValue();
                if (value.getType() == 1l) {
                    deleteUserIds.add(value.getId());
                } else {
                    deleteDeptIds.add(value.getId());                    
                }
            }
            fdhe.deleteNodeDeptUser(nodeId, deleteDeptIds, deleteUserIds);
            deptUserSelectWindow.detach();
            Window parentWnd = (Window)Path.getComponent("/nodeConfigDlg");
            Events.sendEvent(new Event("onLoadNodeData",parentWnd,null));
            showSuccessNotification("Cập nhật thành công");
        } catch (NullPointerException en) {
            LogUtils.addLog(en);
            showNotification("Cập nhật không thành công");

        }
    }
}
