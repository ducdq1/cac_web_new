package com.viettel.module.phamarcy.BO;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Order.class)
public abstract class Order_ {

	public static volatile SingularAttribute<Order, String> note;
	public static volatile SingularAttribute<Order, String> orderNumber;
	public static volatile SingularAttribute<Order, Date> modifyDate;
	public static volatile SingularAttribute<Order, Long> orderId;
	public static volatile SingularAttribute<Order, String> companyName;
	public static volatile SingularAttribute<Order, String> createUserFullNameSearch;
	public static volatile SingularAttribute<Order, String> createUserFullName;
	public static volatile SingularAttribute<Order, Long> type;
	public static volatile SingularAttribute<Order, Long> userCreate;
	public static volatile SingularAttribute<Order, String> companyPhone;
	public static volatile SingularAttribute<Order, Long> userModify;
	public static volatile SingularAttribute<Order, String> companyAdd;
	public static volatile SingularAttribute<Order, Date> orderDate;
	public static volatile SingularAttribute<Order, Date> createDate;

}

