package org.jullaene.walkmong_back.api.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 791016174L;

    public static final QMember member = new QMember("member1");

    public final org.jullaene.walkmong_back.common.QBaseEntity _super = new org.jullaene.walkmong_back.common.QBaseEntity(this);

    public final StringPath availabilityWithSize = createString("availabilityWithSize");

    public final NumberPath<Integer> birthDate = createNumber("birthDate", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final StringPath email = createString("email");

    public final EnumPath<org.jullaene.walkmong_back.common.enums.Gender> gender = createEnum("gender", org.jullaene.walkmong_back.common.enums.Gender.class);

    public final StringPath introduce = createString("introduce");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profile = createString("profile");

    public final EnumPath<org.jullaene.walkmong_back.api.member.domain.enums.Role> role = createEnum("role", org.jullaene.walkmong_back.api.member.domain.enums.Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

