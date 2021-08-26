package com.viettel.module.phamarcy.BO;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Quotation.class)
public abstract class Quotation_ {

	public static volatile SingularAttribute<Quotation, Date> saledDate;
	public static volatile SingularAttribute<Quotation, String> quotationNumber;
	public static volatile SingularAttribute<Quotation, String> note;
	public static volatile SingularAttribute<Quotation, String> fileName;
	public static volatile SingularAttribute<Quotation, Date> modifyDate;
	public static volatile SingularAttribute<Quotation, Long> quotationID;
	public static volatile SingularAttribute<Quotation, BigDecimal> totalPrice;
	public static volatile SingularAttribute<Quotation, String> cusName;
	public static volatile SingularAttribute<Quotation, String> createUserFullNameSearch;
	public static volatile SingularAttribute<Quotation, String> createUserCode;
	public static volatile SingularAttribute<Quotation, String> createUserFullName;
	public static volatile SingularAttribute<Quotation, Integer> type;
	public static volatile SingularAttribute<Quotation, Long> isInvalid;
	public static volatile SingularAttribute<Quotation, Date> quotationDate;
	public static volatile SingularAttribute<Quotation, String> cusPhone;
	public static volatile SingularAttribute<Quotation, Long> userCreate;
	public static volatile SingularAttribute<Quotation, String> cusAddress;
	public static volatile SingularAttribute<Quotation, Long> userModify;
	public static volatile SingularAttribute<Quotation, String> quoationUserName;
	public static volatile SingularAttribute<Quotation, Date> createDate;
	public static volatile SingularAttribute<Quotation, Integer> status;

}

