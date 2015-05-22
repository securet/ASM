package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLVendorServiceAsset is a Querydsl query type for SQLVendorServiceAsset
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLVendorServiceAsset extends com.mysema.query.sql.RelationalPathBase<SQLVendorServiceAsset> {

    private static final long serialVersionUID = -1433106656;

    public static final SQLVendorServiceAsset vendorServiceAsset = new SQLVendorServiceAsset("vendor_service_asset");

    public final NumberPath<Integer> assetId = createNumber("assetId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Integer> serviceTypeId = createNumber("serviceTypeId", Integer.class);

    public final StringPath userId = createString("userId");

    public final com.mysema.query.sql.PrimaryKey<SQLVendorServiceAsset> primary = createPrimaryKey(assetId, serviceTypeId, userId);

    public final com.mysema.query.sql.ForeignKey<SQLAsset> rviv4mm3am5b5we2y0pmc9iv9FK = createForeignKey(assetId, "assetId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> _3jyblkqe3x9rcibrjvbtjov5xFK = createForeignKey(userId, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLServiceType> _80wpd9b9rgyksrc3jsooo4st5FK = createForeignKey(serviceTypeId, "serviceTypeId");

    public SQLVendorServiceAsset(String variable) {
        super(SQLVendorServiceAsset.class, forVariable(variable), "null", "vendor_service_asset");
        addMetadata();
    }

    public SQLVendorServiceAsset(String variable, String schema, String table) {
        super(SQLVendorServiceAsset.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLVendorServiceAsset(Path<? extends SQLVendorServiceAsset> path) {
        super(path.getType(), path.getMetadata(), "null", "vendor_service_asset");
        addMetadata();
    }

    public SQLVendorServiceAsset(PathMetadata<?> metadata) {
        super(SQLVendorServiceAsset.class, metadata, "null", "vendor_service_asset");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(assetId, ColumnMetadata.named("assetId").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(1).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(serviceTypeId, ColumnMetadata.named("serviceTypeId").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userId, ColumnMetadata.named("userId").withIndex(3).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

