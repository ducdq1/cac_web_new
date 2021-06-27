/*    */ package com.viettel.utils;

/*    */
/*    */ public class RequestMonitorConfig /*    */ {
	/*    */ private static volatile Long logDuration;
	/*    */ private static volatile String defAction;
	/*    */ private static volatile String defMessage;

	/*    */
	/*    */ public static String getDefAction() /*    */ {
		/* 18 */ return defAction;
		/*    */ }

	/*    */
	/*    */ public static void setDefAction(String defAction1) {
		/* 22 */ defAction = defAction1;
		/*    */ }

	/*    */
	/*    */ public static String getDefMessage() {
		/* 26 */ return defMessage;
		/*    */ }

	/*    */
	/*    */ public static void setDefMessage(String defMessage1) {
		/* 30 */ defMessage = defMessage1;
		/*    */ }

	/*    */
	/*    */ public static Long getLogDuration() {
		/* 34 */ return logDuration;
		/*    */ }

	/*    */
	/*    */ public static void setLogDuration(Long logDuration1) {
		/* 38 */ logDuration = logDuration1;
		/*    */ }
	/*    */ }

/*
 * Location: C:\Work\RDFW 315.jar Qualified Name:
 * com.viettel.common.util.RequestMonitorConfig JD-Core Version: 0.6.2
 */