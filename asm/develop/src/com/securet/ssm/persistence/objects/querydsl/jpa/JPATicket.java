package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.Ticket;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPATicket is a Querydsl query type for Ticket
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPATicket extends EntityPathBase<Ticket> {

    private static final long serialVersionUID = 1413233204L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPATicket ticket = new JPATicket("ticket");

    public final NumberPath<Integer> actualTat = createNumber("actualTat", Integer.class);

    public final JPAAsset asset;

    public final ListPath<com.securet.ssm.persistence.objects.TicketAttachment, JPATicketAttachment> attachments = this.<com.securet.ssm.persistence.objects.TicketAttachment, JPATicketAttachment>createList("attachments", com.securet.ssm.persistence.objects.TicketAttachment.class, JPATicketAttachment.class, PathInits.DIRECT2);

    public final JPAUser createdBy;

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final JPAIssueType issueType;

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final JPAUser modifiedBy;

    public final JPAEnumeration priority;

    public final JPAUser reporter;

    public final JPAUser resolver;

    public final JPAServiceType serviceType;

    public final JPAEnumeration severity;

    public final StringPath shortDesc = createString("shortDesc");

    public final JPASite site;

    public final StringPath source = createString("source");

    public final JPAEnumeration status;

    public final NumberPath<Integer> stopClock = createNumber("stopClock", Integer.class);

    public final NumberPath<Integer> tat = createNumber("tat", Integer.class);

    public final ListPath<com.securet.ssm.persistence.objects.TicketExt, JPATicketExt> ticketExtensions = this.<com.securet.ssm.persistence.objects.TicketExt, JPATicketExt>createList("ticketExtensions", com.securet.ssm.persistence.objects.TicketExt.class, JPATicketExt.class, PathInits.DIRECT2);

    public final StringPath ticketId = createString("ticketId");

    public final StringPath ticketMasterId = createString("ticketMasterId");

    public final JPAEnumeration ticketType;

    public JPATicket(String variable) {
        this(Ticket.class, forVariable(variable), INITS);
    }

    public JPATicket(Path<? extends Ticket> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPATicket(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPATicket(PathMetadata<?> metadata, PathInits inits) {
        this(Ticket.class, metadata, inits);
    }

    public JPATicket(Class<? extends Ticket> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.asset = inits.isInitialized("asset") ? new JPAAsset(forProperty("asset"), inits.get("asset")) : null;
        this.createdBy = inits.isInitialized("createdBy") ? new JPAUser(forProperty("createdBy"), inits.get("createdBy")) : null;
        this.issueType = inits.isInitialized("issueType") ? new JPAIssueType(forProperty("issueType"), inits.get("issueType")) : null;
        this.modifiedBy = inits.isInitialized("modifiedBy") ? new JPAUser(forProperty("modifiedBy"), inits.get("modifiedBy")) : null;
        this.priority = inits.isInitialized("priority") ? new JPAEnumeration(forProperty("priority")) : null;
        this.reporter = inits.isInitialized("reporter") ? new JPAUser(forProperty("reporter"), inits.get("reporter")) : null;
        this.resolver = inits.isInitialized("resolver") ? new JPAUser(forProperty("resolver"), inits.get("resolver")) : null;
        this.serviceType = inits.isInitialized("serviceType") ? new JPAServiceType(forProperty("serviceType")) : null;
        this.severity = inits.isInitialized("severity") ? new JPAEnumeration(forProperty("severity")) : null;
        this.site = inits.isInitialized("site") ? new JPASite(forProperty("site"), inits.get("site")) : null;
        this.status = inits.isInitialized("status") ? new JPAEnumeration(forProperty("status")) : null;
        this.ticketType = inits.isInitialized("ticketType") ? new JPAEnumeration(forProperty("ticketType")) : null;
    }

}

