package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLEnumeration is a Querydsl query type for SQLEnumeration
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLEnumeration extends com.mysema.query.sql.RelationalPathBase<SQLEnumeration> {

    private static final long serialVersionUID = -1283960726;

    public static final SQLEnumeration enumeration = new SQLEnumeration("enumeration");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath enumDescription = createString("enumDescription");

    public final StringPath enumerationId = createString("enumerationId");

    public final StringPath enumTypeId = createString("enumTypeId");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<SQLEnumeration> primary = createPrimaryKey(enumerationId);

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _ns6cw5tg2yn5vkikrblexy72wFK = createInvForeignKey(enumerationId, "severity");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _tciketTtypeFk = createInvForeignKey(enumerationId, "ticketType");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _efih1u04786q1c8x27o9wvb3eFK = createInvForeignKey(enumerationId, "statusId");

    public final com.mysema.query.sql.ForeignKey<SQLPartOrderRequest> _porStatusEnumFk = createInvForeignKey(enumerationId, "statusId");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _nvtejdn6nnmtkw52oefub1wnFK = createInvForeignKey(enumerationId, "priority");

    public final com.mysema.query.sql.ForeignKey<SQLTicketArchive> _ticketArchiveStatusIdFk = createInvForeignKey(enumerationId, "statusId");

    public SQLEnumeration(String variable) {
        super(SQLEnumeration.class, forVariable(variable), "null", "enumeration");
        addMetadata();
    }

    public SQLEnumeration(String variable, String schema, String table) {
        super(SQLEnumeration.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLEnumeration(Path<? extends SQLEnumeration> path) {
        super(path.getType(), path.getMetadata(), "null", "enumeration");
        addMetadata();
    }

    public SQLEnumeration(PathMetadata<?> metadata) {
        super(SQLEnumeration.class, metadata, "null", "enumeration");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(enumDescription, ColumnMetadata.named("enumDescription").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(enumerationId, ColumnMetadata.named("enumerationId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(enumTypeId, ColumnMetadata.named("enumTypeId").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
    }

}

