package org.jullaene.walkmong_back.api.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -539805620L;

    public static final QBoard board = new QBoard("board");

    public final org.jullaene.walkmong_back.common.QBaseEntity _super = new org.jullaene.walkmong_back.common.QBaseEntity(this);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final NumberPath<Long> dogId = createNumber("dogId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final StringPath locationNegotiationYn = createString("locationNegotiationYn");

    public final StringPath matchingYn = createString("matchingYn");

    public final NumberPath<Long> ownerAddressId = createNumber("ownerAddressId", Long.class);

    public final StringPath preMeetAvailableYn = createString("preMeetAvailableYn");

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final StringPath suppliesProvideYn = createString("suppliesProvideYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final EnumPath<org.jullaene.walkmong_back.api.board.domain.enums.WalkingStatus> walkingStatus = createEnum("walkingStatus", org.jullaene.walkmong_back.api.board.domain.enums.WalkingStatus.class);

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

