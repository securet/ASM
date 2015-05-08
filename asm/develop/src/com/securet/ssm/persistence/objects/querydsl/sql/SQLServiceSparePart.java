package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLServiceSparePart is a Querydsl query type for SQLServiceSparePart
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLServiceSparePart extends com.mysema.query.sql.RelationalPathBase<SQLServiceSparePart> {

    private static final long serialVersionUID = -215108622;

    public static final SQLServiceSparePart serviceSparePart = new SQLServiceSparePart("service_spare_part");

    public final NumberPath<Double> cost = createNumber("cost", Double.class);

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath partDescription = createString("partDescription");

    public final StringPath partName = createString("partName");

    public final NumberPath<Integer> sparePartId = createNumber("sparePartId", Integer.class);

    public final NumberPath<Integer> vendorOrganizationId = createNumber("vendorOrganizationId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SQLServiceSparePart> primary = createPrimaryKey(sparePartId);

    public final com.mysema.query.sql.ForeignKey<SQLOrganization> spareVendorOrgFk = createForeignKey(vendorOrganizationId, "organizationId");

    public final com.mysema.query.sql.ForeignKey<SQLPartOrderRequest> _porSspFk = createInvForeignKey(sparePartId, "sparePartId");

    public SQLServiceSparePart(String variable) {
        super(SQLServiceSparePart.class, forVariable(variable), "null", "service_spare_part");
        addMetadata();
    }

    public SQLServiceSparePart(String variable, String schema, String table) {
        super(SQLServiceSparePart.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLServiceSparePart(Path<? extends SQLServiceSparePart> path) {
        super(path.getType(), path.getMetadata(), "null", "service_spare_part");
        addMetadata();
    }

    public SQLServiceSparePart(PathMetadata<?> metadata) {
        super(SQLServiceSparePart.class, metadata, "null", "service_spare_part");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(cost, ColumnMetadata.named("cost").withIndex(5).ofType(Types.DECIMAL).withSize(10).withDigits(2).notNull());
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(6).ofType(Types.TIMESTAMP));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(7).ofType(Types.TIMESTAMP));
        addMetadata(partDescription, ColumnMetadata.named("partDescription").withIndex(4).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(partName, ColumnMetadata.named("partName").withIndex(3).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(sparePartId, ColumnMetadata.named("sparePartId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(vendorOrganizationId, ColumnMetadata.named("vendorOrganizationId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

