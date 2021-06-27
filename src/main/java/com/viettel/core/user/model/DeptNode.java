/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.viettel.core.base.model.TreeNode;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.utils.Constants;

/**
 *
 * @author ChucHV
 */
public class DeptNode extends TreeNode {

	public static enum TYPE {

		FULL, DEPT_ONLY, SPECIAL_DEPT // Them cac node Tat ca, don vi cha, don
										// vi con, don vi hien tai
	};

	private TYPE type;
	private Department department;

	public DeptNode(Department department) {
		super(department.getDeptId(), department.getDeptName());
		this.department = department;
		this.type = TYPE.FULL;
	}

	public DeptNode(Department department, TYPE type) {
		super(department.getDeptId(), department.getDeptName());
		this.department = department;
		this.type = type;
	}

	public DeptNode(Long id, String name) {
		super(id, name);
		this.type = TYPE.FULL;
	}

	public DeptNode(Long id, String name, TYPE type) {
		super(id, name);
		this.type = type;
	}

	/*
	 * Kiem tra department co department con hay user khong Neu co: return true
	 * Neu khong: return false
	 */
	@Override
	public boolean isLeaf() {
		if (listChild != null) {
			return listChild.isEmpty();
		} else {
			// Lay danh sach cac don vi con
			listChild = new ArrayList<>();
			DepartmentDAOHE ddhe = new DepartmentDAOHE();
			List<Department> listDept = ddhe.getAllChildIdByParentId(getId(), Constants.Status.ACTIVE);
			if (!listDept.isEmpty()) {
				for (Department dept : listDept) {
					listChild.add(new DeptNode(dept, this.type));
				}
				// return listChild.isEmpty();
			}

			switch (type) {
			case FULL:// neu la full, tiep tuc lay danh sach user cua don vi
				UserDAOHE userDAOHE = new UserDAOHE();
				Long deptId = getId();
				boolean phanCongChuyenGia = false;
				if (deptId.equals(Constants.DEPARTMENT.TIEU_BAN_PHAP_CHE)
						|| deptId.equals(Constants.DEPARTMENT.TIEU_BAN_SP)) {
					phanCongChuyenGia = true;
				}
				List<Users> listUser = userDAOHE.getAllUsersOfDepartment(getId(), Constants.Status.ACTIVE);

				if (phanCongChuyenGia) {
					if (listUser != null && !listUser.isEmpty()) {
						Collections.sort(listUser, new Comparator<Users>() {
							public int compare(Users p1, Users p2) {
								return (p2.getPosId()).compareTo(p1.getPosId());
							}
						});
					}
					listUser = new UserDAOHE().evictUsers(listUser);
					for (Users user : listUser) {
						user.setFullName(user.getFullName()+" - " +user.getPosName());
						listChild.add(new UserNode(user));
					}
				} else {
					for (Users user : listUser) {
						listChild.add(new UserNode(user));
					}
				}

				return listChild.isEmpty();
			case DEPT_ONLY:// neu la dept_only, tra lai true
				return listChild.isEmpty();
			default:
				return listChild.isEmpty();
			}
		}
	}

	/*
	 * Con cua 1 department hoac la cac department con hoac la cac user Hien tai
	 * chua ho tro con la ca department va user
	 */
	@Override
	public TreeNode getChild(int i) {
		return listChild.get(i);
	}

	/*
	 * Con cua 1 department hoac la cac department con hoac la cac user Hien tai
	 * chua ho tro con la ca department va user
	 */
	@Override
	public int getChildCount() {
		if (listChild != null) {
			return listChild.size();
		}

		listChild = new ArrayList<>();

		// Lay danh sach cac department con
		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		List<Department> listDept = ddhe.getAllChildIdByParentId(getId(), Constants.Status.ACTIVE);
		if (!listDept.isEmpty()) {
			for (Department dept : listDept) {
				listChild.add(new DeptNode(dept, this.type));
			}
			// return listChild.size();
		}

		switch (type) {
		case FULL:// neu la full, tiep tuc lay danh sach cac user con
			UserDAOHE userDAOHE = new UserDAOHE();
			List<Users> listUser = userDAOHE.getAllUsersOfDepartment(getId(), Constants.Status.ACTIVE);
			for (Users user : listUser) {
				listChild.add(new UserNode(user));
			}
			return listChild.size();
		case DEPT_ONLY:// tra lai 0
			return listChild.size();
		default:
			return listChild.size();

		}
	}

	public Department getDepartment() {
		return department;
	}
}
