package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLGeoAssoc is a Querydsl query type for SQLGeoAssoc
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLGeoAssoc extends com.mysema.query.sql.RelationalPathBase<SQLGeoAssoc> {

    private static final long serialVersionUID = 1006140417;

    public static final SQLGeoAssoc geoAssoc = new SQLGeoAssoc("geo_assoc");

    public final StringPath assocType = createString("assocType");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath geoId = createString("geoId");

    public final StringPath geoIdTo = createString("geoIdTo");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<SQLGeoAssoc> primary = createPrimaryKey(geoId, geoIdTo);

    public final com.mysema.query.sql.ForeignKey<SQLGeo> geoGaToFk = createForeignKey(geoId, "geoId");

    public final com.mysema.query.sql.ForeignKey<SQLGeo> geoGaFromFk = createForeignKey(geoId, "geoId");

    public SQLGeoAssoc(String variable) {
        super(SQLGeoAssoc.class, forVariable(variable), "null", "geo_assoc");
        addMetadata();
    }

    public SQLGeoAssoc(String variable, String schema, String table) {
        super(SQLGeoAssoc.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLGeoAssoc(Path<? extends SQLGeoAssoc> path) {
        super(path.getType(), path.getMetadata(), "null", "geo_assoc");
        addMetadata();
    }

    public SQLGeoAssoc(PathMetadata<?> metadata) {
        super(SQLGeoAssoc.class, metadata, "null", "geo_assoc");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(assocType, ColumnMetadata.named("assocType").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
        addMetadata(geoId, ColumnMetadata.named("geoId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(geoIdTo, ColumnMetadata.named("geoIdTo").withIndex(2).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(4).ofType(Types.TIMESTAMP));
    }

}

