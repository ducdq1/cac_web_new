/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
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

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "V_CONFIRM_PHA_FILE_OTHER")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "VConfirmPhaFileOther.findAll", query = "SELECT v FROM VConfirmPhaFileOther v"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByAttachId", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.attachId = :attachId"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByObjectId", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.objectId = :objectId"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByAttachCat", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.attachCat = :attachCat"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByAttachName", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.attachName = :attachName"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByAttachPath", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.attachPath = :attachPath"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByAttachUrl", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.attachUrl = :attachUrl"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByIsActive", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.isActive = :isActive"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByVersion", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.version = :version"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByCreatorId", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.creatorId = :creatorId"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByModifierId", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.modifierId = :modifierId"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByDateCreate", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.dateCreate = :dateCreate"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByDateModify", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.dateModify = :dateModify"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByAttachType", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.attachType = :attachType"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByAttachCode", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.attachCode = :attachCode"),
		@NamedQuery(name = "VConfirmPhaFileOther.findByTypeFileName", query = "SELECT v FROM VConfirmPhaFileOther v WHERE v.typeFileName = :typeFileName") })
public class VConfirmPhaFileOther implements Serializable {
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Id
	@Column(name = "ATTACH_ID")
	private Long attachId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "OBJECT_ID")
	private Long objectId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ATTACH_CAT")
	private Long attachCat;
	@Size(max = 500)
	@Column(name = "ATTACH_NAME")
	private String attachName;
	@Size(max = 500)
	@Column(name = "ATTACH_PATH")
	private String attachPath;
	@Size(max = 500)
	@Column(name = "ATTACH_URL")
	private String attachUrl;
	@Basic(optional = false)
	@NotNull
	@Column(name = "IS_ACTIVE")
	private Long isActive;
	@Column(name = "VERSION")
	private Integer version;
	@Column(name = "CREATOR_ID")
	private Long creatorId;
	@Column(name = "MODIFIER_ID")
	private Long modifierId;
	@Column(name = "DATE_CREATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreate;
	@Column(name = "DATE_MODIFY")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateModify;
	@Column(name = "ATTACH_TYPE")
	private Long attachType;
	@Size(max = 500)
	@Column(name = "ATTACH_CODE")
	private String attachCode;
	@Size(max = 400)
	@Column(name = "TYPE_FILE_NAME")
	private String typeFileName;

	public VConfirmPhaFileOther() {
	}

	public Long getAttachId() {
		return attachId;
	}

	public void setAttachId(Long attachId) {
		this.attachId = attachId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getAttachCat() {
		return attachCat;
	}

	public void setAttachCat(Long attachCat) {
		this.attachCat = attachCat;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public String getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

	public String getAttachUrl() {
		return attachUrl;
	}

	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getModifierId() {
		return modifierId;
	}

	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getDateModify() {
		return dateModify;
	}

	public void setDateModify(Date dateModify) {
		this.dateModify = dateModify;
	}

	public Long getAttachType() {
		return attachType;
	}

	public void setAttachType(Long attachType) {
		this.attachType = attachType;
	}

	public String getAttachCode() {
		return attachCode;
	}

	public void setAttachCode(String attachCode) {
		this.attachCode = attachCode;
	}

	public String getTypeFileName() {
		return typeFileName;
	}

	public void setTypeFileName(String typeFileName) {
		this.typeFileName = typeFileName;
	}

}
