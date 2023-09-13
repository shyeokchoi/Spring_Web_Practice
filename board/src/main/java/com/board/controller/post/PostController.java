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
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.PutPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.enums.PostStatusEnum;
import com.board.framework.base.BaseController;
import com.board.service.post.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Parameter(in = ParameterIn.HEADER, required = true, description = "Access Token", name = "Access-Token", content = @Content(schema = @Schema(type = "string", defaultValue = "")))
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
    @Parameter(in = ParameterIn.HEADER, required = true, description = "Access Token", name = "Access-Token", content = @Content(schema = @Schema(type = "string", defaultValue = "")))
    @GetMapping("/{postNo}")
    public ResponseEntity<SelectPostDetailDTO> selectPost(
            @PathVariable Integer postNo) {
        return ok(postService.selectPost(postNo));
    }

    /**
     * 게시글 수정
     * 
     * @param memberInfoDTO Interceptor가 제공해주는 현재 로그인 멤버 관련 정보
     * @param putPostDTO
     * @param postNo        Path Variable로 주어진 post no.
     * @return 수정된 게시물 상세정보
     */
    @Operation(summary = "게시글 수정")
    @Parameter(in = ParameterIn.HEADER, required = true, description = "Access Token", name = "Access-Token", content = @Content(schema = @Schema(type = "string", defaultValue = "")))
    @PutMapping("/{postNo}")
    public ResponseEntity<SelectPostDetailDTO> updatePost(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody PutPostDTO putPostDTO,
            @PathVariable Integer postNo) {

        // 수정할 post no 설정
        putPostDTO.setPostNo(postNo);
        // modifier no 설정
        putPostDTO.setModifierNo(memberInfoDTO.getMemberNo());

        postService.updatePost(putPostDTO);
        return ok(postService.selectPost(postNo));
    }

    /**
     * 게시글 삭제
     * 
     * @param memberInfoDTO Interceptor가 제공해주는 현재 로그인 멤버 관련 정보
     * @param postNo        Path Variable로 주어진 post no.
     */
    @Operation(summary = "게시글 삭제")
    @Parameter(in = ParameterIn.HEADER, required = true, description = "Access Token", name = "Access-Token", content = @Content(schema = @Schema(type = "string", defaultValue = "")))
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
    @Parameter(in = ParameterIn.HEADER, required = true, description = "Access Token", name = "Access-Token", content = @Content(schema = @Schema(type = "string", defaultValue = "")))
    @GetMapping()
    public ResponseEntity<List<SelectPostListDTO>> selectPostList(
            @RequestParam(name = "limit", defaultValue = "3", required = false) String limitStr,
            @RequestParam(name = "offset", defaultValue = "0", required = false) String offsetStr) {
        return ok(postService.selectPostList(new PagingDTO(Integer.parseInt(limitStr), Integer.parseInt(offsetStr))));
    }

}
