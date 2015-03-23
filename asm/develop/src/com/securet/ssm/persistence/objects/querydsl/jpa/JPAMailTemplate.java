package com.securet.ssm.persistence.objects.querydsl.jpa;

import static com.mysema.query.types.PathMetadataFactory.*;
import com.securet.ssm.persistence.objects.MailTemplate;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * JPAMailTemplate is a Querydsl query type for MailTemplate
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class JPAMailTemplate extends EntityPathBase<MailTemplate> {

    private static final long serialVersionUID = 1364484217L;

    public static final JPAMailTemplate mailTemplate = new JPAMailTemplate("mailTemplate");

    public final JPASecureTObject _super = new JPASecureTObject(this);

    public final StringPath bcc = createString("bcc");

    public final StringPath cc = createString("cc");

    public final StringPath contentType = createString("contentType");

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdTimestamp = _super.createdTimestamp;

    public final StringPath from = createString("from");

    //inherited
    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = _super.lastUpdatedTimestamp;

    public final StringPath subject = createString("subject");

    public final StringPath templateFileName = createString("templateFileName");

    public final StringPath templateName = createString("templateName");

    public JPAMailTemplate(String variable) {
        super(MailTemplate.class, forVariable(variable));
    }

    public JPAMailTemplate(Path<? extends MailTemplate> path) {
        super(path.getType(), path.getMetadata());
    }

    public JPAMailTemplate(PathMetadata<?> metadata) {
        super(MailTemplate.class, metadata);
    }

}

