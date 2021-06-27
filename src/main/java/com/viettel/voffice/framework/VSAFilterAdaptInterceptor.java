/*    */ package com.viettel.voffice.framework;
/*    */ import javax.servlet.http.HttpServletRequest;

/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.struts2.ServletActionContext;

/*    */
/*    */ import com.opensymphony.xwork2.ActionInvocation;
/*    */ import com.opensymphony.xwork2.interceptor.Interceptor;
/*    */
/*    */ public class VSAFilterAdaptInterceptor
        /*    */ implements Interceptor /*    */ {
    /* 25 */ private static final Logger LOG = Logger.getLogger(VSAFilterAdaptInterceptor.class);
    /*    */
    /*    */ public String intercept(ActionInvocation ai)
            /*    */ throws Exception /*    */ {
        /* 31 */ HttpServletRequest request = ServletActionContext.getRequest();
        /*    */
        /* 34 */ if (("False").equals(request.getAttribute("VSA-IsPassedVSAFilter"))) /*    */ {
            /* 36 */ return "none";
            /*    */        }
        /* 38 */ ai.invoke();
        /*    */
        /* 41 */ return "success";
        /*    */    }
    /*    */
    /*    */ public void destroy() /*    */ {
        /*    */    }
    /*    */
    /*    */ public void init() /*    */ {
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.framework.interceptor.VSAFilterAdaptInterceptor
 * JD-Core Version:    0.6.2
 */