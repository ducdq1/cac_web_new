/*
 * To change this template, choose Tools | Templates
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
 * @author HaVM2
 */
@Entity
@Table(name = "SEND_SHEET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SendSheet.findAll", query = "SELECT s FROM SendSheet s"),
    @NamedQuery(name = "SendSheet.findBySendSheetId", query = "SELECT s FROM SendSheet s WHERE s.sendSheetId = :sendSheetId"),
    @NamedQuery(name = "SendSheet.findBySendDate", query = "SELECT s FROM SendSheet s WHERE s.sendDate = :sendDate"),
    @NamedQuery(name = "SendSheet.findByTitle", query = "SELECT s FROM SendSheet s WHERE s.title = :title"),
    @NamedQuery(name = "SendSheet.findBySummary", query = "SELECT s FROM SendSheet s WHERE s.summary = :summary"),
    @NamedQuery(name = "SendSheet.findBySuggest", query = "SELECT s FROM SendSheet s WHERE s.suggest = :suggest"),
    @NamedQuery(name = "SendSheet.findByCreatorDeptName", query = "SELECT s FROM SendSheet s WHERE s.creatorDeptName = :creatorDeptName"),
    @NamedQuery(name = "SendSheet.findByCreatorUserName", query = "SELECT s FROM SendSheet s WHERE s.creatorUserName = :creatorUserName")})
public class SendSheet implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "SEND_SHEET_SEQ", sequenceName = "SEND_SHEET_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEND_SHEET_SEQ")
    @Column(name = "SEND_SHEET_ID")
    private Long sendSheetId;
    @Column(name = "SEND_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
    @Size(max = 500)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 1000)
    @Column(name = "SUMMARY")
    private String summary;
    @Size(max = 1000)
    @Column(name = "SUGGEST")
    private String suggest;
    @Size(max = 500)
    @Column(name = "CREATOR_DEPT_NAME")
    private String creatorDeptName;
    @Size(max = 500)
    @Column(name = "CREATOR_USER_NAME")
    private String creatorUserName;
    @Column(name = "DOCUMENT_PUBLISH_ID")
    private Long documentPublishId;
    @Column(name = "FILE_ID")
    private Long fileId;
    @Column(name = "MANAGER_NAME")
    private String managerName;
    @Column(name = "MANAGER_ID")
    private Long managerId;
    @Column(name = "CHIEF_NAME")
    private String chiefName;
    @Column(name = "CHIEF_ID")
    private Long chiefId;
    @Column(name = "APPROVE_CONTENT")
    private String approveContent;
    

    public SendSheet() {
    }

    public SendSheet(Long sendSheetId) {
        this.sendSheetId = sendSheetId;
    }

    public Long getSendSheetId() {
        return sendSheetId;
    }

    public void setSendSheetId(Long sendSheetId) {
        this.sendSheetId = sendSheetId;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getCreatorDeptName() {
        return creatorDeptName;
    }

    public void setCreatorDeptName(String creatorDeptName) {
        this.creatorDeptName = creatorDeptName;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public Long getDocumentPublishId() {
        return documentPublishId;
    }

    public void setDocumentPublishId(Long documentPublishId) {
        this.documentPublishId = documentPublishId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getChiefName() {
        return chiefName;
    }

    public void setChiefName(String chiefName) {
        this.chiefName = chiefName;
    }

    public Long getChiefId() {
        return chiefId;
    }

    public void setChiefId(Long chiefId) {
        this.chiefId = chiefId;
    }

    public String getApproveContent() {
        return approveContent;
    }

    public void setApproveContent(String approveContent) {
        this.approveContent = approveContent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sendSheetId != null ? sendSheetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SendSheet)) {
            return false;
        }
        SendSheet other = (SendSheet) object;
        if ((this.sendSheetId == null && other.sendSheetId != null) || (this.sendSheetId != null && !this.sendSheetId.equals(other.sendSheetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Document.SendSheet[ sendSheetId=" + sendSheetId + " ]";
    }
    
}
