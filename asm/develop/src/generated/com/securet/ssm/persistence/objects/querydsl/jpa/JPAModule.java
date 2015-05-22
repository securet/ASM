package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.Module;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAModule is a Querydsl query type for Module
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAModule extends EntityPathBase<Module> {

    private static final long serialVersionUID = 1218409876L;

    public static final JPAModule module = new JPAModule("module");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final StringPath description = createString("description");

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final NumberPath<Integer> moduleId = createNumber("moduleId", Integer.class);

    public final StringPath name = createString("name");

    public JPAModule(String variable) {
        super(Module.class, forVariable(variable));
    }

    public JPAModule(Path<? extends Module> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAModule(PathMetadata<?> metadata) {
        super(Module.class, metadata);
    }

}

