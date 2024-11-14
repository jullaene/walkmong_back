package org.jullaene.walkmong_back.api.board.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "review")
@Entity
@DynamicUpdate
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Comment("게시글 아이디")
    private Long postId;

    @Comment("후기 작성자 아이디")
    private Long reviewerId;

    @Comment("후기 대상자 아이디")
    private Long reviewTargetId;

    @Comment("내용")
    private String content;

    @Comment("평점")
    private Integer rating;
}
