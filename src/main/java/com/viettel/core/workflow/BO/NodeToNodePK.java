/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.BO;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author HaVM2
 */
@Embeddable
public class NodeToNodePK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6525132996405380038L;
	@Basic(optional = false)
    @Column(name = "PREVIOUS_ID")
    private Long previousId;
    @Basic(optional = false)
    @Column(name = "NEXT_ID")
    private Long nextId;

    public NodeToNodePK() {
    }

    public NodeToNodePK(Long previousId, Long nextId) {
        this.previousId = previousId;
        this.nextId = nextId;
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
        hash +=  previousId.intValue();
        hash +=  nextId.intValue();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NodeToNodePK)) {
            return false;
        }
        NodeToNodePK other = (NodeToNodePK) object;
        if (this.previousId != other.previousId) {
            return false;
        }
        if (this.nextId != other.nextId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Flow.NodeToNodePK[ previousId=" + previousId + ", nextId=" + nextId + " ]";
    }
    
}
