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
@Table(name = "V_CONFIRM_PHA_FILE_ADS_MEDIA_2")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "VConfirmPhaFileAdsMedia2.findAll", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByAttachId", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.attachId = :attachId"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByObjectId", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.objectId = :objectId"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByAttachCat", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.attachCat = :attachCat"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByAttachName", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.attachName = :attachName"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByAttachPath", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.attachPath = :attachPath"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByAttachUrl", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.attachUrl = :attachUrl"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByIsActive", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.isActive = :isActive"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByVersion", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.version = :version"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByCreatorId", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.creatorId = :creatorId"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByModifierId", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.modifierId = :modifierId"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByDateCreate", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.dateCreate = :dateCreate"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByDateModify", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.dateModify = :dateModify"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByAttachType", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.attachType = :attachType"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByAttachCode", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.attachCode = :attachCode"),
		@NamedQuery(name = "VConfirmPhaFileAdsMedia2.findByTypeFileName", query = "SELECT v FROM VConfirmPhaFileAdsMedia2 v WHERE v.typeFileName = :typeFileName") })
public class VConfirmPhaFileAdsMedia2 implements Serializable {
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

	public VConfirmPhaFileAdsMedia2() {
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
