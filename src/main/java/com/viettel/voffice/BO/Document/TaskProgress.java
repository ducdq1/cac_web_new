/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.voffice.BO.Document;

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
 * @author thanhvt10
 */
@Entity
@Table(name = "TASK_PROGRESS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TaskProgress.findAll", query = "SELECT t FROM TaskProgress t"),
    @NamedQuery(name = "TaskProgress.findByProgressId", query = "SELECT t FROM TaskProgress t WHERE t.progressId = :progressId"),
    @NamedQuery(name = "TaskProgress.findByTaskId", query = "SELECT t FROM TaskProgress t WHERE t.taskId = :taskId"),
    @NamedQuery(name = "TaskProgress.findByUserCreateName", query = "SELECT t FROM TaskProgress t WHERE t.userCreateName = :userCreateName"),
    @NamedQuery(name = "TaskProgress.findByUserCreateId", query = "SELECT t FROM TaskProgress t WHERE t.userCreateId = :userCreateId"),
    @NamedQuery(name = "TaskProgress.findByUserGroupCreateId", query = "SELECT t FROM TaskProgress t WHERE t.userGroupCreateId = :userGroupCreateId"),
    @NamedQuery(name = "TaskProgress.findByUserGroupCreateName", query = "SELECT t FROM TaskProgress t WHERE t.userGroupCreateName = :userGroupCreateName"),
    @NamedQuery(name = "TaskProgress.findByCreateDate", query = "SELECT t FROM TaskProgress t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TaskProgress.findByCommentTask", query = "SELECT t FROM TaskProgress t WHERE t.commentTask = :commentTask"),
    @NamedQuery(name = "TaskProgress.findByStatus", query = "SELECT t FROM TaskProgress t WHERE t.status = :status"),
    @NamedQuery(name = "TaskProgress.findByProgressPercent", query = "SELECT t FROM TaskProgress t WHERE t.progressPercent = :progressPercent"),
    @NamedQuery(name = "TaskProgress.findByNextStageId", query = "SELECT t FROM TaskProgress t WHERE t.nextStageId = :nextStageId"),
    @NamedQuery(name = "TaskProgress.findByNextStage", query = "SELECT t FROM TaskProgress t WHERE t.nextStage = :nextStage"),
    @NamedQuery(name = "TaskProgress.findByUpdateDate", query = "SELECT t FROM TaskProgress t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TaskProgress.findByIsActive", query = "SELECT t FROM TaskProgress t WHERE t.isActive = :isActive")})
public class TaskProgress implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "TASK_PROGRESS_SEQ", sequenceName = "TASK_PROGRESS_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_PROGRESS_SEQ")
    @Column(name = "PROGRESS_ID")
    private Long progressId;
    @Column(name = "TASK_ID")
    private Long taskId;
    @Size(max = 1000)
    @Column(name = "USER_CREATE_NAME")
    private String userCreateName;
    @Column(name = "USER_CREATE_ID")
    private Long userCreateId;
    @Column(name = "USER_GROUP_CREATE_ID")
    private Long userGroupCreateId;
    @Size(max = 1000)
    @Column(name = "USER_GROUP_CREATE_NAME")
    private String userGroupCreateName;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 3000)
    @Column(name = "COMMENT_TASK")
    private String commentTask;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "PROGRESS_PERCENT")
    private Long progressPercent;
    @Column(name = "NEXT_STAGE_ID")
    private Long nextStageId;
    @Size(max = 1000)
    @Column(name = "NEXT_STAGE")
    private String nextStage;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "IS_ACTIVE")
    private Long isActive;

    public TaskProgress() {
    }

    public TaskProgress(Long progressId) {
        this.progressId = progressId;
    }

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getUserCreateName() {
        return userCreateName;
    }

    public void setUserCreateName(String userCreateName) {
        this.userCreateName = userCreateName;
    }

    public Long getUserCreateId() {
        return userCreateId;
    }

    public void setUserCreateId(Long userCreateId) {
        this.userCreateId = userCreateId;
    }

    public Long getUserGroupCreateId() {
        return userGroupCreateId;
    }

    public void setUserGroupCreateId(Long userGroupCreateId) {
        this.userGroupCreateId = userGroupCreateId;
    }

    public String getUserGroupCreateName() {
        return userGroupCreateName;
    }

    public void setUserGroupCreateName(String userGroupCreateName) {
        this.userGroupCreateName = userGroupCreateName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCommentTask() {
        return commentTask;
    }

    public void setCommentTask(String commentTask) {
        this.commentTask = commentTask;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(Long progressPercent) {
        this.progressPercent = progressPercent;
    }

    public Long getNextStageId() {
        return nextStageId;
    }

    public void setNextStageId(Long nextStageId) {
        this.nextStageId = nextStageId;
    }

    public String getNextStage() {
        return nextStage;
    }

    public void setNextStage(String nextStage) {
        this.nextStage = nextStage;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (progressId != null ? progressId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaskProgress)) {
            return false;
        }
        TaskProgress other = (TaskProgress) object;
        if ((this.progressId == null && other.progressId != null) || (this.progressId != null && !this.progressId.equals(other.progressId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Document.TaskProgress[ progressId=" + progressId + " ]";
    }
    
}
