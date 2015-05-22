package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.TicketArchive;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPATicketArchive is a Querydsl query type for TicketArchive
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPATicketArchive extends EntityPathBase<TicketArchive> {

    private static final long serialVersionUID = 80550894L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPATicketArchive ticketArchive = new JPATicketArchive("ticketArchive");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final JPAUser modifiedBy;

    public final NumberPath<Integer> relatedArchiveId = createNumber("relatedArchiveId", Integer.class);

    public final JPAUser reporter;

    public final JPAUser resolver;

    public final JPAEnumeration status;

    public final NumberPath<Integer> ticketArchiveId = createNumber("ticketArchiveId", Integer.class);

    public final StringPath ticketId = createString("ticketId");

    public final StringPath ticketMasterId = createString("ticketMasterId");

    public JPATicketArchive(String variable) {
        this(TicketArchive.class, forVariable(variable), INITS);
    }

    public JPATicketArchive(Path<? extends TicketArchive> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPATicketArchive(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPATicketArchive(PathMetadata<?> metadata, PathInits inits) {
        this(TicketArchive.class, metadata, inits);
    }

    public JPATicketArchive(Class<? extends TicketArchive> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.modifiedBy = inits.isInitialized("modifiedBy") ? new JPAUser(forProperty("modifiedBy"), inits.get("modifiedBy")) : null;
        this.reporter = inits.isInitialized("reporter") ? new JPAUser(forProperty("reporter"), inits.get("reporter")) : null;
        this.resolver = inits.isInitialized("resolver") ? new JPAUser(forProperty("resolver"), inits.get("resolver")) : null;
        this.status = inits.isInitialized("status") ? new JPAEnumeration(forProperty("status")) : null;
    }

}

