package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLSequenceGenerator is a Querydsl query type for SQLSequenceGenerator
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLSequenceGenerator extends com.mysema.query.sql.RelationalPathBase<SQLSequenceGenerator> {

    private static final long serialVersionUID = -476361739;

    public static final SQLSequenceGenerator sequenceGenerator = new SQLSequenceGenerator("sequence_generator");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath sequenceName = createString("sequenceName");

    public final NumberPath<Long> sequenceValue = createNumber("sequenceValue", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SQLSequenceGenerator> primary = createPrimaryKey(sequenceName);

    public SQLSequenceGenerator(String variable) {
        super(SQLSequenceGenerator.class, forVariable(variable), "null", "sequence_generator");
        addMetadata();
    }

    public SQLSequenceGenerator(String variable, String schema, String table) {
        super(SQLSequenceGenerator.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLSequenceGenerator(Path<? extends SQLSequenceGenerator> path) {
        super(path.getType(), path.getMetadata(), "null", "sequence_generator");
        addMetadata();
    }

    public SQLSequenceGenerator(PathMetadata<?> metadata) {
        super(SQLSequenceGenerator.class, metadata, "null", "sequence_generator");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(4).ofType(Types.TIMESTAMP));
        addMetadata(sequenceName, ColumnMetadata.named("sequenceName").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(sequenceValue, ColumnMetadata.named("sequenceValue").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

