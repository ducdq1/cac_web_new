package com.viettel.core.user.BO;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Position.class)
public abstract class Position_ {

	public static volatile SingularAttribute<Position, Long> posId;
	public static volatile SingularAttribute<Position, String> posName;
	public static volatile SingularAttribute<Position, Long> deptId;
	public static volatile SingularAttribute<Position, String> description;
	public static volatile SingularAttribute<Position, String> posCode;
	public static volatile SingularAttribute<Position, Long> posOrder;
	public static volatile SingularAttribute<Position, Long> status;

}

