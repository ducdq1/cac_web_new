package com.viettel.core.workflow.BO;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Node.class)
public abstract class Node_ {

	public static volatile SingularAttribute<Node, String> nodeName;
	public static volatile SingularAttribute<Node, Float> top;
	public static volatile SingularAttribute<Node, Float> left;
	public static volatile SingularAttribute<Node, Float> bottom;
	public static volatile SingularAttribute<Node, Float> right;
	public static volatile SingularAttribute<Node, Long> type;
	public static volatile SingularAttribute<Node, Long> isActive;
	public static volatile SingularAttribute<Node, Long> nodeId;
	public static volatile SingularAttribute<Node, Long> flowId;
	public static volatile SingularAttribute<Node, Long> status;

}

