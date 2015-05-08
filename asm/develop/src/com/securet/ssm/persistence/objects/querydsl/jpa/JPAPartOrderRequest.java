package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.PartOrderRequest;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAPartOrderRequest is a Querydsl query type for PartOrderRequest
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAPartOrderRequest extends EntityPathBase<PartOrderRequest> {

    private static final long serialVersionUID = -1860751332L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAPartOrderRequest partOrderRequest = new JPAPartOrderRequest("partOrderRequest");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final NumberPath<java.math.BigDecimal> cost = createNumber("cost", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final JPAUser initiatedBy;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final NumberPath<Integer> partOrderRequestId = createNumber("partOrderRequestId", Integer.class);

    public final JPAUser respondedBy;

    public final JPAServiceSparePart serviceSparePart;

    public final JPAEnumeration status;

    public final JPATicket ticket;

    public JPAPartOrderRequest(String variable) {
        this(PartOrderRequest.class, forVariable(variable), INITS);
    }

    public JPAPartOrderRequest(Path<? extends PartOrderRequest> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAPartOrderRequest(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAPartOrderRequest(PathMetadata<?> metadata, PathInits inits) {
        this(PartOrderRequest.class, metadata, inits);
    }

    public JPAPartOrderRequest(Class<? extends PartOrderRequest> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.initiatedBy = inits.isInitialized("initiatedBy") ? new JPAUser(forProperty("initiatedBy"), inits.get("initiatedBy")) : null;
        this.respondedBy = inits.isInitialized("respondedBy") ? new JPAUser(forProperty("respondedBy"), inits.get("respondedBy")) : null;
        this.serviceSparePart = inits.isInitialized("serviceSparePart") ? new JPAServiceSparePart(forProperty("serviceSparePart"), inits.get("serviceSparePart")) : null;
        this.status = inits.isInitialized("status") ? new JPAEnumeration(forProperty("status")) : null;
        this.ticket = inits.isInitialized("ticket") ? new JPATicket(forProperty("ticket"), inits.get("ticket")) : null;
    }

}

