package com.board.controller.comment;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.board.constant.RequestAttributeKeys;
import com.board.dto.auth.MemberInfoDTO;
import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.SelectCommentListDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.framework.base.BaseController;
import com.board.service.comment.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "comments", description = "댓글 관련 API")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class CommentController extends BaseController {
    private final CommentService commentService;

    @Operation(summary = "댓글 등록")
    @PostMapping("/posts/{postNo}/comments")
    public ResponseEntity<Integer> insComment(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid InsCommentDTO insCommentDTO,
            @PathVariable Integer postNo) throws Exception {

        // 댓글 작성자 author no 설정
        insCommentDTO.setAuthorNo(memberInfoDTO.getMemberNo());

        // 원글 번호 설정
        insCommentDTO.setPostNo(postNo);

        // 파일 저장 후 DB 업데이트
        return ok(commentService.insComment(insCommentDTO));
    }

    @Operation(summary = "댓글 리스트 조회")
    @GetMapping("/posts/{postNo}/comments")
    public ResponseEntity<PagingResponseDTO<SelectCommentListDTO>> selectCommentList(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestParam(name = "currPage", required = true) @Min(1) long currPage,
            @RequestParam(name = "pageSize", required = true) @Min(3) @Max(200) long pageSize,
            @PathVariable Integer postNo) throws Exception {

        PagingRequestDTO pagingRequestDTO = new PagingRequestDTO(currPage, pageSize, null);

        return ok(commentService.selectCommentList(postNo, null, pagingRequestDTO));
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/posts/{postNo}/comments/{commentNo}")
    public ResponseEntity<Integer> updateComment(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid UpdateCommentDTO updateCommentDTO,
            @PathVariable Integer postNo,
            @PathVariable Integer commentNo) {
        updateCommentDTO.setCommentNo(commentNo);
        updateCommentDTO.setPostNo(postNo);
        updateCommentDTO.setModifierNo(memberInfoDTO.getMemberNo());

        return ok(commentService.updatePost(updateCommentDTO));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/posts/{postNo}/comments/{commentNo}")
    public ResponseEntity<Void> deleteComment(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @PathVariable Integer postNo,
            @PathVariable Integer commentNo) {

        commentService.deleteComment(memberInfoDTO.getMemberNo(), commentNo);
        return ok();
    }

    @Operation(summary = "내 댓글 목록 보기")
    @GetMapping("comments/self")
    public ResponseEntity<PagingResponseDTO<SelectCommentListDTO>> selectSelfCommentList(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestParam(name = "currPage", required = true) @Min(1) long currPage,
            @RequestParam(name = "pageSize", required = true) @Min(3) @Max(200) long pageSize) {

        PagingRequestDTO pagingRequestDTO = new PagingRequestDTO(currPage, pageSize, null);

        return ok(commentService.selectCommentList(null, memberInfoDTO.getMemberNo(), pagingRequestDTO));
    }
}
