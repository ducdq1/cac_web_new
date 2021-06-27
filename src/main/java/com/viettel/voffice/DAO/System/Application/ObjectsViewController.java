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
public class ObjectsViewController extends BaseComposer{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5401078146839547807L;
	@Wire
    private Window windowViewObject;

    @Listen("onClick = #toolbarTop .back, #toolbarBottom .back")
    public void onClose() {
        windowViewObject.onClose();
    }
}
