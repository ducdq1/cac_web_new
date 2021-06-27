/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.RapidTest;

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
 * @author Linhdx
 */
@Entity
@Table(name = "RT_RAPID_TEST_ATTACH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RtRapidTestAttach.findAll", query = "SELECT r FROM RtRapidTestAttach r"),
    @NamedQuery(name = "RtRapidTestAttach.findById", query = "SELECT r FROM RtRapidTestAttach r WHERE r.rapidTestAttachId = :rapidTestAttachId"),
    @NamedQuery(name = "RtRapidTestAttach.findByRapidTestId", query = "SELECT r FROM RtRapidTestAttach r WHERE r.rapidTestId = :rapidTestId"),
    @NamedQuery(name = "RtRapidTestAttach.findByAttachTypeCode", query = "SELECT r FROM RtRapidTestAttach r WHERE r.attachTypeCode = :attachTypeCode"),
    @NamedQuery(name = "RtRapidTestAttach.findByIsActive", query = "SELECT r FROM RtRapidTestAttach r WHERE r.isActive = :isActive")})
public class RtRapidTestAttach implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "RAPID_TEST_ATTACH_SEQ", sequenceName = "RAPID_TEST_ATTACH_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RAPID_TEST_ATTACH_SEQ")
    @Basic(optional = false)
    @NotNull
    @Column(name = "RAPID_TEST_ATTACH_ID")
    private Long rapidTestAttachId;
    @Column(name = "RAPID_TEST_ID")
    private Long rapidTestId;
    @Size(max = 12)
    @Column(name = "ATTACH_TYPE_CODE")
    private String attachTypeCode;
    @Column(name = "IS_ACTIVE")
    private Short isActive;

    public RtRapidTestAttach() {
    }

    public RtRapidTestAttach(Long rapidTestAttachId) {
        this.rapidTestAttachId = rapidTestAttachId;
    }

    public Long getRapidTestAttachId() {
        return rapidTestAttachId;
    }

    public void setRapidTestAttachId(Long rapidTestAttachId) {
        this.rapidTestAttachId = rapidTestAttachId;
    }

    public Long getRapidTestId() {
        return rapidTestId;
    }

    public void setRapidTestId(Long rapidTestId) {
        this.rapidTestId = rapidTestId;
    }

    public String getAttachTypeCode() {
        return attachTypeCode;
    }

    public void setAttachTypeCode(String attachTypeCode) {
        this.attachTypeCode = attachTypeCode;
    }

    public Short getIsActive() {
        return isActive;
    }

    public void setIsActive(Short isActive) {
        this.isActive = isActive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rapidTestAttachId != null ? rapidTestAttachId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RtRapidTestAttach)) {
            return false;
        }
        RtRapidTestAttach other = (RtRapidTestAttach) object;
        if ((this.rapidTestAttachId == null && other.rapidTestAttachId != null) || (this.rapidTestAttachId != null && !this.rapidTestAttachId.equals(other.rapidTestAttachId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.RapidTest.RtRapidTestAttach[ rapidTestAttachId=" + rapidTestAttachId + " ]";
    }

}
