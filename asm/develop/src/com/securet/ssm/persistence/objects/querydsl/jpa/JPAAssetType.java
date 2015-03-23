package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.AssetType;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAAssetType is a Querydsl query type for AssetType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAAssetType extends EntityPathBase<AssetType> {

    private static final long serialVersionUID = -547490270L;

    public static final JPAAssetType assetType = new JPAAssetType("assetType");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final NumberPath<Integer> assetTypeId = createNumber("assetTypeId", Integer.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath name = createString("name");

    public JPAAssetType(String variable) {
        super(AssetType.class, forVariable(variable));
    }

    public JPAAssetType(Path<? extends AssetType> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAAssetType(PathMetadata<?> metadata) {
        super(AssetType.class, metadata);
    }

}

