package com.board.controller.member;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.constant.RequestAttributeKeys;
import com.board.dto.auth.MemberInfoDTO;
import com.board.dto.comment.SelectCommentListDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingRequestWithSearchKeywordDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.dto.member.IdPwDTO;
import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.SelectMemberDetailDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.dto.member.UpdateMemberDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.framework.base.BaseController;
import com.board.service.comment.CommentService;
import com.board.service.member.MemberService;
import com.board.service.post.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "members", description = "회원 관리에 관한 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController extends BaseController {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    /**
     * 회원가입
     * 
     * @param insMemberDTO 회원가입에 필요한 정보
     * @return 회원 no가 body에 들어간 ResponseEntity
     */
    @Operation(summary = "signup", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Integer> insMember(@RequestBody @Valid InsMemberDTO insMemberDTO) {

        return ok(memberService.insMember(insMemberDTO));
    }

    /**
     * 로그인
     * 
     * @param idPwDTO
     * @return accessToken이 body로 들어간 ResponseEntity
     */
    @Operation(summary = "signin", description = "로그인")
    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDTO> signin(@RequestBody @Valid IdPwDTO idPwDTO) {
        return ok(memberService.signin(idPwDTO));
    }

    /**
     * 로그아웃
     */
    @Operation(summary = "signout", description = "로그아웃")
    @PostMapping("/signout")
    public ResponseEntity<Void> signout(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO) {
        memberService.signout(memberInfoDTO.getMemberNo());
        return ok();
    }

    /**
     * 자기 자신의 정보 조회
     * 
     * @return 회원정보
     */
    @Operation(summary = "member details of oneself", description = "자기 자신의 회원정보 조회")
    @GetMapping("/self")
    public ResponseEntity<SelectMemberDetailDTO> memberDetailOfSelf(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO) {
        return ok(memberService.selectMemberDetailOfSelf(memberInfoDTO.getMemberNo()));
    }

    /**
     * 자기 자신의 정보 수정
     * 
     */
    @Operation(summary = "modify member detail", description = "자기 자신의 정보 수정")
    @PutMapping("/self")
    public ResponseEntity<Void> putMemberDetailOfSelf(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid UpdateMemberDetailDTO updateMemberDetailDTO) {
        memberService.updateMemberDetailOfSelf(memberInfoDTO.getMemberNo(), updateMemberDetailDTO);

        return ok();
    }

    /**
     * 회원탈퇴
     */
    @Operation(summary = "withdraw", description = "회원탈퇴")
    @DeleteMapping("/self")
    public ResponseEntity<Void> withdraw(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO) {
        memberService.withdraw(memberInfoDTO.getMemberNo());
        return ok();
    }

    /**
     * 내 게시물 목록을 불러옵니다.
     * 
     * @param memberInfoDTO
     * @param PagingRequestDTO
     * @return
     */
    @Operation(summary = "내 게시물 목록 보기")
    @GetMapping("/posts/self")
    public ResponseEntity<PagingResponseDTO<SelectPostListDTO>> selectSelfPostList(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @ModelAttribute @Valid PagingRequestWithSearchKeywordDTO pagingRequestDTO) {

        return ok(postService.selectPostList(
                memberInfoDTO.getMemberNo(),
                pagingRequestDTO));
    }

    /**
     * 내가 작성한 댓글 목록 보기
     * 
     * @param memberInfoDTO
     * @param currPage
     * @param pageSize
     * @return
     */
    @Operation(summary = "내 댓글 목록 보기")
    @GetMapping("/comments/self")
    public ResponseEntity<PagingResponseDTO<SelectCommentListDTO>> selectSelfCommentList(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @ModelAttribute @Valid PagingRequestDTO pagingRequestDTO) {

        return ok(commentService.selectCommentList(null, memberInfoDTO.getMemberNo(), pagingRequestDTO));
    }
}
