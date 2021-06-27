/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.model;

import java.io.Serializable;

/**
 *
 * @author HaVM2
 */
public class ActionTypeModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    

    public ActionTypeModel() {
    }

    public ActionTypeModel(Long id, String name){
        this.id = id;
        this.name = name;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
