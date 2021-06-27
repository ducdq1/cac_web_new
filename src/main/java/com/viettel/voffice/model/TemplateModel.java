/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.voffice.model;

import com.viettel.voffice.BO.Document.*;
import java.io.Serializable;
import java.math.BigInteger;
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
 * @author viettel
 */
public class TemplateModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long templateId;
   
    private String templateName;
   
    private BigInteger attachId;
    
    private long deptId;
  
    private long documentTypeId;

    private String documentTypeName;

    private String deptName;

    private String templateContent;
     
    public TemplateModel() {
    }

    public TemplateModel(Long templateId) {
        this.templateId = templateId;
    }

    public TemplateModel(Long templateId, BigInteger attachId, long deptId, long documentTypeId) {
        this.templateId = templateId;
        this.attachId = attachId;
        this.deptId = deptId;
        this.documentTypeId = documentTypeId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public BigInteger getAttachId() {
        return attachId;
    }

    public void setAttachId(BigInteger attachId) {
        this.attachId = attachId;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
    
}
