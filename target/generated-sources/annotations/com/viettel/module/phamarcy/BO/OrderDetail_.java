package com.viettel.module.phamarcy.BO;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrderDetail.class)
public abstract class OrderDetail_ {

	public static volatile SingularAttribute<OrderDetail, String> note;
	public static volatile SingularAttribute<OrderDetail, String> unit;
	public static volatile SingularAttribute<OrderDetail, Double> amount;
	public static volatile SingularAttribute<OrderDetail, String> productCode;
	public static volatile SingularAttribute<OrderDetail, Long> productId;
	public static volatile SingularAttribute<OrderDetail, Boolean> isWarning;
	public static volatile SingularAttribute<OrderDetail, Long> orderId;
	public static volatile SingularAttribute<OrderDetail, Long> price;
	public static volatile SingularAttribute<OrderDetail, String> name;
	public static volatile SingularAttribute<OrderDetail, Long> orderDetailId;
	public static volatile SingularAttribute<OrderDetail, String> productName;

}

