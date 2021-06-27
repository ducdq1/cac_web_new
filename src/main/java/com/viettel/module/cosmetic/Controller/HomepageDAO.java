/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.cosmetic.Controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.ResourceBundleUtil;

/**
 *
 * @author DUC
 */
@SuppressWarnings("serial")
public class HomepageDAO extends BaseComposer {

	

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {

		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
	}

	
	@Listen("onClick= #btnLogin")
	public void onLogin(){		
		Executions.sendRedirect(ResourceBundleUtil.getString("link_sso")+ResourceBundleUtil.getString("link_system")+"/Pages/systems.zul");
	}
	
}
