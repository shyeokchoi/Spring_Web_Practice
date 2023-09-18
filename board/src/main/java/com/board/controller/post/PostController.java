package com.board.controller.post;

import java.util.List;

import javax.validation.Valid;

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
import com.board.dto.common.PagingDTO;
import com.board.dto.common.SearchDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.dto.post.UpdatePostDTO;
import com.board.enums.PostStatusEnum;
import com.board.framework.base.BaseController;
import com.board.service.post.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "posts", description = "게시물 관련 API")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController extends BaseController {
    private final PostService postService;

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
    public ResponseEntity<SelectPostDetailDTO> selectPost(
            @PathVariable Integer postNo) {
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
    public ResponseEntity<Void> updatePost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid UpdatePostDTO updatePostDTO,
            @PathVariable Integer postNo) {

        // 수정할 post no 설정
        updatePostDTO.setPostNo(postNo);
        // modifier no 설정
        updatePostDTO.setModifierNo(memberInfoDTO.getMemberNo());

        postService.updatePost(updatePostDTO)

        return ok();
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
            @PathVariable Integer postNo) {

        postService.deletePost(memberInfoDTO.getMemberNo(), postNo);
        return ok();
    }

    /**
     * 게시글 리스트 불러오기
     * 
     * @param limitStr  query parameter limit
     * @param offsetStr query parameter offset
     * @return 게시물들의 리스트
     */
    @Operation(summary = "게시글 리스트 불러오기")
    @GetMapping()
    public ResponseEntity<List<SelectPostListDTO>> selectPostList(
            @RequestParam(name = "limit", defaultValue = "3", required = false) String limitStr,
            @RequestParam(name = "offset", defaultValue = "0", required = false) String offsetStr,
            @RequestParam(name = "title", required = false) String titleKeyword,
            @RequestParam(name = "authorName", required = false) String authorNameKeyword) {

        return ok(postService.selectPostList(
                null,
                new PagingDTO(Integer.parseInt(limitStr), Integer.parseInt(offsetStr)),
                new SearchDTO(titleKeyword, authorNameKeyword)));
    }

    /**
     * 게시물을 임시저장합니다.
     * 이미 임시저장된 게시물이 있으면 덮어씁니다.
     * 
     * @param insPostDTO
     * @return 임시저장된 게시물의 no.
     */
    @Operation(summary = "임시저장하기")
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
     * 임시저장 불러오기.
     * 불러온 임시저장 글은 삭제됩니다.
     * 
     * @param memberInfoDTO
     * @return 임시저장된 글 정보.
     */
    @Operation(summary = "임시저장 불러오기")
    @GetMapping("/temp")
    public ResponseEntity<SelectPostDetailDTO> selectTempPost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO) {
        return ok(postService.selectTempPost(memberInfoDTO.getMemberNo()));
    }

    /**
     * 내 게시물 목록을 불러옵니다.
     * 
     * @param memberInfoDTO
     * @return
     */
    @Operation(summary = "내 게시물 목록 보기")
    @GetMapping("/self")
    public ResponseEntity<List<SelectPostListDTO>> selectSelfPostList(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestParam(name = "limit", defaultValue = "3", required = false) String limitStr,
            @RequestParam(name = "offset", defaultValue = "0", required = false) String offsetStr) {

        return ok(postService.selectPostList(
                memberInfoDTO.getMemberNo(),
                new PagingDTO(Integer.parseInt(limitStr), Integer.parseInt(offsetStr)),
                null));
    }
}
