/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import com.viettel.core.sys.BO.Category;
import com.viettel.voffice.BO.Document.*;
import java.io.Serializable;
import com.viettel.core.workflow.BO.Process;
/**
 *
 * @author giangpn
 */

public class AttachCategoryModel implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3480927321605472479L;

	private Attachs attachs;

    private Category category;

    
    public AttachCategoryModel() {
    }

    public AttachCategoryModel(Attachs attachs,Category category) {
        this.attachs = attachs;
        this.category = category;
    }
    
    public Attachs getAttach() {
        return attachs;
    }

    public void setAttach(Attachs attachs) {
        this.attachs = attachs;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
