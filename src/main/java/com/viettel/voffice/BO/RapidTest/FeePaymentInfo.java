/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.RapidTest;

import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "FEE_PAYMENT_INFO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FeePaymentInfo.findAll", query = "SELECT r FROM FeePaymentInfo r"),
    @NamedQuery(name = "FeePaymentInfo.findByFeePaymentInfoId", query = "SELECT r FROM FeePaymentInfo r WHERE r.feePaymentInfoId = :feePaymentInfoId"),
    @NamedQuery(name = "FeePaymentInfo.findByStatus", query = "SELECT r FROM FeePaymentInfo r WHERE r.status = :status"),
    @NamedQuery(name = "FeePaymentInfo.findByPaymentPerson", query = "SELECT r FROM FeePaymentInfo r WHERE r.paymentPerson = :paymentPerson"),
    @NamedQuery(name = "FeePaymentInfo.findByPaymentDate", query = "SELECT r FROM FeePaymentInfo r WHERE r.paymentDate = :paymentDate"),
    @NamedQuery(name = "FeePaymentInfo.findByFeeId", query = "SELECT r FROM FeePaymentInfo r WHERE r.feeId = :feeId"),
    @NamedQuery(name = "FeePaymentInfo.findByFeePaymentTypeId", query = "SELECT r FROM FeePaymentInfo r WHERE r.feePaymentTypeId = :feePaymentTypeId"),
    @NamedQuery(name = "FeePaymentInfo.findByPaymentInfo", query = "SELECT r FROM FeePaymentInfo r WHERE r.paymentInfo = :paymentInfo"),
    @NamedQuery(name = "FeePaymentInfo.findByCost", query = "SELECT r FROM FeePaymentInfo r WHERE r.cost = :cost"),
    @NamedQuery(name = "FeePaymentInfo.findByBillPath", query = "SELECT r FROM FeePaymentInfo r WHERE r.attachBillId = :attachBillId"),
    @NamedQuery(name = "FeePaymentInfo.findByCreateDate", query = "SELECT r FROM FeePaymentInfo r WHERE r.createDate = :createDate"),
    @NamedQuery(name = "FeePaymentInfo.findByUpdateDate", query = "SELECT r FROM FeePaymentInfo r WHERE r.updateDate = :updateDate"),
    @NamedQuery(name = "FeePaymentInfo.findByFileId", query = "SELECT r FROM FeePaymentInfo r WHERE r.fileId = :fileId"),
    @NamedQuery(name = "FeePaymentInfo.findByIsActive", query = "SELECT r FROM FeePaymentInfo r WHERE r.isActive = :isActive"),
    @NamedQuery(name = "FeePaymentInfo.findByPaymentCode", query = "SELECT r FROM FeePaymentInfo r WHERE r.paymentCode = :paymentCode"),
    @NamedQuery(name = "FeePaymentInfo.findByPaymentConfirm", query = "SELECT r FROM FeePaymentInfo r WHERE r.paymentConfirm = :paymentConfirm"),
    @NamedQuery(name = "FeePaymentInfo.findByBillCode", query = "SELECT r FROM FeePaymentInfo r WHERE r.billCode = :billCode"),
    @NamedQuery(name = "FeePaymentInfo.findByDateConfirm", query = "SELECT r FROM FeePaymentInfo r WHERE r.dateConfirm = :dateConfirm"),
    @NamedQuery(name = "FeePaymentInfo.findByCommentFee", query = "SELECT r FROM FeePaymentInfo r WHERE r.commentFee = :commentFee"),
    @NamedQuery(name = "FeePaymentInfo.findByFeeName", query = "SELECT r FROM FeePaymentInfo r WHERE r.feeName = :feeName"),
    @NamedQuery(name = "FeePaymentInfo.findByPaymentActionUser", query = "SELECT r FROM FeePaymentInfo r WHERE r.paymentActionUser = :paymentActionUser")})
public class FeePaymentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @SequenceGenerator(name = "FEE_PAYMENT_INFO_SEQ", sequenceName = "FEE_PAYMENT_INFO_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FEE_PAYMENT_INFO_SEQ")
    @Column(name = "FEE_PAYMENT_INFO_ID")
    @Basic(optional = false)
    @NotNull
    private Long feePaymentInfoId;
    @Column(name = "STATUS")
    private Long status;
    @Size(max = 500)
    @Column(name = "PAYMENT_PERSON")
    private String paymentPerson;
    @Column(name = "PAYMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Column(name = "FEE_ID")
    private Long feeId;
    @Column(name = "FEE_PAYMENT_TYPE_ID")
    private Long feePaymentTypeId;
    @Size(max = 2000)
    @Column(name = "PAYMENT_INFO")
    private String paymentInfo;
    @Column(name = "COST")
    private Long cost;
    @Size(max = 1000)
    @Column(name = "ATTACH_BILL_ID")
    private Long attachBillId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date updateDate;
    @Column(name = "FILE_ID")
    private Long fileId;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Size(max = 500)
    @Column(name = "PAYMENT_CODE")
    private String paymentCode;
    @Size(max = 510)
    @Column(name = "PAYMENT_CONFIRM")
    private String paymentConfirm;
    @Size(max = 510)
    @Column(name = "BILL_CODE")
    private String billCode;
    @Column(name = "DATE_CONFIRM")
    @Temporal(TemporalType.DATE)
    private Date dateConfirm;
    @Size(max = 1000)
    @Column(name = "COMMENT_FEE")
    private String commentFee;
    @Size(max = 500)
    @Column(name = "FEE_NAME")
    private String feeName;
    @Size(max = 500)
    @Column(name = "PAYMENT_ACTION_USER")
    private String paymentActionUser;

    @Column(name = "PAYMENT_ACTION_USER_ID")
    private Long paymentActionUserId;
    
    @Column(name = "PROCEDURE_CODE")
    private Long procedureCode;

    public FeePaymentInfo() {
    }

    public FeePaymentInfo(Long feePaymentInfoId) {
        this.feePaymentInfoId = feePaymentInfoId;
    }

    public Long getFeePaymentInfoId() {
        return feePaymentInfoId;
    }

    public void setFeePaymentInfoId(Long feePaymentInfoId) {
        this.feePaymentInfoId = feePaymentInfoId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getPaymentPerson() {
        return paymentPerson;
    }

    public void setPaymentPerson(String paymentPerson) {
        this.paymentPerson = paymentPerson;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getFeeId() {
        return feeId;
    }

    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }

    public Long getFeePaymentTypeId() {
        return feePaymentTypeId;
    }

    public void setFeePaymentTypeId(Long feePaymentTypeId) {
        this.feePaymentTypeId = feePaymentTypeId;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getAttachBillId() {
        return attachBillId;
    }

    public void setAttachBillId(Long attachBillId) {
        this.attachBillId = attachBillId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentConfirm() {
        return paymentConfirm;
    }

    public void setPaymentConfirm(String paymentConfirm) {
        this.paymentConfirm = paymentConfirm;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Date getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(Date dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getPaymentActionUser() {
        return paymentActionUser;
    }

    public void setPaymentActionUser(String paymentActionUser) {
        this.paymentActionUser = paymentActionUser;
    }

    public String getCommentFee() {
        return commentFee;
    }

    public void setCommentFee(String commentFee) {
        this.commentFee = commentFee;
    }

    public Long getPaymentActionUserId() {
        return paymentActionUserId;
    }

    public void setPaymentActionUserId(Long paymentActionUserId) {
        this.paymentActionUserId = paymentActionUserId;
    }

    public Long getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(Long procedureCode) {
        this.procedureCode = procedureCode;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (feePaymentInfoId != null ? feePaymentInfoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FeePaymentInfo)) {
            return false;
        }
        FeePaymentInfo other = (FeePaymentInfo) object;
        if ((this.feePaymentInfoId == null && other.feePaymentInfoId != null) || (this.feePaymentInfoId != null && !this.feePaymentInfoId.equals(other.feePaymentInfoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.RapidTest.FeePaymentInfo[ feePaymentInfoId=" + feePaymentInfoId + " ]";
    }

    public String getPaymentDateStr() {
        if (paymentDate == null) {
            return null;
        }
        return DateTimeUtils.convertDateToString(paymentDate);
    }

    public String getFeePaymentTypeStr() {
        if (Objects.equals(feePaymentTypeId, Constants.RAPID_TEST.PAYMENT.FEE_PAYMENT_TYPE_CODE_KEYPAY)) {
            return Constants.RAPID_TEST.PAYMENT.FEE_PAYMENT_TYPE_CODE_KEYPAY_STR;
        }
        if (Objects.equals(feePaymentTypeId, Constants.RAPID_TEST.PAYMENT.FEE_PAYMENT_TYPE_CODE_CHUYENKHOAN)) {
            return Constants.RAPID_TEST.PAYMENT.FEE_PAYMENT_TYPE_CODE_CHUYENKHOAN_STR;
        }
        if (Objects.equals(feePaymentTypeId, Constants.RAPID_TEST.PAYMENT.FEE_PAYMENT_TYPE_CODE_TIENMAT)) {
            return Constants.RAPID_TEST.PAYMENT.FEE_PAYMENT_TYPE_CODE_TIENMAT_STR;
        }
        return "";

    }

}
