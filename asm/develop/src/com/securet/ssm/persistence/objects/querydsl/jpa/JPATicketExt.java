package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.TicketExt;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPATicketExt is a Querydsl query type for TicketExt
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPATicketExt extends EntityPathBase<TicketExt> {

    private static final long serialVersionUID = -1933952179L;

    public static final JPATicketExt ticketExt = new JPATicketExt("ticketExt");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final StringPath fieldName = createString("fieldName");

    public final StringPath fieldValue = createString("fieldValue");

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath ticketArchiveId = createString("ticketArchiveId");

    public final StringPath ticketId = createString("ticketId");

    public JPATicketExt(String variable) {
        super(TicketExt.class, forVariable(variable));
    }

    public JPATicketExt(Path<? extends TicketExt> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPATicketExt(PathMetadata<?> metadata) {
        super(TicketExt.class, metadata);
    }

}

