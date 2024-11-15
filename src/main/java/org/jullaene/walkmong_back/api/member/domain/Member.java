package org.jullaene.walkmong_back.api.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.member.domain.enums.Role;
import org.jullaene.walkmong_back.common.BaseEntity;
import org.jullaene.walkmong_back.common.enums.Gender;

@Table(name = "member")
@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Comment("계정 로그인 이메일")
    private String email;

    @Comment("비밀번호")
    private String password;

    @Comment("닉네임")
    private String nickname;

    @Comment("이름")
    private String name;

    @Comment("생일")
    private Integer birthDate;

    @Comment("성별")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Comment("역할")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Comment("자기소개")
    private String introduce;

    @Comment("프로필 url")
    private String profile;

    @Comment("산책 가능 반려동물 크기")
    private String availabilityWithSize;


    @Builder
    public Member (Long memberId, String email, String password, String nickname,
                  String name, Integer birthDate, Gender gender, Role role,
                  String introduce, String profile, String availabilityWithSize) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role;
        this.introduce = introduce;
        this.profile = profile;
        this.availabilityWithSize = availabilityWithSize;
    }
}
