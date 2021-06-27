/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.voffice.model;

import java.util.Date;

/**
 *
 * @author huantn1
 */
public class NotifyForm {
    private Long parentNotifyId;
	private Long sendUserId;
	private String sendUserName;
	private Long objectId;
	private Long objectType;
	private String sendUserAvatar;
	private String functionCode;
	private Date sendTime;
	private Date endTime;
	private String title;
	private String content;
	private String notifyLink;
	private Long attachId;
	private Long status;
	private Long type;
	private String multiUser;
	private String multiDept;

    public NotifyForm() {

    }

    public Long getParentNotifyId() {
        return parentNotifyId;
    }

    public void setParentNotifyId(Long parentNotifyId) {
        this.parentNotifyId = parentNotifyId;
    }

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
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

    public String getSendUserAvatar() {
        return sendUserAvatar;
    }

    public void setSendUserAvatar(String sendUserAvatar) {
        this.sendUserAvatar = sendUserAvatar;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotifyLink() {
        return notifyLink;
    }

    public void setNotifyLink(String notifyLink) {
        this.notifyLink = notifyLink;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getMultiUser() {
        return multiUser;
    }

    public void setMultiUser(String multiUser) {
        this.multiUser = multiUser;
    }

    public String getMultiDept() {
        return multiDept;
    }

    public void setMultiDept(String multiDept) {
        this.multiDept = multiDept;
    }
    
}
