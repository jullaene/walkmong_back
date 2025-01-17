package org.jullaene.walkmong_back.api.apply.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jullaene.walkmong_back.api.apply.dto.enums.WalkMatchingStatus;
import org.jullaene.walkmong_back.api.apply.dto.req.ApplyRequestDto;
import org.jullaene.walkmong_back.api.apply.dto.res.*;
import org.jullaene.walkmong_back.api.apply.service.ApplyService;
import org.jullaene.walkmong_back.api.board.dto.res.BoardPreviewResponseDto;
import org.jullaene.walkmong_back.api.board.service.BoardService;
import org.jullaene.walkmong_back.common.BasicResponse;
import org.jullaene.walkmong_back.common.enums.TabStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/walking/apply")
public class ApplyController {
    private final ApplyService applyService;
    private final BoardService boardService;
    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

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

    /**
    * 내가 지원,의뢰한 산책 리스트 보기
    */
    @GetMapping("/history")
    public ResponseEntity<BasicResponse<List<MatchingResponseDto>>> getApplyLists(@RequestParam("tabStatus") TabStatus tabStatus,
                                                                                  @RequestParam("walkMatchingStatus") WalkMatchingStatus status){
        return ResponseEntity.ok(BasicResponse.ofSuccess(applyService.getAllAppliedInfoWithStatus(tabStatus, status)));
    }

    /**
     * 반려인이 산책 지원자 리스트를 조회한다
     */
    @GetMapping("/applicant/{boardId}")
    public ResponseEntity<BasicResponse<ApplicantWithBoardResponseDto>> getApplicantList( @PathVariable("boardId") Long boardId){
        List<ApplicantInfoResponseDto> applicants=applyService.getApplicantList(boardId);
        BoardPreviewResponseDto preview=boardService.getPreview(boardId);

        return ResponseEntity.ok(BasicResponse.ofSuccess(new ApplicantWithBoardResponseDto(applicants,preview)));
    }
    /**
     * 반려인이 산책 지원자의 지원서를 조회한다
     */
    @GetMapping("/form/{boardId}")
    public ResponseEntity<BasicResponse<ApplicationFormResponseDto>> getApplicantList(@PathVariable("boardId") Long boardId,
                                                                                      @RequestParam("applyId") Long applyId){
        ApplicationFormResponseDto responseDto = applyService.getApplicationFormInfo(boardId,applyId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(responseDto));
    }

    /**
     * 매칭 확정 시 다른 지원자의 요청을 취소한다
     */
    @PostMapping("/form/{boardId}")
    public ResponseEntity<BasicResponse<String>> confirmMatching(@PathVariable("boardId") Long boardId,
                                @RequestParam("applyId") Long applyId){
        applyService.confirmMatching(boardId,applyId);

        return ResponseEntity.ok(BasicResponse.ofSuccess("다른 지원자들의 매칭이 취소되었습니다"));
    }

    /**
     * 지원자가 자신의 지원서를 조회한다
     */
    @GetMapping("/myForm/{applyId}")
    public ResponseEntity<BasicResponse<MyFormResponseDto>> getMyForm(@PathVariable("applyId") Long applyId){
        MyFormResponseDto myFormDto=applyService.getMyForm(applyId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(myFormDto));
    }

    /**
     * 지원자가 자신의 지원을 취소한다
     */
    @DeleteMapping("/cancel/{applyId}")
    public ResponseEntity<BasicResponse<String>> cancelApply(@PathVariable("applyId") Long applyId){
        applyService.cancelApply(applyId);
        return ResponseEntity.ok(BasicResponse.ofSuccess("지원이 취소되었습니다"));
    }

    /**
     * 매칭 확정 후 매칭 취소
     */
    @DeleteMapping("/cancel/matching/{applyId}")
    public ResponseEntity<BasicResponse<String>> cancelMatching(@PathVariable("applyId") Long applyId){
        applyService.cancelMatching(applyId);
        return ResponseEntity.ok(BasicResponse.ofSuccess("매칭이 취소되었습니다"));

    }

    /**
     * 매칭 확정 후 산책 정보 조회
     * */
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<BasicResponse<WalkingDtlRes>> getWalkingDtlRes (
            @PathVariable("boardId") Long boardId
    ) {
        return ResponseEntity.ok(BasicResponse.ofSuccess(applyService.getWalkingDtlRes(boardId)));
    }

}
