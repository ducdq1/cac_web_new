/*
 * To change this template, choose Tools | Templates
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
@Table(name = "FLOW")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Flow.findAll", query = "SELECT f FROM Flow f"),
    @NamedQuery(name = "Flow.findByFlowId", query = "SELECT f FROM Flow f WHERE f.flowId = :flowId"),
    @NamedQuery(name = "Flow.findByFlowName", query = "SELECT f FROM Flow f WHERE f.flowName = :flowName"),
    @NamedQuery(name = "Flow.findByDescription", query = "SELECT f FROM Flow f WHERE f.description = :description"),
    @NamedQuery(name = "Flow.findByIsActive", query = "SELECT f FROM Flow f WHERE f.isActive = :isActive"),
    @NamedQuery(name = "Flow.findByObjectName", query = "SELECT f FROM Flow f WHERE f.objectName = :objectName")})
public class Flow implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1931015856024854015L;
    @SequenceGenerator(name = "FLOW_SEQ", sequenceName = "FLOW_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLOW_SEQ")
    @Column(name = "FLOW_ID")
    private Long flowId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "FLOW_NAME")
    private String flowName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "FLOW_CODE")
    private String flowCode;
    @Column(name = "OBJECT_NAME")
    private String objectName;
    @Column(name = "OBJECT_ID")
    private Long objectId;
    @Column(name = "CATEGORY_NAME")
    private String categoryName;
    @Column(name = "CATEGORY_ID")
    private Long categoryId;
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "IS_USING")
    private Long isUsing;

    public Flow() {
    }

    public Flow(Long flowId) {
        this.flowId = flowId;
    }

    public Flow(Long flowId, String flowName, String description, Long isActive) {
        this.flowId = flowId;
        this.flowName = flowName;
        this.description = description;
        this.isActive = isActive;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowCode() {
        return flowCode;
    }

    public void setFlowCode(String flowCode) {
        this.flowCode = flowCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(Long isUsing) {
        this.isUsing = isUsing;
    }

    
    
      @Override
    public Flow clone() throws CloneNotSupportedException {
        Flow ndu = (Flow) super.clone();
        ndu.setFlowId(null);
        return ndu;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flowId != null ? flowId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Flow)) {
            return false;
        }
        Flow other = (Flow) object;
        if ((this.flowId == null && other.flowId != null) || (this.flowId != null && !this.flowId.equals(other.flowId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Flow.Flow[ flowId=" + flowId + " ]";
    }

}
