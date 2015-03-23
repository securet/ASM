package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLTicketAttachment is a Querydsl query type for SQLTicketAttachment
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLTicketAttachment extends com.mysema.query.sql.RelationalPathBase<SQLTicketAttachment> {

    private static final long serialVersionUID = -845653748;

    public static final SQLTicketAttachment ticketAttachment = new SQLTicketAttachment("ticket_attachment");

    public final NumberPath<Integer> attachmentId = createNumber("attachmentId", Integer.class);

    public final StringPath attachmentName = createString("attachmentName");

    public final StringPath attachmentPath = createString("attachmentPath");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath ticketId = createString("ticketId");

    public final com.mysema.query.sql.PrimaryKey<SQLTicketAttachment> primary = createPrimaryKey(attachmentId);

    public final com.mysema.query.sql.ForeignKey<SQLTicket> j0gp5cmysvlvblblsxbm7cge6FK = createForeignKey(ticketId, "ticketId");

    public SQLTicketAttachment(String variable) {
        super(SQLTicketAttachment.class, forVariable(variable), "null", "ticket_attachment");
        addMetadata();
    }

    public SQLTicketAttachment(String variable, String schema, String table) {
        super(SQLTicketAttachment.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLTicketAttachment(Path<? extends SQLTicketAttachment> path) {
        super(path.getType(), path.getMetadata(), "null", "ticket_attachment");
        addMetadata();
    }

    public SQLTicketAttachment(PathMetadata<?> metadata) {
        super(SQLTicketAttachment.class, metadata, "null", "ticket_attachment");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(attachmentId, ColumnMetadata.named("attachmentId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(attachmentName, ColumnMetadata.named("attachmentName").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(attachmentPath, ColumnMetadata.named("attachmentPath").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
        addMetadata(ticketId, ColumnMetadata.named("ticketId").withIndex(6).ofType(Types.VARCHAR).withSize(255));
    }

}

