package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLSite is a Querydsl query type for SQLSite
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLSite extends com.mysema.query.sql.RelationalPathBase<SQLSite> {

    private static final long serialVersionUID = -298980732;

    public static final SQLSite site = new SQLSite("site");

    public final StringPath area = createString("area");

    public final StringPath circle = createString("circle");

    public final StringPath city = createString("city");

    public final StringPath comments = createString("comments");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Integer> moduleId = createNumber("moduleId", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> organizationId = createNumber("organizationId", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final StringPath siteType = createString("siteType");

    public final StringPath state = createString("state");

    public final com.mysema.query.sql.PrimaryKey<SQLSite> primary = createPrimaryKey(siteId);

    public final com.mysema.query.sql.ForeignKey<SQLModule> siteModuleFk = createForeignKey(moduleId, "moduleId");

    public final com.mysema.query.sql.ForeignKey<SQLGeo> _7osstiw3olnj480ttbrvdcmrqFK = createForeignKey(city, "geoId");

    public final com.mysema.query.sql.ForeignKey<SQLGeo> mirv952y6eqy7g8475c8s1supFK = createForeignKey(state, "geoId");

    public final com.mysema.query.sql.ForeignKey<SQLGeo> siteCircleFk = createForeignKey(circle, "geoId");

    public final com.mysema.query.sql.ForeignKey<SQLOrganization> mpobrkd4stmvgjdch7buocqlpFK = createForeignKey(organizationId, "organizationId");

    public final com.mysema.query.sql.ForeignKey<SQLAsset> _ipyw9fgonenky6wr28dsglw9gFK = createInvForeignKey(siteId, "siteId");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _bh7e3r597n6lnlyn6wb2ao0boFK = createInvForeignKey(siteId, "siteId");

    public final com.mysema.query.sql.ForeignKey<SQLClientUserSite> _dg50ref71jbtho58f6dgc7bpdFK = createInvForeignKey(siteId, "siteId");

    public SQLSite(String variable) {
        super(SQLSite.class, forVariable(variable), "null", "site");
        addMetadata();
    }

    public SQLSite(String variable, String schema, String table) {
        super(SQLSite.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLSite(Path<? extends SQLSite> path) {
        super(path.getType(), path.getMetadata(), "null", "site");
        addMetadata();
    }

    public SQLSite(PathMetadata<?> metadata) {
        super(SQLSite.class, metadata, "null", "site");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(area, ColumnMetadata.named("area").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(circle, ColumnMetadata.named("circle").withIndex(14).ofType(Types.VARCHAR).withSize(255));
        addMetadata(city, ColumnMetadata.named("city").withIndex(10).ofType(Types.VARCHAR).withSize(255));
        addMetadata(comments, ColumnMetadata.named("comments").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(latitude, ColumnMetadata.named("latitude").withIndex(6).ofType(Types.DOUBLE).withSize(22).notNull());
        addMetadata(longitude, ColumnMetadata.named("longitude").withIndex(7).ofType(Types.DOUBLE).withSize(22).notNull());
        addMetadata(moduleId, ColumnMetadata.named("moduleId").withIndex(13).ofType(Types.INTEGER).withSize(10));
        addMetadata(name, ColumnMetadata.named("name").withIndex(8).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(organizationId, ColumnMetadata.named("organizationId").withIndex(11).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(siteId, ColumnMetadata.named("siteId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(siteType, ColumnMetadata.named("siteType").withIndex(9).ofType(Types.VARCHAR).withSize(255));
        addMetadata(state, ColumnMetadata.named("state").withIndex(12).ofType(Types.VARCHAR).withSize(255));
    }

}

