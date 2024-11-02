package org.jullaene.walkmong_back.api.request.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRequest is a Querydsl query type for Request
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRequest extends EntityPathBase<Request> {

    private static final long serialVersionUID = 632574242L;

    public static final QRequest request = new QRequest("request");

    public final org.jullaene.walkmong_back.common.QBaseEntity _super = new org.jullaene.walkmong_back.common.QBaseEntity(this);

    public final StringPath addressMemo = createString("addressMemo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final StringPath dogCollarYn = createString("dogCollarYn");

    public final StringPath dongAddress = createString("dongAddress");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final EnumPath<org.jullaene.walkmong_back.api.request.domain.enums.MatchingStatus> matchingStatus = createEnum("matchingStatus", org.jullaene.walkmong_back.api.request.domain.enums.MatchingStatus.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath memoToOwner = createString("memoToOwner");

    public final StringPath nuzzleYn = createString("nuzzleYn");

    public final StringPath poopBagYn = createString("poopBagYn");

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final StringPath preMeetYn = createString("preMeetYn");

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final StringPath roadAddress = createString("roadAddress");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRequest(String variable) {
        super(Request.class, forVariable(variable));
    }

    public QRequest(Path<? extends Request> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRequest(PathMetadata metadata) {
        super(Request.class, metadata);
    }

}

