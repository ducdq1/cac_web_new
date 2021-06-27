/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Files;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.TreeItem;
import com.viettel.core.user.model.DeptUserTreeModel;
import com.viettel.core.workflow.BO.Process;
import com.viettel.utils.Constants;

/**
 *
 * @author HaVM2
 */
public class ParticipantController extends BaseComposer {

    @Wire
    Tree deptUserTree;
    @Wire
    Window deptUserSelectWindow;
    List<TreeItem> lstSelectedItems;
    String parentPath;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        lstSelectedItems = (List) Executions.getCurrent().getArg().get("lstItem");
        parentPath = (String) Executions.getCurrent().getArg().get("parentPath");
        TreeModel model = new DeptUserTreeModel(lstSelectedItems);
        deptUserTree.setModel(model);
    }

    @Listen("onClick=#btnSaveDeptUser")
    public void onSaveDeptUser() {
       
            List lstAddParticipants = new ArrayList();
            List lstDeleteParticipants = new ArrayList();
            Collection<Treeitem> allItems = deptUserTree.getItems();
            for (Treeitem item : allItems) {
                TreeItem value = item.getValue();
                if (value.getType() == 0l) {
                    continue;
                }
                boolean bHas = false;
                //
                // Kiem tra xem item co trong danh sach da chon khong
                //
                if (lstSelectedItems != null) {
                    for (TreeItem selectedItem : lstSelectedItems) {
                        if (selectedItem.getId().equals(value.getId()) && selectedItem.getType().equals(value.getType())) {
                            bHas = true;
                            break;
                        }
                    }
                }

                Process ps = new Process();

                ps.setReceiveUserId(value.getId());
                ps.setReceiveUser(value.getName());
                TreeItem parent = item.getParentItem().getValue();
                ps.setReceiveGroupId(parent.getId());
                ps.setReceiveGroup(parent.getName());
                ps.setProcessType(Constants.PROCESS_TYPE.COOPERATE);
                ps.setStatus(Constants.PROCESS_STATUS.NEW);

                if (item.isSelected()) {
                    //
                    // them vao
                    //
                    if (!bHas) {
                        lstAddParticipants.add(ps);
                    }
                } else {
                    //
                    // remove di
                    //
                    if (bHas) {
                        lstDeleteParticipants.add(ps);
                    }
                }
            }

            HashMap<String, Object> args = new HashMap<String, Object>();
            args.put("add", lstAddParticipants);
            args.put("delete", lstDeleteParticipants);
            deptUserSelectWindow.detach();
            Window parentWnd = (Window) Path.getComponent(parentPath);
            Events.sendEvent(new Event("onSelectDeptUser", parentWnd, args));
       
    }
}
