package org.jullaene.walkmong_back.api.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.post.domain.enums.WalkingStatus;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "post")
@Entity
@DynamicUpdate
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Comment("반려동물 아이디")
    private Long dogId;

    @Comment("반려인 주소 아이디")
    private Long ownerAddressId;

    @Comment("내용")
    private String content;

    @Comment("매칭 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String matchingYn;

    @Comment("산책 시작 시간")
    private LocalDateTime startTime;

    @Comment("산책 종료 시간")
    private LocalDateTime endTime;

    @Comment("산책용품 제공 가능 여부")
    private String suppliesProvideYn;

    @Comment("만남 장소 협의 가능 여부")
    private String locationNegotiableYn;

    @Comment("사전 만남 가능 여부")
    private String preMeetAvailableYn;

    @Comment("주소 메모")
    private String addressMemo;

    @Comment("메모")
    private String memo;

    @Comment("산책 진행 여부")
    private WalkingStatus walkingStatus;

}
