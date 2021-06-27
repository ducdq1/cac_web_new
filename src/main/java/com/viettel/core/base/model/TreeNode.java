/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.base.model;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ChucHV
 */
public abstract class TreeNode {

	protected String name;
	protected Long id;
	protected Long type;
	protected Long selected;
	protected List<TreeNode> listChild;
	protected HashMap<String, Object> data;

	public TreeNode(Long id, String name) {
		this.id = id;
		this.name = name;
		this.data = new HashMap<String, Object>();
	}

	public abstract boolean isLeaf();

	public abstract TreeNode getChild(int i);

	public abstract int getChildCount();

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

	public Object get(String name) {
		return data.get(name);
	}

	public void put(String name, Object value) {
		data.put(name, value);
	}

	@Override
	public String toString() {
		return name;
	}

}
