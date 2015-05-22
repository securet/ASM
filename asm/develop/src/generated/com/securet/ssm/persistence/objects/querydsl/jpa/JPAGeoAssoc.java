package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.GeoAssoc;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAGeoAssoc is a Querydsl query type for GeoAssoc
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAGeoAssoc extends EntityPathBase<GeoAssoc> {

    private static final long serialVersionUID = 775879788L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAGeoAssoc geoAssoc = new JPAGeoAssoc("geoAssoc");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final StringPath assocType = createString("assocType");

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final JPAGeo geoFrom;

    public final JPAGeo geoTo;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public JPAGeoAssoc(String variable) {
        this(GeoAssoc.class, forVariable(variable), INITS);
    }

    public JPAGeoAssoc(Path<? extends GeoAssoc> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAGeoAssoc(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAGeoAssoc(PathMetadata<?> metadata, PathInits inits) {
        this(GeoAssoc.class, metadata, inits);
    }

    public JPAGeoAssoc(Class<? extends GeoAssoc> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.geoFrom = inits.isInitialized("geoFrom") ? new JPAGeo(forProperty("geoFrom")) : null;
        this.geoTo = inits.isInitialized("geoTo") ? new JPAGeo(forProperty("geoTo")) : null;
    }

}

