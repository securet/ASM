package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLAsset is a Querydsl query type for SQLAsset
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLAsset extends com.mysema.query.sql.RelationalPathBase<SQLAsset> {

    private static final long serialVersionUID = -694794413;

    public static final SQLAsset asset = new SQLAsset("asset");

    public final NumberPath<Integer> assetId = createNumber("assetId", Integer.class);

    public final StringPath assetTag = createString("assetTag");

    public final NumberPath<Integer> assetTypeId = createNumber("assetTypeId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> installedDate = createDateTime("installedDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SQLAsset> primary = createPrimaryKey(assetId);

    public final com.mysema.query.sql.ForeignKey<SQLSite> ipyw9fgonenky6wr28dsglw9gFK = createForeignKey(siteId, "siteId");

    public final com.mysema.query.sql.ForeignKey<SQLAssetType> _4n8s7haxr90s0wpblm8a8apcqFK = createForeignKey(assetTypeId, "assetTypeId");

    public final com.mysema.query.sql.ForeignKey<SQLVendorServiceAsset> _rviv4mm3am5b5we2y0pmc9iv9FK = createInvForeignKey(assetId, "assetId");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> __7gi1i3mcjo45f107hg6wr5r9mFK = createInvForeignKey(assetId, "assetId");

    public SQLAsset(String variable) {
        super(SQLAsset.class, forVariable(variable), "null", "asset");
        addMetadata();
    }

    public SQLAsset(String variable, String schema, String table) {
        super(SQLAsset.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLAsset(Path<? extends SQLAsset> path) {
        super(path.getType(), path.getMetadata(), "null", "asset");
        addMetadata();
    }

    public SQLAsset(PathMetadata<?> metadata) {
        super(SQLAsset.class, metadata, "null", "asset");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(assetId, ColumnMetadata.named("assetId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(assetTag, ColumnMetadata.named("assetTag").withIndex(4).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(assetTypeId, ColumnMetadata.named("assetTypeId").withIndex(7).ofType(Types.INTEGER).withSize(10));
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP));
        addMetadata(installedDate, ColumnMetadata.named("installedDate").withIndex(5).ofType(Types.TIMESTAMP).notNull());
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
        addMetadata(name, ColumnMetadata.named("name").withIndex(6).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(siteId, ColumnMetadata.named("siteId").withIndex(8).ofType(Types.INTEGER).withSize(10));
    }

}

