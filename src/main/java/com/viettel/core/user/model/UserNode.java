/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.core.user.model;

import com.viettel.core.user.BO.Users;
import com.viettel.core.base.model.TreeNode;

/**
 *
 * @author ChucHV
 */
public class UserNode extends TreeNode {

	private Users users;

	public UserNode(Users users) {
		super(users.getUserId(), users.getFullName());
		this.users = users;
	}

	public UserNode(Long id, String name) {
		super(id, name);
	}

	/*
	 * User hien nhien la leaf
	 */
	@Override
	public boolean isLeaf() {
		return true;
	}

	/*
	 * return null
	 */
	@Override
	public TreeNode getChild(int i) {
		return null;
	}

	/*
	 * return 0
	 */
	@Override
	public int getChildCount() {
		return 0;
	}

	public Users getUsers() {
		return users;
	}
}
