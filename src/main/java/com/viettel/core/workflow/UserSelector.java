package com.viettel.core.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import com.viettel.utils.Constants;

public class UserSelector extends BaseComposer {
    private static final long serialVersionUID = 7361775434185224999L;

    @Wire
    private Window wdUserSelector;
    @Wire
    private Listbox lbNodeDeptUser;
    private Component targetToReceive;
    private String actionName;
    private Process processCurrent;
    private List<NodeDeptUser> listAvailableNDU;
    private List<Process> listSentProcess;
    private List<NodeDeptUser> listChoosedNDU;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        onLoadData();
    }

    public void onLoadData() {
    	HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
        actionName = (String) arguments.get("actionName");
        processCurrent = (Process) arguments.get("processCurrent");
        listAvailableNDU = (List<NodeDeptUser>) arguments.get("listAvailableNDU");
        listSentProcess = (List<Process>) arguments.get("listSentProcess");
        listChoosedNDU = (List<NodeDeptUser>) arguments.get("listChoosedNDU");
        targetToReceive = (Listbox) arguments.get("target");

        ListModelList model = new ListModelList(listAvailableNDU);
        lbNodeDeptUser.getItems().clear();
        lbNodeDeptUser.setModel(model);
        lbNodeDeptUser.renderAll();
    }

    @Listen("onClick = #btnSelect")
    public void onSelectObject() {
        List<NodeDeptUser> tempListNDU = new ArrayList<>();

        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        /*
         * Phải lấy danh sách đã gửi process phòng trường hợp có người đăng nhập
         * vào tại browser # và thực hiện gửi xử lý đi.
         * http://10.60.15.33/mantis/view.php?id=197095
         */
        if(processCurrent == null)
            listSentProcess = null;
        else
            listSentProcess = processDAOHE.getSentProcess(processCurrent);
        if (listSentProcess != null && !listSentProcess.isEmpty()) {
            // viethd3 18/03/2015
            // viec lay thong tin process da gui chi de phuc vu tim lai nguoi da tung xu ly
            // fix loi viec hien thi danh sach nguoi xu ly truoc hien thi tat ca
            // cac NDU da tung gui trong qua khu (ke ca da gui o cac buoc xu ly khac)
            // comment dong code add NDU cu vao tempListNDU
            //tempListNDU.addAll(processDAOHE.convertProcessToNDU(listSentProcess));
        }

        Listitem selected = lbNodeDeptUser.getSelectedItem();
		if (selected == null) {
			showNotification("Bạn chưa chọn cán bộ!", Constants.Notification.WARNING);
		} else {
			NodeDeptUser index = selected.getValue();
			tempListNDU.add(index);
			HashMap<String, Object> arguments = new HashMap<String,Object>();
			arguments.put("listNDU", tempListNDU);
			arguments.put("actionName", actionName);
			Events.sendEvent("onLoadModel", targetToReceive, arguments);
			wdUserSelector.onClose();
		}
    }
}
