package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLClientUserSite is a Querydsl query type for SQLClientUserSite
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLClientUserSite extends com.mysema.query.sql.RelationalPathBase<SQLClientUserSite> {

    private static final long serialVersionUID = 516989338;

    public static final SQLClientUserSite clientUserSite = new SQLClientUserSite("client_user_site");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final StringPath userId = createString("userId");

    public final com.mysema.query.sql.PrimaryKey<SQLClientUserSite> primary = createPrimaryKey(siteId, userId);

    public final com.mysema.query.sql.ForeignKey<SQLSite> dg50ref71jbtho58f6dgc7bpdFK = createForeignKey(siteId, "siteId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> en28863xmllp06w0faa0lhqueFK = createForeignKey(userId, "userId");

    public SQLClientUserSite(String variable) {
        super(SQLClientUserSite.class, forVariable(variable), "null", "client_user_site");
        addMetadata();
    }

    public SQLClientUserSite(String variable, String schema, String table) {
        super(SQLClientUserSite.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLClientUserSite(Path<? extends SQLClientUserSite> path) {
        super(path.getType(), path.getMetadata(), "null", "client_user_site");
        addMetadata();
    }

    public SQLClientUserSite(PathMetadata<?> metadata) {
        super(SQLClientUserSite.class, metadata, "null", "client_user_site");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(1).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(siteId, ColumnMetadata.named("siteId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userId, ColumnMetadata.named("userId").withIndex(4).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

