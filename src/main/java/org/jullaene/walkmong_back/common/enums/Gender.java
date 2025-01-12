package org.jullaene.walkmong_back.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Gender {
    FEMALE("FEMALE", "여자"),
    MALE("MALE", "남자");

    private final String code;
    private final String name;
}