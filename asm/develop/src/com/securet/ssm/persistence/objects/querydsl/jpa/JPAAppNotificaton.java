package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.AppNotificaton;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAAppNotificaton is a Querydsl query type for AppNotificaton
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAAppNotificaton extends EntityPathBase<AppNotificaton> {

    private static final long serialVersionUID = 843602659L;

    public static final JPAAppNotificaton appNotificaton = new JPAAppNotificaton("appNotificaton");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final BooleanPath appUpdate = createBoolean("appUpdate");

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final DateTimePath<java.sql.Timestamp> fromDate = createDateTime("fromDate", java.sql.Timestamp.class);

    public final BooleanPath isAppUpdate = createBoolean("isAppUpdate");

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final NumberPath<Double> maxAppVersion = createNumber("maxAppVersion", Double.class);

    public final StringPath message = createString("message");

    public final NumberPath<Double> minAppVersion = createNumber("minAppVersion", Double.class);

    public final NumberPath<Integer> notificationId = createNumber("notificationId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> thruDate = createDateTime("thruDate", java.sql.Timestamp.class);

    public JPAAppNotificaton(String variable) {
        super(AppNotificaton.class, forVariable(variable));
    }

    public JPAAppNotificaton(Path<? extends AppNotificaton> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAAppNotificaton(PathMetadata<?> metadata) {
        super(AppNotificaton.class, metadata);
    }

}

