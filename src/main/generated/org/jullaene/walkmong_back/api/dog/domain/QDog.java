package org.jullaene.walkmong_back.api.dog.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDog is a Querydsl query type for Dog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDog extends EntityPathBase<Dog> {

    private static final long serialVersionUID = -556100734L;

    public static final QDog dog = new QDog("dog");

    public final org.jullaene.walkmong_back.common.QBaseEntity _super = new org.jullaene.walkmong_back.common.QBaseEntity(this);

    public final StringPath additionalRequest = createString("additionalRequest");

    public final StringPath barking = createString("barking");

    public final NumberPath<Integer> birthYear = createNumber("birthYear", Integer.class);

    public final StringPath bite = createString("bite");

    public final StringPath breed = createString("breed");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final NumberPath<Long> dogId = createNumber("dogId", Long.class);

    public final EnumPath<org.jullaene.walkmong_back.api.dog.domain.enums.DogSize> dogSize = createEnum("dogSize", org.jullaene.walkmong_back.api.dog.domain.enums.DogSize.class);

    public final StringPath friendly = createString("friendly");

    public final EnumPath<org.jullaene.walkmong_back.common.enums.Gender> gender = createEnum("gender", org.jullaene.walkmong_back.common.enums.Gender.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath name = createString("name");

    public final StringPath neuteringYn = createString("neuteringYn");

    public final StringPath profile = createString("profile");

    public final StringPath rabiesYn = createString("rabiesYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath walkNote = createString("walkNote");

    public final StringPath walkRequest = createString("walkRequest");

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public QDog(String variable) {
        super(Dog.class, forVariable(variable));
    }

    public QDog(Path<? extends Dog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDog(PathMetadata metadata) {
        super(Dog.class, metadata);
    }

}

