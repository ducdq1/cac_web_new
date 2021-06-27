/*     */ package com.viettel.utils.log;
/*     */
/*     */ public abstract interface Logger /*     */ {
    /*  83 */ public static final EventType SECURITY_SUCCESS = new EventType("SECURITY SUCCESS", true);
    /*     */
    /*  88 */ public static final EventType SECURITY_FAILURE = new EventType("SECURITY FAILURE", false);
    /*     */
    /*  93 */ public static final EventType EVENT_SUCCESS = new EventType("EVENT SUCCESS", true);
    /*     */
    /*  98 */ public static final EventType EVENT_FAILURE = new EventType("EVENT FAILURE", false);
    /*     */ public static final int OFF = 2147483647;
    /*     */ public static final int FATAL = 1000;
    /*     */ public static final int ERROR = 800;
    /*     */ public static final int WARNING = 600;
    /*     */ public static final int INFO = 400;
    /*     */ public static final int DEBUG = 200;
    /*     */ public static final int TRACE = 100;
    /*     */ public static final int ALL = -2147483648;
    /*     */
    /*     */ public abstract void setLevel(int paramInt);
    /*     */
    /*     */ public abstract void fatal(String paramString);
    /*     */
    /*     */ public abstract void fatal(String paramString, Throwable paramThrowable);
    /*     */
    /*     */ public abstract boolean isFatalEnabled();
    /*     */
    /*     */ public abstract void error(String paramString);
    /*     */
    /*     */ public abstract void error(String paramString, Throwable paramThrowable);
    /*     */
    /*     */ public abstract boolean isErrorEnabled();
    /*     */
    /*     */ public abstract void warn(String paramString);
    /*     */
    /*     */ public abstract void warn(String paramString, Throwable paramThrowable);
    /*     */
    /*     */ public abstract boolean isWarningEnabled();
    /*     */
    /*     */ public abstract void info(String paramString);
    /*     */
    /*     */ public abstract void info(String paramString, Throwable paramThrowable);
    /*     */
    /*     */ public abstract boolean isInfoEnabled();
    /*     */
    /*     */ public abstract void debug(String paramString);
    /*     */
    /*     */ public abstract void debug(String paramString, Throwable paramThrowable);
    /*     */
    /*     */ public abstract boolean isDebugEnabled();
    /*     */
    /*     */ public abstract void trace(String paramString);
    /*     */
    /*     */ public abstract void trace(String paramString, Throwable paramThrowable);
    /*     */
    /*     */ public abstract boolean isTraceEnabled();
    /*     */
    /*     */ public static class EventType /*     */ {
        /*     */ private String type;
        /*     */ private boolean success;
        /*     */
        /*     */ EventType(String name, boolean newSuccess) /*     */ {
            /* 113 */ this.type = name;
            /* 114 */ this.success = newSuccess;
            /*     */        }
        /*     */
        /*     */ public boolean isSuccess() {
            /* 118 */ return this.success;
            /*     */        }
        /*     */
        /*     */ public String toString() /*     */ {
            /* 127 */ return this.type;
            /*     */        }
        /*     */    }
    /*     */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.log.Logger
 * JD-Core Version:    0.6.2
 */