package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.ServiceSparePart;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAServiceSparePart is a Querydsl query type for ServiceSparePart
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAServiceSparePart extends EntityPathBase<ServiceSparePart> {

    private static final long serialVersionUID = 1871616349L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAServiceSparePart serviceSparePart = new JPAServiceSparePart("serviceSparePart");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final NumberPath<java.math.BigDecimal> cost = createNumber("cost", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath partDescription = createString("partDescription");

    public final StringPath partName = createString("partName");

    public final NumberPath<Integer> sparePartId = createNumber("sparePartId", Integer.class);

    public final JPAOrganization vendorOrganization;

    public JPAServiceSparePart(String variable) {
        this(ServiceSparePart.class, forVariable(variable), INITS);
    }

    public JPAServiceSparePart(Path<? extends ServiceSparePart> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAServiceSparePart(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAServiceSparePart(PathMetadata<?> metadata, PathInits inits) {
        this(ServiceSparePart.class, metadata, inits);
    }

    public JPAServiceSparePart(Class<? extends ServiceSparePart> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.vendorOrganization = inits.isInitialized("vendorOrganization") ? new JPAOrganization(forProperty("vendorOrganization")) : null;
    }

}

