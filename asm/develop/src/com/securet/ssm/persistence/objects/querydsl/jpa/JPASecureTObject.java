package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.SecureTObject;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPASecureTObject is a Querydsl query type for SecureTObject
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class JPASecureTObject extends EntityPathBase<SecureTObject> {

    private static final long serialVersionUID = -1527155404L;

    public static final JPASecureTObject secureTObject = new JPASecureTObject("secureTObject");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public JPASecureTObject(String variable) {
        super(SecureTObject.class, forVariable(variable));
    }

    public JPASecureTObject(Path<? extends SecureTObject> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPASecureTObject(PathMetadata<?> metadata) {
        super(SecureTObject.class, metadata);
    }

}

