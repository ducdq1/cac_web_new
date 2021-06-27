/*     */ package com.viettel.utils;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;

/*     */ import org.apache.struts2.ServletActionContext;

/*     */
/*     */ import com.viettel.utils.log.LogFactory;
/*     */ import com.viettel.utils.log.Logger;
/*     */ import com.viettel.voffice.dojotag.ActionMessage;
/*     */ import com.viettel.voffice.dojotag.ActionMessageImpl;
/*     */
/*     */ public class WFUtil /*     */ {
    /*  24 */ private static volatile LogFactory logFactory = null;
    /*     */
    /*  26 */ private static Logger defaultLogger = null;
    /*     */
    /*     */ public static ActionMessage getActionMessage() /*     */ {
        /*  38 */ return new ActionMessageImpl();
        /*     */    }
    /*     */
    /*     */ public static HttpServletRequest currentRequest() /*     */ {
        /*  46 */ return ServletActionContext.getRequest();
        /*     */    }
///*     */ 
/*     */ public static HttpServletResponse currentResponse() /*     */ {
        /*  54 */ return ServletActionContext.getResponse();
        /*     */    }
    /*     */
    /*     */ public static Logger getLogger(Class clazz) /*     */ {
        /*  62 */ return logFactory().getLogger(clazz);
        /*     */    }
    /*     */
    /*     */ public static Logger getLogger(String moduleName) /*     */ {
        /*  70 */ return logFactory().getLogger(moduleName);
        /*     */    }
///*     */ 
/*     */ public static Logger log() /*     */ {
        /*  77 */ if (defaultLogger == null) {
            /*  78 */       //defaultLogger = logFactory().getLogger("DefaultLogger");
/*     */        }
        /*  80 */ return defaultLogger;
        /*     */    }
    /*     */
    /*     */   /*     */ 
        /*  89 */  
        /*     */    
    /*     */
    /*     */ private static LogFactory logFactory() /*     */ {
        /*  98 */ if (logFactory == null) {
            /*  99 */ String logFactoryName = ResourceBundleUtil.getLogFactoryClassPath();
            /*     */ try {
                /* 101 */ Class theClass = Class.forName(logFactoryName);
                /* 102 */ logFactory = (LogFactory) theClass.newInstance();
                /*     */            } catch (ClassNotFoundException ex) {
                	LogUtils.addLog(ex);
                /* 104 */ System.out.println(ex + " LogFactory class (" + logFactoryName + ") must be in class path.");
                /*     */            } catch (InstantiationException ex) {
                	LogUtils.addLog(ex);
                /* 106 */ System.out.println(ex + " LogFactory class (" + logFactoryName + ") must be concrete.");
                /*     */            } catch (IllegalAccessException ex) {
                	LogUtils.addLog(ex);
                /* 108 */ System.out.println(ex + " LogFactory class (" + logFactoryName + ") must have a no-arg constructor.");
                /*     */            }
            /*     */        }
        /* 111 */ return logFactory;
        /*     */    }
    /*     */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.WFUtil
 * JD-Core Version:    0.6.2
 */