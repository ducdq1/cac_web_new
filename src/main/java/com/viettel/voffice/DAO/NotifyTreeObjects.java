package com.viettel.voffice.DAO;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PseudoRootNode;
import com.viettel.core.base.model.TreeModel;
import com.viettel.core.base.model.TreeNode;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.DeptNode;
import com.viettel.core.user.model.UserNode;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.utils.ResourceBundleUtil;

public class NotifyTreeObjects extends BaseComposer {

    private static final long serialVersionUID = 7361775434185224999L;

    @Wire
    private Window wdTreeNotify;
    @Wire
    private Window notifyWindowUserDept;
    @Wire
    private Tree tree;
    private Listbox lstNotify;
    private List<NodeDeptUser> listNDU;
    private List<NodeDeptUser> listChoosedNDU;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
                .getCurrent().getArg();
//        notifyWindowUserDept = (Window) Executions.getCurrent().getArg().get("parentWindow");
        lstNotify = (Listbox) arguments.get("lstNotify");
        onLoadData();
    }

    @SuppressWarnings("unchecked")
    public void onLoadData() {

        DepartmentDAOHE ddhe = new DepartmentDAOHE();
        PseudoRootNode pseudoRootNode;
        Department rootDepartment;
        Long rootDeptId = ddhe.getParents(getDeptId()).get(0);
        rootDepartment = ddhe.findById(rootDeptId);
        pseudoRootNode = new PseudoRootNode(-1L, "", new DeptNode(
                rootDepartment));
        pseudoRootNode.put("ndu", new NodeDeptUser());
        TreeModel model = new TreeModel(pseudoRootNode);
        model.setMultiple(true);
        tree.setModel(model);
        tree.setItemRenderer(new TreeRenderer());
    }
    
    @SuppressWarnings("unchecked")
    @Listen("onClick = #btnSelect")
    public void onSelectObject() {
        List<NodeDeptUser> tempListNDU = new ArrayList<NodeDeptUser>();
        Set<Treeitem> items = tree.getSelectedItems();
        listNDU = convertSelectedTreeitemsToListNDU(items);
        tempListNDU.addAll(listNDU);
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("listNDU", tempListNDU);
        Events.sendEvent("onLoadModel", lstNotify, arguments);
        wdTreeNotify.onClose();
    }

    public List<NodeDeptUser> convertSelectedTreeitemsToListNDU(
            Collection<Treeitem> items) {
        TreeNode treeNode;
        List<NodeDeptUser> listNDU = new ArrayList<>();
        NodeDeptUser ndu;
        for (Treeitem item : items) {
            if (!item.isDisabled()) {
                treeNode = item.getValue();
                ndu = (NodeDeptUser) treeNode.get("ndu");
                // Không chỉ rõ loại xử lý thì mặc định là "Phối hợp"
//                if (ndu.getProcessType() == null) {
//                    ndu.setProcessType(Constants.PROCESS_TYPE.COOPERATE);
//                }
                listNDU.add(ndu);
            }
        }
        return listNDU;
    }
    private void putNDU(TreeNode treeNode) {
        NodeDeptUser ndu = new NodeDeptUser();
        if (treeNode instanceof DeptNode) {
            if (((DeptNode) treeNode).getDepartment() != null) {
                ndu.setDeptId(((DeptNode) treeNode).getDepartment().getDeptId());
                ndu.setDeptName(((DeptNode) treeNode).getDepartment()
                        .getDeptName());
            }
        } else if (treeNode instanceof UserNode) {
            if (((UserNode) treeNode).getUsers() != null) {
                ndu.setDeptId(((UserNode) treeNode).getUsers().getDeptId());
                ndu.setDeptName(((UserNode) treeNode).getUsers().getDeptName());
                ndu.setUserId(((UserNode) treeNode).getUsers().getUserId());
                ndu.setUserName(((UserNode) treeNode).getUsers().getFullName());
            }
        }
        treeNode.put("ndu", ndu);
    }

    private void matchToListNDU(TreeNode treeNode) {
        if (listNDU != null) {
            for (NodeDeptUser ndu : listNDU) {
                if (Objects.equals(ndu.getUserId(), treeNode.getId())
                        && Objects.equals(ndu.getDeptId(),
                                ((UserNode) treeNode).getUsers().getDeptId())) {
                    treeNode.put("ndu", ndu);
                }
            }
        }
    }

    // TODO: clean code trùng với notify
    private AImage getAvatar(Long userId) throws IOException {
        AImage aImage;
        String avatarPath;
        String folderPath = Executions.getCurrent().getDesktop().getWebApp()
                .getRealPath(ResourceBundleUtil.getString("dir_avartar"));
        String defaultPath = getDefaultImgPath();

        UserDAOHE userDAOHE = new UserDAOHE();
        Users user = userDAOHE.getUserById(userId);
        if (user == null) {
            avatarPath = defaultPath;
        } else {
            if (user.getAvartarPath() == null) {
                avatarPath = defaultPath;
            } else {
                File file = new File(folderPath + "\\" + user.getUserId());
                if (file.exists()) {
                    avatarPath = folderPath + "\\" + user.getUserId();
                } else {
                    // load default avatar
                    avatarPath = defaultPath;
                }
            }
        }
        aImage = new AImage(avatarPath);
        return aImage;
    }

    public String getDefaultImgPath() {
        String path = Executions.getCurrent().getDesktop().getWebApp()
                .getRealPath("/Share/avatar/")
                + File.separator + "default-avatar.png";
        return path;
    }
    @Listen("onOpen = #tree")
    public void onOpenTree() {
        // TODO
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
                avatar.setContent(getAvatar(treeNode.getId()));
            }
            Label name = new Label(treeNode.getName());
            firstLayout.appendChild(avatar);
            firstLayout.appendChild(name);
            firstLayout.setSclass("h-inline-block");
            firstCell.appendChild(firstLayout);

            putNDU(treeNode);
            if (listChoosedNDU != null) {
                for (NodeDeptUser choosedNDU : listChoosedNDU) {
                    NodeDeptUser ndu = (NodeDeptUser) treeNode.get("ndu");
                    if (Objects.equals(ndu.getDeptId(), choosedNDU.getDeptId())
                            && Objects.equals(ndu.getUserId(),
                                    choosedNDU.getUserId())) {
                        ndu.setProcessType(choosedNDU.getProcessType());
                        treeNode.put("ndu", ndu);
                        treeItem.setSelected(true);
                    }
                }
            }

            Image imgSelectProcessType = new Image("/Share/img/icon/user.gif");
            Hlayout secondLayout = new Hlayout();
            secondLayout.appendChild(imgSelectProcessType);
            secondLayout.setSclass("h-inline-block");
            secondCell.appendChild(secondLayout);

            treeRow.appendChild(firstCell);
            treeRow.appendChild(secondCell);
        }
    }
}
