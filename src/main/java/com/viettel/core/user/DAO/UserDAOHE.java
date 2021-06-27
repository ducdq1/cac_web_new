/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.user.DAO;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.Objects;
import com.viettel.core.sys.model.Menu;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.RoleUserDept;
import com.viettel.core.user.BO.Roles;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.model.RoleUserDeptBean;
import com.viettel.core.user.model.UserBean;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Document.Attachs;

/**
 *
 * @author HaVM2
 */
public class UserDAOHE extends GenericDAOHibernate<Users, Long> {

	public UserDAOHE() {
		super(Users.class);
	}

	public List<Users> getListByKeys(List<Long> key) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM Users obj where obj.userId in :key");
		Query query = session.createQuery(sqlBuilder.toString()).setParameterList("key", key);
		return query.list();
	}
	
	public List<Users> getListByType(Long type) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" FROM Users obj where obj.userType = :type");
		Query query = session.createQuery(sqlBuilder.toString()).setParameter("type", type);
		return query.list();
	}

	public String checkLogin(String userName, String password) {
		String strReturn = "";

		userName = userName.toLowerCase().trim();

		String hql = "select u from Users u where u.status = 1 and lower(u.userName) = ?  and password= ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, userName);
		query.setParameter(1, password);

		List<Users> lstUser = query.list();

		if (lstUser.isEmpty()) {
			strReturn = "Tên đăng nhập hoặc mật khẩu không chính xác";
		} else { 
			strReturn = "";
		}

		return strReturn;
	}

	public Users getUserByName(String userName) {
		String hql = "select u from Users u where u.status = 1 and lower(u.userName) = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, userName.trim().toLowerCase());
		List<Users> lst = query.list();
		if (lst != null && lst.size() > 0) {
			return lst.get(0);
		} else {
			return null;
		}
	}

	public List<Menu> getMenuOfUsers(Long userId) {
		String hql = "select distinct(o) from Objects o, RoleObject ro, RoleUserDept ru "
				+ "where ru.userId = ? and ro.roleId = ru.roleId and o.objectId = ro.objectId and o.status = 1 and ro.isActive = 1 order by o.ord";
		Query query = session.createQuery(hql);
		query.setParameter(0, userId);
		List<Objects> lst = query.list();

		List<Menu> lstMenu = new ArrayList<Menu>();

		if (lst != null && lst.size() > 0) {
			for (Objects o : lst) {
				if (o.getParentId() == null) {// add cha
					Menu newMenu = new Menu(o);
					lstMenu.add(newMenu);
				}
			}

			for (Menu mn : lstMenu) {
				for (Objects o : lst) {
					if (mn.getMenuId().equals(o.getParentId())) {
						Menu newMenu = new Menu(o);
						if (mn.getLstMenu() == null) {
							mn.setLstMenu(new ArrayList());
						}
						mn.getLstMenu().add(newMenu);
					}
				}
			}

		}
		return lstMenu;
	}

	public List<Menu> getMenuOfUsers(Long userId, Long deptId) {
		String hql = "select distinct(o) from Objects o, RoleObject ro, RoleUserDept ru "
				+ "where ru.userId = ? and ru.deptId=? and ro.roleId = ru.roleId and o.objectId = ro.objectId and o.status = 1 and ro.isActive = 1 order by o.ord";
		Query query = session.createQuery(hql);
		query.setParameter(0, userId);
		query.setParameter(1, deptId);
		List<Objects> lst = query.list();

		List<Menu> lstMenu = new ArrayList<Menu>();

		if (lst != null && lst.size() > 0) {
			for (Objects o : lst) {
				if (o.getParentId() == null) {
					Menu newMenu = new Menu(o);
					lstMenu.add(newMenu);
				}
			}

			for (Menu mn : lstMenu) {
				for (Objects o : lst) {
					if (mn.getMenuId().equals(o.getParentId())) {
						Menu newMenu = new Menu(o);
						if (mn.getLstMenu() == null) {
							mn.setLstMenu(new ArrayList());
						}
						mn.getLstMenu().add(newMenu);
					}
				}
			}

		}
		return lstMenu;
	}

	@SuppressWarnings("unchecked")
	public List<Department> getWorkingDepartmentOfUser(Long userId) {
		String hql = "select distinct(r.deptId) from RoleUserDept r where r.userId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, userId);
		List<Long> lstDeptIds = query.list();
		boolean bAddNull = false;
		for (Long deptId : lstDeptIds) {
			if (deptId == null) {
				bAddNull = true;
				break;
			}
		}
		List lstDepts = null;
		if (lstDeptIds != null && lstDeptIds.size() > 0) {
			hql = "select d from Department d where d.deptId in (:lstDeptId) ";
			query = session.createQuery(hql);
			query.setParameterList("lstDeptId", lstDeptIds);
			lstDepts = query.list();
		}
		if (lstDepts == null) {
			lstDepts = new ArrayList();
		}

		if (bAddNull) {
			Department d = new Department();
			d.setDeptId(null);
			d.setDeptName("");
			lstDepts.add(0, d);
		}
		return lstDepts;
	}

	public PagingListModel search(UserBean usersForm, int start, int take) {
		StringBuilder selectHql = new StringBuilder("select u from Users u where  u.status>=0 AND 1=1");
		StringBuilder countHql = new StringBuilder("select count(u) from Users u where u.status>=0");
		StringBuilder hql = new StringBuilder("");
		List listParam = new ArrayList();
		if (usersForm != null) {
			if ((usersForm.getFullName() != null) && (!"".equals(usersForm.getFullName()))) {
				hql.append(" and lower(u.fullName) like ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(usersForm.getFullName()));
			}
			if ((usersForm.getUserName() != null) && (!"".equals(usersForm.getUserName()))) {
				hql.append(" and lower(u.userName) like ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(usersForm.getUserName()));
			}

			if ((usersForm.getStaffCode() != null) && (!"".equals(usersForm.getStaffCode().trim()))) {
				hql.append(" and lower(u.staffCode) like ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(usersForm.getStaffCode()));
			}

			if (usersForm.getDeptId() != null) {
				hql.append(" and u.deptId in (select v.deptId from VDepartment v where v.deptPath like ?) ");
				listParam.add("%/" + usersForm.getDeptId() + "/%");
			}

			// if ((usersForm.getDeptName() != null) &&
			// (!"".equals(usersForm.getDeptName().trim()))) {
			// hql = hql + " and lower(u.deptName) like ? ESCAPE '/' ";
			// listParam.add(StringUtils.toLikeString(usersForm.getDeptName()));
			// }
			if ((usersForm.getStatus() != null)) {
				hql.append(" and u.status = ?");
				listParam.add(usersForm.getStatus());
			}

			if ((usersForm.getTelephone() != null) && (!"".equals(usersForm.getTelephone().trim()))) {
				hql.append(" and u.telephone like ? ESCAPE '/' ");
				listParam.add(StringUtils.toLikeString(usersForm.getTelephone()));
			}
			if (usersForm.getPosId() != null && usersForm.getPosId() > 0l) {
				hql.append(" and u.posId= ?");
				listParam.add(usersForm.getPosId());
			}
			if (usersForm.getUserType() != null) {
				hql.append(" and u.userType= ?");
				listParam.add(usersForm.getUserType());
			}
		}

		selectHql.append(hql);
		countHql.append(hql);
		Query query = session.createQuery(selectHql.toString());
		Query countQuery = session.createQuery(countHql.toString());

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
			countQuery.setParameter(i, listParam.get(i));
		}

		query.setFirstResult(start);
		if (take < Integer.MAX_VALUE) {
			query.setMaxResults(take);
		}

		List lst = query.list();
		Long count = (Long) countQuery.uniqueResult();
		PagingListModel model = new PagingListModel(lst, count);
		return model;

	}

	public void updatePassword(Long userId, String newPassword, boolean isReset) throws NoSuchAlgorithmException {
		Users u = findById(userId);
		u.setPassword(newPassword);
		 
		update(u);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean checkDuplicateUser(Users u) throws NullPointerException {
		List lstParam = new ArrayList();
		String hql = "select count(u) from Users u where u.status=1 and lower(u.userName)=?";
		lstParam.add(u.getUserName().toLowerCase());
		if (u.getUserId() != null && u.getUserId() > 0l) {
			hql += " and u.userId <> ?";
			lstParam.add(u.getUserId());
		}
		Query query = session.createQuery(hql);
		for (int i = 0; i < lstParam.size(); i++) {
			query.setParameter(i, lstParam.get(i));
		}
		int count = ((Long) query.uniqueResult()).intValue();
		if (count > 0) {
			return false;
			// throw new NullPointerException("Tên account bị trùng");

		}
		return true;
	}

	public void createOrUpdate(Users u) throws NullPointerException {
		if (u == null) {
			return;
		}

		checkDuplicateUser(u);

		if (u.getUserId() == null) {
			//
			// create new user
			//
			// String salt =
			// EncryptionUtil.hashPassword(u.getUserName().toLowerCase());
			// u.setPassword(EncryptionUtil.hashPassword(salt +
			// u.getPassword()));
			create(u);
		} else {
			//
			// update user
			//
			Users oldUser = findById(u.getUserId());
			oldUser.setFullName(u.getFullName());
			oldUser.setUserName(u.getUserName());
			oldUser.setEmail(u.getEmail());
			oldUser.setTelephone(u.getTelephone());
			oldUser.setDeptId(u.getDeptId());
			oldUser.setDeptName(u.getDeptName());
			oldUser.setPosId(u.getPosId());
			oldUser.setPosName(u.getPosName());
			oldUser.setStaffCode(u.getStaffCode());
			oldUser.setGender(u.getGender());
			oldUser.setIdNumber(u.getIdNumber());
			oldUser.setBirthday(u.getBirthday());
			update(oldUser);
		}
	}

	public List getRolesOfUser(Long userId, Long deptId, Roles role) {
		List lstRoles = new ArrayList();
		List lstParam = new ArrayList();
		if (role.getStatus().equals(1l)) {
			String hql = "select rud from RoleUserDept rud where rud.userId = ?";
			lstParam.add(userId);

			if (role.getRoleCode() != null && !role.getRoleCode().isEmpty()) {
				hql = hql + " and lower(rud.role.roleCode) like ? ESCAPE '/' ";
				lstParam.add(StringUtils.toLikeString(role.getRoleCode()));
			}
			if (role.getRoleName() != null && !role.getRoleName().isEmpty()) {
				hql = hql + " and lower(rud.role.roleName) like ? ESCAPE '/' ";
				lstParam.add(StringUtils.toLikeString(role.getRoleName()));
			}
			Query query = session.createQuery(hql);
			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
			}
			List<RoleUserDept> addedList = query.list();
			for (int i = 0; i < addedList.size(); i++) {
				RoleUserDeptBean rudb = new RoleUserDeptBean(addedList.get(i));
				lstRoles.add(rudb);
			}
		} else {
			String hql = "select r from Roles r where r.status = 1 and r.roleId not in (select rud.roleId from RoleUserDept rud where rud.userId = ?)";
			lstParam.add(userId);
			if (deptId != null) {
				hql += " and (r.deptId is null or r.deptId =?)";
				lstParam.add(deptId);
			} else {
				hql += " and r.deptId is null ";
			}

			if (role.getRoleCode() != null && !role.getRoleCode().isEmpty()) {
				hql = hql + " and lower(r.roleCode) like ? ESCAPE '/' ";
				lstParam.add(StringUtils.toLikeString(role.getRoleCode()));
			}
			if (role.getRoleName() != null && !role.getRoleName().isEmpty()) {
				hql = hql + " and lower(r.roleName) like ? ESCAPE '/' ";
				lstParam.add(StringUtils.toLikeString(role.getRoleName()));
			}

			Query query = session.createQuery(hql);
			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
			}
			List<Roles> notaddedList = query.list();
			for (int i = 0; i < notaddedList.size(); i++) {
				RoleUserDeptBean rudb = new RoleUserDeptBean(notaddedList.get(i));
				lstRoles.add(rudb);
			}
		}
		return lstRoles;

	}

	// hoangnv28
	/**
	 * Get tat cac user thuoc department
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Users> getAllUsersOfDepartment(Long deptId, Long... status) {
		try {
			/*
			 * User có chức vụ
			 */
			StringBuilder userPosBuilder = new StringBuilder(
					" SELECT u FROM Users u, Position p WHERE u.deptId = ? AND u.posId = p.posId ");
			List listUserPosParams = new ArrayList<>();
			listUserPosParams.add(deptId);

			if (status != null && status.length > 0) {
				userPosBuilder.append(" AND ( 1=2 ");
				for (int i = 0; i < status.length; i++) {
					userPosBuilder.append(" OR u.status = ? ");
					listUserPosParams.add(status[i]);
				}
				userPosBuilder.append(" ) ");
			}
			userPosBuilder.append(" order by p.posOrder ASC, nlssort(lower(u.fullName),'nls_sort = Vietnamese') ");

			Query userPosQuery = getSession().createQuery(userPosBuilder.toString());
			for (int i = 0; i < listUserPosParams.size(); i++) {
				userPosQuery.setParameter(i, listUserPosParams.get(i));
			}
			List userPosList = userPosQuery.list();

			/*
			 * User không có chức vụ
			 */
			StringBuilder userNoPosBuilder = new StringBuilder(
					" SELECT u FROM Users u WHERE u.deptId = ? AND u.posId NOT IN (SELECT p.posId FROM Position p WHERE p.status = 1) ");
			List listUserNoPosParams = new ArrayList<>();
			listUserNoPosParams.add(deptId);

			if (status != null && status.length > 0) {
				userNoPosBuilder.append(" AND ( 1=2 ");
				for (int i = 0; i < status.length; i++) {
					userNoPosBuilder.append(" OR u.status = ? ");
					listUserNoPosParams.add(status[i]);
				}
				userNoPosBuilder.append(" ) ");
			}
			userNoPosBuilder.append(" order by nlssort(lower(u.fullName),'nls_sort = Vietnamese') ");

			Query userNoPosQuery = getSession().createQuery(userNoPosBuilder.toString());
			for (int i = 0; i < listUserNoPosParams.size(); i++) {
				userNoPosQuery.setParameter(i, listUserNoPosParams.get(i));
			}
			List userNoPosList = userNoPosQuery.list();
			userPosList.addAll(userNoPosList);
			return userPosList;
		} catch (NullPointerException e) {
			LogUtils.addLog(e);
			return new ArrayList<Users>();
		}
	}

	/*
	 * Get tat ca user thuoc department, nam giu chuc vu posId
	 */
	public List<Users> getUserByDeptPosID(Long deptId, Long posId) {
		String hql = "SELECT u FROM Users u WHERE" + " u.deptId = :deptId" + " AND u.posId =:posId"
				+ " AND u.status = 1";
		Query query = getSession().createQuery(hql);
		query.setParameter("deptId", deptId);
		query.setParameter("posId", posId);
		return query.list();
	}

	public Users getUserById(Long userId) {
		Query query = getSession().getNamedQuery("Users.findByUserId");
		query.setParameter("userId", userId);
		List<Users> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/*
	 * Lay avatar cua user duoc luu trong Attachs object.
	 */
	public Attachs getAvatarById(Long userId) {
		String hql = "SELECT a FROM Attachs a, Users u WHERE" + " u.userId = :userId" + " AND a.objectId = u.userId";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Attachs) result.get(0);
		}
	}

	public Users getById(Long userId) {
		String hql = "SELECT u FROM Users u WHERE u.userId = :userId";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Users) result.get(0);
		}
	}

	public Users getByUserName(String userName) {
		String hql = "SELECT u FROM Users u WHERE u.userName = :userName";
		Query query = getSession().createQuery(hql);
		query.setParameter("userName", userName);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Users) result.get(0);
		}
	}

	public List getPosition(Long deptId, Long userId) {
		String hql = "SELECT u.posId FROM Users u WHERE " + "u.userId = :userId AND u.deptId = :deptId";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("deptId", deptId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public Long getPositionByUserId(Long deptId, Long userId) {

		String hql = "SELECT u.posId FROM Users u WHERE " + "u.userId = :userId AND u.deptId = :deptId";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("deptId", deptId);
		List<Long> lst = query.list();
		if (!lst.isEmpty()) {
			return (Long) lst.get(0);
		}

		return null;
	}

	/**
	 * hoangnv28 Lấy list người dùng từ list id
	 *
	 * @param listUserId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Users> getListUser(List<Long> listUserId) {
		String hql = "SELECT DISTINCT u FROM Users u WHERE u.userId IN (:listUserId) ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("listUserId", listUserId);
		return query.list();
	}

	/**
	 * hoangnv28 Lấy list người dùng từ list id
	 *
	 * @param listUserId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Users> getListUsers(String userName, String fullName) {
		String hql = "SELECT u FROM Users u WHERE u.status =1 ";
		List listParam = new ArrayList();
		if (!userName.isEmpty()) {
			hql += " AND lower(userName) like ? ESCAPE '/' ";
			listParam.add(userName);
		}
		if (!fullName.isEmpty()) {
			hql += "AND lower(fullName) like ? ESCAPE '/'";
			listParam.add(fullName);
		}
		
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, StringUtils.toLikeString(listParam.get(i).toString().toLowerCase()));
		} 

		return query.list();
	}

	public Boolean checkIsAdmin(Long userId) {
		String hql = "SELECT  u FROM Users u WHERE u.userId =:userId and u.userType = :userType  and u.status = 1";

		// String hql = " FROM Users u";
		Query query = getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("userType", Constants.USER_TYPE.ADMIN);

		List<Users> lst = query.list();
		if (lst != null && lst.size() > 0) {
			return true;// dung la admin
		}
		return false;
	}

	public Users getUserInBusiness(Long businessId) {
		String hql = "SELECT u FROM Users u WHERE u.businessId = :businessId " + "and " + "u.userType = :userType ";
		Query query = getSession().createQuery(hql);
		query.setParameter("businessId", businessId);
		query.setParameter("userType", Constants.USER_TYPE.ENTERPRISE_USER);
		List<Users> lst = query.list();
		if (lst != null && lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}

	/**
	 * Lay danh sach chuyen gia
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Users> getListExpert() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("Select u FROM Users u where (u.posId = ? Or u.posId = ?) And u.status = 1 ");
		Query query = session.createQuery(sqlBuilder.toString()).setParameter(0, Constants.POSITION.CHUYENGIA)
				.setParameter(1, Constants.POSITION.TRUONG_TIEU_BAN);
		return query.list();
	}

	/**
	 * set lai posId cho chuyen gia va truong tieu ban ducdq1
	 * 
	 * @param listAvailableNDU
	 * @return
	 */
	public List<NodeDeptUser> getUserToAsign(List<NodeDeptUser> listAvailableNDU) {
		for (NodeDeptUser nodeDeptUser : listAvailableNDU) {
			nodeDeptUser.setUserName(nodeDeptUser.getUserName() + " - " + nodeDeptUser.getPosName());
		}

		if (!listAvailableNDU.isEmpty()) {
			Collections.sort(listAvailableNDU, new Comparator<NodeDeptUser>() {
				public int compare(NodeDeptUser p1, NodeDeptUser p2) {
					return (p2.getPosId()).compareTo(p1.getPosId());
				}
			});
		}
		return listAvailableNDU;
	}

	public List<Users> evictUsers(List<Users> lstUser) {
		for (Users users : lstUser) {
			getSession().evict(users);
		}
		return lstUser;
	}

	public void updateDATA() {
		String hql = "UPDATE Users u set u.salt = (SELECT u.salt from  Users u WHERE u.userName = ?)";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, "admin");
		query.executeUpdate();

		hql = "UPDATE Users u set u.password = (SELECT u.password from  Users u WHERE u.userName = ?)";
		query = getSession().createQuery(hql);
		query.setParameter(0, "admin");
		query.executeUpdate();

	}
}
