/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.viettel.core.sys.BO.ActionLog;
import com.viettel.voffice.BO.Calendar.Calendar;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.user.BO.Department;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.user.BO.Roles;
import com.viettel.core.user.BO.Users;
import com.viettel.core.sys.DAO.ActionLogDAOHE;
import com.viettel.voffice.DAOHE.Calendar.CalendarDAOHE;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.voffice.DAOHE.DocumentDAOHE;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.user.DAO.RolesDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import java.util.Date;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.util.Clients;

/**
 *
 * @author HaVM2
 */
public class LogUtils {

    private static Logger logger = Logger.getLogger("");

    public static void addLog(String description) {
        LogStruct log = new LogStruct();
        log.className = Thread.currentThread().getStackTrace()[2].getClassName();
        log.lineOfCode = Thread.currentThread().getStackTrace()[2].getLineNumber();
        log.methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.description = description;
        logger.debug(log);
    }

    public static void addLog(Exception en) {
        LogStruct log = new LogStruct();
        log.className = Thread.currentThread().getStackTrace()[2].getClassName();
        log.lineOfCode = Thread.currentThread().getStackTrace()[2].getLineNumber();
        log.methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.description = en.getMessage();
        logger.debug(log);
    }

   

    public static void addLog(Long deptId, Long userId, String userName,
            Long actionType, String actionName, Long modun, Long objectId,
            Long objectType, String objectTitle, String ip, Object obj) {
        ActionLog log = new ActionLog();
        log.setUserId(userId);
        log.setUserName(userName);
        log.setActionType(actionType);
        log.setActionName(actionName);
        log.setModun(modun);
        log.setObjectId(objectId);
        log.setObjectType(objectType);
        log.setObjectTitle(objectTitle);
        log.setActionDate(new Date());
        log.setIp(ip);
        if (obj != null) {
            GsonBuilder b = new GsonBuilder();
            b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson = b.create();
            //Gson gson = new Gson();
            String json = gson.toJson(obj);
            log.setOldData(json);
        }
        ActionLogDAOHE adhe = new ActionLogDAOHE();
        adhe.saveOrUpdate(log);
    }

    //
    // Khoi phuc lai log
    //
    public static void undoLog(Long logId) {
        ActionLogDAOHE ldhe = new ActionLogDAOHE();
        ActionLog log = ldhe.findById(logId);
        if (log == null) {
            return;
        }

        if (!log.getActionType().equals(Constants.ACTION.TYPE.UPDATE) && !log.getActionType().equals(Constants.ACTION.TYPE.DELETE)) {
            Clients.showNotification("Chỉ có thể khôi phục được các hành động chỉnh sửa, xóa");
            return;
        }

        if (log.getOldData() == null) {
            Clients.showNotification("Không thể undo được");
            return;
        }

        try {
            Gson gson = new Gson();

            if (log.getObjectType().equals(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE)) {
                DocumentReceive obj = gson.fromJson(log.getOldData(), DocumentReceive.class);
                DocumentReceiveDAOHE dhe = new DocumentReceiveDAOHE();
                dhe.update(obj);
            } else if (log.getObjectType().equals(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH)) {
                DocumentPublish obj = gson.fromJson(log.getOldData(), DocumentPublish.class);
                DocumentDAOHE dhe = new DocumentDAOHE();
                dhe.update(obj);
            } else if (log.getObjectType().equals(Constants.OBJECT_TYPE.FILES)) {
//                Files obj = gson.fromJson(log.getOldData(), Files.class);
//                FilesDAOHE dhe = new FilesDAOHE();
//                dhe.update(obj);
            } else if (log.getObjectType().equals(Constants.OBJECT_TYPE.CALENDAR)) {
                Calendar obj = gson.fromJson(log.getOldData(), Calendar.class);
                CalendarDAOHE dhe = new CalendarDAOHE();
                dhe.update(obj);
            } else if (log.getObjectType().equals(Constants.OBJECT_TYPE.USER)) {
                Users obj = gson.fromJson(log.getOldData(), Users.class);
                UserDAOHE dhe = new UserDAOHE();
                dhe.update(obj);
            } else if (log.getObjectType().equals(Constants.OBJECT_TYPE.DEPT)) {
                Department obj = gson.fromJson(log.getOldData(), Department.class);
                DepartmentDAOHE dhe = new DepartmentDAOHE();
                dhe.update(obj);
            } else if (log.getObjectType().equals(Constants.OBJECT_TYPE.CATEGORY)) {
                Category obj = gson.fromJson(log.getOldData(), Category.class);
                CategoryDAOHE dhe = new CategoryDAOHE();
                dhe.update(obj);
            } else if (log.getObjectType().equals(Constants.OBJECT_TYPE.ROLE)) {
                Roles obj = gson.fromJson(log.getOldData(), Roles.class);
                RolesDAOHE dhe = new RolesDAOHE();
                dhe.update(obj);
            } else if (log.getObjectType().equals(Constants.OBJECT_TYPE.FLOW)) {
                Flow obj = gson.fromJson(log.getOldData(), Flow.class);
                FlowDAOHE dhe = new FlowDAOHE();
                dhe.update(obj);
            }
            //
            // undo xong thi phai xoa di
            //
            ldhe.delete(log);

        } catch (JsonParseException en) {
            addLog(en);
            Clients.showNotification("Undo bị lỗi");
        }
    }
}

class LogStruct {

    String className;
    int lineOfCode;
    String methodName;
    String description;
}
