package com.viettel.module.phamarcy.BO;

import com.viettel.voffice.BO.Document.Attachs;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuotationDetail.class)
public abstract class QuotationDetail_ {

	public static volatile SingularAttribute<QuotationDetail, Long> quotationDetailId;
	public static volatile SingularAttribute<QuotationDetail, String> note;
	public static volatile SingularAttribute<QuotationDetail, Attachs> image;
	public static volatile SingularAttribute<QuotationDetail, String> unit;
	public static volatile SingularAttribute<QuotationDetail, Double> amount;
	public static volatile SingularAttribute<QuotationDetail, String> productCode;
	public static volatile SingularAttribute<QuotationDetail, Long> productId;
	public static volatile SingularAttribute<QuotationDetail, Boolean> isWarning;
	public static volatile SingularAttribute<QuotationDetail, Long> quotationId;
	public static volatile SingularAttribute<QuotationDetail, Long> price;
	public static volatile SingularAttribute<QuotationDetail, Long> attachId;
	public static volatile SingularAttribute<QuotationDetail, String> productName;

}

