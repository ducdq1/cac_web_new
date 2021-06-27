package com.viettel.voffice.DAO.DocIn.include;

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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.ResourceBundleUtil;

public class TreeObjectsToSendProcess extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 7361775434185224999L;

    @Wire
    private Window wdTree;
    @Wire
    private Tree tree;
    private Component targetToReceive;
    private String actionName;
    private Process processCurrent;
    private List<NodeDeptUser> listAvailableNDU;
    private List<Process> listSentProcess;
    private List<NodeDeptUser> listChoosedNDU;
    private List<Long> listValidatedDept;// Danh sách đơn vị có thể chuyển
    private List<Long> listValidatedUser;// Danh sách người dùng có thể chuyển

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        listValidatedDept = new ArrayList<>();
        listValidatedUser = new ArrayList<>();
        onLoadData();
    }

    /*
     * Cho ý kiến, Hoàn thành loại 1 sẽ ko gọi đến hàm này do ko cần hiển thị
     * tree Chỉ có chuyển xử lí, hoàn thành loại 2 mà thôi.
     */
    public void onLoadData() {
        HashMap<String, Object> arguments = (HashMap<String, Object>) Executions
                .getCurrent().getArg();
        actionName = (String) arguments.get("actionName");
        processCurrent = (Process) arguments.get("processCurrent");
        listAvailableNDU = (List<NodeDeptUser>) arguments.get("listAvailableNDU");
        listSentProcess = (List<Process>) arguments.get("listSentProcess");
        listChoosedNDU = (List<NodeDeptUser>) arguments.get("listChoosedNDU");
        targetToReceive = (Listbox) arguments.get("target");

        DepartmentDAOHE ddhe = new DepartmentDAOHE();
        List<Long> lstParents;

        if (listAvailableNDU != null) {
            for (NodeDeptUser ndu : listAvailableNDU) {
                if (ndu.getUserId() != null) {
                    if (!listValidatedUser.contains(ndu.getUserId())) {
                        lstParents = ddhe.getParents(ndu.getDeptId());
                        for (Long deptId : lstParents) {
                            if (!listValidatedDept.contains(ndu.getDeptId())) {
                                listValidatedDept.add(deptId);
                            }
                        }
                        if (ndu.getUserId() != null) {
                            listValidatedUser.add(ndu.getUserId());
                        }
                    }
                } else {
                    if (!listValidatedDept.contains(ndu.getDeptId())) {
                        lstParents = ddhe.getParents(ndu.getDeptId());
                        for (Long deptId : lstParents) {
                            if (!listValidatedDept.contains(ndu.getDeptId())) {
                                listValidatedDept.add(deptId);
                            }
                        }
                        if (ndu.getUserId() != null) {
                            listValidatedUser.add(ndu.getUserId());
                        }
                    }
                }
            }
        }
        // Add id for pseudoRootNode
        listValidatedDept.add(-1L);

        /*
         * 1. Trường hợp văn bản đến và chuyển xử lý: --------------------------
         * Chỉ load cây đơn vị từ vị trí đơn vị gốc mà thôi, không load toàn cây
         * 2. Trường hợp xin ý kiến: load toàn cây đơn vị.
         */
        PseudoRootNode pseudoRootNode;
        Department rootDepartment;
//        if (isGetOpinion(actionName)) {
//            Long rootDeptId = ddhe.getParents(getDeptId()).get(0);
//            rootDepartment = ddhe.findById(rootDeptId);
//        } else {
//            rootDepartment = ddhe.findBOById(getDeptId());
//            pseudoRootNode = new PseudoRootNode(-1L, "", new DeptNode(
//                    rootDepartment));
//        }
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

    /**
     * Khởi tạo ndu cho treeNode
     *
     * @param treeNode
     */
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

    /**
     * Nếu treeNode match với listNDU (Danh sách ndu được phép chuyển xử lí) thì
     * set ndu của treeNode = ndu match đó luôn.
     *
     * @param treeNode
     */
    private void matchToListNDU(TreeNode treeNode) {
        if (isGetOpinion(actionName)) {
            ((NodeDeptUser) treeNode.get("ndu"))
                    .setProcessType(Constants.PROCESS_TYPE.COMMENT);
            return;
        }

        if (listAvailableNDU != null) {
            for (NodeDeptUser ndu : listAvailableNDU) {
                if (Objects.equals(ndu.getUserId(), treeNode.getId())
                        && Objects.equals(ndu.getDeptId(),
                                ((UserNode) treeNode).getUsers().getDeptId())) {
                    treeNode.put("ndu", ndu);
                }
            }
        }
    }

    public String loadLabelProcessType(Long processType) {
        return ProcessDAOHE.loadProcessTypeName(processType);
    }

    // clean code trùng với notify
    public AImage getAvatar(Long userId) throws IOException {
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

    public boolean isGetOpinion(String actionName) {
        return actionName == null ? false : "xin ý kiến".equals(actionName
                .toLowerCase());
    }

    @Listen("onChangeProcessType = #lbNodeDeptUser")
    public Long onChangeProcessType(Long processType) {
        // Neu la "Xin y kien" thi ko cho thay doi loai xu li
        if (isGetOpinion(actionName)) {
            return null;
        }

        if (processType == null) {
            return Constants.PROCESS_TYPE.COOPERATE;
        } else {
            if (processType < Constants.PROCESS_TYPE.RECEIVE_TO_KNOW) {
                return processType + 1;
            } else {
                return Constants.PROCESS_TYPE.COOPERATE;
            }
        }
    }

    @Listen("onOpen = #tree")
    public void onOpenTree() {
        
    }

    @SuppressWarnings("unchecked")
    @Listen("onClick = #btnSelect")
    public void onSelectObject() {
        List<NodeDeptUser> tempListNDU = new ArrayList<>();

        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        /*
         * Phải lấy danh sách đã gửi process phòng trường hợp có người đăng nhập
         * vào tại browser # và thực hiện gửi xử lý đi.
         * http://10.60.15.33/mantis/view.php?id=197095
         */
        listSentProcess = processDAOHE.getSentProcess(processCurrent);
        if (listSentProcess != null && !listSentProcess.isEmpty()) {
            tempListNDU.addAll(processDAOHE
                    .convertProcessToNDU(listSentProcess));
        }

        Set<Treeitem> items = tree.getSelectedItems();
        List<NodeDeptUser> listSelectedNDU = convertSelectedTreeitemsToListNDU(items);
        boolean isSelected ;
        if (listChoosedNDU != null) {
            for (NodeDeptUser choosedNDU : listChoosedNDU) {
                isSelected = false;
                for (NodeDeptUser selectedNDU : listSelectedNDU) {
                    if (Objects.equals(selectedNDU.getDeptId(), choosedNDU.getDeptId())
                            && Objects.equals(selectedNDU.getUserId(), choosedNDU.getUserId())) {
                        isSelected = true;
                    }
                }
                if (!isSelected) {
                    listSelectedNDU.add(choosedNDU);
                }
            }
        }

        tempListNDU.addAll(listSelectedNDU);

        String message = processDAOHE.isListNDUValidatedToSend(tempListNDU);
        if (message != null) {
            showNotification(message, Constants.Notification.WARNING);
        } else {
            HashMap<String, Object> arguments = new HashMap<String, Object>();
            arguments.put("listNDU", tempListNDU);
            arguments.put("actionName", actionName);
            Events.sendEvent("onLoadModel", targetToReceive, arguments);
            wdTree.onClose();
        }
    }

    public List<NodeDeptUser> convertSelectedTreeitemsToListNDU(
            Collection<Treeitem> items) {
        TreeNode treeNode;
        List<NodeDeptUser> listNDU = new ArrayList<>();
        NodeDeptUser ndu;
        for (Treeitem item : items) {
            //loại bỏ item đã được gửi
            if (!item.isDisabled()) {
                treeNode = item.getValue();
                ndu = (NodeDeptUser) treeNode.get("ndu");
                // Không chỉ rõ loại xử lý thì mặc định là "Phối hợp"
                if (ndu.getProcessType() == null) {
                    ndu.setProcessType(Constants.PROCESS_TYPE.COOPERATE);
                }
                listNDU.add(ndu);
            }
        }
        return listNDU;
    }

    /**
     * Những treeitem nào tương ứng với process đã gửi (ko tính trả lại và thu
     * hồi) thì phải được disable và selected.
     *
     * @param listSentProcess
     * @param treeItem
     */
    public void renderSentTreeItem(List<Process> listSentProcess,
            Treeitem treeItem) {
        if (listSentProcess != null) {
            TreeNode treeNode = treeItem.getValue();
            for (Process p : listSentProcess) {
                NodeDeptUser ndu = (NodeDeptUser) treeNode.get("ndu");
                if (Objects.equals(ndu.getDeptId(), (p.getReceiveGroupId()))
                        && Objects
                        .equals(ndu.getUserId(), p.getReceiveUserId())
                        && !Constants.PROCESS_STATUS.RETURN.equals(p
                                .getStatus())
                        && !Constants.PROCESS_STATUS.RETRIEVE.equals(p
                                .getStatus())) {
                    ndu.setProcessType(p.getProcessType());
                    treeNode.put("ndu", ndu);
                    treeItem.setDisabled(true);
                    treeItem.setSelected(true);
                }
            }
        }
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
                if (!isGetOpinion(actionName)
                        && !listValidatedDept.contains(treeNode.getId())) {
                    treeItem.setVisible(false);
                    // treeItem.getParent().removeChild(treeItem);
                }
                avatar.setSrc("/Share/img/icon/folder.png");
            } else if (treeNode instanceof UserNode) {
                if (!isGetOpinion(actionName)
                        && !listValidatedUser.contains(treeNode.getId())) {
                    treeItem.setVisible(false);
                    // treeItem.getParent().removeChild(treeItem);
                }
                avatar.setContent(getAvatar(treeNode.getId()));
            }
            Label name = new Label(treeNode.getName());
            firstLayout.appendChild(avatar);
            firstLayout.appendChild(name);
            firstLayout.setSclass("h-inline-block");
            firstCell.appendChild(firstLayout);

            putNDU(treeNode);
            matchToListNDU(treeNode);
            /*
             *  Cần sửa lại đoạn này
             */
            renderSentTreeItem(listSentProcess, treeItem);

            //
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

            final Label lbProcessType = new Label(
                    loadLabelProcessType(((NodeDeptUser) treeNode.get("ndu"))
                            .getProcessType()));
            Image imgSelectProcessType = new Image("/Share/img/icon/user.gif");
            if (!isGetOpinion(actionName)) {
                imgSelectProcessType.addEventListener(Events.ON_CLICK,
                        new EventListener<Event>() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                NodeDeptUser ndu = (NodeDeptUser) treeNode
                                .get("ndu");
                                Long nextProcessType = onChangeProcessType(ndu
                                        .getProcessType());
                                if (nextProcessType != null) {
                                    ndu.setProcessType(nextProcessType);
                                    lbProcessType
                                    .setValue(loadLabelProcessType(ndu
                                                    .getProcessType()));
                                }
                            }
                        });
            }
            Hlayout secondLayout = new Hlayout();
            secondLayout.appendChild(imgSelectProcessType);
            secondLayout.appendChild(lbProcessType);
            secondLayout.setSclass("h-inline-block");
            secondCell.appendChild(secondLayout);

            treeRow.appendChild(firstCell);
            treeRow.appendChild(secondCell);
        }
    }
}
