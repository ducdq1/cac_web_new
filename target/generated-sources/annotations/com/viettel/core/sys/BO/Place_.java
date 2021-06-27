package com.viettel.core.sys.BO;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Place.class)
public abstract class Place_ {

	public static volatile SingularAttribute<Place, String> code;
	public static volatile SingularAttribute<Place, Long> placeId;
	public static volatile SingularAttribute<Place, String> name;
	public static volatile SingularAttribute<Place, String> placeTypeCode;
	public static volatile SingularAttribute<Place, Long> isActive;
	public static volatile SingularAttribute<Place, Long> parentId;

}

