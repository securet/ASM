package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLModule is a Querydsl query type for SQLModule
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLModule extends com.mysema.query.sql.RelationalPathBase<SQLModule> {

    private static final long serialVersionUID = 275633769;

    public static final SQLModule module = new SQLModule("module");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final NumberPath<Integer> moduleId = createNumber("moduleId", Integer.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<SQLModule> primary = createPrimaryKey(moduleId);

    public final com.mysema.query.sql.ForeignKey<SQLSite> _siteModuleFk = createInvForeignKey(moduleId, "moduleId");

    public SQLModule(String variable) {
        super(SQLModule.class, forVariable(variable), "null", "module");
        addMetadata();
    }

    public SQLModule(String variable, String schema, String table) {
        super(SQLModule.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLModule(Path<? extends SQLModule> path) {
        super(path.getType(), path.getMetadata(), "null", "module");
        addMetadata();
    }

    public SQLModule(PathMetadata<?> metadata) {
        super(SQLModule.class, metadata, "null", "module");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(4).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(5).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(moduleId, ColumnMetadata.named("moduleId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(255));
    }

}

