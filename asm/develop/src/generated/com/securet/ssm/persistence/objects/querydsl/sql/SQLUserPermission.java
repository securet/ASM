package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLUserPermission is a Querydsl query type for SQLUserPermission
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLUserPermission extends com.mysema.query.sql.RelationalPathBase<SQLUserPermission> {

    private static final long serialVersionUID = 876834551;

    public static final SQLUserPermission userPermission = new SQLUserPermission("user_permission");

    public final StringPath permissionsPermissionId = createString("permissionsPermissionId");

    public final StringPath userUserId = createString("userUserId");

    public final com.mysema.query.sql.ForeignKey<SQLPermission> _3uhj3herm3yikij6yl0v3ouvFK = createForeignKey(permissionsPermissionId, "permissionId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> durpi1ak6uhdfdj9wgv9ppqucFK = createForeignKey(userUserId, "userId");

    public SQLUserPermission(String variable) {
        super(SQLUserPermission.class, forVariable(variable), "null", "user_permission");
        addMetadata();
    }

    public SQLUserPermission(String variable, String schema, String table) {
        super(SQLUserPermission.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLUserPermission(Path<? extends SQLUserPermission> path) {
        super(path.getType(), path.getMetadata(), "null", "user_permission");
        addMetadata();
    }

    public SQLUserPermission(PathMetadata<?> metadata) {
        super(SQLUserPermission.class, metadata, "null", "user_permission");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(permissionsPermissionId, ColumnMetadata.named("permissions_permissionId").withIndex(2).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(userUserId, ColumnMetadata.named("User_userId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

