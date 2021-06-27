/*    */ package com.viettel.voffice.framework;
/*    */
/*    */ import com.opensymphony.xwork2.ActionInvocation;
/*    */ import com.opensymphony.xwork2.interceptor.Interceptor;
/*    */
/*    */ public class ResultInterceptor
        /*    */ implements Interceptor /*    */ {
    /*    */ public String intercept(ActionInvocation ai)
            /*    */ throws Exception /*    */ {
        /* 31 */ ai.addPreResultListener(new ActionPreResultListener());
        /* 32 */ ai.invoke();
        /* 33 */ return "success";
        /*    */    }
    /*    */
    /*    */ public void destroy() /*    */ {
        /*    */    }
    /*    */
    /*    */ public void init() /*    */ {
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.framework.interceptor.ResultInterceptor
 * JD-Core Version:    0.6.2
 */