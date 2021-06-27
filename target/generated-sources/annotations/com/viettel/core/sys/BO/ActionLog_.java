package com.viettel.core.sys.BO;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ActionLog.class)
public abstract class ActionLog_ {

	public static volatile SingularAttribute<ActionLog, String> objectTitle;
	public static volatile SingularAttribute<ActionLog, String> ip;
	public static volatile SingularAttribute<ActionLog, Long> deptId;
	public static volatile SingularAttribute<ActionLog, Long> modun;
	public static volatile SingularAttribute<ActionLog, String> userName;
	public static volatile SingularAttribute<ActionLog, Long> userId;
	public static volatile SingularAttribute<ActionLog, Long> objectType;
	public static volatile SingularAttribute<ActionLog, Long> actionType;
	public static volatile SingularAttribute<ActionLog, String> oldData;
	public static volatile SingularAttribute<ActionLog, Date> actionDate;
	public static volatile SingularAttribute<ActionLog, Long> actionLogId;
	public static volatile SingularAttribute<ActionLog, Long> objectId;
	public static volatile SingularAttribute<ActionLog, String> actionName;

}

