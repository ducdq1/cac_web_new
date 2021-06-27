/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Flow;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.workflow.WorkflowAPI;
import com.viettel.core.workflow.DAO.ProcessDAOHE;

/**
 *
 * @author HaVM2
 */
public class FlowCurrentViewController extends BaseComposer {
    private static final long serialVersionUID = 1L;

    @Wire
    Window flowConfigDlg;
    @Wire
    Listbox lbProcess;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Long objectId = (Long) Executions.getCurrent().getArg().get("objectId");
        Long objectType = (Long) Executions.getCurrent().getArg().get("objectType");
        displayFlow(objectId, objectType);
    }

    @SuppressWarnings("rawtypes")
	public void displayFlow(Long objectId, Long objectType) {
        ProcessDAOHE pdhe = new ProcessDAOHE();
        List lstProcess = pdhe.getProcessProcess(objectId, objectType);
        if(lstProcess == null || lstProcess.isEmpty()){
            return;
        }
        //
        // Hien thi tren danh sach
        //
        ListModelList lstModel = new ListModelList(lstProcess);
        lbProcess.setModel(lstModel);
        //
        // hien thi do hoa
        //
        // viethd3 19/03/2015
        // li do: phan ve do thi luong xu ly dang bi loi nen khong hien thi
        //Clients.evalJavaScript("page.process = " + json + ";");
        //Clients.evalJavaScript("loadAndDrawFlowContent();");
    }

    @Listen("onBack=#flowConfigDlg")
    public void onBack() {
        flowConfigDlg.detach();
    }
    
    public String getStatus(Long status){
        return WorkflowAPI.getStatusName(status);
    }
    
    public String getName(String userName) {
		Users mUser = new UserDAOHE().getByUserName(userName);
		if(mUser!=null)
		{
			return mUser.getFullName();
		}
		else
		{
			return userName;
		}
	}
    
}
