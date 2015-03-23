package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.SequenceGenerator;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPASequenceGenerator is a Querydsl query type for SequenceGenerator
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPASequenceGenerator extends EntityPathBase<SequenceGenerator> {

    private static final long serialVersionUID = -212397078L;

    public static final JPASequenceGenerator sequenceGenerator = new JPASequenceGenerator("sequenceGenerator");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath sequenceName = createString("sequenceName");

    public final NumberPath<Long> sequenceValue = createNumber("sequenceValue", Long.class);

    public JPASequenceGenerator(String variable) {
        super(SequenceGenerator.class, forVariable(variable));
    }

    public JPASequenceGenerator(Path<? extends SequenceGenerator> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPASequenceGenerator(PathMetadata<?> metadata) {
        super(SequenceGenerator.class, metadata);
    }

}

