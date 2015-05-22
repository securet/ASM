package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.ClientUserSite;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAClientUserSite is a Querydsl query type for ClientUserSite
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAClientUserSite extends EntityPathBase<ClientUserSite> {

    private static final long serialVersionUID = -26090043L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAClientUserSite clientUserSite = new JPAClientUserSite("clientUserSite");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final JPAUser clientUser;

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final JPASite site;

    public JPAClientUserSite(String variable) {
        this(ClientUserSite.class, forVariable(variable), INITS);
    }

    public JPAClientUserSite(Path<? extends ClientUserSite> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAClientUserSite(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAClientUserSite(PathMetadata<?> metadata, PathInits inits) {
        this(ClientUserSite.class, metadata, inits);
    }

    public JPAClientUserSite(Class<? extends ClientUserSite> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clientUser = inits.isInitialized("clientUser") ? new JPAUser(forProperty("clientUser"), inits.get("clientUser")) : null;
        this.site = inits.isInitialized("site") ? new JPASite(forProperty("site"), inits.get("site")) : null;
    }

}

