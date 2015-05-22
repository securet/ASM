package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLTicketArchive is a Querydsl query type for SQLTicketArchive
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLTicketArchive extends com.mysema.query.sql.RelationalPathBase<SQLTicketArchive> {

    private static final long serialVersionUID = 1483542905;

    public static final SQLTicketArchive ticketArchive = new SQLTicketArchive("ticket_archive");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath modifiedBy = createString("modifiedBy");

    public final NumberPath<Integer> relatedArchiveId = createNumber("relatedArchiveId", Integer.class);

    public final StringPath reporterUserId = createString("reporterUserId");

    public final StringPath resolverUserId = createString("resolverUserId");

    public final StringPath statusId = createString("statusId");

    public final NumberPath<Integer> ticketArchiveId = createNumber("ticketArchiveId", Integer.class);

    public final StringPath ticketId = createString("ticketId");

    public final StringPath ticketMasterId = createString("ticketMasterId");

    public final com.mysema.query.sql.PrimaryKey<SQLTicketArchive> primary = createPrimaryKey(ticketArchiveId);

    public final com.mysema.query.sql.ForeignKey<SQLUser> _4jc28ku9yq01w7fno1yig7nyeFK = createForeignKey(resolverUserId, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLTicketArchive> ticketArchiveRelatedArchiveIdFk = createForeignKey(relatedArchiveId, "ticketArchiveId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> bjy6xtu2369egiu9cav4aoj4oFK = createForeignKey(reporterUserId, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> ticketArchiveModifiedByFk = createForeignKey(modifiedBy, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLEnumeration> ticketArchiveStatusIdFk = createForeignKey(statusId, "enumerationId");

    public final com.mysema.query.sql.ForeignKey<SQLTicketArchive> _ticketArchiveRelatedArchiveIdFk = createInvForeignKey(ticketArchiveId, "relatedArchiveId");

    public SQLTicketArchive(String variable) {
        super(SQLTicketArchive.class, forVariable(variable), "null", "ticket_archive");
        addMetadata();
    }

    public SQLTicketArchive(String variable, String schema, String table) {
        super(SQLTicketArchive.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLTicketArchive(Path<? extends SQLTicketArchive> path) {
        super(path.getType(), path.getMetadata(), "null", "ticket_archive");
        addMetadata();
    }

    public SQLTicketArchive(PathMetadata<?> metadata) {
        super(SQLTicketArchive.class, metadata, "null", "ticket_archive");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(description, ColumnMetadata.named("description").withIndex(4).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(modifiedBy, ColumnMetadata.named("modifiedBy").withIndex(9).ofType(Types.VARCHAR).withSize(255));
        addMetadata(relatedArchiveId, ColumnMetadata.named("relatedArchiveId").withIndex(11).ofType(Types.INTEGER).withSize(10));
        addMetadata(reporterUserId, ColumnMetadata.named("reporterUserId").withIndex(7).ofType(Types.VARCHAR).withSize(255));
        addMetadata(resolverUserId, ColumnMetadata.named("resolverUserId").withIndex(8).ofType(Types.VARCHAR).withSize(255));
        addMetadata(statusId, ColumnMetadata.named("statusId").withIndex(10).ofType(Types.VARCHAR).withSize(255));
        addMetadata(ticketArchiveId, ColumnMetadata.named("ticketArchiveId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(ticketId, ColumnMetadata.named("ticketId").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(ticketMasterId, ColumnMetadata.named("ticketMasterId").withIndex(6).ofType(Types.VARCHAR).withSize(255));
    }

}

