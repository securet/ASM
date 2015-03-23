package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLUserLogin is a Querydsl query type for SQLUserLogin
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLUserLogin extends com.mysema.query.sql.RelationalPathBase<SQLUserLogin> {

    private static final long serialVersionUID = 1302146369;

    public static final SQLUserLogin userLogin = new SQLUserLogin("user_login");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> disabledTimestamp = createDateTime("disabledTimestamp", java.sql.Timestamp.class);

    public final BooleanPath enabled = createBoolean("enabled");

    public final DateTimePath<java.sql.Timestamp> lastLoginTimestamp = createDateTime("lastLoginTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath password = createString("password");

    public final StringPath userId = createString("userId");

    public final com.mysema.query.sql.PrimaryKey<SQLUserLogin> primary = createPrimaryKey(userId);

    public final com.mysema.query.sql.ForeignKey<SQLUser> userUserloginFk = createForeignKey(userId, "userId");

    public SQLUserLogin(String variable) {
        super(SQLUserLogin.class, forVariable(variable), "null", "user_login");
        addMetadata();
    }

    public SQLUserLogin(String variable, String schema, String table) {
        super(SQLUserLogin.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLUserLogin(Path<? extends SQLUserLogin> path) {
        super(path.getType(), path.getMetadata(), "null", "user_login");
        addMetadata();
    }

    public SQLUserLogin(PathMetadata<?> metadata) {
        super(SQLUserLogin.class, metadata, "null", "user_login");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP));
        addMetadata(disabledTimestamp, ColumnMetadata.named("disabledTimestamp").withIndex(4).ofType(Types.TIMESTAMP));
        addMetadata(enabled, ColumnMetadata.named("enabled").withIndex(5).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(lastLoginTimestamp, ColumnMetadata.named("lastLoginTimestamp").withIndex(6).ofType(Types.TIMESTAMP));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
        addMetadata(password, ColumnMetadata.named("password").withIndex(7).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(userId, ColumnMetadata.named("userId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

