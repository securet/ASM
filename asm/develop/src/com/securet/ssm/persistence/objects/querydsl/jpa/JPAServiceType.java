package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.ServiceType;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAServiceType is a Querydsl query type for ServiceType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAServiceType extends EntityPathBase<ServiceType> {

    private static final long serialVersionUID = -1073295001L;

    public static final JPAServiceType serviceType = new JPAServiceType("serviceType");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> serviceTypeId = createNumber("serviceTypeId", Integer.class);

    public JPAServiceType(String variable) {
        super(ServiceType.class, forVariable(variable));
    }

    public JPAServiceType(Path<? extends ServiceType> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAServiceType(PathMetadata<?> metadata) {
        super(ServiceType.class, metadata);
    }

}

