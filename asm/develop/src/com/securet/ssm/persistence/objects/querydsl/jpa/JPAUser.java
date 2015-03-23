package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.User;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAUser is a Querydsl query type for User
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -972790541L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAUser user = new JPAUser("user");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final StringPath activeRoles = createString("activeRoles");

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final StringPath emailId = createString("emailId");

    public final BooleanPath enableNotifications = createBoolean("enableNotifications");

    public final StringPath fullName = createString("fullName");

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath mobile = createString("mobile");

    public final JPAOrganization organization;

    public final ListPath<com.securet.ssm.persistence.objects.Permission, JPAPermission> permissions = this.<com.securet.ssm.persistence.objects.Permission, JPAPermission>createList("permissions", com.securet.ssm.persistence.objects.Permission.class, JPAPermission.class, PathInits.DIRECT2);

    public final ListPath<com.securet.ssm.persistence.objects.RoleType, JPARoleType> roles = this.<com.securet.ssm.persistence.objects.RoleType, JPARoleType>createList("roles", com.securet.ssm.persistence.objects.RoleType.class, JPARoleType.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> rolesList = this.<String, StringPath>createList("rolesList", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath userId = createString("userId");

    public final JPAUserLogin userLogin;

    public JPAUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public JPAUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAUser(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAUser(PathMetadata<?> metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public JPAUser(Class<? extends User> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.organization = inits.isInitialized("organization") ? new JPAOrganization(forProperty("organization")) : null;
        this.userLogin = inits.isInitialized("userLogin") ? new JPAUserLogin(forProperty("userLogin"), inits.get("userLogin")) : null;
    }

}

