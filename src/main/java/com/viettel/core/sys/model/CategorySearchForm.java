/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.model;

import com.viettel.core.sys.BO.Category;

/**
 *
 * @author Administrator
 */
public class CategorySearchForm {

    private String type;
    private String code;
    private String name;
    private Long categoryId;
    private Long createdBy;
    private String description;
    private Long isActive;

    public CategorySearchForm() {
    }

    public CategorySearchForm(Category entity) {
        if (entity == null) {
            return;
        } else {
            this.categoryId = entity.getCategoryId();
            this.code = entity.getCode();
            this.createdBy = entity.getCreatedBy();
            this.description = entity.getDescription();
            this.isActive = entity.getIsActive();
            this.name = entity.getName();
            //this.type = entity.getCode();
        }

    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Category convertToEntity() {
        Category item = new Category();
        if (this.getCategoryId() != null) {
            item.setCategoryId(this.categoryId);
        }
        item.setCode(this.code);
        //item.setType(this.type);
        item.setName(this.name);
        item.setDescription(this.description);
        item.setIsActive(this.isActive);

        return item;
    }
}
