/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Document;

import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
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
@Table(name = "DOCUMENT_RECEIVE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentReceive.findAll", query = "SELECT d FROM DocumentReceive d"),
    @NamedQuery(name = "DocumentReceive.findByDocumentReceiveId", query = "SELECT d FROM DocumentReceive d WHERE d.documentReceiveId = :documentReceiveId"),
    @NamedQuery(name = "DocumentReceive.findByReceiveDate", query = "SELECT d FROM DocumentReceive d WHERE d.receiveDate = :receiveDate"),
    @NamedQuery(name = "DocumentReceive.findByDocumentCode", query = "SELECT d FROM DocumentReceive d WHERE d.documentCode = :documentCode"),
    @NamedQuery(name = "DocumentReceive.findByPublishAgencyName", query = "SELECT d FROM DocumentReceive d WHERE d.publishAgencyName = :publishAgencyName"),
    @NamedQuery(name = "DocumentReceive.findByPublishAgencyIds", query = "SELECT d FROM DocumentReceive d WHERE d.publishAgencyIds = :publishAgencyIds"),
    @NamedQuery(name = "DocumentReceive.findBySigner", query = "SELECT d FROM DocumentReceive d WHERE d.signer = :signer"),
    @NamedQuery(name = "DocumentReceive.findByPublishDate", query = "SELECT d FROM DocumentReceive d WHERE d.publishDate = :publishDate"),
    @NamedQuery(name = "DocumentReceive.findByDocumentAbstract", query = "SELECT d FROM DocumentReceive d WHERE d.documentAbstract = :documentAbstract"),
    @NamedQuery(name = "DocumentReceive.findByNumberOfDoc", query = "SELECT d FROM DocumentReceive d WHERE d.numberOfDoc = :numberOfDoc"),
    @NamedQuery(name = "DocumentReceive.findByNumberOfPage", query = "SELECT d FROM DocumentReceive d WHERE d.numberOfPage = :numberOfPage"),
    @NamedQuery(name = "DocumentReceive.findByIsLawDocument", query = "SELECT d FROM DocumentReceive d WHERE d.isLawDocument = :isLawDocument"),
    @NamedQuery(name = "DocumentReceive.findByReplyForDocument", query = "SELECT d FROM DocumentReceive d WHERE d.replyForDocument = :replyForDocument"),
    @NamedQuery(name = "DocumentReceive.findByStatus", query = "SELECT d FROM DocumentReceive d WHERE d.status = :status"),
    @NamedQuery(name = "DocumentReceive.findByDeadlineByDate", query = "SELECT d FROM DocumentReceive d WHERE d.deadlineByDate = :deadlineByDate"),
    @NamedQuery(name = "DocumentReceive.findByDeadlineByWd", query = "SELECT d FROM DocumentReceive d WHERE d.deadlineByWd = :deadlineByWd")})
public class DocumentReceive implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "DOCUMENT_RECEIVE_SEQ", sequenceName = "DOCUMENT_RECEIVE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_RECEIVE_SEQ")
    @Column(name = "DOCUMENT_RECEIVE_ID")
    private Long documentReceiveId;
    @Column(name = "RECEIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveDate;
    @Size(max = 20)
    @Column(name = "DOCUMENT_CODE")
    private String documentCode;
    @Size(max = 1000)
    @Column(name = "PUBLISH_AGENCY_NAME")
    private String publishAgencyName;
    @Size(max = 200)
    @Column(name = "PUBLISH_AGENCY_IDS")
    private String publishAgencyIds;
    @Size(max = 100)
    @Column(name = "SIGNER")
    private String signer;
    @Column(name = "PUBLISH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;
    @Size(max = 500)
    @Column(name = "ABSTRACT")
    private String documentAbstract;
    @Column(name = "NUMBER_OF_DOC")
    private Long numberOfDoc;
    @Column(name = "NUMBER_OF_PAGE")
    private Long numberOfPage;
    @Column(name = "IS_LAW_DOCUMENT")
    private Long isLawDocument;
    @Column(name = "REPLY_FOR_DOCUMENT")
    private String replyForDocument;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "DEADLINE_BY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadlineByDate;
    @Column(name = "DEADLINE_BY_WD")
    private Long deadlineByWd;
    // hoangnv28
    @Column(name = "DOCUMENT_TYPE")
    private Long documentType;
    @Column(name = "PRIORITY_ID")
    private Long priorityId;
    @Column(name = "RECEIVE_TYPE_ID")
    private Long receiveTypeId;
    @Column(name = "SECURITY_TYPE")
    private Long securityType;
    @Column(name = "DOCUMENT_AREA_ID")
    private Long documentAreaId;
    @Column(name = "SEND_PACKING")
    private Long sendPacking;

    public DocumentReceive() {
        documentAbstract = "";
        documentCode = "";
        documentType = Constants.OBJECT_TYPE.DOCUMENT_RECEIVE;
        isLawDocument = 0L;
        numberOfDoc = 1L;
        numberOfPage = 1L;
        publishAgencyIds = "";
        publishAgencyName = "";
        publishDate = new Date();
        receiveDate = new Date();
        replyForDocument = "";
        signer = "";
        status = Constants.Status.ACTIVE;
    }

    public DocumentReceive(Long documentReceiveId) {
        this.documentReceiveId = documentReceiveId;
    }

    public Long getDocumentReceiveId() {
        return documentReceiveId;
    }

    public void setDocumentReceiveId(Long documentReceiveId) {
        this.documentReceiveId = documentReceiveId;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public String getReceiveDateStr() {
        if (receiveDate == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(receiveDate);
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getPublishAgencyName() {
        return publishAgencyName;
    }

    public void setPublishAgencyName(String publishAgencyName) {
        this.publishAgencyName = publishAgencyName;
    }

    public String getPublishAgencyIds() {
        return publishAgencyIds;
    }

    public void setPublishAgencyIds(String publishAgencyIds) {
        this.publishAgencyIds = publishAgencyIds;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getPublishDateStr() {
        if (publishDate == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(publishDate);
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDocumentAbstract() {
        return documentAbstract;
    }

    public void setDocumentAbstract(String documentAbstract) {
        this.documentAbstract = documentAbstract;
    }

    public Long getNumberOfDoc() {
        return numberOfDoc;
    }

    public void setNumberOfDoc(Long numberOfDoc) {
        this.numberOfDoc = numberOfDoc;
    }

    public Long getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(Long numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    public Long getIsLawDocument() {
        return isLawDocument;
    }

    public void setIsLawDocument(Long isLawDocument) {
        this.isLawDocument = isLawDocument;
    }

    public String getReplyForDocument() {
        return replyForDocument;
    }

    public void setReplyForDocument(String replyForDocument) {
        this.replyForDocument = replyForDocument;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getDeadlineByDate() {
        return deadlineByDate;
    }

    public String getDeadlineByDateStr() {
        if (deadlineByDate == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(deadlineByDate);
    }

    public void setDeadlineByDate(Date deadlineByDate) {
        this.deadlineByDate = deadlineByDate;
    }

    public Long getDeadlineByWd() {
        return deadlineByWd;
    }

    public void setDeadlineByWd(Long deadlineByWd) {
        this.deadlineByWd = deadlineByWd;
    }

    public Long getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Long documentType) {
        this.documentType = documentType;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public Long getReceiveTypeId() {
        return receiveTypeId;
    }

    public void setReceiveTypeId(Long receiveTypeId) {
        this.receiveTypeId = receiveTypeId;
    }

    public Long getSecurityType() {
        return securityType;
    }

    public void setSecurityType(Long securityType) {
        this.securityType = securityType;
    }

    public Long getDocumentAreaId() {
        return documentAreaId;
    }

    public void setDocumentAreaId(Long documentAreaId) {
        this.documentAreaId = documentAreaId;
    }

    public Long getSendPacking() {
        return sendPacking;
    }

    public void setSendPacking(Long sendPacking) {
        this.sendPacking = sendPacking;
    }

    @Override
    public DocumentReceive clone() throws CloneNotSupportedException {
        DocumentReceive documentReceive = (DocumentReceive) super.clone();
        documentReceive.setDocumentReceiveId(null);
        return documentReceive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentReceiveId != null ? documentReceiveId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DocumentReceive)) {
            return false;
        }
        DocumentReceive other = (DocumentReceive) object;
        if ((this.documentReceiveId == null && other.documentReceiveId != null)
                || (this.documentReceiveId != null && !this.documentReceiveId
                .equals(other.documentReceiveId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Document.DocumentReceive[ documentReceiveId="
                + documentReceiveId + " ]";
    }

}
