/*    */ package com.viettel.voffice.framework;
/*    */
/*    */ import com.opensymphony.xwork2.ActionInvocation;
/*    */ import com.opensymphony.xwork2.interceptor.Interceptor;
/*    */ import com.viettel.utils.HibernateUtil;
import com.viettel.utils.LogUtils;

/*    */ import java.util.HashMap;
/*    */ import org.apache.log4j.Logger;
/*    */
/*    */ public class HibernateConnectInterceptor
        /*    */ implements Interceptor /*    */ {
    /* 28 */ private static final Logger log = Logger.getLogger(HibernateConnectInterceptor.class);
    /*    */
    /*    */ public String intercept(ActionInvocation ai)
            /*    */ throws Exception /*    */ {
        /* 38 */ HashMap sessionsToRollBack = new HashMap<String, Object>();
        /*    */ try /*    */ {
            /* 41 */ boolean errorWhenExcuteAction = false;
            /* 42 */ Exception exceptionObject = null;
            /*    */ try {
                /* 44 */ ai.invoke();
                /*    */            } /*    */ catch (Exception ex) /*    */ {
                	LogUtils.addLog(ex);
                /* 47 */ errorWhenExcuteAction = true;
                /* 48 */ exceptionObject = ex;
                /* 49 */ throw exceptionObject;
                /*    */            } /*    */ finally {
                /* 52 */ if (errorWhenExcuteAction) {
                    /* 53 */ sessionsToRollBack = HibernateUtil.getCurrentSessions();
                    /* 54 */ String msg = String.format("Lỗi khi thực hiện action %s, rollback toàn bộ các transaction chưa được commit", new Object[]{ai.getAction().toString()});
                    /*    */
                    /* 57 */ log.error(msg, exceptionObject);
                    /*    */                } /*    */ else /*    */ {
                    /* 67 */ sessionsToRollBack = HibernateUtil.commitCurrentSessions();
                    /* 68 */ if (sessionsToRollBack.size() > 0) {
                        /* 69 */ String msg = "Lỗi khi thực hiện commit transaction, rollback các transaction chưa được commit";
                        /* 70 */ log.error(msg);
                        /*    */                    }
                    /*    */
                    /*    */                }
                /*    */
                /*    */            }
            /*    */
            /*    */        } /*    */ catch (Exception ex) /*    */ {
            	LogUtils.addLog(ex);
            /* 84 */ HibernateUtil.rollBackSessions(sessionsToRollBack);
            /* 86 */ throw ex;
            /*    */        } /*    */ finally {
            /* 89 */ HibernateUtil.closeCurrentSessions();
            /*    */        }
        /*    */
        /* 92 */ return "success";
        /*    */    }
    /*    */
    /*    */ public void destroy() /*    */ {
        /*    */    }
    /*    */
    /*    */ public void init() /*    */ {
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.framework.interceptor.HibernateConnectInterceptor
 * JD-Core Version:    0.6.2
 */