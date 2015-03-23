package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.RoleType;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPARoleType is a Querydsl query type for RoleType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPARoleType extends EntityPathBase<RoleType> {

    private static final long serialVersionUID = -1296499208L;

    public static final JPARoleType roleType1 = new JPARoleType("roleType1");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath roleName = createString("roleName");

    public final StringPath roleType = createString("roleType");

    public final NumberPath<Integer> roleTypeId = createNumber("roleTypeId", Integer.class);

    public JPARoleType(String variable) {
        super(RoleType.class, forVariable(variable));
    }

    public JPARoleType(Path<? extends RoleType> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPARoleType(PathMetadata<?> metadata) {
        super(RoleType.class, metadata);
    }

}

