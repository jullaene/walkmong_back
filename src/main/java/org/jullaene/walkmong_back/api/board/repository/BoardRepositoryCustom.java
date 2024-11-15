package org.jullaene.walkmong_back.api.board.repository;

import org.jullaene.walkmong_back.api.board.dto.res.BoardRes;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.api.member.domain.Address;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;

import java.time.LocalDate;
import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardRes> getBoardsWithFilters(LocalDate date, Address walkerAddress, DistanceRange distance, DogSize dogSize, String matchingYn);
}
