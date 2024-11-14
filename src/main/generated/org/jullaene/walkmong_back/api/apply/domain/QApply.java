package org.jullaene.walkmong_back.api.apply.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QApply is a Querydsl query type for Apply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApply extends EntityPathBase<Apply> {

    private static final long serialVersionUID = 57863106L;

    public static final QApply apply = new QApply("apply");

    public final org.jullaene.walkmong_back.common.QBaseEntity _super = new org.jullaene.walkmong_back.common.QBaseEntity(this);

    public final StringPath addressDetail = createString("addressDetail");

    public final StringPath addressMemo = createString("addressMemo");

    public final NumberPath<Long> applyId = createNumber("applyId", Long.class);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final StringPath dogCollarYn = createString("dogCollarYn");

    public final StringPath dongAddress = createString("dongAddress");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final EnumPath<org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus> matchingStatus = createEnum("matchingStatus", org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath memoToOwner = createString("memoToOwner");

    public final StringPath nuzzleYn = createString("nuzzleYn");

    public final StringPath poopBagYn = createString("poopBagYn");

    public final StringPath preMeetingYn = createString("preMeetingYn");

    public final StringPath roadAddress = createString("roadAddress");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QApply(String variable) {
        super(Apply.class, forVariable(variable));
    }

    public QApply(Path<? extends Apply> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApply(PathMetadata metadata) {
        super(Apply.class, metadata);
    }

}

