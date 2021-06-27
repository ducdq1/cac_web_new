package com.viettel.module.sso.BO;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CasUser.class)
public abstract class CasUser_ {

	public static volatile SingularAttribute<CasUser, String> address;
	public static volatile SingularAttribute<CasUser, Long> deptId;
	public static volatile SingularAttribute<CasUser, String> fullName;
	public static volatile SingularAttribute<CasUser, String> company;
	public static volatile SingularAttribute<CasUser, String> userName;
	public static volatile SingularAttribute<CasUser, Integer> type;
	public static volatile SingularAttribute<CasUser, Boolean> isActive;
	public static volatile SingularAttribute<CasUser, Long> casUserId;

}

