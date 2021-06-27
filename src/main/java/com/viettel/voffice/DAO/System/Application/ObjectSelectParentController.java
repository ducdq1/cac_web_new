/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Application;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.viettel.core.base.model.TreeModel;
import com.viettel.core.base.model.TreeNode;
import com.viettel.core.sys.BO.Applications;
import com.viettel.core.sys.BO.Objects;
import com.viettel.core.sys.DAO.ApplicationDAOHE;
import com.viettel.core.sys.DAO.ObjectsDAOHE;
import com.viettel.core.sys.model.AppNode;
import com.viettel.core.sys.model.ObjectsNode;

/**
 *
 * @author ChucHV
 */
public class ObjectSelectParentController extends SelectorComposer<Component> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2188005881461318854L;
	@Wire
    private Window windowSelectParent;
    @Wire
    private Tree treeObject;

    private Objects selectedObject;
    private Applications selectedApplication;
    private Window parentWindow;

    @SuppressWarnings("unchecked")
	@Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
        parentWindow = (Window) arguments.get("parentWindow");

        TreeModel treeModel = new TreeModel(new AppNode(-1L, ""));
        treeObject.setModel(treeModel);
    }

    @Listen("onSelect = #treeObject")
    public void onSelect() {
        TreeNode treeNode = treeObject.getSelectedItem().getValue();
        if (treeNode instanceof ObjectsNode) {
            //get Object
            ObjectsNode objectsNode = (ObjectsNode) treeNode;
            ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
            selectedObject = objectsDAOHE.getObjectsById(objectsNode.getId());
            Events.sendEvent("onAfterSelectParentObject", parentWindow, selectedObject);
        } else {//neu chon application
            AppNode appNode = (AppNode) treeNode;
            ApplicationDAOHE applicationDAOHE = new ApplicationDAOHE();
            selectedApplication = applicationDAOHE.findById(appNode.getId());
            Events.sendEvent("onAfterSelectParentObject", parentWindow, selectedApplication);
        }

        //Close window
        windowSelectParent.onClose();
    }
}
