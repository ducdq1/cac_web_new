/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Document;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "BOOK_DOCUMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookDocument.findAll", query = "SELECT b FROM BookDocument b"),
    @NamedQuery(name = "BookDocument.findByBookDocumentId", query = "SELECT b FROM BookDocument b WHERE b.bookDocumentId = :bookDocumentId"),
    @NamedQuery(name = "BookDocument.findByBookNumber", query = "SELECT b FROM BookDocument b WHERE b.bookNumber = :bookNumber"),
    @NamedQuery(name = "BookDocument.findByDocumentId", query = "SELECT b FROM BookDocument b WHERE b.documentId = :documentId"),
    @NamedQuery(name = "BookDocument.findByStatus", query = "SELECT b FROM BookDocument b WHERE b.status = :status")})
public class BookDocument implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "BOOK_DOCUMENT_SEQ", sequenceName = "BOOK_DOCUMENT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_DOCUMENT_SEQ")
    @Column(name = "BOOK_DOCUMENT_ID")
    private Long bookDocumentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "BOOK_NUMBER")
    private Long bookNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "BOOK_ID")
    private Long bookId;
    @Column(name = "CREATOR_ID")
    private Long creatorId;
    @Column(name = "CREATOR_NAME")
    private String creatorName;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;

    public BookDocument() {
    }

    public BookDocument(Long bookDocumentId) {
        this.bookDocumentId = bookDocumentId;
    }

    public BookDocument(Long bookDocumentId, Long bookNumber, Long documentId, Long status) {
        this.bookDocumentId = bookDocumentId;
        this.bookNumber = bookNumber;
        this.documentId = documentId;
        this.status = status;
    }

    public Long getBookDocumentId() {
        return bookDocumentId;
    }

    public void setBookDocumentId(Long bookDocumentId) {
        this.bookDocumentId = bookDocumentId;
    }

    public Long getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(Long bookNumber) {
        this.bookNumber = bookNumber;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookDocumentId != null ? bookDocumentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BookDocument)) {
            return false;
        }
        BookDocument other = (BookDocument) object;
        if ((this.bookDocumentId == null && other.bookDocumentId != null) || (this.bookDocumentId != null && !this.bookDocumentId.equals(other.bookDocumentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Document.BookDocument[ bookDocumentId=" + bookDocumentId + " ]";
    }
}
