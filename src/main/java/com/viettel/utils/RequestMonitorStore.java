/*    */ package com.viettel.utils;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ import java.util.Set;
/*    */
/*    */ public class RequestMonitorStore /*    */ {
    /* 20 */ private static HashMap<String, RequestMonitorStoreItem> store = new HashMap<String, RequestMonitorStoreItem>();
    /* 21 */ private static HashMap<String, RequestMonitorStoreItem> overloadStore = new HashMap<String, RequestMonitorStoreItem>();
    /*    */
    /*    */ public static void pushToStore(RequestMonitorStoreItem item) {
        /* 24 */ store.put(item.getActionKey(), item);
        /*    */    }
    /*    */
    /*    */ public static void clearStore() {
        /* 28 */ store.clear();
        /*    */    }
    /*    */
    /*    */ public static HashMap<String, RequestMonitorStoreItem> getStore() {
        /* 32 */ return store;
        /*    */    }
    /*    */
    /*    */ public static void pushToOverloadStore(RequestMonitorStoreItem item) {
        /* 36 */ overloadStore.put(item.getActionKey(), item);
        /*    */    }
    /*    */
    /*    */ public static void clearOverloadStore() {
        /* 40 */ overloadStore.clear();
        /*    */    }
    /*    */
    /*    */ public static HashMap<String, RequestMonitorStoreItem> getOverloadStore() {
        /* 44 */ return overloadStore;
        /*    */    }
    /*    */
    /*    */ public static void showStoreToConsole(String type) {
        /* 48 */ String outputStr = "";
        /*    */
        /* 51 */ HashMap<String, RequestMonitorStoreItem> storeToShow ;
        /*    */
        /* 53 */ if ("overload".equalsIgnoreCase(type)) /* 54 */ {
            storeToShow = overloadStore;
        } /*    */ else {
            /* 56 */ storeToShow = store;
            /*    */        }
        /*    */
        /* 59 */ Set actionKeySet = storeToShow.keySet();
        /* 60 */ Iterator actionKeyIterator = actionKeySet.iterator();
        /* 61 */ while (actionKeyIterator.hasNext()) {
            /* 62 */ RequestMonitorStoreItem RMSItem = (RequestMonitorStoreItem) storeToShow.get((String) actionKeyIterator.next());
            /* 63 */ RequestMonitorConfigItem RMCItem = RMSItem.getRequestMonitorConfigItem();
            /* 64 */ outputStr = outputStr + "RMSI-[Action: " + RMSItem.getActionKey() + "]-[C_Request: " + RMCItem.getConcurrent() + "]-[R_Threshold: " + RMCItem.getThreshold() + "]-[P_Time: " + RMSItem.getProcessTime() + "]-[L_Date: " + RMSItem.getLogDate() + "]\n";
            /*    */        }
        /*    */
        /* 67 */ System.out.println("----s:RequestMonitorStore " + type + "Log----\n" + outputStr + "----e:RequestMonitorStore " + type + "Log----");
        /*    */    }
    /*    */ }

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.RequestMonitorStore
 * JD-Core Version:    0.6.2
 */