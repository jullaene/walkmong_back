package org.jullaene.walkmong_back.api.board.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jullaene.walkmong_back.api.board.domain.enums.WalkingStatus;
import org.jullaene.walkmong_back.api.board.dto.req.BoardRequestDto;
import org.jullaene.walkmong_back.api.board.dto.req.GeoReq;
import org.jullaene.walkmong_back.api.board.dto.req.MeetAddressReq;
import org.jullaene.walkmong_back.api.board.dto.res.BoardDetailResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.BoardResponseDto;
import org.jullaene.walkmong_back.api.board.dto.res.GeoRes;
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

    /**
     * 만남 장소 변경
     * */
    @PatchMapping("/meet/address/{boardId}")
    public ResponseEntity<BasicResponse<String>> changeMeetAddress (@PathVariable("boardId") Long boardId, @RequestBody MeetAddressReq meetAddressReq) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.changeMeetAddress(boardId, meetAddressReq)));
    }

    /**
     * 현재 위치 저장
     * */
    @PostMapping("/geo/{boardId}")
    public ResponseEntity<BasicResponse<String>> updateCurrentGeo (@PathVariable("boardId") Long boardId, @RequestBody GeoReq geoReq) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.saveOrUpdateGeoPost(boardId, geoReq)));
    }

    /**
     * 현재 위치 반환
     * */
    @GetMapping("/geo/{boardId}")
    public ResponseEntity<BasicResponse<GeoRes>> getCurrentGeo (@PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.getGeoPost(boardId)));
    }

    /**
     * 산책 완료
     * */
    @PatchMapping("/walk/status/{boardId}")
    public ResponseEntity<BasicResponse<String>> completeWalking (
            @PathVariable("boardId") Long boardId,
            @RequestParam("status") WalkingStatus status
    ) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(boardService.changeWalkingStatus(boardId, status)));
    }

}
