package com.board.controller.post;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.constant.RequestAttributeKeys;
import com.board.dto.auth.MemberInfoDTO;
import com.board.dto.comment.CommentSimpleDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingRequestWithSearchKeywordDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.PostDetailDTO;
import com.board.dto.post.PostSimpleDTO;
import com.board.dto.post.UpdatePostDTO;
import com.board.enums.PostStatusEnum;
import com.board.framework.base.BaseController;
import com.board.service.comment.CommentService;
import com.board.service.post.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "posts", description = "게시물 관련 API")
@RestController
@Validated
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController extends BaseController {
    private final PostService postService;
    private final CommentService commentService;

    /**
     * 게시물 작성
     * 
     * @param memberInfoDTO Interceptor가 제공해주는 현재 로그인 멤버 관련 정보
     * @param insPostDTO
     * @return 작성된 게시물의 no
     */
    @Operation(summary = "게시물 작성")
    @PostMapping()
    public ResponseEntity<Integer> insPost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid InsPostDTO insPostDTO) {

        // author no 설정
        insPostDTO.setAuthorNo(memberInfoDTO.getMemberNo());

        // status 설정 - 새롭게 등록되는 글이니 POSTED
        insPostDTO.setStatus(PostStatusEnum.POSTED);

        return ok(postService.insPost(insPostDTO));
    }

    /**
     * 게시글 상세정보
     * 
     * @param postNo Path Variable로 주어진 post no.
     * @return 게시물 상세정보
     */
    @Operation(summary = "게시글 상세정보")
    @GetMapping("/{postNo}")
    public ResponseEntity<PostDetailDTO> selectPost(
            @PathVariable @Min(1) int postNo) {
        return ok(postService.selectPost(postNo));
    }

    /**
     * 게시글 수정
     * 
     * @param memberInfoDTO Interceptor가 제공해주는 현재 로그인 멤버 관련 정보
     * @param updatePostDTO
     * @param postNo        Path Variable로 주어진 post no.
     * @return 수정된 게시물 상세정보
     */
    @Operation(summary = "게시글 수정")
    @PutMapping("/{postNo}")
    public ResponseEntity<Integer> updatePost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid UpdatePostDTO updatePostDTO,
            @PathVariable @Min(1) int postNo) {

        // 수정할 post no 설정
        updatePostDTO.setPostNo(postNo);
        // modifier no 설정
        updatePostDTO.setModifierNo(memberInfoDTO.getMemberNo());
        // 게시글 상태 수정
        updatePostDTO.setPostStatus(PostStatusEnum.POSTED);

        return ok(postService.updatePost(updatePostDTO));
    }

    /**
     * 게시글 삭제
     * 
     * @param memberInfoDTO Interceptor가 제공해주는 현재 로그인 멤버 관련 정보
     * @param postNo        Path Variable로 주어진 post no.
     */
    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postNo}")
    public ResponseEntity<Void> deletePost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @PathVariable @Min(1) int postNo) {

        postService.deletePost(memberInfoDTO.getMemberNo(), postNo);
        return ok();
    }

    /**
     * 게시글 리스트 불러오기
     * 
     * @param pagingDTO
     * @return 게시물들의 리스트
     */
    @Operation(summary = "게시글 리스트 불러오기")
    @GetMapping()
    public ResponseEntity<PagingResponseDTO<PostSimpleDTO>> selectPostList(
            @ModelAttribute @Valid PagingRequestWithSearchKeywordDTO pagingRequestDTO) {

        return ok(postService.selectPostList(pagingRequestDTO));
    }

    /**
     * 게시물을 임시저장합니다.
     * 이미 임시저장된 게시물이 있으면 덮어씁니다.
     * 
     * @param insPostDTO
     * @return 임시저장된 게시물의 no.
     */
    @Operation(summary = "새로운 임시저장", description = "기존 임시저장 게시글은 덮어씁니다.")
    @PostMapping("/temp")
    public ResponseEntity<Integer> insTempPost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid InsPostDTO insPostDTO) {
        // author no 설정
        insPostDTO.setAuthorNo(memberInfoDTO.getMemberNo());

        // status 설정 - 임시저장이니 TEMP
        insPostDTO.setStatus(PostStatusEnum.TEMP);

        return ok(postService.insTempPost(insPostDTO));
    }

    /**
     * 자신의 임시저장글 번호 알아오기.
     * 
     * @param memberInfoDTO
     * @return 임시저장된 글 번호.
     */
    @Operation(summary = "임시저장 불러오기")
    @GetMapping("/temp")
    public ResponseEntity<Integer> selectTempPost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO) {
        return ok(postService.selectTempPostNo(memberInfoDTO.getMemberNo()));
    }

    /**
     * 임시저장글 수정.
     * 자신의 글만 수정 가능
     * 
     * @param memberInfoDTO
     * @param postNo
     * @return
     */
    @Operation(summary = "임시저장글 수정")
    @PutMapping("/temp/{postNo}")
    public ResponseEntity<Integer> updateTempPost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid UpdatePostDTO updatePostDTO,
            @PathVariable @Min(1) int postNo) {

        // 수정할 post no 설정
        updatePostDTO.setPostNo(postNo);
        // modifier no 설정
        updatePostDTO.setModifierNo(memberInfoDTO.getMemberNo());
        // 게시글 상태 설정
        updatePostDTO.setPostStatus(PostStatusEnum.TEMP);

        return ok(postService.updateTempPost(memberInfoDTO.getMemberNo(), updatePostDTO));
    }

    /**
     * 임시저장글 최종 게시글로 등록
     * 자신의 글만 최종 게시글로 확정 가능
     * 
     * @param memberInfoDTO
     * @param postNo
     * @return
     */
    @Operation(summary = "임시저장글 최종 게시글로 등록")
    @PostMapping("/temp/{postNo}")
    public ResponseEntity<Integer> finalizeTempPost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @PathVariable @Min(1) int postNo) {
        return ok(postService.finalizeTempPost(memberInfoDTO.getMemberNo(), postNo));
    }

    /**
     * 댓글 리스트 조회
     * 
     * @param memberInfoDTO
     * @param currPage
     * @param pageSize
     * @param postNo
     * @return
     */
    @Operation(summary = "댓글 리스트 조회")
    @GetMapping("/{postNo}/comments")
    public ResponseEntity<PagingResponseDTO<CommentSimpleDTO>> selectCommentList(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @ModelAttribute @Valid PagingRequestDTO pagingRequestDTO,
            @PathVariable @Min(1) int postNo) {

        return ok(commentService.selectCommentList(postNo, null, pagingRequestDTO));
    }
}
