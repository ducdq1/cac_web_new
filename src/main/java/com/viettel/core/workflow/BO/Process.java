/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.BO;

import com.viettel.utils.DateTimeUtils;
import com.viettel.voffice.BO.Home.Notify;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "PROCESS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Process.findAll", query = "SELECT p FROM Process p"),
    @NamedQuery(name = "Process.findByProcessId", query = "SELECT p FROM Process p WHERE p.processId = :processId"),
    @NamedQuery(name = "Process.findByObjectId", query = "SELECT p FROM Process p WHERE p.objectId = :objectId"),
    @NamedQuery(name = "Process.findByObjectType", query = "SELECT p FROM Process p WHERE p.objectType = :objectType"),
    @NamedQuery(name = "Process.findBySendUserId", query = "SELECT p FROM Process p WHERE p.sendUserId = :sendUserId"),
    @NamedQuery(name = "Process.findBySendUser", query = "SELECT p FROM Process p WHERE p.sendUser = :sendUser"),
    @NamedQuery(name = "Process.findBySendGroupId", query = "SELECT p FROM Process p WHERE p.sendGroupId = :sendGroupId"),
    @NamedQuery(name = "Process.findBySendGroup", query = "SELECT p FROM Process p WHERE p.sendGroup = :sendGroup"),
    @NamedQuery(name = "Process.findBySendDate", query = "SELECT p FROM Process p WHERE p.sendDate = :sendDate"),
    @NamedQuery(name = "Process.findByReceiveUserId", query = "SELECT p FROM Process p WHERE p.receiveUserId = :receiveUserId"),
    @NamedQuery(name = "Process.findByReceiveUser", query = "SELECT p FROM Process p WHERE p.receiveUser = :receiveUser"),
    @NamedQuery(name = "Process.findByReceiveGroupId", query = "SELECT p FROM Process p WHERE p.receiveGroupId = :receiveGroupId"),
    @NamedQuery(name = "Process.findByReceiveGroup", query = "SELECT p FROM Process p WHERE p.receiveGroup = :receiveGroup"),
    @NamedQuery(name = "Process.findByProcessType", query = "SELECT p FROM Process p WHERE p.processType = :processType"),
    @NamedQuery(name = "Process.findByOrderProcess", query = "SELECT p FROM Process p WHERE p.orderProcess = :orderProcess"),
    @NamedQuery(name = "Process.findByStatus", query = "SELECT p FROM Process p WHERE p.status = :status"),
    @NamedQuery(name = "Process.findByDeadline", query = "SELECT p FROM Process p WHERE p.deadline = :deadline"),
    @NamedQuery(name = "Process.findByDeadlineNumber", query = "SELECT p FROM Process p WHERE p.deadlineNumber = :deadlineNumber"),
    @NamedQuery(name = "Process.findByIsNotifyByEmail", query = "SELECT p FROM Process p WHERE p.isNotifyByEmail = :isNotifyByEmail"),
    @NamedQuery(name = "Process.findByIsNotifyByMessage", query = "SELECT p FROM Process p WHERE p.isNotifyByMessage = :isNotifyByMessage"),
    @NamedQuery(name = "Process.findByNote", query = "SELECT p FROM Process p WHERE p.note = :note"),
    @NamedQuery(name = "Process.findByIsActive", query = "SELECT p FROM Process p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "Process.findByNodeId", query = "SELECT p FROM Process p WHERE p.nodeId = :nodeId")})
public class Process implements Serializable, Cloneable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1725206240602311331L;
	@SequenceGenerator(name = "PROCESS_SEQ", sequenceName = "PROCESS_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESS_SEQ")
    @Column(name = "PROCESS_ID")
    private Long processId;
    @Column(name = "OBJECT_ID")
    private Long objectId;
    @Column(name = "OBJECT_TYPE")
    private Long objectType;
    @Column(name = "SEND_USER_ID")
    private Long sendUserId;
    @Size(max = 1000)
    @Column(name = "SEND_USER")
    private String sendUser;
    @Column(name = "SEND_GROUP_ID")
    private Long sendGroupId;
    @Size(max = 1000)
    @Column(name = "SEND_GROUP")
    private String sendGroup;
    @Column(name = "SEND_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
    @Column(name = "RECEIVE_USER_ID")
    private Long receiveUserId;
    @Column(name = "RECEIVE_USER")
    private String receiveUser;
    @Column(name = "RECEIVE_GROUP_ID")
    private Long receiveGroupId;
    @Size(max = 1000)
    @Column(name = "RECEIVE_GROUP")
    private String receiveGroup;
    @Column(name = "PROCESS_TYPE")
    private Long processType;
    @Column(name = "ORDER_PROCESS")
    private Long orderProcess;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "DEADLINE")
    @Temporal(TemporalType.DATE)
    private Date deadline;
    @Column(name = "DEADLINE_NUMBER")
    private Long deadlineNumber;
    @Column(name = "IS_NOTIFY_BY_EMAIL")
    private Long isNotifyByEmail;
    @Column(name = "IS_NOTIFY_BY_MESSAGE")
    private Long isNotifyByMessage;
    @Size(max = 4000)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "NODE_ID")
    private Long nodeId;
    @Column(name = "FINISH_DATE")
    @Temporal(TemporalType.DATE)
    private Date finishDate;
    @Column(name = "ACTION_TYPE")
    private Long actionType;
    @Column(name = "ACTION_NAME")
    private String actionName;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "PREVIOUS_NODE_ID")
    private Long previousNodeId;
    //@Column(name = "ROOT_PROCESS_TYPE")
    //private Long rootProcessType;// Loại xử lý gốc, ngay khi văn bản được tạo và
    // chuyển.
    @Column(name = "FIRST_PROCESS_TYPE")
    private Long firstProcessType;

    @Transient
    private Notify notify;

    @Transient
    private String positionName;

    public Process() {
    }

    public Process(Long processId) {
        this.processId = processId;
    }

    public Process(Long processId, Long objectId, Long objectType,
            Long processType, Long receiveUserId) {
        this.processId = processId;
        this.objectId = objectId;
        this.objectType = objectType;
        this.processType = processType;
        this.receiveUserId = receiveUserId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Long getObjectType() {
        return objectType;
    }

    public void setObjectType(Long objectType) {
        this.objectType = objectType;
    }

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public Long getSendGroupId() {
        return sendGroupId;
    }

    public void setSendGroupId(Long sendGroupId) {
        this.sendGroupId = sendGroupId;
    }

    public String getSendGroup() {
        return sendGroup;
    }

    public void setSendGroup(String sendGroup) {
        this.sendGroup = sendGroup;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public String getSendDateString() {
        if (sendDate == null) {
            return null;
        }
        return DateTimeUtils.convertDateTimeToString(sendDate);
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Long getReceiveGroupId() {
        return receiveGroupId;
    }

    public void setReceiveGroupId(Long receiveGroupId) {
        this.receiveGroupId = receiveGroupId;
    }

    public String getReceiveGroup() {
        return receiveGroup;
    }

    public void setReceiveGroup(String receiveGroup) {
        this.receiveGroup = receiveGroup;
    }

    public Long getProcessType() {
        return processType;
    }

    public void setProcessType(Long processType) {
        this.processType = processType;
    }

    public Long getOrderProcess() {
        return orderProcess;
    }

    public void setOrderProcess(Long orderProcess) {
        this.orderProcess = orderProcess;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Long getDeadlineNumber() {
        return deadlineNumber;
    }

    public void setDeadlineNumber(Long deadlineNumber) {
        this.deadlineNumber = deadlineNumber;
    }

    public Long getIsNotifyByEmail() {
        return isNotifyByEmail;
    }

    public void setIsNotifyByEmail(Long isNotifyByEmail) {
        this.isNotifyByEmail = isNotifyByEmail;
    }

    public Long getIsNotifyByMessage() {
        return isNotifyByMessage;
    }

    public void setIsNotifyByMessage(Long isNotifyByMessage) {
        this.isNotifyByMessage = isNotifyByMessage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Notify getNotify() {
        return notify;
    }

    public void setNotify(Notify notify) {
        this.notify = notify;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    
    /*
    public Long getRootProcessType() {
        return rootProcessType;
    }
    
    public void setRootProcessType(Long rootProcessType) {
        this.rootProcessType = rootProcessType;
    }
    */
    public Long getFirstProcessType() {
        return firstProcessType;
    }

    public void setFirstProcessType(Long firstProcessType) {
        this.firstProcessType = firstProcessType;
    }

    @Override
    public Process clone() throws CloneNotSupportedException {
        Process process = (Process) super.clone();
        return process;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (processId != null ? processId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Process)) {
            return false;
        }
        Process other = (Process) object;
        if ((this.processId == null && other.processId != null)
                || (this.processId != null && !this.processId
                .equals(other.processId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Document.Process[ processId="
                + processId + " ]";
    }

    /**
     * @return the previousNodeId
     */
    public Long getPreviousNodeId() {
        return previousNodeId;
    }

    /**
     * @param previousNodeId the previousNodeId to set
     */
    public void setPreviousNodeId(Long previousNodeId) {
        this.previousNodeId = previousNodeId;
    }

}
