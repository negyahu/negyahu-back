package ga.negyahu.music.account;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = -33983002L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccount account = new QAccount("account");

    public final ga.negyahu.music.account.entity.QAddress address;

    public final ga.negyahu.music.area.QArea area;

    public final BooleanPath areaCertify = createBoolean("areaCertify");

    public final StringPath country = createString("country");

    public final StringPath email = createString("email");

    public final ListPath<ga.negyahu.music.fileupload.account.AccountFileUpLoad, ga.negyahu.music.fileupload.account.QAccountFileUpLoad> fileUpLoads = this.<ga.negyahu.music.fileupload.account.AccountFileUpLoad, ga.negyahu.music.fileupload.account.QAccountFileUpLoad>createList("fileUpLoads", ga.negyahu.music.fileupload.account.AccountFileUpLoad.class, ga.negyahu.music.fileupload.account.QAccountFileUpLoad.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isMemberShip = createBoolean("isMemberShip");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final EnumPath<ga.negyahu.music.account.entity.Role> role = createEnum("role", ga.negyahu.music.account.entity.Role.class);

    public final DatePath<java.time.LocalDate> signUpDate = createDate("signUpDate", java.time.LocalDate.class);

    public final EnumPath<ga.negyahu.music.account.entity.State> state = createEnum("state", ga.negyahu.music.account.entity.State.class);

    public final StringPath username = createString("username");

    public QAccount(String variable) {
        this(Account.class, forVariable(variable), INITS);
    }

    public QAccount(Path<? extends Account> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccount(PathMetadata metadata, PathInits inits) {
        this(Account.class, metadata, inits);
    }

    public QAccount(Class<? extends Account> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new ga.negyahu.music.account.entity.QAddress(forProperty("address")) : null;
        this.area = inits.isInitialized("area") ? new ga.negyahu.music.area.QArea(forProperty("area")) : null;
    }

}

