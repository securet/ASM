package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.Organization;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAOrganization is a Querydsl query type for Organization
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAOrganization extends EntityPathBase<Organization> {

    private static final long serialVersionUID = 1916452443L;

    public static final JPAOrganization organization = new JPAOrganization("organization");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath logo = createString("logo");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> organizationId = createNumber("organizationId", Integer.class);

    public final StringPath organizationType = createString("organizationType");

    public final StringPath shortDesc = createString("shortDesc");

    public JPAOrganization(String variable) {
        super(Organization.class, forVariable(variable));
    }

    public JPAOrganization(Path<? extends Organization> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAOrganization(PathMetadata<?> metadata) {
        super(Organization.class, metadata);
    }

}

