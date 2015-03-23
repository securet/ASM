package com.securet.ssm.persistence.objects.querydsl.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;
import java.sql.Types;




/**
 * SQLUser is a Querydsl query type for SQLUser
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SQLUser extends com.mysema.query.sql.RelationalPathBase<SQLUser> {

    private static final long serialVersionUID = -298911992;

    public static final SQLUser user = new SQLUser("user");

    public final DateTimePath<java.sql.Timestamp> createdTimestamp = createDateTime("createdTimestamp", java.sql.Timestamp.class);

    public final StringPath emailId = createString("emailId");

    public final BooleanPath enableNotifications = createBoolean("enableNotifications");

    public final StringPath fullName = createString("fullName");

    public final DateTimePath<java.sql.Timestamp> lastUpdatedTimestamp = createDateTime("lastUpdatedTimestamp", java.sql.Timestamp.class);

    public final StringPath mobile = createString("mobile");

    public final NumberPath<Integer> organizationId = createNumber("organizationId", Integer.class);

    public final StringPath userId = createString("userId");

    public final com.mysema.query.sql.PrimaryKey<SQLUser> primary = createPrimaryKey(userId);

    public final com.mysema.query.sql.ForeignKey<SQLOrganization> gcfvorwtyx7ixe0mxbnkg1b39FK = createForeignKey(organizationId, "organizationId");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _drigf1w0r9ivlvn5hjki8n4tvFK = createInvForeignKey(userId, "resolverUserId");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _ticketModifiedByFk = createInvForeignKey(userId, "modifiedBy");

    public final com.mysema.query.sql.ForeignKey<SQLTicketArchive> __4jc28ku9yq01w7fno1yig7nyeFK = createInvForeignKey(userId, "resolverUserId");

    public final com.mysema.query.sql.ForeignKey<SQLUserLogin> _userUserloginFk = createInvForeignKey(userId, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _jf247bmk0iqmf67x6btuv5yrlFK = createInvForeignKey(userId, "reporterUserId");

    public final com.mysema.query.sql.ForeignKey<SQLTicket> _pex2v8n74m2im2ir3cb37ju26FK = createInvForeignKey(userId, "createdBy");

    public final com.mysema.query.sql.ForeignKey<SQLUserPermission> _durpi1ak6uhdfdj9wgv9ppqucFK = createInvForeignKey(userId, "User_userId");

    public final com.mysema.query.sql.ForeignKey<SQLVendorServiceAsset> __3jyblkqe3x9rcibrjvbtjov5xFK = createInvForeignKey(userId, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLTicketArchive> _bjy6xtu2369egiu9cav4aoj4oFK = createInvForeignKey(userId, "reporterUserId");

    public final com.mysema.query.sql.ForeignKey<SQLUserRole> __5ban12q0ns21khmjy86hjvvoeFK = createInvForeignKey(userId, "User_userId");

    public final com.mysema.query.sql.ForeignKey<SQLClientUserSite> _en28863xmllp06w0faa0lhqueFK = createInvForeignKey(userId, "userId");

    public final com.mysema.query.sql.ForeignKey<SQLTicketArchive> _ticketArchiveModifiedByFk = createInvForeignKey(userId, "modifiedBy");

    public SQLUser(String variable) {
        super(SQLUser.class, forVariable(variable), "null", "user");
        addMetadata();
    }

    public SQLUser(String variable, String schema, String table) {
        super(SQLUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SQLUser(Path<? extends SQLUser> path) {
        super(path.getType(), path.getMetadata(), "null", "user");
        addMetadata();
    }

    public SQLUser(PathMetadata<?> metadata) {
        super(SQLUser.class, metadata, "null", "user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdTimestamp, ColumnMetadata.named("createdTimestamp").withIndex(2).ofType(Types.TIMESTAMP));
        addMetadata(emailId, ColumnMetadata.named("emailId").withIndex(4).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(enableNotifications, ColumnMetadata.named("enableNotifications").withIndex(5).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(fullName, ColumnMetadata.named("fullName").withIndex(6).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(lastUpdatedTimestamp, ColumnMetadata.named("lastUpdatedTimestamp").withIndex(3).ofType(Types.TIMESTAMP));
        addMetadata(mobile, ColumnMetadata.named("mobile").withIndex(7).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(organizationId, ColumnMetadata.named("organizationId").withIndex(8).ofType(Types.INTEGER).withSize(10));
        addMetadata(userId, ColumnMetadata.named("userId").withIndex(1).ofType(Types.VARCHAR).withSize(255).notNull());
    }

}

