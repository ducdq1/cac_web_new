/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Category;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.TreeItem;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

/**
 *
 * @author HaVM2
 */
public class CategorySelectController extends BaseComposer {

    @Wire
    Textbox txtDisplayIdComponentId, txtDisplayNameComponentId;
    @Wire
    Window selectCategoryDlg;
    @Wire
    Tree tree;

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        String idOfDisplayNameComp = (String) Executions.getCurrent().getArg().get("idOfDisplayNameComp");
        txtDisplayNameComponentId.setValue(idOfDisplayNameComp);
        String idOfDisplayIdComp = (String) Executions.getCurrent().getArg().get("idOfDisplayIdComp");
        txtDisplayIdComponentId.setValue(idOfDisplayIdComp);
    }

    @Listen("onSelect=#tree")
    public void onSelect() {
        TreeItem item = tree.getSelectedItem().getValue();
        if (item.getType() != 2l) {
            Clients.showNotification("Bạn phải chọn danh mục chứ không phải loại danh mục");
        } else if (item.getType() == 2l) {
            String name = item.getName();
            Long id = item.getId();
            String idOfDisplayName = txtDisplayNameComponentId.getValue();
            String idOfDisplayId = txtDisplayIdComponentId.getValue();
            Textbox txtDeptName = (Textbox) Path.getComponent(idOfDisplayName);
            txtDeptName.setValue(name);
            Textbox txtDeptId = (Textbox) Path.getComponent(idOfDisplayId);
            txtDeptId.setValue(id.toString());
            selectCategoryDlg.detach();
        }
    }
}
