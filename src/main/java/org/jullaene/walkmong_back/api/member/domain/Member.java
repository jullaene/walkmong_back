package org.jullaene.walkmong_back.api.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.member.domain.enums.Role;
import org.jullaene.walkmong_back.api.member.dto.req.MemberCreateReq;
import org.jullaene.walkmong_back.api.member.dto.req.WalkExperienceReq;
import org.jullaene.walkmong_back.common.BaseEntity;
import org.jullaene.walkmong_back.common.enums.Gender;

import java.time.LocalDate;

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
    private LocalDate birthDate;

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

    @Comment("반려견 키운 경험")
    private String dogOwnership;

    @Comment("산책 경험 여부")
    private String dogWalkingExperienceYn;

    @Comment("산책 가능 반려동물 크기")
    private String availabilityWithSize;


    @Builder
    public Member (MemberCreateReq memberCreateReq, String profileUrl) {
        this.email = memberCreateReq.getEmail();
        this.password = memberCreateReq.getPassword();
        this.nickname = memberCreateReq.getNickname();
        this.name = memberCreateReq.getName();
        this.birthDate = memberCreateReq.getBirthDate();
        this.gender = memberCreateReq.getGender();
        this.role = Role.WALKER;
        this.profile = profileUrl;
    }

    public void addWalkingExperience(WalkExperienceReq walkExperienceReq) {
        this.dogOwnership = walkExperienceReq.getDogOwnershipYn();
        this.dogWalkingExperienceYn = walkExperienceReq.getDogWalkingExperienceYn();
        this.availabilityWithSize = walkExperienceReq.getAvailabilityWithSize();
        this.introduce = walkExperienceReq.getIntroduction();
    }

    @Override
    public String toString () {
        return "dogOwnerShip " + this.dogOwnership;
    }
}
