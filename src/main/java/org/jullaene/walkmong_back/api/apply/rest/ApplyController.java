package org.jullaene.walkmong_back.api.apply.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.domain.enums.MatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.dto.res.ApplyInfoDto;
import org.jullaene.walkmong_back.api.apply.dto.res.RecordResponseDto;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.jullaene.walkmong_back.api.board.service.BoardService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/walking/apply")
public class ApplyController {
    private final ApplyService applyService;
    private final BoardService boardService;

    @PostMapping("/{boardId}")
    public ResponseEntity<BasicResponse<Long>> saveApply(
            @PathVariable("boardId") Long boardId,
            @RequestBody ApplyRequestDto applyRequestDto){
        return ResponseEntity.ok(BasicResponse.ofCreateSuccess(applyService.saveApply(boardId, applyRequestDto)));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BasicResponse<ApplyInfoDto>> getApplyInfo(
            @PathVariable("boardId") Long boardId
    ){
        ApplyInfoDto applyInfoDto=applyService.getApplyInfo(boardId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(applyInfoDto));
    }

    /*
    * 내가 지원,의뢰한 산책 리스트 보기
    */
    @GetMapping("/history")
    public List<RecordResponseDto> getApplyLists(@RequestParam("record") String record,
                                                      @RequestParam("status")MatchingStatus status){
        /*
        record
        applied: 지원한 산책
        requested: 의뢰한 산책
        all: 전체
        */

         /*
        <status>
        PENDING: 매칭중
        CONFIRMED: 매칭확정
        REJECTED: 매칭취소
         */
        List<RecordResponseDto> recordResponseDto=null;
        if (record.equals("applied")) { //내가 지원한 산책
            recordResponseDto = applyService.getAllAppliedInfoWithStatus(status)
                    .stream()
                    .map(dto -> (RecordResponseDto) dto)
                    .toList();
        }
        else if (record.equals("requested")){ //내가 의뢰한 산책
            recordResponseDto = boardService.getAllRequestedInfoWithStatus(status)
                    .stream()
                    .map(dto -> (RecordResponseDto) dto)
                    .toList();
        }
        return recordResponseDto;

    }
}
