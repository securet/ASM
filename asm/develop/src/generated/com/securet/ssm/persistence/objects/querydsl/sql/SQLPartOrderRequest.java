package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLPartOrderRequest is a Querydsl query type for SQLPartOrderRequest
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLPartOrderRequest extends com.mysema.query.sql.RelationalPathBase<SQLPartOrderRequest> {

    private static final long serialVersionUID = 347490993;

    public static final SQLPartOrderRequest partOrderRequest = new SQLPartOrderRequest("part_order_request");

    public final NumberPath<java.math.BigDecimal> cost = createNumber("cost", java.math.BigDecimal.class);

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath initiatedBy = createString("initiatedBy");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Integer> partOrderRequestId = createNumber("partOrderRequestId", Integer.class);

    public final StringPath respondedBy = createString("respondedBy");

    public final NumberPath<Integer> sparePartId = createNumber("sparePartId", Integer.class);

    public final StringPath statusId = createString("statusId");

    public final StringPath ticketId = createString("ticketId");

    public final com.mysema.query.sql.PrimaryKey<SQLPartOrderRequest> primary = createPrimaryKey(partOrderRequestId);

    public final com.mysema.query.sql.ForeignKey<SQLServiceSparePart> k07o4v8bgort6vcepqeoof083FK = createForeignKey(sparePartId, "sparePartId");

    public SQLPartOrderRequest(String variable) {
        super(SQLPartOrderRequest.class, forVariable(variable), "null", "part_order_request");
        addMetadata();
    }

    public SQLPartOrderRequest(String variable, String schema, String table) {
        super(SQLPartOrderRequest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLPartOrderRequest(Path<? extends SQLPartOrderRequest> path) {
        super(path.getType(), path.getMetadata(), "null", "part_order_request");
        addMetadata();
    }

    public SQLPartOrderRequest(PathMetadata<?> metadata) {
        super(SQLPartOrderRequest.class, metadata, "null", "part_order_request");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(cost, ColumnMetadata.named("cost").withIndex(4).ofType(Types.DECIMAL).withSize(19).withDigits(2).notNull());
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(initiatedBy, ColumnMetadata.named("initiatedBy").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(partOrderRequestId, ColumnMetadata.named("partOrderRequestId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(respondedBy, ColumnMetadata.named("respondedBy").withIndex(6).ofType(Types.VARCHAR).withSize(255));
        addMetadata(sparePartId, ColumnMetadata.named("sparePartId").withIndex(7).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(statusId, ColumnMetadata.named("statusId").withIndex(8).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(ticketId, ColumnMetadata.named("ticketId").withIndex(9).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

