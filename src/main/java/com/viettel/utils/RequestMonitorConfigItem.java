/*    */ package com.viettel.utils;
/*    */
/*    */ public class RequestMonitorConfigItem /*    */ {
    /*    */ private String actionName;
    /*    */ private String className;
    /*    */ private String methodName;
    /*    */ private Long threshold;
    /*    */ private Long concurrent;
    /*    */ private String descriptiton;
    /*    */ private String message;
    /*    */
    /*    */ public RequestMonitorConfigItem() /*    */ {
        /*    */    }
    /*    */
    /*    */ public RequestMonitorConfigItem(String actionName, String className, String methodName, Long threshold, String descriptiton, String message) /*    */ {
        /* 25 */ this.actionName = actionName;
        /* 26 */ this.className = className;
        /* 27 */ this.methodName = methodName;
        /* 28 */ this.threshold = threshold;
        /* 29 */ this.concurrent = Long.valueOf(0L);
        /* 30 */ this.descriptiton = descriptiton;
        /* 31 */ this.message = message;
        /*    */    }
    /*    */
    /*    */ public String getActionName() {
        /* 35 */ return this.actionName;
        /*    */    }
    /*    */
    /*    */ public void setActionName(String actionName) {
        /* 39 */ this.actionName = actionName;
        /*    */    }
    /*    */
    /*    */ public String getClassName() {
        /* 43 */ return this.className;
        /*    */    }
    /*    */
    /*    */ public void setClassName(String className) {
        /* 47 */ this.className = className;
        /*    */    }
    /*    */
    /*    */ public String getDescriptiton() {
        /* 51 */ return this.descriptiton;
        /*    */    }
    /*    */
    /*    */ public void setDescriptiton(String descriptiton) {
        /* 55 */ this.descriptiton = descriptiton;
        /*    */    }
    /*    */
    /*    */ public String getMessage() {
        /* 59 */ return this.message;
        /*    */    }
    /*    */
    /*    */ public void setMessage(String message) {
        /* 63 */ this.message = message;
        /*    */    }
    /*    */
    /*    */ public String getMethodName() {
        /* 67 */ return this.methodName;
        /*    */    }
    /*    */
    /*    */ public void setMethodName(String methodName) {
        /* 71 */ this.methodName = methodName;
        /*    */    }
    /*    */
    /*    */ public Long getThreshold() {
        /* 75 */ return this.threshold;
        /*    */    }
    /*    */
    /*    */ public void setThreshold(Long threshold) {
        /* 79 */ this.threshold = threshold;
        /*    */    }
    /*    */
    /*    */ public Long getConcurrent() {
        /* 83 */ return this.concurrent;
        /*    */    }
    /*    */
    /*    */ public void setConcurrent(Long concurrent) {
        /* 87 */ this.concurrent = concurrent;
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.RequestMonitorConfigItem
 * JD-Core Version:    0.6.2
 */