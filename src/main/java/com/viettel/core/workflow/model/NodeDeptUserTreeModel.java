/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.model;

import com.viettel.utils.Constants;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.base.model.TreeItem;
import java.util.ArrayList;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.TreeSelectableModel;

/**
 *
 * @author HaVM2
 */
public class NodeDeptUserTreeModel extends AbstractTreeModel implements TreeSelectableModel {

	private static final long serialVersionUID = 7822729366554623684L;
	private ArrayList _tree = null;
	private Long nodeId;

	public NodeDeptUserTreeModel() {
		super(new TreeItem(0l, "Cây đơn vị", 0l, 0l));
		TreeItem root = new TreeItem(-1l, "Cây đơn vị", 0l, 0l);
		_tree = new ArrayList();
		_tree.add(root);
	}

	public NodeDeptUserTreeModel(Long nodeId) {
		super(new TreeItem(0l, "Cây đơn vị", 0l, 0l));
		TreeItem root = new TreeItem(-1l, "Cây đơn vị", 0l, 0l);
		_tree = new ArrayList();
		_tree.add(root);
		this.nodeId = nodeId;
	}

	public NodeDeptUserTreeModel(ArrayList<TreeItem> tree) {
		super(tree);
		_tree = tree;
	}

	@Override
	public boolean isLeaf(Object e) {
		TreeItem item = (TreeItem) e;
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		int count = 0;
		if (item.getType() == 0l) {
			//
			// TreeItem la goc
			//
			count = ddhe.getCountChildDeptAndUser(item.getId());
			if (item.getId().equals(-5l)) {
				count += 6;
			}
		} else if (item.getType() == 1l) {
			//
			// TreeItem la nguoi dung
			//
			count = 0;
		}
		return (count == 0);
	}

	@Override
	public TreeItem getChild(Object e, int i) {
		TreeItem item = (TreeItem) e;
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		TreeItem child = null;
		if (item.getType() == 0l) {
			if (item.getId() == 0l) {
				if (i <= 5) {
					switch (i) {
					case 0:
						child = new TreeItem(Constants.NODE_ACTOR_RELATIVE.ALL, "Tất cả (*)", 0l, 0l);
						break;
					case 1:
						child = new TreeItem(Constants.NODE_ACTOR_RELATIVE.PARENT, "Đơn vị cha (..\\)", 0l, 0l);
						break;
					case 2:
						child = new TreeItem(Constants.NODE_ACTOR_RELATIVE.CURRENT, "Đơn vị hiện tại (.\\)", 0l, 0l);
						break;
					case 3:
						child = new TreeItem(Constants.NODE_ACTOR_RELATIVE.CHILD, "Đơn vị con (.\\*)", 0l, 0l);
						break;
					case 4:
						child = new TreeItem(Constants.NODE_ACTOR_RELATIVE.SAME_PARENT, "Đơn vị cùng cấp (..\\*)", 0l,
								0l);
						break;
					case 5:
						child = new TreeItem(Constants.NODE_ACTOR_RELATIVE.SAME_PARENT_LEVEL,
								"Đơn vị thuộc cùng n cấp đơn vị", 0l, 0l);
						break;
					}
					
					if (child != null) {
						if (ddhe.isDeptIsSelected(nodeId, child.getId())) {
							child.setSelected(1l);
						}
					}
				} else {
					i -= 6;
					int countDept = ddhe.getCountChildByParent(item.getId());
					if (countDept > i) {
						child = ddhe.getChildByParentAndIndexOfNode(nodeId, item.getId(), i);
					} else {
						int index = i - countDept;
						child = ddhe.getChildUserByParentAndIndex(nodeId, item.getId(), index);
					}
				}
			} else {
				int countDept = ddhe.getCountChildByParent(item.getId());
				if (countDept > i) {
					child = ddhe.getChildByParentAndIndexOfNode(nodeId, item.getId(), i);
				} else {
					int index = i - countDept;
					child = ddhe.getChildUserByParentAndIndex(nodeId, item.getId(), index);
				}
			}
		}
		return child;

	}

	@Override
	public int getChildCount(Object e) {
		TreeItem item = (TreeItem) e;
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		int count = 0;
		if (item.getType() == 0l) {
			//
			// TreeItem la goc
			//
			count = ddhe.getCountChildDeptAndUser(item.getId());
			if (item.getId() == 0l) {
				count += 6;
			}
		}
		return count;
	}
}
