/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model.exporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.zkoss.zk.ui.Component;

import com.viettel.core.base.DAO.AttachDAO;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author ChucHV
 */
public class Utils {

    public static String getAlign(Component cmp) {
        return (String) invokeComponentGetter(cmp, "getAlign");
    }

    public static Object invokeComponentGetter(Component target, String... methods) {
        Class<? extends Component> cls = target.getClass();
        for (String methodName : methods) {
            try {
                Method method = cls.getMethod(methodName, (Class<?>[]) null);
                Object ret = method.invoke(target, (Object[]) null);
                if (ret != null) {
                    return ret;
                }
            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            	LogUtils.addLog(e);
            }
        }
        return null;
    }  
    //Gom ky tu chu so va dau "( ) + -"
    public static boolean validatePhoneNumber(String phoneNo) {
    	
    	if (phoneNo.matches("^[0-9[()]+-]{1,31}$")) return true;
    	else return false;
    	 
    	 
//		//validate phone numbers of format "1234567890"
//		if (phoneNo.matches("\\d{10}")) return true;
//		//validating phone number with -, . or spaces
//		else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
//		//validating phone number with extension length from 3 to 5
//		else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
//		//validating phone number where area code is in braces ()
//		else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
//		//return false if nothing matches the input
//		else return false;
		
	}
    
    /**
	 * check co cong van sua doi bs khong
	 *
	 * @param fileId
	 * @return
	 */
	public static int checkAddition(Long fileId) {
		if (fileId == null) {
			return Constants.CHECK_VIEW.NOT_VIEW; // Khong hien thi
		}
		AttachDAO dao = new AttachDAO();
		int check = dao.checkAttachFiles(fileId, Constants.OBJECT_TYPE.PHAMARCY_SDBS_DISPATH);
		if (check == 0) {
			check = dao.checkAttachFiles(fileId, Constants.OBJECT_TYPE.PHAMARCY_SDBS_DISPATH_VT);
		}
		return check;
	}

}

