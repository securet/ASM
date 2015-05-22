package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.TicketAttachment;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPATicketAttachment is a Querydsl query type for TicketAttachment
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPATicketAttachment extends EntityPathBase<TicketAttachment> {

    private static final long serialVersionUID = 1241071223L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPATicketAttachment ticketAttachment = new JPATicketAttachment("ticketAttachment");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final NumberPath<Integer> attachmentId = createNumber("attachmentId", Integer.class);

    public final StringPath attachmentName = createString("attachmentName");

    public final StringPath attachmentPath = createString("attachmentPath");

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final JPATicket ticket;

    public JPATicketAttachment(String variable) {
        this(TicketAttachment.class, forVariable(variable), INITS);
    }

    public JPATicketAttachment(Path<? extends TicketAttachment> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPATicketAttachment(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPATicketAttachment(PathMetadata<?> metadata, PathInits inits) {
        this(TicketAttachment.class, metadata, inits);
    }

    public JPATicketAttachment(Class<? extends TicketAttachment> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ticket = inits.isInitialized("ticket") ? new JPATicket(forProperty("ticket"), inits.get("ticket")) : null;
    }

}

