/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.base.model;

/**
 *
 * @author HaVM2
 */
public class TreeItem {
    private String name;
    private Long id;
    private Long type;
    private Long selected;

    public TreeItem(Long id, String name){
        this.id = id;
        this.name = name;
    }
    
    public TreeItem(Long id, String name, Long type){
        this.id = id;
        this.name = name;
        this.type = type;
    }
    
    public TreeItem(Long id, String name, Long type, Long selected){
        this.id = id;
        this.name = name;
        this.type = type;
        this.selected = selected;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getSelected() {
        return selected;
    }

    public void setSelected(Long selected) {
        this.selected = selected;
    }
}
