package com.viettel.module.phamarcy.BO;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PhaDocumentary.class)
public abstract class PhaDocumentary_ {

	public static volatile SingularAttribute<PhaDocumentary, String> note;
	public static volatile SingularAttribute<PhaDocumentary, String> orgName;
	public static volatile SingularAttribute<PhaDocumentary, Date> receptionDate;
	public static volatile SingularAttribute<PhaDocumentary, Long> phaDocumentaryId;
	public static volatile SingularAttribute<PhaDocumentary, Date> sendDate;
	public static volatile SingularAttribute<PhaDocumentary, Long> creatorId;
	public static volatile SingularAttribute<PhaDocumentary, String> orgAddress;
	public static volatile SingularAttribute<PhaDocumentary, Long> isActive;
	public static volatile SingularAttribute<PhaDocumentary, String> orgPhone;
	public static volatile SingularAttribute<PhaDocumentary, String> content;
	public static volatile SingularAttribute<PhaDocumentary, String> MedicineName;
	public static volatile SingularAttribute<PhaDocumentary, byte[]> rejectContent;
	public static volatile SingularAttribute<PhaDocumentary, String> fileNo;
	public static volatile SingularAttribute<PhaDocumentary, String> medicineForm;
	public static volatile SingularAttribute<PhaDocumentary, String> receptionNo;
	public static volatile SingularAttribute<PhaDocumentary, String> orgFax;
	public static volatile SingularAttribute<PhaDocumentary, Long> fileType;
	public static volatile SingularAttribute<PhaDocumentary, Long> fileId;
	public static volatile SingularAttribute<PhaDocumentary, Date> createDate;

}

