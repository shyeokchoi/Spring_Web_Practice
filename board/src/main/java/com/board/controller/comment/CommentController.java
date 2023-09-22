package com.board.controller.comment;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.constant.RequestAttributeKeys;
import com.board.dto.auth.MemberInfoDTO;
import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.framework.base.BaseController;
import com.board.service.comment.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "comments", description = "댓글 관련 API")
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController extends BaseController {
    private final CommentService commentService;

    /**
     * 댓글 등록
     * 
     * @param memberInfoDTO
     * @param insCommentDTO
     * @param postNo        path에 포함된 post no.
     * @return 새롭게 등록된 댓글의 no.
     */
    @Operation(summary = "댓글 등록")
    @PostMapping("")
    public ResponseEntity<Integer> insComment(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid InsCommentDTO insCommentDTO) {

        // 댓글 작성자 author no 설정
        insCommentDTO.setAuthorNo(memberInfoDTO.getMemberNo());

        // 파일 저장 후 DB 업데이트
        return ok(commentService.insComment(insCommentDTO));
    }

    /**
     * 댓글 수정
     * 
     * @param memberInfoDTO
     * @param updateCommentDTO
     * @param postNo
     * @param commentNo
     * @return
     */
    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentNo}")
    public ResponseEntity<Integer> updateComment(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid UpdateCommentDTO updateCommentDTO,
            @PathVariable @Min(1) int commentNo) {
        updateCommentDTO.setCommentNo(commentNo);
        updateCommentDTO.setModifierNo(memberInfoDTO.getMemberNo());

        return ok(commentService.updateComment(updateCommentDTO));
    }

    /**
     * 댓글 삭제
     * 
     * @param memberInfoDTO
     * @param postNo
     * @param commentNo
     * @return
     */
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<Void> deleteComment(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @PathVariable @Min(1) int commentNo) {

        commentService.deleteComment(memberInfoDTO.getMemberNo(), commentNo);
        return ok();
    }

}
