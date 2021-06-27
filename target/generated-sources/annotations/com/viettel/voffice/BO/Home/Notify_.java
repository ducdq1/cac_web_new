package com.viettel.voffice.BO.Home;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Notify.class)
public abstract class Notify_ {

	public static volatile SingularAttribute<Notify, Long> parentNotifyId;
	public static volatile SingularAttribute<Notify, String> functionCode;
	public static volatile SingularAttribute<Notify, String> multiUser;
	public static volatile SingularAttribute<Notify, String> title;
	public static volatile SingularAttribute<Notify, Long> type;
	public static volatile SingularAttribute<Notify, Long> userId;
	public static volatile SingularAttribute<Notify, String> content;
	public static volatile SingularAttribute<Notify, Long> objectType;
	public static volatile SingularAttribute<Notify, Date> sendTime;
	public static volatile SingularAttribute<Notify, String> notifyLink;
	public static volatile SingularAttribute<Notify, Long> sendUserId;
	public static volatile SingularAttribute<Notify, String> multiDept;
	public static volatile SingularAttribute<Notify, String> sendUserName;
	public static volatile SingularAttribute<Notify, Long> notifyId;
	public static volatile SingularAttribute<Notify, Date> endTime;
	public static volatile SingularAttribute<Notify, Long> attachId;
	public static volatile SingularAttribute<Notify, Long> objectId;
	public static volatile SingularAttribute<Notify, String> sendUserAvatar;
	public static volatile SingularAttribute<Notify, Long> status;

}

