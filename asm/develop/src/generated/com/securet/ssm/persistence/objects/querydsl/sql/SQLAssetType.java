package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLAssetType is a Querydsl query type for SQLAssetType
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLAssetType extends com.mysema.query.sql.RelationalPathBase<SQLAssetType> {

    private static final long serialVersionUID = -1999345363;

    public static final SQLAssetType assetType = new SQLAssetType("asset_type");

    public final NumberPath<Integer> assetTypeId = createNumber("assetTypeId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<SQLAssetType> primary = createPrimaryKey(assetTypeId);

    public final com.mysema.query.sql.ForeignKey<SQLAsset> __4n8s7haxr90s0wpblm8a8apcqFK = createInvForeignKey(assetTypeId, "assetTypeId");

    public SQLAssetType(String variable) {
        super(SQLAssetType.class, forVariable(variable), "null", "asset_type");
        addMetadata();
    }

    public SQLAssetType(String variable, String schema, String table) {
        super(SQLAssetType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLAssetType(Path<? extends SQLAssetType> path) {
        super(path.getType(), path.getMetadata(), "null", "asset_type");
        addMetadata();
    }

    public SQLAssetType(PathMetadata<?> metadata) {
        super(SQLAssetType.class, metadata, "null", "asset_type");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(assetTypeId, ColumnMetadata.named("assetTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(name, ColumnMetadata.named("name").withIndex(4).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

