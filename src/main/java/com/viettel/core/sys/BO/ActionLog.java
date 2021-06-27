/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.core.sys.BO;

import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "ACTION_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActionLog.findAll", query = "SELECT a FROM ActionLog a"),
    @NamedQuery(name = "ActionLog.findByActionLogId", query = "SELECT a FROM ActionLog a WHERE a.actionLogId = :actionLogId"),
    @NamedQuery(name = "ActionLog.findByUserId", query = "SELECT a FROM ActionLog a WHERE a.userId = :userId"),
    @NamedQuery(name = "ActionLog.findByUserName", query = "SELECT a FROM ActionLog a WHERE a.userName = :userName"),
    @NamedQuery(name = "ActionLog.findByActionName", query = "SELECT a FROM ActionLog a WHERE a.actionName = :actionName"),
    @NamedQuery(name = "ActionLog.findByModun", query = "SELECT a FROM ActionLog a WHERE a.modun = :modun"),
    @NamedQuery(name = "ActionLog.findByObjectId", query = "SELECT a FROM ActionLog a WHERE a.objectId = :objectId"),
    @NamedQuery(name = "ActionLog.findByObjectType", query = "SELECT a FROM ActionLog a WHERE a.objectType = :objectType")})
public class ActionLog implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "ACTION_LOG_SEQ", sequenceName = "ACTION_LOG_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTION_LOG_SEQ")
    @Column(name = "ACTION_LOG_ID")
    private Long actionLogId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USER_ID")
    private Long userId;
    @Size(max = 500)
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Size(max = 500)
    @Column(name = "ACTION_NAME")
    private String actionName;
    @Column(name = "ACTION_TYPE")
    private Long actionType;
    @Column(name = "MODUN")
    private Long modun;
    @Column(name = "OBJECT_ID")
    private Long objectId;
    @Column(name = "OBJECT_TITLE")
    private String objectTitle;
    @Column(name = "OBJECT_TYPE")
    private Long objectType;
    @Column(name = "IP")
    private String ip;
    @Column(name = "ACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;    
    @Column(name = "OLD_DATA")
    private String oldData;

    public ActionLog() {
    }

    public ActionLog(Long actionLogId) {
        this.actionLogId = actionLogId;
    }

    public ActionLog(Long actionLogId, Long userId) {
        this.actionLogId = actionLogId;
        this.userId = userId;
    }

    public Long getActionLogId() {
        return actionLogId;
    }

    public void setActionLogId(Long actionLogId) {
        this.actionLogId = actionLogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }
    
    public String getActionTypeStr(){
//            public static final Long INSERT = 0l;
//            public static final Long UPDATE = 1l;
//            public static final Long DELETE = 2l;
//            public static final Long APPROVE = 3l;
//            public static final Long REJECT = 4l;
//            public static final Long VIEW = 5l;
//            public static final Long TRANSFER = 6l;
//            public static final Long LOCK = 7l;
//            public static final Long UNLOCK = 8l;
//            public static final Long LOGIN = 9l;
//            public static final Long LOGOUT = 10l;
        String str = "";
        if(actionType.equals(Constants.ACTION.TYPE.INSERT)){
            str= Constants.ACTION.NAME.INSERT;
        } else if(modun.equals(Constants.ACTION.TYPE.UPDATE)){
            str= Constants.ACTION.NAME.UPDATE;
        } else if(modun.equals(Constants.ACTION.TYPE.DELETE)){
            str= Constants.ACTION.NAME.DELETE;
        } else if(modun.equals(Constants.ACTION.TYPE.APPROVE)){
            str= Constants.ACTION.NAME.APPROVE;
        } else if(modun.equals(Constants.ACTION.TYPE.REJECT)){
            str= Constants.ACTION.NAME.REJECT;
        } else if(modun.equals(Constants.ACTION.TYPE.VIEW)){
            str= Constants.ACTION.NAME.INSERT;
        } else if(modun.equals(Constants.ACTION.TYPE.TRANSFER)){
            str= Constants.ACTION.NAME.TRANSFER;
        } else if(modun.equals(Constants.ACTION.TYPE.LOCK)){
            str= Constants.ACTION.NAME.LOCK;
        } else if(modun.equals(Constants.ACTION.TYPE.UNLOCK)){
            str= Constants.ACTION.NAME.UNLOCK;
        } else if(modun.equals(Constants.ACTION.TYPE.LOGIN)){
            str= Constants.ACTION.NAME.LOGIN;
        } else if(modun.equals(Constants.ACTION.TYPE.LOGOUT)){
            str= Constants.ACTION.NAME.LOGOUT;
        } 
        return str;
        
    }

    public Long getModun() {
        return modun;
    }

    public String getModunStr() {
        String modunStr = "";
        if(modun.equals(Constants.OBJECT_TYPE.DOCUMENT_RECEIVE)){
            modunStr= "Văn bản đến";
        } else if(modun.equals(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH)){
            modunStr= "Văn bản đi";    
        } else if(modun.equals(Constants.OBJECT_TYPE.DOCUMENT_REF)){
            modunStr= "Văn bản tham khảo";    
        } else if(modun.equals(Constants.OBJECT_TYPE.PROFILE_WORK)){
            modunStr= "Hồ sơ công việc";    
        } else if(modun.equals(Constants.OBJECT_TYPE.FORM)){
            modunStr= "Phiếu yêu cầu";    
        } else if(modun.equals(Constants.OBJECT_TYPE.FILES)){
            modunStr= "Hồ sơ";    
        } else if(modun.equals(Constants.OBJECT_TYPE.CALENDAR)){
            modunStr= "Lịch";    
        } else if(modun.equals(Constants.OBJECT_TYPE.NOTIFY)){
            modunStr= "Thông báo";    
        } else if(modun.equals(Constants.OBJECT_TYPE.LOG_IN)){
            modunStr= "Đăng nhập";    
        } else if(modun.equals(Constants.OBJECT_TYPE.LOG_OUT)){
            modunStr= "Đăng xuất";    
        } else if(modun.equals(Constants.OBJECT_TYPE.USER)){
            modunStr= "Người dùng";    
        } else if(modun.equals(Constants.OBJECT_TYPE.DEPT)){
            modunStr= "Đơn vị";    
        } else if(modun.equals(Constants.OBJECT_TYPE.CATEGORY)){
            modunStr= "Danh mục";    
        } else if(modun.equals(Constants.OBJECT_TYPE.ROLE)){
            modunStr= "Vai trò";    
        }
        return modunStr;
    }

    public void setModun(Long modun) {
        this.modun = modun;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getObjectTitle() {
        return objectTitle;
    }

    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    public Long getObjectType() {
        return objectType;
    }

    public void setObjectType(Long objectType) {
        this.objectType = objectType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public String getActionDateStr() {
        if(actionDate == null)
            return "";
        else 
            return DateTimeUtils.convertDateTimeToString(actionDate);
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionLogId != null ? actionLogId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionLog)) {
            return false;
        }
        ActionLog other = (ActionLog) object;
        if ((this.actionLogId == null && other.actionLogId != null) || (this.actionLogId != null && !this.actionLogId.equals(other.actionLogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.ActionLog[ actionLogId=" + actionLogId + " ]";
    }
    
}
