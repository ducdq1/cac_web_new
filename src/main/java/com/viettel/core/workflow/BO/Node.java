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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "NODE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Node.findAll", query = "SELECT n FROM Node n"),
    @NamedQuery(name = "Node.findByNodeId", query = "SELECT n FROM Node n WHERE n.nodeId = :nodeId"),
    @NamedQuery(name = "Node.findByFlowId", query = "SELECT n FROM Node n WHERE n.flowId = :flowId"),
    @NamedQuery(name = "Node.findByNodeName", query = "SELECT n FROM Node n WHERE n.nodeName = :nodeName"),
    @NamedQuery(name = "Node.findByStatus", query = "SELECT n FROM Node n WHERE n.status = :status")})
public class Node implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4893954263397967175L;
	@SequenceGenerator(name = "NODE_SEQ", sequenceName = "NODE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NODE_SEQ")
    @Column(name = "NODE_ID")
    private Long nodeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NODE_NAME")
    private String nodeName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LEFT")
    private Float left;
    @Column(name = "RIGHT")
    private Float right;
    @Column(name = "TOP")
    private Float top;
    @Column(name = "BOTTOM")
    private Float bottom;
    @Column(name = "TYPE")
    private Long type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "FLOW_ID")
    private Long flowId;
    @Transient
    private Long id;
    


    public Node() {
    }
    
    public Node(Node n){
        this.nodeId = n.nodeId;
        this.nodeName = n.nodeName;
        this.isActive = n.isActive;
        this.flowId = n.flowId;
        this.top = n.top;
        this.bottom = n.bottom;
        this.right = n.right;
        this.left = n.left;
        this.type = n.type;
        this.status = n.status;
    }

    public Node(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Node(Long nodeId, String nodeName,Long isActive) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.isActive = isActive;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Float getLeft() {
        return left;
    }

    public void setLeft(Float left) {
        this.left = left;
    }

    public Float getRight() {
        return right;
    }

    public void setRight(Float right) {
        this.right = right;
    }

    public Float getTop() {
        return top;
    }

    public void setTop(Float top) {
        this.top = top;
    }

    public Float getBottom() {
        return bottom;
    }

    public void setBottom(Float bottom) {
        this.bottom = bottom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   @Override
    public Node clone() throws CloneNotSupportedException {
        Node ndu = (Node) super.clone();
        ndu.setNodeId(null);
        return ndu;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nodeId != null ? nodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Node)) {
            return false;
        }
        Node other = (Node) object;
        if ((this.nodeId == null && other.nodeId != null) || (this.nodeId != null && !this.nodeId.equals(other.nodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Flow.Node[ nodeId=" + nodeId + " ]";
    }
    
}
