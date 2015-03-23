package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLGeo is a Querydsl query type for SQLGeo
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLGeo extends com.mysema.query.sql.RelationalPathBase<SQLGeo> {

    private static final long serialVersionUID = -9656204;

    public static final SQLGeo geo = new SQLGeo("geo");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath geoId = createString("geoId");

    public final StringPath geoName = createString("geoName");

    public final NumberPath<Integer> geoType = createNumber("geoType", Integer.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<SQLGeo> primary = createPrimaryKey(geoId);

    public final com.mysema.query.sql.ForeignKey<SQLSite> __7osstiw3olnj480ttbrvdcmrqFK = createInvForeignKey(geoId, "city");

    public final com.mysema.query.sql.ForeignKey<SQLGeoAssoc> _geoGaToFk = createInvForeignKey(geoId, "geoId");

    public final com.mysema.query.sql.ForeignKey<SQLGeoAssoc> _geoGaFromFk = createInvForeignKey(geoId, "geoId");

    public final com.mysema.query.sql.ForeignKey<SQLSite> _mirv952y6eqy7g8475c8s1supFK = createInvForeignKey(geoId, "state");

    public final com.mysema.query.sql.ForeignKey<SQLSite> _siteCircleFk = createInvForeignKey(geoId, "circle");

    public SQLGeo(String variable) {
        super(SQLGeo.class, forVariable(variable), "null", "geo");
        addMetadata();
    }

    public SQLGeo(String variable, String schema, String table) {
        super(SQLGeo.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLGeo(Path<? extends SQLGeo> path) {
        super(path.getType(), path.getMetadata(), "null", "geo");
        addMetadata();
    }

    public SQLGeo(PathMetadata<?> metadata) {
        super(SQLGeo.class, metadata, "null", "geo");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP));
        addMetadata(geoId, ColumnMetadata.named("geoId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(geoName, ColumnMetadata.named("geoName").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(geoType, ColumnMetadata.named("geoType").withIndex(4).ofType(Types.INTEGER).withSize(10));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
    }

}

