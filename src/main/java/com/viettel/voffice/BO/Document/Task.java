/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.voffice.BO.Document;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thanhvt10
 */
@Entity
@Table(name = "TASK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findByTaskId", query = "SELECT t FROM Task t WHERE t.taskId = :taskId"),
    @NamedQuery(name = "Task.findByProgressPercent", query = "SELECT t FROM Task t WHERE t.progressPercent = :progressPercent"),
    @NamedQuery(name = "Task.findByTaskName", query = "SELECT t FROM Task t WHERE t.taskName = :taskName"),
    @NamedQuery(name = "Task.findByStartTime", query = "SELECT t FROM Task t WHERE t.startTime = :startTime"),
    @NamedQuery(name = "Task.findByDeadline", query = "SELECT t FROM Task t WHERE t.deadline = :deadline"),
    @NamedQuery(name = "Task.findByCompleteDate", query = "SELECT t FROM Task t WHERE t.completeDate = :completeDate"),
    @NamedQuery(name = "Task.findByDescription", query = "SELECT t FROM Task t WHERE t.description = :description"),
    @NamedQuery(name = "Task.findByTaskParentId", query = "SELECT t FROM Task t WHERE t.taskParentId = :taskParentId"),
    @NamedQuery(name = "Task.findByTaskParentName", query = "SELECT t FROM Task t WHERE t.taskParentName = :taskParentName"),
    @NamedQuery(name = "Task.findByTaskPriorityId", query = "SELECT t FROM Task t WHERE t.taskPriorityId = :taskPriorityId"),
    @NamedQuery(name = "Task.findByTaskPriorityName", query = "SELECT t FROM Task t WHERE t.taskPriorityName = :taskPriorityName"),
    @NamedQuery(name = "Task.findByTaskType", query = "SELECT t FROM Task t WHERE t.taskType = :taskType"),
    @NamedQuery(name = "Task.findByTaskTypeName", query = "SELECT t FROM Task t WHERE t.taskTypeName = :taskTypeName"),
    @NamedQuery(name = "Task.findByCreateDate", query = "SELECT t FROM Task t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "Task.findByUserCreateId", query = "SELECT t FROM Task t WHERE t.userCreateId = :userCreateId"),
    @NamedQuery(name = "Task.findByUserCreateName", query = "SELECT t FROM Task t WHERE t.userCreateName = :userCreateName"),
    @NamedQuery(name = "Task.findByUserGroupId", query = "SELECT t FROM Task t WHERE t.userGroupId = :userGroupId"),
    @NamedQuery(name = "Task.findByUserGroupName", query = "SELECT t FROM Task t WHERE t.userGroupName = :userGroupName"),
    @NamedQuery(name = "Task.findByIsActive", query = "SELECT t FROM Task t WHERE t.isActive = :isActive"),
    @NamedQuery(name = "Task.findByStatus", query = "SELECT t FROM Task t WHERE t.status = :status"),
    @NamedQuery(name = "Task.findByPath", query = "SELECT t FROM Task t WHERE t.path = :path"),
    @NamedQuery(name = "Task.findByUserPerformId", query = "SELECT t FROM Task t WHERE t.userPerformId = :userPerformId"),
    @NamedQuery(name = "Task.findByUserPerformName", query = "SELECT t FROM Task t WHERE t.userPerformName = :userPerformName"),
    @NamedQuery(name = "Task.findByUsersHelpId", query = "SELECT t FROM Task t WHERE t.usersHelpId = :usersHelpId"),
    @NamedQuery(name = "Task.findByUserHeptName", query = "SELECT t FROM Task t WHERE t.userHeptName = :userHeptName"),
    @NamedQuery(name = "Task.findByUpdateDate", query = "SELECT t FROM Task t WHERE t.updateDate = :updateDate")})
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "TASK_SEQ", sequenceName = "TASK_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_SEQ")
    @Column(name = "TASK_ID")
    private Long taskId;
    @Column(name = "PROGRESS_PERCENT")
    private Long progressPercent;
    @Size(max = 1000)
    @Column(name = "TASK_NAME")
    private String taskName;
    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "DEADLINE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;
    @Column(name = "COMPLETE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completeDate;
    @Size(max = 3000)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TASK_PARENT_ID")
    private Long taskParentId;
    @Size(max = 3000)
    @Column(name = "TASK_PARENT_NAME")
    private String taskParentName;
    @Column(name = "TASK_PRIORITY_ID")
    private Long taskPriorityId;
    @Size(max = 3000)
    @Column(name = "TASK_PRIORITY_NAME")
    private String taskPriorityName;
    @Column(name = "TASK_TYPE")
    private Long taskType;
    @Size(max = 300)
    @Column(name = "TASK_TYPE_NAME")
    private String taskTypeName;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "USER_CREATE_ID")
    private Long userCreateId;
    @Size(max = 1000)
    @Column(name = "USER_CREATE_NAME")
    private String userCreateName;
    @Column(name = "USER_GROUP_ID")
    private Long userGroupId;
    @Size(max = 1000)
    @Column(name = "USER_GROUP_NAME")
    private String userGroupName;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "STATUS")
    private Long status;
    @Size(max = 1000)
    @Column(name = "STATUS_STR")
    private String statusStr;
    @Size(max = 1000)
    @Column(name = "PATH")
    private String path;
    @Column(name = "USER_PERFORM_ID")
    private Long userPerformId;
    @Size(max = 1000)
    @Column(name = "USER_PERFORM_NAME")
    private String userPerformName;
    @Column(name = "USERS_HELP_ID")
    private String usersHelpId;
    @Size(max = 3000)
    @Column(name = "USER_HEPT_NAME")
    private String userHeptName;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    private transient String startTimeStr;
    private transient String deadlineStr;
    private transient String createDateStr;

    public Task() {
    }

    public Task(Long taskId) {
        this.taskId = taskId;
    }
    
    public Task(Long taskId, String taskName){
        this.taskId = taskId;
        this.taskName = taskName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Long progressPercent) {
        this.progressPercent = progressPercent;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTaskParentId() {
        return taskParentId;
    }

    public void setTaskParentId(Long taskParentId) {
        this.taskParentId = taskParentId;
    }

    public String getTaskParentName() {
        return taskParentName;
    }

    public void setTaskParentName(String taskParentName) {
        this.taskParentName = taskParentName;
    }

    public Long getTaskPriorityId() {
        return taskPriorityId;
    }

    public void setTaskPriorityId(Long taskPriorityId) {
        this.taskPriorityId = taskPriorityId;
    }

    public String getTaskPriorityName() {
        return taskPriorityName;
    }

    public void setTaskPriorityName(String taskPriorityName) {
        this.taskPriorityName = taskPriorityName;
    }

    public Long getTaskType() {
        return taskType;
    }

    public void setTaskType(Long taskType) {
        this.taskType = taskType;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUserCreateId() {
        return userCreateId;
    }

    public void setUserCreateId(Long userCreateId) {
        this.userCreateId = userCreateId;
    }

    public String getUserCreateName() {
        return userCreateName;
    }

    public void setUserCreateName(String userCreateName) {
        this.userCreateName = userCreateName;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getUserPerformId() {
        return userPerformId;
    }

    public void setUserPerformId(Long userPerformId) {
        this.userPerformId = userPerformId;
    }

    public String getUserPerformName() {
        return userPerformName;
    }

    public void setUserPerformName(String userPerformName) {
        this.userPerformName = userPerformName;
    }

    public String getUsersHelpId() {
        return usersHelpId;
    }

    public void setUsersHelpId(String usersHelpId) {
        this.usersHelpId = usersHelpId;
    }

    public String getUserHeptName() {
        return userHeptName;
    }

    public void setUserHeptName(String userHeptName) {
        this.userHeptName = userHeptName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getDeadlineStr() {
        return deadlineStr;
    }

    public void setDeadlineStr(String deadlineStr) {
        this.deadlineStr = deadlineStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskId != null ? taskId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.taskId == null && other.taskId != null) || (this.taskId != null && !this.taskId.equals(other.taskId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Document.Task[ taskId=" + taskId + " ]";
    }
    
}
