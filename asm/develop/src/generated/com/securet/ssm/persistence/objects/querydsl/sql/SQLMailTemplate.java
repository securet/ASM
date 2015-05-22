package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLMailTemplate is a Querydsl query type for SQLMailTemplate
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLMailTemplate extends com.mysema.query.sql.RelationalPathBase<SQLMailTemplate> {

    private static final long serialVersionUID = -529920626;

    public static final SQLMailTemplate mailTemplate = new SQLMailTemplate("mail_template");

    public final StringPath bcc = createString("bcc");

    public final StringPath cc = createString("cc");

    public final StringPath contentType = createString("contentType");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath from = createString("from");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath subject = createString("subject");

    public final StringPath templateFileName = createString("templateFileName");

    public final StringPath templateName = createString("templateName");

    public final com.mysema.query.sql.PrimaryKey<SQLMailTemplate> primary = createPrimaryKey(templateName);

    public SQLMailTemplate(String variable) {
        super(SQLMailTemplate.class, forVariable(variable), "null", "mail_template");
        addMetadata();
    }

    public SQLMailTemplate(String variable, String schema, String table) {
        super(SQLMailTemplate.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLMailTemplate(Path<? extends SQLMailTemplate> path) {
        super(path.getType(), path.getMetadata(), "null", "mail_template");
        addMetadata();
    }

    public SQLMailTemplate(PathMetadata<?> metadata) {
        super(SQLMailTemplate.class, metadata, "null", "mail_template");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bcc, ColumnMetadata.named("bcc").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(cc, ColumnMetadata.named("cc").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(contentType, ColumnMetadata.named("contentType").withIndex(9).ofType(Types.VARCHAR).withSize(255));
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(7).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(from, ColumnMetadata.named("from").withIndex(2).ofType(Types.VARCHAR).withSize(255));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(8).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(subject, ColumnMetadata.named("subject").withIndex(5).ofType(Types.VARCHAR).withSize(255));
        addMetadata(templateFileName, ColumnMetadata.named("templateFileName").withIndex(6).ofType(Types.VARCHAR).withSize(255));
        addMetadata(templateName, ColumnMetadata.named("templateName").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

