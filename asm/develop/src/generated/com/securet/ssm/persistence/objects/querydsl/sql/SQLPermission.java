package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLPermission is a Querydsl query type for SQLPermission
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLPermission extends com.mysema.query.sql.RelationalPathBase<SQLPermission> {

    private static final long serialVersionUID = -192565428;

    public static final SQLPermission permission1 = new SQLPermission("permission");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath permission = createString("permission");

    public final StringPath permissionId = createString("permissionId");

    public final com.mysema.query.sql.PrimaryKey<SQLPermission> primary = createPrimaryKey(permissionId);

    public final com.mysema.query.sql.ForeignKey<SQLUserPermission> __3uhj3herm3yikij6yl0v3ouvFK = createInvForeignKey(permissionId, "permissions_permissionId");

    public SQLPermission(String variable) {
        super(SQLPermission.class, forVariable(variable), "null", "permission");
        addMetadata();
    }

    public SQLPermission(String variable, String schema, String table) {
        super(SQLPermission.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLPermission(Path<? extends SQLPermission> path) {
        super(path.getType(), path.getMetadata(), "null", "permission");
        addMetadata();
    }

    public SQLPermission(PathMetadata<?> metadata) {
        super(SQLPermission.class, metadata, "null", "permission");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(permission, ColumnMetadata.named("permission").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(permissionId, ColumnMetadata.named("permissionId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

