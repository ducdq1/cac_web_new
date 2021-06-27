/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.system;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Button;

/**
 *
 * @author havm2
 */
public class Home extends SelectorComposer<Component> {

    @Wire
    private Button btnSend;
    private static Logger logger = Logger.getLogger(Home.class.getName());

    @Listen("onClick = #btnSend")
    public void onSubmit() {
        logger.debug("a user was added.");
    }

    public Button getBtnSend() {
        return btnSend;
    }

    public void setBtnSend(Button btnSend) {
        this.btnSend = btnSend;
    }
}
