/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.phamarcy.controller;

import com.viettel.core.base.DAO.BaseComposer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

/**
 *
 * @author ChucHV
 */
public class PhamarcyAllSearchController extends BaseComposer {

    private static final long serialVersionUID = 1L;

    @Wire
    private Datebox dbFromDay, dbToDay;

    @Wire
    private Textbox tbNSWFileCode; //Ma ho so 
    @Wire
    private Textbox tbRapidTestNo;
    
    @Wire
    private Listbox lboxStatus,lboxDocumentTypeCode,lboxOrder;
    
    @Wire
    private Groupbox fullSearchGbx;


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }
}
