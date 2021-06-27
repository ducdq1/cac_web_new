/*    */ package com.viettel.voffice.dbbo;
/*    */
/*    */ import com.viettel.utils.DateTimeUtils;
/*    */ import java.util.Date;
/*    */
/*    */ public class DateTimeRule /*    */ {
    /*    */ private Date startDate;
    /*    */ private Date endDate;
    /*    */ private String sessionName;
    /*    */
    /*    */ public Date getEndDate() /*    */ {
        /* 25 */ return this.endDate;
        /*    */    }
    /*    */
    /*    */ public void setEndDate(Date endDate) {
        /* 29 */ this.endDate = endDate;
        /*    */    }
    /*    */
    /*    */ public void setEndDate(String endDate)
            /*    */ throws Exception /*    */ {
        /* 37 */ this.endDate = DateTimeUtils.convertStringToDate(endDate);
        /*    */    }
    /*    */
    /*    */ public String getSessionName() {
        /* 41 */ return this.sessionName;
        /*    */    }
    /*    */
    /*    */ public void setSessionName(String sessionName) {
        /* 45 */ this.sessionName = sessionName;
        /*    */    }
    /*    */
    /*    */ public Date getStartDate() {
        /* 49 */ return this.startDate;
        /*    */    }
    /*    */
    /*    */ public void setStartDate(Date startDate) {
        /* 53 */ this.startDate = startDate;
        /*    */    }
    /*    */
    /*    */ public void setStartDate(String startDate)
            /*    */ throws Exception /*    */ {
        /* 61 */ this.startDate = DateTimeUtils.convertStringToDate(startDate);
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.database.BO.DateTimeRule
 * JD-Core Version:    0.6.2
 */