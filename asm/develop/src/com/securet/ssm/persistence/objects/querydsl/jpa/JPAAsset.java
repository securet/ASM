package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.Asset;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAAsset is a Querydsl query type for Asset
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAAsset extends EntityPathBase<Asset> {

    private static final long serialVersionUID = -110192952L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAAsset asset = new JPAAsset("asset");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final NumberPath<Integer> assetId = createNumber("assetId", Integer.class);

    public final StringPath assetTag = createString("assetTag");

    public final JPAAssetType assetType;

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final DateTimePath<java.util.Date> installedDate = createDateTime("installedDate", java.util.Date.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath name = createString("name");

    public final JPASite site;

    public JPAAsset(String variable) {
        this(Asset.class, forVariable(variable), INITS);
    }

    public JPAAsset(Path<? extends Asset> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAAsset(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAAsset(PathMetadata<?> metadata, PathInits inits) {
        this(Asset.class, metadata, inits);
    }

    public JPAAsset(Class<? extends Asset> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.assetType = inits.isInitialized("assetType") ? new JPAAssetType(forProperty("assetType")) : null;
        this.site = inits.isInitialized("site") ? new JPASite(forProperty("site"), inits.get("site")) : null;
    }

}

