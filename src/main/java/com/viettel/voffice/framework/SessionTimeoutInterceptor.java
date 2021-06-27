/*    */ package com.viettel.voffice.framework;
/*    */ import javax.servlet.http.HttpServletRequest;

/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.struts2.ServletActionContext;

/*    */
/*    */ import com.opensymphony.xwork2.ActionInvocation;
/*    */ import com.opensymphony.xwork2.interceptor.Interceptor;
/*    */
/*    */ public class SessionTimeoutInterceptor
        /*    */ implements Interceptor /*    */ {
    /* 24 */ private static final Logger LOG = Logger.getLogger(SessionTimeoutInterceptor.class);
    /*    */
    /*    */ public String intercept(ActionInvocation ai)
            /*    */ throws Exception /*    */ {
        /* 30 */ HttpServletRequest request = ServletActionContext.getRequest();
        /*    */
        /* 37 */ if (request.getSession().isNew()) {
            /* 38 */ ai.setResultCode("sessionTimeout");
            /* 39 */ return "sessionTimeout";
            /*    */        }
        /*    */
        /* 42 */ return "success";
        /*    */    }
    /*    */
    /*    */ public void destroy() /*    */ {
        /*    */    }
    /*    */
    /*    */ public void init() /*    */ {
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.framework.interceptor.SessionTimeoutInterceptor
 * JD-Core Version:    0.6.2
 */