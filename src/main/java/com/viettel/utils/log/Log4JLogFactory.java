/*     */ package com.viettel.utils.log;
/*     */ import java.util.HashMap;

/*     */ import org.apache.log4j.Level;

/*     */
/*     */ import com.viettel.utils.WFUtil;
/*     */ //import viettel.passport.client.UserToken;
/*     */ //import viettel.passport.util.Connector;
/*     */
/*     */ public class Log4JLogFactory
        /*     */ implements LogFactory /*     */ {
    /*  23 */ private HashMap loggersMap = new HashMap<String, Object>();
    /*     */
    /*     */ public Logger getLogger(Class clazz) /*     */ {
        /*  38 */ Logger classLogger = (Logger) this.loggersMap.get(clazz);
        /*     */
        /*  40 */ if (classLogger == null) {
            /*  41 */       //classLogger = new Log4JLogger(clazz.getName(), null);
/*  42 */ this.loggersMap.put(clazz, classLogger);
            /*     */        }
        /*  44 */ return classLogger;
        /*     */    }
    /*     */
    /*     */ public Logger getLogger(String moduleName) /*     */ {
        /*  52 */ Logger moduleLogger = (Logger) this.loggersMap.get(moduleName);
        /*     */
        /*  54 */ if (moduleLogger == null) {
            /*  55 */       //moduleLogger = new Log4JLogger(moduleName, null);
/*  56 */ this.loggersMap.put(moduleName, moduleLogger);
            /*     */        }
        /*  58 */ return moduleLogger;
        /*     */    }
    /*     */
    /*     */ private static class Log4JLogger
            /*     */ implements Logger /*     */ {
        /*  75 */ private org.apache.log4j.Logger jlogger = null;
        /*     */
        /*  77 */ private String moduleName = null;
        /*     */
        /*  79 */     //private String applicationName = Connector.domainCode;
/*     */
        /*  82 */ private static boolean logAppName = false;
        /*     */
        /*  85 */ private static boolean logServerIP = true;
        /*     */
        /*     */ private Log4JLogger(String moduleName) /*     */ {
            /*  94 */ this.moduleName = moduleName;
            /*  95 */       //this.jlogger = org.apache.log4j.Logger.getLogger(this.applicationName + ":" + moduleName);
/*     */        }
        /*     */
        /*     */ public void setLevel(int level) /*     */ {
            /*     */ try /*     */ {
                /* 106 */ this.jlogger.setLevel(convertESAPILeveltoLoggerLevel(level));
                /*     */            } catch (IllegalArgumentException e) {
                /* 108 */ error("", e);
                /*     */            }
            /*     */        }
        /*     */
        /*     */ private static Level convertESAPILeveltoLoggerLevel(int level) /*     */ {
            /* 119 */ switch (level) {
                /*     */ case 2147483647:
                    /* 121 */ return Level.OFF;
                /*     */ case 1000:
                    /* 123 */ return Level.FATAL;
                /*     */ case 800:
                    /* 125 */ return Level.ERROR;
                /*     */ case 600:
                    /* 127 */ return Level.WARN;
                /*     */ case 400:
                    /* 129 */ return Level.INFO;
                /*     */ case 200:
                    /* 131 */ return Level.DEBUG;
                /*     */ case 100:
                    /* 133 */ return Level.TRACE;
                /*     */ case -2147483648:
                    /* 135 */ return Level.ALL;
                /*     */            }
            /* 137 */ throw new IllegalArgumentException("Invalid logging level. Value was: " + level);
            /*     */        }
        /*     */
        /*     */ public void trace(String message, Throwable throwable) /*     */ {
            /* 145 */ log(Level.TRACE, message, throwable);
            /*     */        }
        /*     */
        /*     */ public void trace(String message) /*     */ {
            /* 152 */ log(Level.TRACE, message, null);
            /*     */        }
        /*     */
        /*     */ public void debug(String message, Throwable throwable) /*     */ {
            /* 159 */ log(Level.DEBUG, message, throwable);
            /*     */        }
        /*     */
        /*     */ public void debug(String message) /*     */ {
            /* 166 */ log(Level.DEBUG, message, null);
            /*     */        }
        /*     */
        /*     */ public void info(String message) /*     */ {
            /* 173 */ log(Level.INFO, message, null);
            /*     */        }
        /*     */
        /*     */ public void info(String message, Throwable throwable) /*     */ {
            /* 180 */ log(Level.INFO, message, throwable);
            /*     */        }
        /*     */
        /*     */ public void warn(String message, Throwable throwable) /*     */ {
            /* 187 */ log(Level.WARN, message, throwable);
            /*     */        }
        /*     */
        /*     */ public void warn(String message) /*     */ {
            /* 194 */ log(Level.WARN, message, null);
            /*     */        }
        /*     */
        /*     */ public void error(String message, Throwable throwable) /*     */ {
            /* 201 */ log(Level.ERROR, message, throwable);
            /*     */        }
        /*     */
        /*     */ public void error(String message) /*     */ {
            /* 208 */ log(Level.ERROR, message, null);
            /*     */        }
        /*     */
        /*     */ public void fatal(String message, Throwable throwable) /*     */ {
            /* 215 */ log(Level.FATAL, message, throwable);
            /*     */        }
        /*     */
        /*     */ public void fatal(String message) /*     */ {
            /* 222 */ log(Level.FATAL, message, null);
            /*     */        }
        /*     */
        /*     */ private void log(Level level, String message, Throwable throwable) /*     */ {
            /* 242 */ if (!this.jlogger.isEnabledFor(level)) {
                /* 243 */ return;
                /*     */            }
            /*     */
            /*     */
            /* 251 */ if (message == null) {
                /* 252 */ message = "";
                /*     */            }
            /*     */
            /* 256 */ String clean = message.replace('\n', '_').replace('\r', '_');
            /*     */
            /* 259 */       //UserToken user = (UserToken)request.getSession().getAttribute("vsaUserToken");
/* 260 */ String userInfo = "";
///* 261 */       if ((user != null) && (WFUtil.currentRequest() != null)) {
///* 262 */         Object temp = (String)WFUtil.currentRequest().getSession().getAttribute("VTS-IP");
///* 263 */         String remoteAddr = temp == null ? WFUtil.currentRequest().getRemoteAddr() : (String)temp;
///* 264 */         temp = (String)WFUtil.currentRequest().getSession().getAttribute("VTS-MAC");
///* 265 */         String macAddr = temp == null ? "unknow mac" : (String)temp;
///* 266 */         userInfo = String.format("%s@%s-%s", new Object[] { user.getUserName(), remoteAddr, macAddr });
///*     */       }
/*     */
            /* 270 */ StringBuilder appInfo = new StringBuilder();
            /* 271 */ if ((WFUtil.currentRequest() != null) && (logServerIP)) {
                /* 272 */ appInfo.append(WFUtil.currentRequest().getServerName() + ":" + WFUtil.currentRequest().getServerPort());
                /*     */            }
            /* 274 */ if (logAppName) {
                /* 275 */         //appInfo.append("/" + this.applicationName);
/*     */            }
            /* 277 */ appInfo.append("/" + this.moduleName);
            /*     */
            /* 280 */ this.jlogger.log(level, "[" + userInfo + " -> " + appInfo + "] " + clean, throwable);
            /*     */        }
        /*     */
        /*     */ public boolean isDebugEnabled() /*     */ {
            /* 287 */ return this.jlogger.isEnabledFor(Level.DEBUG);
            /*     */        }
        /*     */
        /*     */ public boolean isErrorEnabled() /*     */ {
            /* 294 */ return this.jlogger.isEnabledFor(Level.ERROR);
            /*     */        }
        /*     */
        /*     */ public boolean isFatalEnabled() /*     */ {
            /* 301 */ return this.jlogger.isEnabledFor(Level.FATAL);
            /*     */        }
        /*     */
        /*     */ public boolean isInfoEnabled() /*     */ {
            /* 308 */ return this.jlogger.isEnabledFor(Level.INFO);
            /*     */        }
        /*     */
        /*     */ public boolean isTraceEnabled() /*     */ {
            /* 315 */ return this.jlogger.isEnabledFor(Level.TRACE);
            /*     */        }
        /*     */
        /*     */ public boolean isWarningEnabled() /*     */ {
            /* 322 */ return this.jlogger.isEnabledFor(Level.WARN);
            /*     */        }
        /*     */    }
    /*     */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.log.Log4JLogFactory
 * JD-Core Version:    0.6.2
 */