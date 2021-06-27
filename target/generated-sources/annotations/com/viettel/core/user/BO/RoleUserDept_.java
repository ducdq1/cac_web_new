package com.viettel.core.user.BO;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RoleUserDept.class)
public abstract class RoleUserDept_ {

	public static volatile SingularAttribute<RoleUserDept, Roles> role;
	public static volatile SingularAttribute<RoleUserDept, Long> roleId;
	public static volatile SingularAttribute<RoleUserDept, Long> deptId;
	public static volatile SingularAttribute<RoleUserDept, Long> roleUserDeptId;
	public static volatile SingularAttribute<RoleUserDept, Long> isAdmin;
	public static volatile SingularAttribute<RoleUserDept, Department> dept;
	public static volatile SingularAttribute<RoleUserDept, Long> isActive;
	public static volatile SingularAttribute<RoleUserDept, Long> userId;

}

