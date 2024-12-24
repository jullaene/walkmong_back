package org.jullaene.walkmong_back.api.dog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.common.enums.Gender;
import org.jullaene.walkmong_back.common.BaseEntity;

@Table(name = "dog")
@Entity
@DynamicUpdate
public class Dog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Long dogId;

    @Comment("사용자 아이디")
    private Long memberId;

    @Comment("이름")
    private String name;

    @Comment("성별")
    private Gender gender;

    @Comment("출생년도")
    private Integer birthYear;

    @Comment("무게")
    private Double weight;

    @Comment("품종")
    private String breed;

    @Comment("사이즈")
    private DogSize dogSize;

    @Comment("프로필 url")
    private String profile;

    @Comment("중성화 유무")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String neuteringYn;

    @Comment("입질 여부")
    private String bite;

    @Comment("친화력")
    private String friendly;

    @Comment("짖음 여부")
    private String barking;

    @Comment("성견 여부")
    private String adultYn;

    @Comment("광견병 접종 여부")
    @Column(columnDefinition = "VARCHAR(1) default 'N'")
    private String rabiesYn;

    @Comment("산책 참고 사항")
    private String walkRequest;

    @Comment("산책 요청 사항")
    private String walkNote;

    @Comment("추가 안내 사항")
    private String additionalRequest;

}
