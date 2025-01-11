package org.jullaene.walkmong_back.api.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jullaene.walkmong_back.api.member.domain.enums.Provider;
import org.jullaene.walkmong_back.api.member.domain.enums.Role;
import org.jullaene.walkmong_back.api.member.dto.req.MemberCreateReq;
import org.jullaene.walkmong_back.api.member.dto.req.MemberReqDto;
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

    @Comment("폰 번호")
    private String phone;

    @Comment("반려견 키운 경험")
    private String dogOwnership;

    @Comment("산책 경험 여부")
    private String dogWalkingExperienceYn;

    @Comment("산책 가능 반려동물 크기")
    private String availabilityWithSize;

    @Comment("소셜 로그인 공급자 ID")
    private String providerId;

    @Comment("소셜 로그인 제공자")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Builder
    public Member (MemberCreateReq memberCreateReq, String profileUrl, String password) {
        this.email = memberCreateReq.getEmail();
        this.password = password;
        this.nickname = memberCreateReq.getNickname();
        this.name = memberCreateReq.getName();
        this.birthDate = memberCreateReq.getBirthDate();
        this.gender = memberCreateReq.getGender();
        this.phone = memberCreateReq.getPhone();
        this.role = Role.WALKER;
        this.profile = profileUrl;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Member(String email, String providerId, Provider provider) {
        this.email = email;
        this.providerId = providerId;
        this.provider = provider;
    }

    public void linkSocialAccount(String providerId, Provider provider) {
        this.providerId = providerId;
        this.provider = provider;
    }

    public void addWalkingExperience(WalkExperienceReq walkExperienceReq) {
        this.dogOwnership = walkExperienceReq.getDogOwnershipYn();
        this.dogWalkingExperienceYn = walkExperienceReq.getDogWalkingExperienceYn();
        this.availabilityWithSize = walkExperienceReq.getAvailabilityWithSize();
        this.introduce = walkExperienceReq.getIntroduction();
    }

    public void update(MemberReqDto memberReqDto, String profileUrl) {
        this.nickname = memberReqDto.getNickname();
        this.introduce = memberReqDto.getIntroduction();
        this.name = memberReqDto.getName();
        this.gender = memberReqDto.getGender();
        this.birthDate = memberReqDto.getBirthDate();
        this.phone = memberReqDto.getPhone();
        this.profile = profileUrl;
    }
}
