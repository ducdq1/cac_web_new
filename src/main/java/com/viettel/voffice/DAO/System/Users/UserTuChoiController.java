/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.text.MessageFormat;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.DAO.RegisterDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Register;
import com.viettel.voffice.model.PublicFunctionModel;
import com.viettel.ws.SendEmailSms;

/**
 *
 * @author HaVM2
 */
public class UserTuChoiController extends BaseComposer {
	static String TAG = "UserTuChoiController";
	
    @Wire
    Window userdialogTuChoi;
    Window parentWindow;
    @Wire
    private Textbox tbDescription;
    private Long id;
    
    /*@Wire
	private Label lbWarning;*/
    
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        Map<String, Object> arguments = (Map<String, Object>) Executions
                .getCurrent().getArg();
        id = (Long) arguments.get("id");
        parentWindow = (Window) arguments.get("parentWindow");
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

    }

    @Listen("onClick =#userdialogTuChoi #btnTuChoiOK")
    public void onClickXacNhanTuChoi() {
    	
    	//Validate 
    	if(!isValidate()){
    		return;
    	} else {
    		hideWarningMessage();
    	};
        //update truong ly do
        RegisterDAOHE registerDAOHE = new RegisterDAOHE();
        Register reg = registerDAOHE.findViewByFileId(id);
        String description = tbDescription.getValue();
        String notify =  getLabelRt("register_reject_success");
        Long status = reg.getStatus();
        
        if(description != null){
        	description = description.trim();
        }
        reg.setDescription(description);                
        reg.setStatus(Constants.Status.DELETE);
        if(Constants.Status.UPDATEPENDING == status){
        	reg.setStatus(Constants.Status.UPDATEREJECT);
        }

        registerDAOHE.saveOrUpdate(reg);
        
        userdialogTuChoi.onClose();
      
        Events.sendEvent("onVisible", parentWindow, null);
          parentWindow.onClose();
        showNotification(notify, Constants.Notification.INFO);
        sendRejectNotificationByEmail(description, status);
        

    }

    /**
     * Gui thong bao tu choi qua email
     * @author: trungmhv
     * @modifier trungmhv
     * @param: 
     * @createdDate: Jan 27, 2016
     * @updatedDate: Jan 27, 2016
     * @return:void
     */
    private void sendRejectNotificationByEmail(String msgReject, Long status){
    	RegisterDAOHE registerDAOHE = new RegisterDAOHE();
        Register reg = registerDAOHE.findViewByFileId(id);
        SendEmailSms ses = new SendEmailSms();
        String businessName = StringUtils.getValue(reg.getBusinessNameVi());
        msgReject = StringUtils.getValue(msgReject);
        
        String rejectContentEmail = getLabelRt("register_email_reject");
        
        if(Constants.Status.UPDATEPENDING == status){
        	rejectContentEmail = getLabelRt("update_email_reject");
        }
        rejectContentEmail = rejectContentEmail
        		.replace("reg_Bussiness_Name", businessName)
        		.replace("reg_Reason", msgReject);
        
        ses.sendEmailManual("Thong bao trang thai phe duyet tai khoan", reg.getUserEmail(), rejectContentEmail);
    }
    
    /**
     * Validate input data
     * @author: trungmhv
     * @modifier trungmhv
     * @param: 
     * @createdDate: Feb 17, 2016
     * @updatedDate: Feb 17, 2016
     * @return:boolean
     */
    private boolean isValidate(){    	
    	if (!validateTextBox(tbDescription)) {
			return false;
		}   	
    	
    	return true;
    }
    
    protected void hideWarningMessage() {
    	tbDescription.setErrorMessage("");
	}
}
