/*    */ package com.viettel.utils;
/*    */
/*    */ //import com.opensymphony.xwork2.util.ValueStack;
/*    */ import javax.servlet.jsp.PageContext;
/*    */ //import org.apache.struts2.views.jsp.TagUtils;
/*    */
/*    */ public class VTTagUtil /*    */ {
    /*    */ public static String getMessageByKey(PageContext pageContext, String key, String replacement) /*    */ {
///* 30 */     String msgContent = (String)TagUtils.getStack(pageContext).findValue("getText('" + key + "')", String.class);
///* 31 */     if (key.equalsIgnoreCase(msgContent))
///*    */     {
///* 33 */       msgContent = key;
///*    */     }
/* 35 */ return "";
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.VTTagUtil
 * JD-Core Version:    0.6.2
 */