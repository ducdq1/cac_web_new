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
@Table(name = "NODE_TO_NODE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NodeToNode.findAll", query = "SELECT n FROM NodeToNode n"),
    @NamedQuery(name = "NodeToNode.findByAction", query = "SELECT n FROM NodeToNode n WHERE n.action = :action"),
    @NamedQuery(name = "NodeToNode.findByIsActive", query = "SELECT n FROM NodeToNode n WHERE n.isActive = :isActive"),
    @NamedQuery(name = "NodeToNode.findByType", query = "SELECT n FROM NodeToNode n WHERE n.type = :type"),
    @NamedQuery(name = "NodeToNode.findById", query = "SELECT n FROM NodeToNode n WHERE n.id = :id"),
    @NamedQuery(name = "NodeToNode.findByFormId", query = "SELECT n FROM NodeToNode n WHERE n.formId = :formId")})
public class NodeToNode implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "NODE_TO_NODE_SEQ", sequenceName = "NODE_TO_NODE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NODE_TO_NODE_SEQ")
    @Column(name = "ID")
    private Long id;
    @Size(max = 200)
    @Column(name = "ACTION")
    private String action;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "TYPE")
    private Long type;

    @Column(name = "FORM_ID")
    private Long formId;
    @Column(name = "PREVIOUS_ID")
    private Long previousId;
    @Column(name = "NEXT_ID")
    private Long nextId;
    @Column(name = "STATUS")
    private Long status;

    public NodeToNode() {
    }

    public NodeToNode(Long id) {
        this.id = id;
    }

    public NodeToNode(Long id, Long isActive) {
        this.id = id;
        this.isActive = isActive;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getPreviousId() {
        return previousId;
    }

    public void setPreviousId(Long previousId) {
        this.previousId = previousId;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NodeToNode)) {
            return false;
        }
        NodeToNode other = (NodeToNode) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Flow2.NodeToNode[ id=" + id + " ]";
    }

    /**
     * @return the status
     */
    public Long getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Long status) {
        this.status = status;
    }

}
