package com.viettel.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

public class AppListener extends HttpServlet
        implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        HibernateUtil.startup();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HibernateUtil.shutdown();
    }
}

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.AppListener
 * JD-Core Version:    0.6.2
 */