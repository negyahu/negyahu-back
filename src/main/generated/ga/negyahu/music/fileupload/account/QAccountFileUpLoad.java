package ga.negyahu.music.fileupload.account;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccountFileUpLoad is a Querydsl query type for AccountFileUpLoad
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAccountFileUpLoad extends EntityPathBase<AccountFileUpLoad> {

    private static final long serialVersionUID = 995894430L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccountFileUpLoad accountFileUpLoad = new QAccountFileUpLoad("accountFileUpLoad");

    public final ga.negyahu.music.account.QAccount account;

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath originalName = createString("originalName");

    public QAccountFileUpLoad(String variable) {
        this(AccountFileUpLoad.class, forVariable(variable), INITS);
    }

    public QAccountFileUpLoad(Path<? extends AccountFileUpLoad> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccountFileUpLoad(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccountFileUpLoad(PathMetadata metadata, PathInits inits) {
        this(AccountFileUpLoad.class, metadata, inits);
    }

    public QAccountFileUpLoad(Class<? extends AccountFileUpLoad> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new ga.negyahu.music.account.QAccount(forProperty("account"), inits.get("account")) : null;
    }

}

