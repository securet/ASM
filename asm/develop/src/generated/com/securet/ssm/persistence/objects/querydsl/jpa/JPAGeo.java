package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.Geo;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAGeo is a Querydsl query type for Geo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAGeo extends EntityPathBase<Geo> {

    private static final long serialVersionUID = -1416867543L;

    public static final JPAGeo geo = new JPAGeo("geo");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final StringPath geoId = createString("geoId");

    public final EnumPath<Geo.GeoType> geoType = createEnum("geoType", Geo.GeoType.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath name = createString("name");

    public JPAGeo(String variable) {
        super(Geo.class, forVariable(variable));
    }

    public JPAGeo(Path<? extends Geo> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAGeo(PathMetadata<?> metadata) {
        super(Geo.class, metadata);
    }

}

