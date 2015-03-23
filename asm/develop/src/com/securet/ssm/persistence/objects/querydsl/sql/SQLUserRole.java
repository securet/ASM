package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLUserRole is a Querydsl query type for SQLUserRole
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLUserRole extends com.mysema.query.sql.RelationalPathBase<SQLUserRole> {

    private static final long serialVersionUID = -1066195042;

    public static final SQLUserRole userRole = new SQLUserRole("user_role");

    public final NumberPath<Integer> rolesRoleTypeId = createNumber("rolesRoleTypeId", Integer.class);

    public final StringPath userUserId = createString("userUserId");

    public final com.mysema.query.sql.ForeignKey<SQLRoleType> pf69iacod2cfptqoyrpp97lypFK = createForeignKey(rolesRoleTypeId, "roleTypeId");

    public final com.mysema.query.sql.ForeignKey<SQLUser> _5ban12q0ns21khmjy86hjvvoeFK = createForeignKey(userUserId, "userId");

    public SQLUserRole(String variable) {
        super(SQLUserRole.class, forVariable(variable), "null", "user_role");
        addMetadata();
    }

    public SQLUserRole(String variable, String schema, String table) {
        super(SQLUserRole.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLUserRole(Path<? extends SQLUserRole> path) {
        super(path.getType(), path.getMetadata(), "null", "user_role");
        addMetadata();
    }

    public SQLUserRole(PathMetadata<?> metadata) {
        super(SQLUserRole.class, metadata, "null", "user_role");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(rolesRoleTypeId, ColumnMetadata.named("roles_roleTypeId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userUserId, ColumnMetadata.named("User_userId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

