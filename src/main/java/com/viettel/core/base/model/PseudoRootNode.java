/**
 * ZK không load Root của cây, vì vậy ta phải tạo root giả có con là root thật
 * Khi đó ZK sẽ load root thật, dĩ nhiên root giả không được load.
 */
package com.viettel.core.base.model;

import com.viettel.core.base.model.TreeNode;

public class PseudoRootNode extends TreeNode {
	private TreeNode realRealNode;

	public PseudoRootNode(Long id, String name, TreeNode realRootNode) {
		super(id, name);
		this.realRealNode = realRootNode;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public TreeNode getChild(int i) {
		if (i == 0) {
			return realRealNode;
		}
		return null;
	}

	@Override
	public int getChildCount() {
		return 1;
	}

}
