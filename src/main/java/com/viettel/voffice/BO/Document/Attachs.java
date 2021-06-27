/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Document;

import java.io.InputStream;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.zkoss.image.Image;

import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.FileUtil;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "ATTACHS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attachs.findAll", query = "SELECT a FROM Attachs a"),
    @NamedQuery(name = "Attachs.findByAttachId", query = "SELECT a FROM Attachs a WHERE a.attachId = :attachId"),
    @NamedQuery(name = "Attachs.findByObjectId", query = "SELECT a FROM Attachs a WHERE a.objectId = :objectId"),
    @NamedQuery(name = "Attachs.findByObjectIdAndActive", query = "SELECT a FROM Attachs a WHERE a.objectId = :objectId AND a.isActive = 1"),
    @NamedQuery(name = "Attachs.findByAttachCat", query = "SELECT a FROM Attachs a WHERE a.attachCat = :attachCat"),
    @NamedQuery(name = "Attachs.findByAttachName", query = "SELECT a FROM Attachs a WHERE a.attachName = :attachName"),
    @NamedQuery(name = "Attachs.findByAttachPath", query = "SELECT a FROM Attachs a WHERE a.attachPath = :attachPath"),
    @NamedQuery(name = "Attachs.findByAttachUrl", query = "SELECT a FROM Attachs a WHERE a.attachUrl = :attachUrl"),
    @NamedQuery(name = "Attachs.findByIsActive", query = "SELECT a FROM Attachs a WHERE a.isActive = :isActive"),
    @NamedQuery(name = "Attachs.findByVersion", query = "SELECT a FROM Attachs a WHERE a.version = :version"),
    @NamedQuery(name = "Attachs.findByCreatorId", query = "SELECT a FROM Attachs a WHERE a.creatorId = :creatorId"),
    @NamedQuery(name = "Attachs.findByModifierId", query = "SELECT a FROM Attachs a WHERE a.modifierId = :modifierId"),
    @NamedQuery(name = "Attachs.findByDateCreate", query = "SELECT a FROM Attachs a WHERE a.dateCreate = :dateCreate"),
    @NamedQuery(name = "Attachs.findByAttachType", query = "SELECT a FROM Attachs a WHERE a.attachType = :attachType"),
    @NamedQuery(name = "Attachs.findByDateModify", query = "SELECT a FROM Attachs a WHERE a.dateModify = :dateModify")})
public class Attachs implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @SequenceGenerator(name = "ATTACHS_SEQ", sequenceName = "ATTACHS_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTACHS_SEQ")
    @Basic(optional = false)
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
    private Long version;

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

    @Column(name = "ATTACH_DES")
    private String attachDes;
    @Transient
    private String creatorName;
    @Transient
    private Image content;
    @Transient
    private String modifierName;

    public Attachs() {
    }

    public Attachs(Long attachId) {
        this.attachId = attachId;
    }

    public Attachs(Long attachId, Long objectId, Long attachCat, Long isActive) {
        this.attachId = attachId;
        this.objectId = objectId;
        this.attachCat = attachCat;
        this.isActive = isActive;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
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

    public String getDateCreateStr() {
        if (dateCreate == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(dateCreate);
    }

    public String getDateModifyStr() {
        if (dateModify == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(dateModify);
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setAttachDes(String attachDes) {
        this.attachDes = attachDes;
    }

    public String getAttachDes() {
        return attachDes;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public Long getAttachType() {
        return attachType;
    }

    public void setAttachType(Long attachType) {
        this.attachType = attachType;
    }

    public String getAttachTypeName() {
        if (attachType == null) {
            return null;
        }
        String result;
        if (attachType.equals(Constants.ATTACH_TYPE.ATT_REPORT)) {
            result = "Tệp phiếu trình";
        } else if (attachType.equals(Constants.ATTACH_TYPE.ATT_DRAFT)) {
            result = "Tệp dự thảo";
        } else if (attachType.equals(Constants.ATTACH_TYPE.ATT_DOC_RELATION)) {
            result = "Tệp VB liên quan";
        } else {
            result = "";
        }
        return result;
    }

    @Override
    public Attachs clone() throws CloneNotSupportedException {
        Attachs attachs = (Attachs) super.clone();
        attachs.setAttachId(null);
        return attachs;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attachId != null ? attachId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Attachs)) {
            return false;
        }
        Attachs other = (Attachs) object;
        if ((this.attachId == null && other.attachId != null)
                || (this.attachId != null && !this.attachId
                .equals(other.attachId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Document.Attachs[ attachId=" + attachId
                + " ]";
    }

    /**
     * @return the attachCode
     */
    public String getAttachCode() {
        return attachCode;
    }

    /**
     * @param attachCode the attachCode to set
     */
    public void setAttachCode(String attachCode) {
        this.attachCode = attachCode;
    }

    public String getFullPathFile() {
        attachName = FileUtil.getSafeFileName(attachName);
        String fileNameRemoveVietnameseChar = (new BaseGenericForwardComposer()).removeVietnameseChar(attachName);
        if(attachPath.endsWith("/")){
        	return  attachPath + fileNameRemoveVietnameseChar;
        }
        String fullPathFile = attachPath + "_" + fileNameRemoveVietnameseChar;
        return fullPathFile;
    }

    //hieptq update 300615
    public String getFullPathFileOriginal() {
        attachName = FileUtil.getSafeFileName(attachName);
        String fileNameRemoveVietnameseChar = (new BaseGenericForwardComposer()).removeVietnameseChar(attachName);
        String fullPathFile = attachPath + fileNameRemoveVietnameseChar;
        return fullPathFile;
    }

    public Image getContent() {
		return content;
	}

	public void setContent(Image content) {
		this.content = content;
	}

	/**
     * @author giangnh20
     * @return
     */
    public Attachs copyAttachsWithNoId() {
        Attachs tmp = new Attachs();
        tmp.setAttachName(getAttachName());
        tmp.setAttachCat(getAttachCat());
        tmp.setAttachCode(getAttachCode());
        tmp.setAttachDes(getAttachDes());
        tmp.setAttachPath(getAttachPath());
        tmp.setAttachType(getAttachType());
        tmp.setAttachUrl(getAttachUrl());
        tmp.setCreatorId(getCreatorId());
        tmp.setCreatorName(getCreatorName());
        tmp.setModifierName(getModifierName());
        tmp.setDateCreate(getDateCreate());
        tmp.setDateModify(getDateModify());
        tmp.setIsActive(getIsActive());
        tmp.setModifierId(getModifierId());
        tmp.setModifierName(getModifierName());
        tmp.setObjectId(getObjectId());
        tmp.setVersion(getVersion());
        return tmp;
    }
}
