package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.UserLogin;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAUserLogin is a Querydsl query type for UserLogin
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAUserLogin extends EntityPathBase<UserLogin> {

    private static final long serialVersionUID = -1540965834L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAUserLogin userLogin = new JPAUserLogin("userLogin");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final DateTimePath<java.sql.Timestamp> disabledTimestamp = createDateTime("disabledTimestamp", java.sql.Timestamp.class);

    public final BooleanPath enabled = createBoolean("enabled");

    public final DateTimePath<java.sql.Timestamp> lastLoginTimestamp = createDateTime("lastLoginTimestamp", java.sql.Timestamp.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath password = createString("password");

    public final JPAUser user;

    public final StringPath userId = createString("userId");

    public JPAUserLogin(String variable) {
        this(UserLogin.class, forVariable(variable), INITS);
    }

    public JPAUserLogin(Path<? extends UserLogin> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAUserLogin(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAUserLogin(PathMetadata<?> metadata, PathInits inits) {
        this(UserLogin.class, metadata, inits);
    }

    public JPAUserLogin(Class<? extends UserLogin> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new JPAUser(forProperty("user"), inits.get("user")) : null;
    }

}

