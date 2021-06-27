/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Ycnk.wf;

import com.viettel.core.workflow.BusinessController;
import com.viettel.utils.Constants;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;

/**
 *
 * @author duv
 */
public class ActiveFlowController extends BusinessController {

    private static final long serialVersionUID = 1L;
    @Wire
    Textbox txtValidate;

    @Override
    @Listen("onClick=#btnSubmit")
    public void onSubmit() {
        txtValidate.setText(Constants.IS_VALIDATE_OK.toString());
        if (Long.parseLong(txtValidate.getText()) == Constants.IS_VALIDATE_OK) {
            showNotification(String.format(Constants.Notification.SENDED_SUCCESS, "hồ sơ"), Constants.Notification.INFO);
        }
        super.onSubmit();
    }

}
