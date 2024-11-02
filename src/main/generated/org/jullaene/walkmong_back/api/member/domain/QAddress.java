package org.jullaene.walkmong_back.api.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddress extends EntityPathBase<Address> {

    private static final long serialVersionUID = 950091296L;

    public static final QAddress address = new QAddress("address");

    public final org.jullaene.walkmong_back.common.QBaseEntity _super = new org.jullaene.walkmong_back.common.QBaseEntity(this);

    public final NumberPath<Long> addressId = createNumber("addressId", Long.class);

    public final StringPath basicAddressYn = createString("basicAddressYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final EnumPath<org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange> distanceRange = createEnum("distanceRange", org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange.class);

    public final StringPath dongAddress = createString("dongAddress");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath roadAddress = createString("roadAddress");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAddress(String variable) {
        super(Address.class, forVariable(variable));
    }

    public QAddress(Path<? extends Address> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddress(PathMetadata metadata) {
        super(Address.class, metadata);
    }

}

