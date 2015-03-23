package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.IssueType;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * JPAIssueType is a Querydsl query type for IssueType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAIssueType extends EntityPathBase<IssueType> {

    private static final long serialVersionUID = -1678010485L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final JPAIssueType issueType = new JPAIssueType("issueType");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final NumberPath<Integer> issueTypeId = createNumber("issueTypeId", Integer.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath name = createString("name");

    public final JPAServiceType serviceType;

    public JPAIssueType(String variable) {
        this(IssueType.class, forVariable(variable), INITS);
    }

    public JPAIssueType(Path<? extends IssueType> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAIssueType(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public JPAIssueType(PathMetadata<?> metadata, PathInits inits) {
        this(IssueType.class, metadata, inits);
    }

    public JPAIssueType(Class<? extends IssueType> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.serviceType = inits.isInitialized("serviceType") ? new JPAServiceType(forProperty("serviceType")) : null;
    }

}

