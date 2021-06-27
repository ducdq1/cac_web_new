/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.BO;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ChucHV
 */
@Entity
@Table(name = "PROCESS_ATTACH")
@XmlRootElement
public class ProcessAttachs implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SequenceGenerator(name = "PROCESS_ATTACH_SEQ", sequenceName = "PROCESS_ATTACH_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESS_ATTACH_SEQ")
    @Column(name = "PROCESS_ATTACH_ID")
    private Long processAttachId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROCESS_ID")
    private Long processId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private Long status;
    @Basic(optional = false)
    @NotNull    
    @Column(name = "ATTACH_ID")
    private Long attachId;

    public ProcessAttachs() {

    }

    public Long getProcessAttachId() {
        return processAttachId;
    }

    public void setProcessAttachId(Long processAttachId) {
        this.processAttachId = processAttachId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

}
