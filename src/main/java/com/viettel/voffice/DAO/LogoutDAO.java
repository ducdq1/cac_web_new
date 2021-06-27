/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.ResourceBundleUtil;

/**
 *
 * @author HaVM2
 */
public class LogoutDAO extends BaseComposer {

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        Session session = Sessions.getCurrent();
        session.invalidate();
        Executions.sendRedirect(ResourceBundleUtil.getString("link_sso_logout")+ResourceBundleUtil.getString("link_system"));
    }

}
