package com.viettel.core.user.BO;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Roles.class)
public abstract class Roles_ {

	public static volatile SingularAttribute<Roles, Long> posId;
	public static volatile SingularAttribute<Roles, String> deptName;
	public static volatile SingularAttribute<Roles, Long> roleId;
	public static volatile SingularAttribute<Roles, String> roleCode;
	public static volatile SingularAttribute<Roles, String> roleName;
	public static volatile SingularAttribute<Roles, Long> deptId;
	public static volatile SingularAttribute<Roles, String> description;
	public static volatile SingularAttribute<Roles, Department> dept;
	public static volatile SingularAttribute<Roles, Long> status;

}

