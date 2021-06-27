/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Document;

import com.viettel.utils.Constants;
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
@Table(name = "BOOKS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Books.findAll", query = "SELECT b FROM Books b"),
    @NamedQuery(name = "Books.findByBookId", query = "SELECT b FROM Books b WHERE b.bookId = :bookId"),
    @NamedQuery(name = "Books.findByBookName", query = "SELECT b FROM Books b WHERE b.bookName = :bookName"),
    @NamedQuery(name = "Books.findByBookOrder", query = "SELECT b FROM Books b WHERE b.bookOrder = :bookOrder"),
    @NamedQuery(name = "Books.findByIsDefault", query = "SELECT b FROM Books b WHERE b.isDefault = :isDefault"),
    @NamedQuery(name = "Books.findByCurrentNumber", query = "SELECT b FROM Books b WHERE b.currentNumber = :currentNumber"),
    @NamedQuery(name = "Books.findByPrefix", query = "SELECT b FROM Books b WHERE b.prefix = :prefix"),
    @NamedQuery(name = "Books.findByPrefixBy", query = "SELECT b FROM Books b WHERE b.prefixBy = :prefixBy"),
    @NamedQuery(name = "Books.findByStatus", query = "SELECT b FROM Books b WHERE b.status = :status"),
    @NamedQuery(name = "Books.findByDeptId", query = "SELECT b FROM Books b WHERE b.deptId = :deptId"),
    @NamedQuery(name = "Books.findByBookObjectTypeId", query = "SELECT b FROM Books b WHERE b.bookObjectTypeId = :bookObjectTypeId")
    })
public class Books implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
    @Column(name = "BOOK_ID")
    private Long bookId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "BOOK_NAME")
    private String bookName;
    @Column(name = "BOOK_ORDER")
    private Long bookOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_DEFAULT")
    private Long isDefault;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CURRENT_NUMBER")
    private Long currentNumber;
    @Size(max = 50)
    @Column(name = "PREFIX")
    private String prefix;
    @Column(name = "PREFIX_BY")
    private Long prefixBy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private Long status;

    @Basic(optional = false)
    @Column(name = "DEPT_ID")
    private Long deptId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "BOOK_OBJECT_TYPE_ID")
    private Long bookObjectTypeId;
    
    @Size(max = 500)
    @Column(name = "DEPT_NAME")
    private String deptName;
    
    public Books() {
    }

    public Books(Long bookId) {
        this.bookId = bookId;
    }

    public Books(Long bookId, String bookName, Long isDefault, Long currentNumber, Long status) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.isDefault = isDefault;
        this.currentNumber = currentNumber;
        this.status = status;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Long getBookOrder() {
        return bookOrder;
    }

    public void setBookOrder(Long bookOrder) {
        this.bookOrder = bookOrder;
    }

    public Long getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Long isDefault) {
        this.isDefault = isDefault;
    }

    public Long getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(Long currentNumber) {
        this.currentNumber = currentNumber;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getPrefixBy() {
        return prefixBy;
    }

    public void setPrefixBy(Long prefixBy) {
        this.prefixBy = prefixBy;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

     public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    
    public Long getBookObjectTypeId() {
        return bookObjectTypeId;
    }

    public void setBookObjectTypeId(Long bookObjectTypeId) {
        this.bookObjectTypeId = bookObjectTypeId;
    }
    
    public String getBookObjectTypeName() {
        if (bookObjectTypeId == null) {
            return null;
        }
        return bookObjectTypeId== Constants.OBJECT_TYPE.DOCUMENT_RECEIVE ? Constants.OBJECT_TYPE_STR.DOCUMENT_RECEIVE_STR : Constants.OBJECT_TYPE_STR.DOCUMENT_PUBLISH_STR;
    }
    
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptName() {
        return deptName;
    }
    
    public String getStatusName() {
        if (status == null) {
            return null;
        }
        return status== Constants.Status.ACTIVE ? Constants.Status.ACTIVE_MSG : Constants.Status.INACTIVE_MSG;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Books)) {
            return false;
        }
        Books other = (Books) object;
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Document.Books[ bookId=" + bookId + " ]";
    }
    
}
