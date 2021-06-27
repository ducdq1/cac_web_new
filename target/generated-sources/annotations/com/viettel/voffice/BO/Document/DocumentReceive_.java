package com.viettel.voffice.BO.Document;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DocumentReceive.class)
public abstract class DocumentReceive_ {

	public static volatile SingularAttribute<DocumentReceive, String> documentCode;
	public static volatile SingularAttribute<DocumentReceive, String> publishAgencyIds;
	public static volatile SingularAttribute<DocumentReceive, Long> documentReceiveId;
	public static volatile SingularAttribute<DocumentReceive, Long> deadlineByWd;
	public static volatile SingularAttribute<DocumentReceive, Long> documentType;
	public static volatile SingularAttribute<DocumentReceive, Long> documentAreaId;
	public static volatile SingularAttribute<DocumentReceive, String> replyForDocument;
	public static volatile SingularAttribute<DocumentReceive, Date> publishDate;
	public static volatile SingularAttribute<DocumentReceive, Date> receiveDate;
	public static volatile SingularAttribute<DocumentReceive, String> documentAbstract;
	public static volatile SingularAttribute<DocumentReceive, Long> numberOfDoc;
	public static volatile SingularAttribute<DocumentReceive, Long> numberOfPage;
	public static volatile SingularAttribute<DocumentReceive, Long> priorityId;
	public static volatile SingularAttribute<DocumentReceive, String> publishAgencyName;
	public static volatile SingularAttribute<DocumentReceive, Long> sendPacking;
	public static volatile SingularAttribute<DocumentReceive, Long> securityType;
	public static volatile SingularAttribute<DocumentReceive, Long> receiveTypeId;
	public static volatile SingularAttribute<DocumentReceive, Long> isLawDocument;
	public static volatile SingularAttribute<DocumentReceive, Date> deadlineByDate;
	public static volatile SingularAttribute<DocumentReceive, String> signer;
	public static volatile SingularAttribute<DocumentReceive, Long> status;

}

