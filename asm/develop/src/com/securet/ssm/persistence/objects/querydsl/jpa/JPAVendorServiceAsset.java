package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.VendorServiceAsset;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAVendorServiceAsset is a Querydsl query type for VendorServiceAsset
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAVendorServiceAsset extends EntityPathBase<VendorServiceAsset> {

    private static final long serialVersionUID = -1840136757L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAVendorServiceAsset vendorServiceAsset = new JPAVendorServiceAsset("vendorServiceAsset");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final JPAAsset asset;

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final JPAServiceType serviceType;

    public final JPAUser vendorUser;

    public JPAVendorServiceAsset(String variable) {
        this(VendorServiceAsset.class, forVariable(variable), INITS);
    }

    public JPAVendorServiceAsset(Path<? extends VendorServiceAsset> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAVendorServiceAsset(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAVendorServiceAsset(PathMetadata<?> metadata, PathInits inits) {
        this(VendorServiceAsset.class, metadata, inits);
    }

    public JPAVendorServiceAsset(Class<? extends VendorServiceAsset> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.asset = inits.isInitialized("asset") ? new JPAAsset(forProperty("asset"), inits.get("asset")) : null;
        this.serviceType = inits.isInitialized("serviceType") ? new JPAServiceType(forProperty("serviceType")) : null;
        this.vendorUser = inits.isInitialized("vendorUser") ? new JPAUser(forProperty("vendorUser"), inits.get("vendorUser")) : null;
    }

}

