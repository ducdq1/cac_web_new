/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Home;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "NOTIFY")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Notify.findAll", query = "SELECT n FROM Notify n"),
		@NamedQuery(name = "Notify.findByNotifyId", query = "SELECT n FROM Notify n WHERE n.notifyId = :notifyId"),
		@NamedQuery(name = "Notify.findBySendUserName", query = "SELECT n FROM Notify n WHERE n.sendUserName = :sendUserName"),
		@NamedQuery(name = "Notify.findByObjectId", query = "SELECT n FROM Notify n WHERE n.objectId = :objectId"),
		@NamedQuery(name = "Notify.findByObjectType", query = "SELECT n FROM Notify n WHERE n.objectType = :objectType"),
		@NamedQuery(name = "Notify.findBySendUserAvatar", query = "SELECT n FROM Notify n WHERE n.sendUserAvatar = :sendUserAvatar"),
		@NamedQuery(name = "Notify.findByFunctionCode", query = "SELECT n FROM Notify n WHERE n.functionCode = :functionCode"),
		@NamedQuery(name = "Notify.findBySendTime", query = "SELECT n FROM Notify n WHERE n.sendTime = :sendTime"),
		@NamedQuery(name = "Notify.findByEndTime", query = "SELECT n FROM Notify n WHERE n.endTime = :endTime"),
		@NamedQuery(name = "Notify.findByTitle", query = "SELECT n FROM Notify n WHERE n.title = :title"),
		@NamedQuery(name = "Notify.findByContent", query = "SELECT n FROM Notify n WHERE n.content = :content"),
		@NamedQuery(name = "Notify.findByNotifyLink", query = "SELECT n FROM Notify n WHERE n.notifyLink = :notifyLink"),
		@NamedQuery(name = "Notify.findByAttachId", query = "SELECT n FROM Notify n WHERE n.attachId = :attachId"),
		@NamedQuery(name = "Notify.findByStatus", query = "SELECT n FROM Notify n WHERE n.status = :status") })
public class Notify implements Serializable {
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "NOTIFY_SEQ", sequenceName = "NOTIFY_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFY_SEQ")
	@Column(name = "NOTIFY_ID")
	@Basic(optional = false)
	@NotNull
	private Long notifyId;
	@Size(max = 500)
	@Column(name = "PARENT_NOTIFY_ID")
	private Long parentNotifyId;
	@Column(name = "USER_ID")
	private Long userId;
	@Column(name = "SEND_USER_ID")
	private Long sendUserId;
	@Column(name = "SEND_USER_NAME")
	private String sendUserName;
	@Column(name = "OBJECT_ID")
	private Long objectId;
	@Column(name = "OBJECT_TYPE")
	private Long objectType;
	@Size(max = 500)
	@Column(name = "SEND_USER_AVATAR")
	private String sendUserAvatar;
	@Size(max = 20)
	@Column(name = "FUNCTION_CODE")
	private String functionCode;
	@Column(name = "SEND_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendTime;
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	@Size(max = 500)
	@Column(name = "TITLE")
	private String title;
	@Size(max = 1000)
	@Column(name = "CONTENT")
	private String content;
	@Size(max = 500)
	@Column(name = "NOTIFY_LINK")
	private String notifyLink;
	@Column(name = "ATTACH_ID")
	private Long attachId;
	@Column(name = "STATUS")
	private Long status;
	@Column (name = "TYPE")
	private Long type;
	@Column (name = "MULTI_USER")
	private String multiUser;
	@Column (name = "MULTI_DEPT")
	private String multiDept;

	public Notify() {
	}

	public Notify(Long notifyId) {
		this.notifyId = notifyId;
	}

	public Long getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(Long notifyId) {
		this.notifyId = notifyId;
	}

	public Long getParentNotifyId() {
		return parentNotifyId;
	}

	public void setParentNotifyId(Long parentNotifyId) {
		this.parentNotifyId = parentNotifyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getSendTimeByDateStr() {
		if (sendTime == null) {
			return null;
		}
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(sendTime);
	}

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }
        
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (notifyId != null ? notifyId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Notify)) {
			return false;
		}
		Notify other = (Notify) object;
		if ((this.notifyId == null && other.notifyId != null)
				|| (this.notifyId != null && !this.notifyId
						.equals(other.notifyId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.viettel.voffice.BO.Home.Notify[ notifyId=" + notifyId
				+ " ]";
	}

}
