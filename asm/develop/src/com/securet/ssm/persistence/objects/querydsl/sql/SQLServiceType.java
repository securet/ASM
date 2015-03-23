package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLServiceType is a Querydsl query type for SQLServiceType
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLServiceType extends com.mysema.query.sql.RelationalPathBase<SQLServiceType> {

    private static final long serialVersionUID = -441668174;

    public static final SQLServiceType serviceType = new SQLServiceType("service_type");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> serviceTypeId = createNumber("serviceTypeId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SQLServiceType> primary = createPrimaryKey(serviceTypeId);

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _gviwhrbxs5ouesx8mekr0of9aFK = createInvForeignKey(serviceTypeId, "serviceTypeId");

    public final com.mysema.query.sql.ForeignKey<SQLIssueType> _l2clwhmr3wvv786viy4sp0315FK = createInvForeignKey(serviceTypeId, "serviceTypeId");

    public final com.mysema.query.sql.ForeignKey<SQLVendorServiceAsset> __80wpd9b9rgyksrc3jsooo4st5FK = createInvForeignKey(serviceTypeId, "serviceTypeId");

    public SQLServiceType(String variable) {
        super(SQLServiceType.class, forVariable(variable), "null", "service_type");
        addMetadata();
    }

    public SQLServiceType(String variable, String schema, String table) {
        super(SQLServiceType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLServiceType(Path<? extends SQLServiceType> path) {
        super(path.getType(), path.getMetadata(), "null", "service_type");
        addMetadata();
    }

    public SQLServiceType(PathMetadata<?> metadata) {
        super(SQLServiceType.class, metadata, "null", "service_type");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
        addMetadata(name, ColumnMetadata.named("name").withIndex(4).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(serviceTypeId, ColumnMetadata.named("serviceTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

