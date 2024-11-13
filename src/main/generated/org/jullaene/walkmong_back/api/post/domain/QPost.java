package org.jullaene.walkmong_back.api.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Board> {

    private static final long serialVersionUID = -294090118L;

    public static final QPost post = new QPost("post");

    public final org.jullaene.walkmong_back.common.QBaseEntity _super = new org.jullaene.walkmong_back.common.QBaseEntity(this);

    public final StringPath addressMemo = createString("addressMemo");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final NumberPath<Long> dogId = createNumber("dogId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final StringPath locationNegotiableYn = createString("locationNegotiableYn");

    public final StringPath matchingYn = createString("matchingYn");

    public final StringPath memo = createString("memo");

    public final NumberPath<Long> ownerAddressId = createNumber("ownerAddressId", Long.class);

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final StringPath preMeetAvailableYn = createString("preMeetAvailableYn");

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final StringPath suppliesProvideYn = createString("suppliesProvideYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final EnumPath<org.jullaene.walkmong_back.api.post.domain.enums.WalkingStatus> walkingStatus = createEnum("walkingStatus", org.jullaene.walkmong_back.api.post.domain.enums.WalkingStatus.class);

    public QPost(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QPost(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPost(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

