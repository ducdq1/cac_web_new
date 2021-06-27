/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Application;

import com.viettel.core.base.DAO.BaseComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author ChucHV
 */
public class AppViewController extends BaseComposer {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3345802890939311549L;
	@Wire
    private Window windowViewApp;

    @Listen("onClick = #toolbarTop .back, #toolbarBottom .back")
    public void onClose() {
        windowViewApp.onClose();
    }
}
