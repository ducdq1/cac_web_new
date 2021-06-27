package com.viettel.voffice.BO.Calendar;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Calendar.class)
public abstract class Calendar_ {

	public static volatile SingularAttribute<Calendar, Date> timeEnd;
	public static volatile SingularAttribute<Calendar, Date> recurrenceEndDate;
	public static volatile SingularAttribute<Calendar, Long> createUserId;
	public static volatile SingularAttribute<Calendar, String> locationName;
	public static volatile SingularAttribute<Calendar, String> description;
	public static volatile SingularAttribute<Calendar, String> manParticipant;
	public static volatile SingularAttribute<Calendar, String> title;
	public static volatile SingularAttribute<Calendar, Long> recurrenceType;
	public static volatile SingularAttribute<Calendar, Long> recurrenceDay;
	public static volatile SingularAttribute<Calendar, Date> timeStart;
	public static volatile SingularAttribute<Calendar, Date> recurrenceStartDate;
	public static volatile SingularAttribute<Calendar, Long> calendarId;
	public static volatile SingularAttribute<Calendar, Long> locationId;
	public static volatile SingularAttribute<Calendar, String> manChief;
	public static volatile SingularAttribute<Calendar, String> manPrepare;
	public static volatile SingularAttribute<Calendar, Long> status;

}

