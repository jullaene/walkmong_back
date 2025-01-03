package org.jullaene.walkmong_back.api.board.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.board.dto.req.BoardRequestDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardDetailResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardResponseDto;
import org.jullaene.walkmong_back.api.board.service.BoardService;
import org.jullaene.walkmong_back.api.dog.domain.enums.DogSize;
import org.jullaene.walkmong_back.api.member.domain.enums.DistanceRange;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Board", description = "게시글 정보 관련 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<BasicResponse<List<BoardResponseDto>>> getBoards (
            @RequestParam(name = "date", required = false) LocalDate date,
            @RequestParam(name = "addressId", required = false) Long addressId,
            @RequestParam(name = "distance", required = false) DistanceRange distance,
            @RequestParam(name = "dogSize", required = false) DogSize dogSize,
            @RequestParam(name = "matchingYn", required = false) String matchingYn
    ) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getBoards(date, addressId, distance, dogSize, matchingYn)));
    }

    @GetMapping("/detail/{boardId}")
    public ResponseEntity<BasicResponse<BoardDetailResponseDto>> getBoardDetails(@PathVariable("boardId") Long boardId){
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getBoardDetail(boardId)));
    }

    @PostMapping("/register")
    public ResponseEntity<BasicResponse<Long>> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(boardService.createBoard(boardRequestDto)));
    }

}
