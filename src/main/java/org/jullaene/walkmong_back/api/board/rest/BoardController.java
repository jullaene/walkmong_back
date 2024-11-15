package org.jullaene.walkmong_back.api.board.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.board.dto.res.BoardRes;
import org.jullaene.walkmong_back.api.board.service.BoardService;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Board", description = "게시글 정보 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<BasicResponse<List<BoardRes>>> getBoards (
            @RequestParam(name = "date", required = false) LocalDate date,
            @RequestParam(name = "addressId", required = false) Long addressId,
            @RequestParam(name = "distance", required = false) DistanceRange distance,
            @RequestParam(name = "dogSize", required = false) DogSize dogSize,
            @RequestParam(name = "matchingYn", required = false) String matchingYn
    ) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getBoards(date, addressId, distance, dogSize, matchingYn)));
    }

}
