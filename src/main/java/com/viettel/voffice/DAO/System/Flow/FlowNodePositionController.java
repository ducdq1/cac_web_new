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
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Position;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.PositionDAOHE;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class FlowNodePositionController extends BaseComposer {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8147048899903685974L;
	@Wire
    Listbox lstPositionSelect;
    @Wire
    Window nodePositionDlg;
    Long nodeId;
    Long deptId;
    Long deptLevel;
    Long type;
    String parent;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        type = (Long) Executions.getCurrent().getArg().get("type");
        if (type == null) {
            nodeId = (Long) Executions.getCurrent().getArg().get("nodeId");
            deptId = (Long) Executions.getCurrent().getArg().get("deptId");
            deptLevel = (Long) Executions.getCurrent().getArg().get("deptLevel");
            onLoadNodePosition();
        } else {
            parent = (String) Executions.getCurrent().getArg().get("parent");
            onLoadPosition();
        }
    }

    public void onLoadPosition() {
        PositionDAOHE pdhe = new PositionDAOHE();
        List<Position> lstPositionData = pdhe.getAllPosition();
        List lstData = new ArrayList();
        for (Position p : lstPositionData) {
            NodeDeptUser ndu = new NodeDeptUser();
            ndu.setPosId(p.getPosId());
            ndu.setPosName(p.getPosName());
            lstData.add(ndu);
        }
        ListModelArray lstModel = new ListModelArray(lstData);
        lstModel.setMultiple(true);
        lstPositionSelect.setModel(lstModel);

    }

    public void onLoadNodePosition() {
        FlowDAOHE fdhe = new FlowDAOHE();
        PositionDAOHE pdhe = new PositionDAOHE();
        List<Position> lstPositionData = pdhe.getAllPosition();
        List<NodeDeptUser> lstNodeDeptUser = fdhe.getNodeDeptUser(nodeId, deptId);
        List lstData = new ArrayList();
        boolean bAdd = false;
        for (Position p : lstPositionData) {
            if (lstNodeDeptUser != null) {
                bAdd = false;
            }
            for (NodeDeptUser ndu : lstNodeDeptUser) {
                if (p.getPosId().equals(ndu.getPosId())) {
                    lstData.add(ndu);
                    bAdd = true;
                    break;
                }
            }
            if (!bAdd) {
                NodeDeptUser ndu = new NodeDeptUser();
                ndu.setPosId(p.getPosId());
                ndu.setPosName(p.getPosName());
                lstData.add(ndu);
            }
        }
        ListModelArray lstModel = new ListModelArray(lstData);
        lstModel.setMultiple(true);
        lstPositionSelect.setModel(lstModel);
        //lstPositionSelect.setMultiple(true);
    }

    @Listen("onClick=#btnSaveNodePosition")
    public void onSaveNodePosition() {
        try {            
            Set<Listitem> lst = lstPositionSelect.getSelectedItems();
            if(type != null && type == 1l){
                Component parentComp = Path.getComponent(parent);
                Events.sendEvent("onSelectPos", parentComp, lst);
                return;
            }
            String deptName = "";
            if (deptId > 0) {
                DepartmentDAOHE ddhe = new DepartmentDAOHE();
                Department dept = ddhe.findBOById(deptId);
                deptName = dept.getDeptName();
            } else {
                if (deptId == Constants.NODE_ACTOR_RELATIVE.ALL) {
                    deptName = "Tất cả (*)";
                } else if (deptId == Constants.NODE_ACTOR_RELATIVE.PARENT) {
                    deptName = "Đơn vị cha (..\\)";
                } else if (deptId == Constants.NODE_ACTOR_RELATIVE.CURRENT) {
                    deptName = "Đơn vị hiện tại (.\\)";
                } else if (deptId == Constants.NODE_ACTOR_RELATIVE.CHILD) {
                    deptName = "Đơn vị con (.\\*)";
                } else if (deptId == Constants.NODE_ACTOR_RELATIVE.SAME_PARENT) {
                    deptName = "Đơn vị cùng cấp (..\\*)";
                } else if (deptId == Constants.NODE_ACTOR_RELATIVE.SAME_PARENT_LEVEL) {
                    deptName = "Đơn vị thuộc cùng n cấp đơn vị ";
                }
            }
            FlowDAOHE fdhe = new FlowDAOHE();
            List<NodeDeptUser> lstNodeDeptUser = fdhe.getNodeDeptUser(nodeId, deptId);
            boolean exist;
            for (Listitem item : lst) {
                NodeDeptUser selectedItem = item.getValue();
                exist = false;
                for (NodeDeptUser ndu : lstNodeDeptUser) {
                    if (selectedItem.getPosId().equals(ndu.getPosId())) {
                        lstNodeDeptUser.remove(ndu);
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    selectedItem.setDeptId(deptId);
                    selectedItem.setDeptName(deptName);
                    selectedItem.setNodeId(nodeId);
                    selectedItem.setDeptLevel(deptId);
                    fdhe.saveNodeDeptUser(selectedItem);
                }
            }

            for (NodeDeptUser ndu : lstNodeDeptUser) {
                fdhe.deleteNodeDeptUser(ndu);
            }

            nodePositionDlg.detach();
            //
            // Thong bao va cap nhat lai danh sach actor
            //
            Window parentWnd = (Window) Path.getComponent("/nodeConfigDlg");
            Events.sendEvent(new Event("onLoadNodeData", parentWnd, null));
            showSuccessNotification("Cập nhật thành công");

        } catch (NullPointerException en) {
            LogUtils.addLog(en);
            showNotification("Cập nhật bị lỗi");
        }
    }
}
