package com.viettel.module.phamarcy.BO;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PhaFile.class)
public abstract class PhaFile_ {

	public static volatile SingularAttribute<PhaFile, Long> personalId;
	public static volatile SingularAttribute<PhaFile, String> code;
	public static volatile SingularAttribute<PhaFile, Long> agreement;
	public static volatile SingularAttribute<PhaFile, Date> businessSignedWhen;
	public static volatile SingularAttribute<PhaFile, Date> sendDate;
	public static volatile SingularAttribute<PhaFile, Date> expertDeadLine;
	public static volatile SingularAttribute<PhaFile, String> registerForm;
	public static volatile SingularAttribute<PhaFile, Long> isActive;
	public static volatile SingularAttribute<PhaFile, Long> isOntime;
	public static volatile SingularAttribute<PhaFile, String> processor;
	public static volatile SingularAttribute<PhaFile, Long> orgId;
	public static volatile SingularAttribute<PhaFile, Date> deadLine;
	public static volatile SingularAttribute<PhaFile, Date> createdDate;
	public static volatile SingularAttribute<PhaFile, String> businessSignedWho;
	public static volatile SingularAttribute<PhaFile, String> fileNo;
	public static volatile SingularAttribute<PhaFile, String> businessSignedWhere;
	public static volatile SingularAttribute<PhaFile, Long> systemFileId;
	public static volatile SingularAttribute<PhaFile, Date> deadLinePaidDate;
	public static volatile SingularAttribute<PhaFile, Long> oldFileId;
	public static volatile SingularAttribute<PhaFile, Date> onTimeDate;
	public static volatile SingularAttribute<PhaFile, Date> paymentDate;
	public static volatile SingularAttribute<PhaFile, Long> isAddition;
	public static volatile SingularAttribute<PhaFile, Long> fileType;
	public static volatile SingularAttribute<PhaFile, Long> fileId;

}

