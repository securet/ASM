package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLIssueType is a Querydsl query type for SQLIssueType
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLIssueType extends com.mysema.query.sql.RelationalPathBase<SQLIssueType> {

    private static final long serialVersionUID = 1165101718;

    public static final SQLIssueType issueType = new SQLIssueType("issue_type");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath issueGroup = createString("issueGroup");

    public final NumberPath<Integer> issueTypeId = createNumber("issueTypeId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> serviceTypeId = createNumber("serviceTypeId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SQLIssueType> primary = createPrimaryKey(issueTypeId);

    public final com.mysema.query.sql.ForeignKey<SQLServiceType> l2clwhmr3wvv786viy4sp0315FK = createForeignKey(serviceTypeId, "serviceTypeId");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _fvv5yh20hv79k0v5sfthn3uitFK = createInvForeignKey(issueTypeId, "issueTypeId");

    public SQLIssueType(String variable) {
        super(SQLIssueType.class, forVariable(variable), "null", "issue_type");
        addMetadata();
    }

    public SQLIssueType(String variable, String schema, String table) {
        super(SQLIssueType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLIssueType(Path<? extends SQLIssueType> path) {
        super(path.getType(), path.getMetadata(), "null", "issue_type");
        addMetadata();
    }

    public SQLIssueType(PathMetadata<?> metadata) {
        super(SQLIssueType.class, metadata, "null", "issue_type");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(issueGroup, ColumnMetadata.named("issueGroup").withIndex(6).ofType(Types.VARCHAR).withSize(255));
        addMetadata(issueTypeId, ColumnMetadata.named("issueTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(name, ColumnMetadata.named("name").withIndex(4).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(serviceTypeId, ColumnMetadata.named("serviceTypeId").withIndex(5).ofType(Types.INTEGER).withSize(10));
    }

}

