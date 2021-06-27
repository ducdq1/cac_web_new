/*    */ package com.viettel.voffice.dojotag;
/*    */
/*    */ import java.util.HashMap;
/*    */ 
/*    */ //import net.sf.json.JSONObject;
/*    */
/*    */ public class ActionMessageImpl
        /*    */ implements ActionMessage /*    */ {
    /*    */ private String type;
    /*    */ private String content;
    /*    */ private Object fieldErrors;
    /* 26 */ private HashMap<String, Object> map = new HashMap<String, Object>();
    /*    */
    /*    */ public String toJSONContent() /*    */ {
        /* 32 */ getMap().put("type", getType());
        /* 33 */ getMap().put("content", getContent());
        /* 34 */ getMap().put("fieldErrors", getFieldErrors());
        /*    */
        /* 36 */ StringBuilder sb = new StringBuilder();
///* 37 */     JSONObject jsonObject = JSONObject.fromObject(this.map);
///* 38 */     sb.append("/* " + jsonObject.toString() + " */");
/* 39 */ return sb.toString();
        /*    */    }
    /*    */
    /*    */ public Object getFieldErrors() {
        /* 43 */ return this.fieldErrors;
        /*    */    }
    /*    */
    /*    */ public void setFieldErrors(Object fieldErrors) {
        /* 47 */ this.fieldErrors = fieldErrors;
        /*    */    }
    /*    */
    /*    */ public String getContent() {
        /* 51 */ return this.content;
        /*    */    }
    /*    */
    /*    */ public void setContent(String content) {
        /* 55 */ this.content = content;
        /*    */    }
    /*    */
    /*    */ public String getType() {
        /* 59 */ return this.type;
        /*    */    }
    /*    */
    /*    */ public void setType(String type) {
        /* 63 */ this.type = type;
        /*    */    }
    /*    */
    /*    */ public HashMap<String, Object> getMap() {
        /* 67 */ return this.map;
        /*    */    }
    /*    */
    /*    */ public void setMap(HashMap<String, Object> map) {
        /* 71 */ this.map = map;
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.dojoTag.ActionMessageImpl
 * JD-Core Version:    0.6.2
 */