package org.jullaene.walkmong_back.api.review.repository;

import org.jullaene.walkmong_back.api.review.dto.res.HashtagResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagCustomRepository {
    List<HashtagResponseDto> findTop3HashtagsByWalkerId(Long walkerId);

}
