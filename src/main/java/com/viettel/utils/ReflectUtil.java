package com.viettel.utils;

import java.lang.reflect.Method;

public class ReflectUtil {

    public static Method findFirstMethod(Class source, String methodName) {
        Method result = null;
        Method[] listOfMethods = source.getMethods();
        for (int i = 0; i < listOfMethods.length; i++) {
            if (listOfMethods[i].getName().equals(methodName)) {
                result = listOfMethods[i];
                break;
            }
        }

        return result;
    }
}

/* Location:           C:\Work\RDFW 315.jar
 * Qualified Name:     com.viettel.common.util.ReflectUtil
 * JD-Core Version:    0.6.2
 */