/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.FileHandler;
import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "DOCUMENT_REF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentRef.findAll", query = "SELECT d FROM DocumentRef d"),
    @NamedQuery(name = "DocumentRef.findByDocumentRefId", query = "SELECT d FROM DocumentRef d WHERE d.documentRefId = :documentRefId"),
    @NamedQuery(name = "DocumentRef.findByFileId", query = "SELECT d FROM DocumentRef d WHERE d.fileId = :fileId"),
    @NamedQuery(name = "DocumentRef.findByObjectType", query = "SELECT d FROM DocumentRef d WHERE d.objectType = :objectType"),
    @NamedQuery(name = "DocumentRef.findByObjectId", query = "SELECT d FROM DocumentRef d WHERE d.objectId = :objectId"),
    @NamedQuery(name = "DocumentRef.findByDocumentCode", query = "SELECT d FROM DocumentRef d WHERE d.documentCode = :documentCode"),
    @NamedQuery(name = "DocumentRef.findByDocumentTitle", query = "SELECT d FROM DocumentRef d WHERE d.documentTitle = :documentTitle"),
    @NamedQuery(name = "DocumentRef.findByDocumentSummary", query = "SELECT d FROM DocumentRef d WHERE d.documentSummary = :documentSummary")})
public class DocumentRef implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1118763677983156123L;
	@SequenceGenerator(name = "DOCUMENT_REF_SEQ", sequenceName = "DOCUMENT_REF_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_REF_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "DOCUMENT_REF_ID")
    private Long documentRefId;
    @Column(name = "FILE_ID")
    private Long fileId;
    @Column(name = "OBJECT_TYPE")
    private Long objectType;
    @Column(name = "OBJECT_ID")
    private Long objectId;
    @Size(max = 100)
    @Column(name = "DOCUMENT_CODE")
    private String documentCode;
    @Size(max = 500)
    @Column(name = "DOCUMENT_TITLE")
    private String documentTitle;
    @Size(max = 1000)
    @Column(name = "DOCUMENT_SUMMARY")
    private String documentSummary;
    @Column(name = "PUBLISH_AGENCY")
    private String publishAgency;

    public DocumentRef() {
    }

    public DocumentRef(Long documentRefId) {
        this.documentRefId = documentRefId;
    }

    public DocumentRef(Long documentRefId, Long fileId) {
        this.documentRefId = documentRefId;
        this.fileId = fileId;
    }

    public Long getDocumentRefId() {
        return documentRefId;
    }

    public void setDocumentRefId(Long documentRefId) {
        this.documentRefId = documentRefId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getObjectType() {
        return objectType;
    }

    public void setObjectType(Long objectType) {
        this.objectType = objectType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentSummary() {
        return documentSummary;
    }

    public void setDocumentSummary(String documentSummary) {
        this.documentSummary = documentSummary;
    }

    public String getPublishAgency() {
        return publishAgency;
    }

    public void setPublishAgency(String publishAgency) {
        this.publishAgency = publishAgency;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentRefId != null ? documentRefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentRef)) {
            return false;
        }
        DocumentRef other = (DocumentRef) object;
        if ((this.documentRefId == null && other.documentRefId != null) || (this.documentRefId != null && !this.documentRefId.equals(other.documentRefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Files.DocumentRef[ documentRefId=" + documentRefId + " ]";
    }
    
}
