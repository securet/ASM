package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLOrganization is a Querydsl query type for SQLOrganization
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLOrganization extends com.mysema.query.sql.RelationalPathBase<SQLOrganization> {

    private static final long serialVersionUID = 22047600;

    public static final SQLOrganization organization = new SQLOrganization("organization");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath logo = createString("logo");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> organizationId = createNumber("organizationId", Integer.class);

    public final StringPath organizationType = createString("organizationType");

    public final StringPath shortDesc = createString("shortDesc");

    public final com.mysema.query.sql.PrimaryKey<SQLOrganization> primary = createPrimaryKey(organizationId);

    public final com.mysema.query.sql.ForeignKey<SQLUser> _gcfvorwtyx7ixe0mxbnkg1b39FK = createInvForeignKey(organizationId, "organizationId");

    public final com.mysema.query.sql.ForeignKey<SQLServiceSparePart> _spareVendorOrgFk = createInvForeignKey(organizationId, "vendorOrganizationId");

    public final com.mysema.query.sql.ForeignKey<SQLSite> _mpobrkd4stmvgjdch7buocqlpFK = createInvForeignKey(organizationId, "organizationId");

    public SQLOrganization(String variable) {
        super(SQLOrganization.class, forVariable(variable), "null", "organization");
        addMetadata();
    }

    public SQLOrganization(String variable, String schema, String table) {
        super(SQLOrganization.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLOrganization(Path<? extends SQLOrganization> path) {
        super(path.getType(), path.getMetadata(), "null", "organization");
        addMetadata();
    }

    public SQLOrganization(PathMetadata<?> metadata) {
        super(SQLOrganization.class, metadata, "null", "organization");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(logo, ColumnMetadata.named("logo").withIndex(4).ofType(Types.VARCHAR).withSize(255));
        addMetadata(name, ColumnMetadata.named("name").withIndex(5).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(organizationId, ColumnMetadata.named("organizationId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(organizationType, ColumnMetadata.named("organizationType").withIndex(6).ofType(Types.VARCHAR).withSize(255));
        addMetadata(shortDesc, ColumnMetadata.named("shortDesc").withIndex(7).ofType(Types.VARCHAR).withSize(255));
    }

}

