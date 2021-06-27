/*    */ package com.viettel.utils;
/*    */
/*    */ import java.util.Date;
/*    */
/*    */ public class RequestMonitorStoreItem /*    */ {
    /*    */ private String actionKey;
    /*    */ private Float processTime;
    /*    */ private Date logDate;
    /*    */ private RequestMonitorConfigItem requestMonitorConfigItem;
    /*    */
    /*    */ public RequestMonitorStoreItem() /*    */ {
        /*    */    }
    /*    */
    /*    */ public RequestMonitorStoreItem(String actionKey, Float processTime, Date logDate) /*    */ {
        /* 25 */ this.processTime = processTime;
        /* 26 */ this.logDate = logDate;
        /*    */    }
    /*    */
    /*    */ public String getActionKey() {
        /* 30 */ return this.actionKey;
        /*    */    }
    /*    */
    /*    */ public void setActionKey(String actionKey) {
        /* 34 */ this.actionKey = actionKey;
        /*    */    }
    /*    */
    /*    */ public Date getLogDate() {
        /* 38 */ return this.logDate;
        /*    */    }
    /*    */
    /*    */ public void setLogDate(Date logDate) {
        /* 42 */ this.logDate = logDate;
        /*    */    }
    /*    */
    /*    */ public Float getProcessTime() {
        /* 46 */ return this.processTime;
        /*    */    }
    /*    */
    /*    */ public void setProcessTime(Float processTime) {
        /* 50 */ this.processTime = processTime;
        /*    */    }
    /*    */
    /*    */ public RequestMonitorConfigItem getRequestMonitorConfigItem() {
        /* 54 */ return this.requestMonitorConfigItem;
        /*    */    }
    /*    */
    /*    */ public void setRequestMonitorConfigItem(RequestMonitorConfigItem requestMonitorConfigItem) {
        /* 58 */ this.requestMonitorConfigItem = requestMonitorConfigItem;
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.RequestMonitorStoreItem
 * JD-Core Version:    0.6.2
 */