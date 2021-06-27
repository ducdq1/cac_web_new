/*     */ package com.viettel.utils;
/*     */
/*     */ import java.util.Collection;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */
/*     */ public final class ArgChecker /*     */ {
    /*  22 */ private static Log log = LogFactory.getLog(ArgChecker.class);
    /*     */
    /*     */ public static void denyNull(Object arg0) /*     */ {
        /*  36 */ denyNull(arg0, ConstConfig.NOT_NULL, ConstConfig.NOT_NULL);
        /*     */    }
    /*     */
    /*     */ public static void denyNull(Object arg0, Object arg1) /*     */ {
        /*  47 */ denyNull(arg0, arg1, ConstConfig.NOT_NULL);
        /*     */    }
    /*     */
    /*     */ public static void denyNull(Object arg0, Object arg1, Object arg2) /*     */ {
        /*  59 */ if ((arg0 == null) || (arg1 == null) || (arg2 == null)) /*  60 */ {
            deny("Null value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyBlank(String arg0) /*     */ {
        /*  71 */ denyBlank(arg0, "A", "A");
        /*     */    }
    /*     */
    /*     */ public static void denyBlank(String arg0, String arg1) /*     */ {
        /*  83 */ denyBlank(arg0, arg1, "A");
        /*     */    }
    /*     */
    /*     */ public static void denyBlank(String arg0, String arg1, String arg2) /*     */ {
        /*  96 */ if ((StringUtils.isBlank(arg0)) || (StringUtils.isBlank(arg1)) || (StringUtils.isBlank(arg2))) /*  97 */ {
            deny("Blank value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(String arg0) /*     */ {
        /* 108 */ denyEmpty(arg0, "A", "A");
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(String arg0, String arg1) /*     */ {
        /* 120 */ denyEmpty(arg0, arg1, "A");
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(String arg0, String arg1, String arg2) /*     */ {
        /* 133 */ if ((StringUtils.isEmpty(arg0)) || (StringUtils.isEmpty(arg1)) || (StringUtils.isEmpty(arg2))) /* 134 */ {
            deny("Empty value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(Collection arg0) /*     */ {
        /* 145 */ denyNull(arg0);
        /* 146 */ if (arg0.size() == 0) /* 147 */ {
            deny("Empty value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(Object[] arg0) /*     */ {
        /* 158 */ denyNull(arg0);
        /* 159 */ if (arg0.length == 0) /* 160 */ {
            deny("Empty value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(short[] arg0) /*     */ {
        /* 171 */ denyNull(arg0);
        /* 172 */ if (arg0.length == 0) /* 173 */ {
            deny("Empty value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(int[] arg0) /*     */ {
        /* 184 */ denyNull(arg0);
        /* 185 */ if (arg0.length == 0) /* 186 */ {
            deny("Empty value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(long[] arg0) /*     */ {
        /* 197 */ denyNull(arg0);
        /* 198 */ if (arg0.length == 0) /* 199 */ {
            deny("Empty value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(float[] arg0) /*     */ {
        /* 210 */ denyNull(arg0);
        /* 211 */ if (arg0.length == 0) /* 212 */ {
            deny("Empty value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyEmpty(double[] arg0) /*     */ {
        /* 223 */ denyNull(arg0);
        /* 224 */ if (arg0.length == 0) /* 225 */ {
            deny("Empty value is denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyNotEquals(Object arg0, Object arg1) /*     */ {
        /* 238 */ if ((arg0 == null) && (arg1 == null)) {
            /* 239 */ return;
            /*     */        }
        /* 241 */ if ((arg0 == null) || (arg1 == null) || (!arg1.equals(arg0))) /* 242 */ {
            deny("Different values are denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyNotEquals(boolean arg0, boolean arg1) /*     */ {
        /* 254 */ if (arg0 != arg1) /* 255 */ {
            deny("Different values are denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyNotEquals(short arg0, short arg1) /*     */ {
        /* 267 */ if (arg0 != arg1) /* 268 */ {
            deny("Different values are denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyNotEquals(int arg0, int arg1) /*     */ {
        /* 280 */ if (arg0 != arg1) /* 281 */ {
            deny("Different values are denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyNotEquals(long arg0, long arg1) /*     */ {
        /* 293 */ if (arg0 != arg1) /* 294 */ {
            deny("Different values are denied");
        }
        /*     */    }
    /*     */
    /*     */ public static void denyNotAssignableFrom(Class arg0, Class arg1) /*     */ {
        /* 310 */ if ((arg0 == null) || (arg1 == null) || (!arg0.isAssignableFrom(arg1))) /* 311 */ {
            deny("Not assignable from");
        }
        /*     */    }
    /*     */
    /*     */ private static void deny(String denyMsg) /*     */ {
        /* 322 */ log.error("Illegal Argument : " + denyMsg);
        /* 323 */ throw new IllegalArgumentException("Illegal Argument : " + denyMsg);
        /*     */    }
    /*     */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.ArgChecker
 * JD-Core Version:    0.6.2
 */