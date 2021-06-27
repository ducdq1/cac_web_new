package com.viettel.voffice.DAO.Document;

import com.viettel.core.user.BO.Department;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.user.BO.Users;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.voffice.DAO.DocIn.include.TreeObjectsToSendProcess;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.model.DeptNode;
import com.viettel.core.base.model.PseudoRootNode;
import com.viettel.core.base.model.TreeModel;
import com.viettel.core.base.model.TreeNode;
import com.viettel.core.user.model.UserNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

/**
 *
 * @author thanhvt10
 */
public class TreeUsersDeptController extends BaseComposer {

    @Wire
    Window usersDeptTree;
    @Wire
    Tree tree;
    @Wire
    Textbox userPerformName;
    @Wire
    Listbox usersHelp;
    Window windowparent;
//    private TaskManageDAOHE taskDAOHE = new TaskManageDAOHE();
    String type;
    TreeObjectsToSendProcess treeObject = new TreeObjectsToSendProcess();
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        String usersPerform = (String) Executions.getCurrent().getArg().get("idOfUserPerform");
         windowparent = (Window) Executions.getCurrent().getArg().get("parentWindow");
        type = (String) Executions.getCurrent().getArg().get("type");
        userPerformName.setValue(usersPerform);
        DepartmentDAOHE ddhe = new DepartmentDAOHE();
//            List<Long> lstParents;
        Long rootDeptId = ddhe.getParents(getDeptId()).get(0);
        Department rootDepartment = ddhe.findById(rootDeptId);
        PseudoRootNode pseudoRootNode = new PseudoRootNode(-1L, "",
                new DeptNode(rootDepartment));
        pseudoRootNode.put("ndu", new NodeDeptUser());
        TreeModel model = new TreeModel(pseudoRootNode);
        model.setMultiple(true);
        tree.setModel(model);
        tree.setItemRenderer(new TreeRenderer() );
    }
     public class TreeRenderer implements TreeitemRenderer<TreeNode> {

        @Override
        public void render(Treeitem treeItem, final TreeNode treeNode, int index)
                throws Exception {

            Treerow treeRow = new Treerow();
            treeRow.setParent(treeItem);
            treeItem.setValue(treeNode);
            Treecell firstCell = new Treecell();
            Treecell secondCell = new Treecell();

            Hlayout firstLayout = new Hlayout();    
            Image avatar = new Image();
            avatar.setWidth("16px");
            avatar.setHeight("16px");
            if (treeNode instanceof DeptNode) {
                avatar.setSrc("/Share/img/icon/folder.png");
            } else if (treeNode instanceof UserNode) {
                avatar.setContent(treeObject.getAvatar(treeNode.getId()));
            }
            Label name = new Label(treeNode.getName());
            firstLayout.appendChild(avatar);
            firstLayout.appendChild(name);
            firstLayout.setSclass("z-hlayout-inner");
            firstCell.appendChild(firstLayout);

//            treeObject.putNDU(treeNode);
//            treeObject.matchToListNDU(treeNode);
            Image imgSelectProcessType = new Image("/Share/img/icon/user.gif");
            Hlayout secondLayout = new Hlayout();
            secondLayout.appendChild(imgSelectProcessType);
            secondLayout.setSclass("z-hlayout-inner");
            secondCell.appendChild(secondLayout);

            treeRow.appendChild(firstCell);
            treeRow.appendChild(secondCell);
        }
    }
    
    @Listen("onClick = #btnSelect")
	public void onSelectObject() {
            if(!"".equals(type) && type != null){
                List<Users> listUser = new ArrayList<Users>();
                Set<Treeitem> treeitems = tree.getSelectedItems();
                Users user;
                for (Treeitem item : treeitems) {
                    user = new Users();
                    if (item.getValue() instanceof UserNode) {
                        user.setUserId(((UserNode)item.getValue()).getId());
                        user.setUserName(((UserNode)item.getValue()).getName());
                        listUser.add(user);
                    }
                }
                Events.sendEvent("onSelectUsersHelp", windowparent, listUser);
            }else{
                Treeitem items = tree.getSelectedItem();
                UserNode userNode = (UserNode) items.getValue();
                Users userPerform = new Users();
                userPerform.setUserId(userNode.getId());
                userPerform.setUserName(userNode.getName());
                Events.sendEvent("onSelectUsersPerform", windowparent, userPerform);
            }
                usersDeptTree.onClose();
	}

}
