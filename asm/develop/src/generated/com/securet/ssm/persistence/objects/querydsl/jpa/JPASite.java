package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.Site;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPASite is a Querydsl query type for Site
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPASite extends EntityPathBase<Site> {

    private static final long serialVersionUID = -972859281L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPASite site = new JPASite("site");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final StringPath area = createString("area");

    public final JPAGeo circle;

    public final JPAGeo city;

    public final StringPath comments = createString("comments");

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final JPAModule module;

    public final StringPath name = createString("name");

    public final JPAOrganization organization;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final StringPath siteType = createString("siteType");

    public final JPAGeo state;

    public JPASite(String variable) {
        this(Site.class, forVariable(variable), INITS);
    }

    public JPASite(Path<? extends Site> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPASite(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPASite(PathMetadata<?> metadata, PathInits inits) {
        this(Site.class, metadata, inits);
    }

    public JPASite(Class<? extends Site> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.circle = inits.isInitialized("circle") ? new JPAGeo(forProperty("circle")) : null;
        this.city = inits.isInitialized("city") ? new JPAGeo(forProperty("city")) : null;
        this.module = inits.isInitialized("module") ? new JPAModule(forProperty("module")) : null;
        this.organization = inits.isInitialized("organization") ? new JPAOrganization(forProperty("organization")) : null;
        this.state = inits.isInitialized("state") ? new JPAGeo(forProperty("state")) : null;
    }

}

