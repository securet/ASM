package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLRoleType is a Querydsl query type for SQLRoleType
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLRoleType extends com.mysema.query.sql.RelationalPathBase<SQLRoleType> {

    private static final long serialVersionUID = -1066238579;

    public static final SQLRoleType roleType1 = new SQLRoleType("role_type");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath roleName = createString("roleName");

    public final StringPath roleType = createString("roleType");

    public final NumberPath<Integer> roleTypeId = createNumber("roleTypeId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SQLRoleType> primary = createPrimaryKey(roleTypeId);

    public final com.mysema.query.sql.ForeignKey<SQLUserRole> _pf69iacod2cfptqoyrpp97lypFK = createInvForeignKey(roleTypeId, "roles_roleTypeId");

    public SQLRoleType(String variable) {
        super(SQLRoleType.class, forVariable(variable), "null", "role_type");
        addMetadata();
    }

    public SQLRoleType(String variable, String schema, String table) {
        super(SQLRoleType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLRoleType(Path<? extends SQLRoleType> path) {
        super(path.getType(), path.getMetadata(), "null", "role_type");
        addMetadata();
    }

    public SQLRoleType(PathMetadata<?> metadata) {
        super(SQLRoleType.class, metadata, "null", "role_type");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(roleName, ColumnMetadata.named("roleName").withIndex(4).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(roleType, ColumnMetadata.named("roleType").withIndex(5).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(roleTypeId, ColumnMetadata.named("roleTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

