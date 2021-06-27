/*     */ package com.viettel.voffice.dbbo;
/*     */
/*     */ import java.io.ByteArrayOutputStream;
/*     */
/*     */ public class ActionResultBO /*     */ {
    /*  17 */ private String insertActionLogDAOMethodName = null;
    /*     */
    /*  19 */ private String actionLogDAOClassName = null;
    /*     */
    /*  21 */ private Object[] insertActionLogDAOMethodArg = null;
    /*     */ private String pageForward;
    /*     */ private String className;
    /*     */ private String methodName;
    /*     */ private boolean redirect;
    /*  31 */ private ByteArrayOutputStream outPutStream = null;
    /*     */
    /*  33 */ private String contentType = null;
    /*     */
    /*  35 */ private String encoding = null;
    /*     */
    /*  37 */ private String headerArg0 = null;
    /*     */
    /*  39 */ private String headerArg1 = null;
    /*     */
    /*     */ public ActionResultBO() /*     */ {
        /*  45 */ this.className = null;
        /*  46 */ this.methodName = null;
        /*  47 */ this.redirect = true;
        /*     */    }
    /*     */
    /*     */ public String getActionLogDAOClassName() {
        /*  51 */ return this.actionLogDAOClassName;
        /*     */    }
    /*     */
    /*     */ public void setActionLogDAOClassName(String actionLogDAOClassName) {
        /*  55 */ this.actionLogDAOClassName = actionLogDAOClassName;
        /*     */    }
    /*     */
    /*     */ public Object[] getInsertActionLogDAOMethodArg() {
        /*  59 */ return this.insertActionLogDAOMethodArg;
        /*     */    }
    /*     */
    /*     */ public void setInsertActionLogDAOMethodArg(Object[] insertActionLogDAOMethodArg) {
        /*  63 */ this.insertActionLogDAOMethodArg = insertActionLogDAOMethodArg;
        /*     */    }
    /*     */
    /*     */ public String getInsertActionLogDAOMethodName() {
        /*  67 */ return this.insertActionLogDAOMethodName;
        /*     */    }
    /*     */
    /*     */ public void setInsertActionLogDAOMethodName(String insertActionLogDAOMethodName) {
        /*  71 */ this.insertActionLogDAOMethodName = insertActionLogDAOMethodName;
        /*     */    }
    /*     */
    /*     */ public String getPageForward() {
        /*  75 */ return this.pageForward;
        /*     */    }
    /*     */
    /*     */ public void setPageForward(String pageForward) {
        /*  79 */ this.pageForward = pageForward;
        /*     */    }
    /*     */
    /*     */ public String getClassName() {
        /*  83 */ return this.className;
        /*     */    }
    /*     */
    /*     */ public void setClassName(String className) {
        /*  87 */ this.className = className;
        /*     */    }
    /*     */
    /*     */ public String getMethodName() {
        /*  91 */ return this.methodName;
        /*     */    }
    /*     */
    /*     */ public void setMethodName(String methodName) {
        /*  95 */ this.methodName = methodName;
        /*     */    }
    /*     */
    /*     */ public boolean isRedirect() {
        /*  99 */ return this.redirect;
        /*     */    }
    /*     */
    /*     */ public void setRedirect(boolean redirect) {
        /* 103 */ this.redirect = redirect;
        /*     */    }
    /*     */
    /*     */ public ByteArrayOutputStream getOutPutStream() {
        /* 107 */ return this.outPutStream;
        /*     */    }
    /*     */
    /*     */ public void setOutPutStream(ByteArrayOutputStream outPutStream) {
        /* 111 */ this.outPutStream = outPutStream;
        /*     */    }
    /*     */
    /*     */ public String getContentType() {
        /* 115 */ return this.contentType;
        /*     */    }
    /*     */
    /*     */ public void setContentType(String contentType) {
        /* 119 */ this.contentType = contentType;
        /*     */    }
    /*     */
    /*     */ public String getEncoding() {
        /* 123 */ return this.encoding;
        /*     */    }
    /*     */
    /*     */ public void setEncoding(String encoding) {
        /* 127 */ this.encoding = encoding;
        /*     */    }
    /*     */
    /*     */ public String getHeaderArg0() {
        /* 131 */ return this.headerArg0;
        /*     */    }
    /*     */
    /*     */ public void setHeaderArg0(String headerArg0) {
        /* 135 */ this.headerArg0 = headerArg0;
        /*     */    }
    /*     */
    /*     */ public String getHeaderArg1() {
        /* 139 */ return this.headerArg1;
        /*     */    }
    /*     */
    /*     */ public void setHeaderArg1(String headerArg1) {
        /* 143 */ this.headerArg1 = headerArg1;
        /*     */    }
    /*     */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.database.BO.ActionResultBO
 * JD-Core Version:    0.6.2
 */