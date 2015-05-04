package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLAppNotification is a Querydsl query type for SQLAppNotification
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLAppNotification extends com.mysema.query.sql.RelationalPathBase<SQLAppNotification> {

    private static final long serialVersionUID = 37464655;

    public static final SQLAppNotification appNotification = new SQLAppNotification("app_notification");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> fromDate = createDateTime("fromDate", java.sql.Timestamp.class);

    public final BooleanPath isAppUpdate = createBoolean("isAppUpdate");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Double> maxAppVersion = createNumber("maxAppVersion", Double.class);

    public final StringPath message = createString("message");

    public final NumberPath<Double> minAppVersion = createNumber("minAppVersion", Double.class);

    public final NumberPath<Integer> notificationId = createNumber("notificationId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> thruDate = createDateTime("thruDate", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<SQLAppNotification> primary = createPrimaryKey(notificationId);

    public SQLAppNotification(String variable) {
        super(SQLAppNotification.class, forVariable(variable), "null", "app_notification");
        addMetadata();
    }

    public SQLAppNotification(String variable, String schema, String table) {
        super(SQLAppNotification.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLAppNotification(Path<? extends SQLAppNotification> path) {
        super(path.getType(), path.getMetadata(), "null", "app_notification");
        addMetadata();
    }

    public SQLAppNotification(PathMetadata<?> metadata) {
        super(SQLAppNotification.class, metadata, "null", "app_notification");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(8).ofType(Types.TIMESTAMP));
        addMetadata(fromDate, ColumnMetadata.named("fromDate").withIndex(4).ofType(Types.TIMESTAMP).notNull());
        addMetadata(isAppUpdate, ColumnMetadata.named("isAppUpdate").withIndex(3).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(9).ofType(Types.TIMESTAMP));
        addMetadata(maxAppVersion, ColumnMetadata.named("maxAppVersion").withIndex(7).ofType(Types.DOUBLE).withSize(22).notNull());
        addMetadata(message, ColumnMetadata.named("message").withIndex(2).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(minAppVersion, ColumnMetadata.named("minAppVersion").withIndex(6).ofType(Types.DOUBLE).withSize(22).notNull());
        addMetadata(notificationId, ColumnMetadata.named("notificationId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(thruDate, ColumnMetadata.named("thruDate").withIndex(5).ofType(Types.TIMESTAMP));
    }

}

