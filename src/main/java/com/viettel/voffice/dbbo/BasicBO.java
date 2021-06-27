/*    */ package com.viettel.voffice.dbbo;
/*    */
/*    */ import java.io.Serializable;

import com.viettel.utils.LogUtils;
/*    */
/*    */ public abstract class BasicBO
        /*    */ implements Serializable, Cloneable /*    */ {
    /*    */ private static final long serialVersionUID = 1L;
    /*    */
    /*    */ public Object clone() /*    */ {
        /*    */ try /*    */ {
            /* 22 */ return super.clone();
            /*    */        } catch (CloneNotSupportedException e) {
		             LogUtils.addLog(e);
            /* 24 */ throw new InternalError(e.toString());
            /*    */        }
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.database.BO.BasicBO
 * JD-Core Version:    0.6.2
 */