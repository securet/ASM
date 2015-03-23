package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.Enumeration;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAEnumeration is a Querydsl query type for Enumeration
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAEnumeration extends EntityPathBase<Enumeration> {

    private static final long serialVersionUID = -1915587553L;

    public static final JPAEnumeration enumeration = new JPAEnumeration("enumeration");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final StringPath enumDescription = createString("enumDescription");

    public final StringPath enumerationId = createString("enumerationId");

    public final StringPath enumTypeId = createString("enumTypeId");

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public JPAEnumeration(String variable) {
        super(Enumeration.class, forVariable(variable));
    }

    public JPAEnumeration(Path<? extends Enumeration> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAEnumeration(PathMetadata<?> metadata) {
        super(Enumeration.class, metadata);
    }

}

