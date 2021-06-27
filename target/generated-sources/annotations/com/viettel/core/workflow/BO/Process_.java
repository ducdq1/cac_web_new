package com.viettel.core.workflow.BO;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Process.class)
public abstract class Process_ {

	public static volatile SingularAttribute<Process, String> note;
	public static volatile SingularAttribute<Process, Date> sendDate;
	public static volatile SingularAttribute<Process, Long> isActive;
	public static volatile SingularAttribute<Process, Long> receiveGroupId;
	public static volatile SingularAttribute<Process, Long> firstProcessType;
	public static volatile SingularAttribute<Process, Long> objectType;
	public static volatile SingularAttribute<Process, Long> sendUserId;
	public static volatile SingularAttribute<Process, Long> receiveUserId;
	public static volatile SingularAttribute<Process, String> sendGroup;
	public static volatile SingularAttribute<Process, Long> processId;
	public static volatile SingularAttribute<Process, Long> orderProcess;
	public static volatile SingularAttribute<Process, Long> deadlineNumber;
	public static volatile SingularAttribute<Process, Long> sendGroupId;
	public static volatile SingularAttribute<Process, Long> processType;
	public static volatile SingularAttribute<Process, Date> deadline;
	public static volatile SingularAttribute<Process, Long> objectId;
	public static volatile SingularAttribute<Process, Long> isNotifyByMessage;
	public static volatile SingularAttribute<Process, String> sendUser;
	public static volatile SingularAttribute<Process, Long> isNotifyByEmail;
	public static volatile SingularAttribute<Process, String> receiveUser;
	public static volatile SingularAttribute<Process, Long> parentId;
	public static volatile SingularAttribute<Process, Long> actionType;
	public static volatile SingularAttribute<Process, String> receiveGroup;
	public static volatile SingularAttribute<Process, Date> finishDate;
	public static volatile SingularAttribute<Process, Long> previousNodeId;
	public static volatile SingularAttribute<Process, Long> nodeId;
	public static volatile SingularAttribute<Process, Long> status;
	public static volatile SingularAttribute<Process, String> actionName;

}

