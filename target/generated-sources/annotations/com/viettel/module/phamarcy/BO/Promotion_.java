package com.viettel.module.phamarcy.BO;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Promotion.class)
public abstract class Promotion_ {

	public static volatile SingularAttribute<Promotion, String> numberSaleOff;
	public static volatile SingularAttribute<Promotion, String> imageUrl;
	public static volatile SingularAttribute<Promotion, String> name;
	public static volatile SingularAttribute<Promotion, String> description;
	public static volatile SingularAttribute<Promotion, Long> id;
	public static volatile SingularAttribute<Promotion, Long> isActive;
	public static volatile SingularAttribute<Promotion, Date> createDate;

}

