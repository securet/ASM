package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLTicket is a Querydsl query type for SQLTicket
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLTicket extends com.mysema.query.sql.RelationalPathBase<SQLTicket> {

    private static final long serialVersionUID = 470457097;

    public static final SQLTicket ticket = new SQLTicket("ticket");

    public final NumberPath<Integer> assetId = createNumber("assetId", Integer.class);

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> issueTypeId = createNumber("issueTypeId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath modifiedBy = createString("modifiedBy");

    public final StringPath priority = createString("priority");

    public final StringPath reporterUserId = createString("reporterUserId");

    public final StringPath resolverUserId = createString("resolverUserId");

    public final NumberPath<Integer> serviceTypeId = createNumber("serviceTypeId", Integer.class);

    public final StringPath severity = createString("severity");

    public final StringPath shortDesc = createString("shortDesc");

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final StringPath source = createString("source");

    public final StringPath statusId = createString("statusId");

    public final StringPath ticketId = createString("ticketId");

    public final StringPath ticketMasterId = createString("ticketMasterId");

    public final StringPath ticketType = createString("ticketType");

    public final com.mysema.query.sql.PrimaryKey<SQLTicket> primary = createPrimaryKey(ticketId);

    public final com.mysema.query.sql.ForeignKey<SQLUser> drigf1w0r9ivlvn5hjki8n4tvFK = createForeignKey(resolverUserId, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLIssueType> fvv5yh20hv79k0v5sfthn3uitFK = createForeignKey(issueTypeId, "issueTypeId");

    public final com.mysema.query.sql.ForeignKey<SQLEnumeration> ns6cw5tg2yn5vkikrblexy72wFK = createForeignKey(severity, "enumerationId");

    public final com.mysema.query.sql.ForeignKey<SQLEnumeration> tciketTtypeFk = createForeignKey(ticketType, "enumerationId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> ticketModifiedByFk = createForeignKey(modifiedBy, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLEnumeration> efih1u04786q1c8x27o9wvb3eFK = createForeignKey(statusId, "enumerationId");

    public final com.mysema.query.sql.ForeignKey<SQLServiceType> gviwhrbxs5ouesx8mekr0of9aFK = createForeignKey(serviceTypeId, "serviceTypeId");

    public final com.mysema.query.sql.ForeignKey<SQLSite> bh7e3r597n6lnlyn6wb2ao0boFK = createForeignKey(siteId, "siteId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> jf247bmk0iqmf67x6btuv5yrlFK = createForeignKey(reporterUserId, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> pex2v8n74m2im2ir3cb37ju26FK = createForeignKey(createdBy, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLEnumeration> nvtejdn6nnmtkw52oefub1wnFK = createForeignKey(priority, "enumerationId");

    public final com.mysema.query.sql.ForeignKey<SQLAsset> _7gi1i3mcjo45f107hg6wr5r9mFK = createForeignKey(assetId, "assetId");

    public final com.mysema.query.sql.ForeignKey<SQLTicketAttachment> _j0gp5cmysvlvblblsxbm7cge6FK = createInvForeignKey(ticketId, "ticketId");

    public final com.mysema.query.sql.ForeignKey<SQLPartOrderRequest> _porTicketFk = createInvForeignKey(ticketId, "ticketId");

    public final com.mysema.query.sql.ForeignKey<SQLTicketExt> __4ka3q84ykcjs2rshx3xdmt3c6FK = createInvForeignKey(ticketId, "ticketId");

    public SQLTicket(String variable) {
        super(SQLTicket.class, forVariable(variable), "null", "ticket");
        addMetadata();
    }

    public SQLTicket(String variable, String schema, String table) {
        super(SQLTicket.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLTicket(Path<? extends SQLTicket> path) {
        super(path.getType(), path.getMetadata(), "null", "ticket");
        addMetadata();
    }

    public SQLTicket(PathMetadata<?> metadata) {
        super(SQLTicket.class, metadata, "null", "ticket");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(assetId, ColumnMetadata.named("assetId").withIndex(10).ofType(Types.INTEGER).withSize(10));
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(11).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP));
        addMetadata(description, ColumnMetadata.named("description").withIndex(4).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(issueTypeId, ColumnMetadata.named("issueTypeId").withIndex(12).ofType(Types.INTEGER).withSize(10));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
        addMetadata(latitude, ColumnMetadata.named("latitude").withIndex(5).ofType(Types.DOUBLE).withSize(22).notNull());
        addMetadata(longitude, ColumnMetadata.named("longitude").withIndex(6).ofType(Types.DOUBLE).withSize(22).notNull());
        addMetadata(modifiedBy, ColumnMetadata.named("modifiedBy").withIndex(21).ofType(Types.VARCHAR).withSize(255));
        addMetadata(priority, ColumnMetadata.named("priority").withIndex(13).ofType(Types.VARCHAR).withSize(255));
        addMetadata(reporterUserId, ColumnMetadata.named("reporterUserId").withIndex(14).ofType(Types.VARCHAR).withSize(255));
        addMetadata(resolverUserId, ColumnMetadata.named("resolverUserId").withIndex(15).ofType(Types.VARCHAR).withSize(255));
        addMetadata(serviceTypeId, ColumnMetadata.named("serviceTypeId").withIndex(16).ofType(Types.INTEGER).withSize(10));
        addMetadata(severity, ColumnMetadata.named("severity").withIndex(17).ofType(Types.VARCHAR).withSize(255));
        addMetadata(shortDesc, ColumnMetadata.named("shortDesc").withIndex(7).ofType(Types.VARCHAR).withSize(255));
        addMetadata(siteId, ColumnMetadata.named("siteId").withIndex(18).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(source, ColumnMetadata.named("source").withIndex(8).ofType(Types.VARCHAR).withSize(255));
        addMetadata(statusId, ColumnMetadata.named("statusId").withIndex(19).ofType(Types.VARCHAR).withSize(255));
        addMetadata(ticketId, ColumnMetadata.named("ticketId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(ticketMasterId, ColumnMetadata.named("ticketMasterId").withIndex(9).ofType(Types.VARCHAR).withSize(255));
        addMetadata(ticketType, ColumnMetadata.named("ticketType").withIndex(20).ofType(Types.VARCHAR).withSize(255));
    }

}

