package com.viettel.module.phamarcy.controller;



import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;

/**
 * Doanh nghiep xem Noi dung Yeu cau Sua doi bo sung
 * 
 * @author ducdq1
 *
 */
public class PhamarcyViewAdditionController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2822992487973502251L;
	/**
	*
	*/
	@Wire
	private Window windowView;
	@Wire
	private Div divDispath, main;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		divDispath = (Div) Executions.createComponents("/Pages/module/phamarcy/organization/viewAdditionalInclude.zul",
				main, arguments);
	}

 
}
