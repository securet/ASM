package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.Permission;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAPermission is a Querydsl query type for Permission
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAPermission extends EntityPathBase<Permission> {

    private static final long serialVersionUID = 1865269495L;

    public static final JPAPermission permission1 = new JPAPermission("permission1");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath permission = createString("permission");

    public final StringPath permissionId = createString("permissionId");

    public JPAPermission(String variable) {
        super(Permission.class, forVariable(variable));
    }

    public JPAPermission(Path<? extends Permission> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAPermission(PathMetadata<?> metadata) {
        super(Permission.class, metadata);
    }

}

