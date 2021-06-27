// Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov  Date: 8/15/2012 3:00:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   VsaFilter.java
package com.viettel.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.model.Menu;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.utils.Constants;
import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;

// Referenced classes of package viettel.passport.util:
//            Connector
public class SessionFilter extends BaseComposer implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean errorWhenExcuteAction = false;
		try {
			HttpServletRequest req = null;
			HttpServletResponse res = null;
			if (request instanceof HttpServletRequest) {
				req = (HttpServletRequest) request;
			}

			if (response instanceof HttpServletResponse) {
				res = (HttpServletResponse) response;
			}

			String url = ResourceBundleUtil.getString("url_path_nologin");
			String[] arrUrl;
			String regex = ",";
			arrUrl = url.split(regex);

			if (req != null && !"POST".equals(req.getMethod())) {

				if (req.getSession().getAttribute("userToken") == null) {

					boolean check = true;
					for (int i = 0; i < arrUrl.length; i++) {
						if (!arrUrl[i].isEmpty() && !arrUrl[i].trim().isEmpty()
								&& req.getRequestURL().indexOf(req.getContextPath() + arrUrl[i].trim()) >= 0) {
							chain.doFilter(request, response);
							check = false;
							break;
						}
					}
					if (check && res != null) {

						//res.sendRedirect("http://localhost:8081/cas/login?service="+ResourceBundleUtil.getString("link_system")+req.getContextPath()+"/Pages/systems.zul");

						// --- SSO ---//
						 res.sendRedirect(req.getContextPath()+"/Pages/login.zul");
					}
					// linhdx end
				} else {
					if (checkPermission(req)) {
						chain.doFilter(request, response);
					}
				}
			} else {
				chain.doFilter(request, response);
			}
		} catch (IOException | ServletException en) {
			errorWhenExcuteAction = true;
			LogUtils.addLog(en);
		} finally {
			if (!errorWhenExcuteAction) {
				try {
					HibernateUtil.commitCurrentSessions();
				} catch (Exception ex) {
					Logger.getLogger(SessionFilter.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					try {
						HibernateUtil.closeCurrentSessions();
					} catch (Exception ex) {
						Logger.getLogger(SessionFilter.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}
	}

	@Override
	public void destroy() {
	}

	private boolean checkPermission(HttpServletRequest req) {
		String url = req.getRequestURI();
		if (url.contains("/Pages/login.zul") || url.contains("/index.zul") || url.contains("/Share/upload/")
				|| url.contains("countMenu") || url.contains("/Pages/module/phamarcy/generalview/viewPDF.zul")) {
			return true;
		}
		UserToken token = (UserToken) req.getSession().getAttribute("userToken");
		if (token == null) {
			return false;
		}
		
		if (url.equals(req.getContextPath() + "/") || url.equals(req.getContextPath() + "/index.zul")
				|| url.equals(req.getContextPath() + "/Pages/logout.zul")) {
			return true;
		}
		
		if (token.getLstMenu() != null) {
			for (int i = 0; i < token.getLstMenu().size(); i++) {
				Menu menu = (Menu) token.getLstMenu().get(i);
				if (url.equals(menu.getMenuUrl())) {
					return true;
				} else if (menu.getLstMenu() != null) {
					for (int j = 0; j < menu.getLstMenu().size(); j++) {
						Menu childMenu = (Menu) menu.getLstMenu().get(j);
						if (url.equals(childMenu.getMenuUrl())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
