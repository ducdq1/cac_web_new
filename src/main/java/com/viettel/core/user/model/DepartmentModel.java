package com.viettel.core.user.model;

import java.util.List;

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import com.viettel.utils.LogUtils;
import com.viettel.voffice.BEAN.DeptNode;

/**
 * giangpn
 */
public class DepartmentModel extends DefaultTreeModel<DeptNode> {

	private static final long serialVersionUID = 1L;
	DefaultTreeNode<DeptNode> _root;

	public DepartmentModel(DepartmentTreeNode deptTreeNode) {
		super(deptTreeNode);
		_root = deptTreeNode;
	}

	// public boolean isLeaf(DeptNode node) {
	// return ((DeptNode)node).getChildren().size() == 0; //at most 4 levels
	// }
	//
	// public DeptNode getChild(DeptNode parent, int index) {
	// return ((DeptNode)parent).getChildren().get(index);
	// }
	//
	// public int getChildCount(DeptNode parent) {
	// return ((DeptNode)parent).getChildren().size(); //each node has 5
	// children
	// }
	//
	// public int getIndexOfChild(DeptNode parent, DeptNode child) {
	// List<DeptNode> children = ((DeptNode)parent).getChildren();
	// for (int i = 0; i < children.size(); i++) {
	// if (children.get(i).equals(children))
	// return i;
	// }
	// return -1;
	// }
	/**
	 * remove the nodes which parent is <code>parent</code> with indexes
	 * <code>indexes</code>
	 *
	 * @param parent
	 *            The parent of nodes are removed
	 * @param indexFrom
	 *            the lower index of the change range
	 * @param indexTo
	 *            the upper index of the change range
	 * @throws IndexOutOfBoundsException
	 *             - indexFrom < 0 or indexTo > number of parent's children
	 */
	public void remove(DefaultTreeNode<DeptNode> parent, int indexFrom, int indexTo) throws IndexOutOfBoundsException {
		DefaultTreeNode<DeptNode> stn = parent;
		for (int i = indexTo; i >= indexFrom; i--) {
			try {
				stn.getChildren().remove(i);
			} catch (NullPointerException | IndexOutOfBoundsException en) {
				LogUtils.addLog(en);
			}
		}
	}

	public void remove(DefaultTreeNode<DeptNode> target) throws IndexOutOfBoundsException {
		int index;
		// find the parent and index of target
		DefaultTreeNode<DeptNode> parent = dfSearchParent(_root, target);
		for (index = 0; index < parent.getChildCount(); index++) {
			if (parent.getChildAt(index).equals(target)) {
				break;
			}
		}
		remove(parent, index, index);
	}

	/**
	 * insert new nodes which parent is <code>parent</code> with indexes
	 * <code>indexes</code> by new nodes <code>newNodes</code>
	 *
	 * @param parent
	 *            The parent of nodes are inserted
	 * @param indexFrom
	 *            the lower index of the change range
	 * @param indexTo
	 *            the upper index of the change range
	 * @param newNodes
	 *            New nodes which are inserted
	 * @throws IndexOutOfBoundsException
	 *             - indexFrom < 0 or indexTo > number of parent's children
	 */
	public void insert(DefaultTreeNode<DeptNode> parent, int indexFrom, int indexTo,
			DefaultTreeNode<DeptNode>[] newNodes) throws IndexOutOfBoundsException {
		DefaultTreeNode<DeptNode> stn = parent;
		for (int i = indexFrom; i <= indexTo; i++) {
			try {
				stn.getChildren().add(i, newNodes[i - indexFrom]);
			} catch (NullPointerException | IndexOutOfBoundsException en) {
				LogUtils.addLog(en);
			}
		}
	}

	/**
	 * append new nodes which parent is <code>parent</code> by new nodes
	 * <code>newNodes</code>
	 *
	 * @param parent
	 *            The parent of nodes are appended
	 * @param newNodes
	 *            New nodes which are appended
	 */
	public void add(DefaultTreeNode<DeptNode> parent, DefaultTreeNode<DeptNode>[] newNodes) {
		DefaultTreeNode<DeptNode> stn = (DefaultTreeNode<DeptNode>) parent;

		for (int i = 0; i < newNodes.length; i++) {
			List<TreeNode<DeptNode>> childs = stn.getChildren();
			if (childs == null) {
				// stn.add(newNodes[i]);
			} else {
				childs.add(newNodes[i]);
			}
		}
	}

	private DefaultTreeNode<DeptNode> dfSearchParent(DefaultTreeNode<DeptNode> node, DefaultTreeNode<DeptNode> target) {
		if (node.getChildren() != null && node.getChildren().contains(target)) {
			return node;
		} else {
			int size = getChildCount(node);
			for (int i = 0; i < size; i++) {
				DefaultTreeNode<DeptNode> parent = dfSearchParent((DefaultTreeNode<DeptNode>) getChild(node, i),
						target);
				if (parent != null) {
					return parent;
				}
			}
		}
		return null;
	}
};
