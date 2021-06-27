/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.model;

import com.viettel.core.workflow.BO.NodeToNode;
import java.io.Serializable;

/**
 *
 * @author HaVM2
 */
public class NodeToNodeModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8384987748017576650L;
	private String action;
    private Long isActive = 1l;
    private Long nextId;
    private Long previousId;
    private Long type;
    private Long id;
    private Long formId;
    private Long status;

    public NodeToNodeModel() {
    }

    public NodeToNodeModel(long previousId, long nextId) {
        this.nextId = nextId;
        this.previousId = previousId;
    }
    
    public NodeToNode toEntity(){
        NodeToNode entities = new NodeToNode(); 
        entities.setPreviousId(previousId);
        entities.setNextId(nextId);
        entities.setType(type);
        entities.setAction(action);
        //entities.setId(id);
        entities.setFormId(formId);
        entities.setStatus(status);
        entities.setIsActive(1l);
        return entities;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    public Long getPreviousId() {
        return previousId;
    }

    public void setPreviousId(Long previousId) {
        this.previousId = previousId;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the formId
     */
    public Long getFormId() {
        return formId;
    }

    /**
     * @param formId the formId to set
     */
    public void setFormId(Long formId) {
        this.formId = formId;
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
