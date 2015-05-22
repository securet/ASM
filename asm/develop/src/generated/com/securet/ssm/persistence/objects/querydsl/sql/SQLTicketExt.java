package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLTicketExt is a Querydsl query type for SQLTicketExt
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLTicketExt extends com.mysema.query.sql.RelationalPathBase<SQLTicketExt> {

    private static final long serialVersionUID = 909160024;

    public static final SQLTicketExt ticketExt = new SQLTicketExt("ticket_ext");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath fieldName = createString("fieldName");

    public final StringPath fieldValue = createString("fieldValue");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath ticketArchiveId = createString("ticketArchiveId");

    public final StringPath ticketId = createString("ticketId");

    public final com.mysema.query.sql.PrimaryKey<SQLTicketExt> primary = createPrimaryKey(fieldName, ticketArchiveId, ticketId);

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _4ka3q84ykcjs2rshx3xdmt3c6FK = createForeignKey(ticketId, "ticketId");

    public SQLTicketExt(String variable) {
        super(SQLTicketExt.class, forVariable(variable), "null", "ticket_ext");
        addMetadata();
    }

    public SQLTicketExt(String variable, String schema, String table) {
        super(SQLTicketExt.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLTicketExt(Path<? extends SQLTicketExt> path) {
        super(path.getType(), path.getMetadata(), "null", "ticket_ext");
        addMetadata();
    }

    public SQLTicketExt(PathMetadata<?> metadata) {
        super(SQLTicketExt.class, metadata, "null", "ticket_ext");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(4).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(fieldName, ColumnMetadata.named("fieldName").withIndex(3).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(fieldValue, ColumnMetadata.named("fieldValue").withIndex(6).ofType(Types.VARCHAR).withSize(255));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(5).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(ticketArchiveId, ColumnMetadata.named("ticketArchiveId").withIndex(2).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(ticketId, ColumnMetadata.named("ticketId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

