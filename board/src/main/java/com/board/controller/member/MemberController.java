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
import com.board.dto.comment.CommentSimpleDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingRequestWithSearchKeywordDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.dto.common.ReqInfoDTO;
import com.board.dto.member.IdPwDTO;
import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberDetailDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.dto.member.UpdateMemberDetailDTO;
import com.board.dto.post.PostSimpleDTO;
import com.board.enums.MemberStatusEnum;
import com.board.framework.base.BaseController;
import com.board.service.comment.CommentService;
import com.board.service.member.MemberService;
import com.board.service.post.PostService;
import com.board.util.PwEncryptor;

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

    @Operation(summary = "signup", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Integer> insMember(@RequestBody @Valid InsMemberDTO insMemberDTO) {
        insMemberDTO.setPw(PwEncryptor.encryptPw(insMemberDTO.getPw()));
        insMemberDTO.setStatus(MemberStatusEnum.NORMAL);
        return ok(memberService.insMember(insMemberDTO));
    }

    @Operation(summary = "signin", description = "로그인")
    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDTO> signin(
            @RequestBody @Valid IdPwDTO idPwDTO,
            @RequestAttribute(name = RequestAttributeKeys.REQ_INFO) ReqInfoDTO reqInfoDTO) {
        return ok(memberService.signin(idPwDTO, reqInfoDTO));
    }

    @Operation(summary = "signout", description = "로그아웃")
    @PostMapping("/signout")
    public ResponseEntity<Void> signout(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO) {
        memberService.signout(memberInfoDTO.getMemberNo());
        return ok();
    }

    @Operation(summary = "member details of oneself", description = "자기 자신의 회원정보 조회")
    @GetMapping("/self")
    public ResponseEntity<MemberDetailDTO> selectMemberDetailOfSelf(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO) {
        return ok(memberService.selectMemberDetail(memberInfoDTO.getMemberNo()));
    }

    @Operation(summary = "modify member detail", description = "자기 자신의 정보 수정")
    @PutMapping("/self")
    public ResponseEntity<Void> updateMemberDetailOfSelf(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @RequestBody @Valid UpdateMemberDetailDTO updateMemberDetailDTO) {
        updateMemberDetailDTO.setNo(memberInfoDTO.getMemberNo());
        updateMemberDetailDTO.setPw(PwEncryptor.encryptPw(updateMemberDetailDTO.getPw()));
        memberService.updateMemberDetail(updateMemberDetailDTO);
        return ok();
    }

    @Operation(summary = "withdraw", description = "회원탈퇴")
    @DeleteMapping("/self")
    public ResponseEntity<Void> withdraw(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO) {
        memberService.withdraw(memberInfoDTO.getMemberNo());
        return ok();
    }

    @Operation(summary = "내 게시물 목록 보기")
    @GetMapping("/posts/self")
    public ResponseEntity<PagingResponseDTO<PostSimpleDTO>> selectSelfPostList(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @ModelAttribute @Valid PagingRequestWithSearchKeywordDTO pagingRequestDTO) {
        return ok(postService.selectPostList(
                memberInfoDTO.getMemberNo(),
                pagingRequestDTO));
    }

    @Operation(summary = "내 댓글 목록 보기")
    @GetMapping("/comments/self")
    public ResponseEntity<PagingResponseDTO<CommentSimpleDTO>> selectSelfCommentList(
            @RequestAttribute(name = RequestAttributeKeys.MEMBER_INFO) MemberInfoDTO memberInfoDTO,
            @ModelAttribute @Valid PagingRequestDTO pagingRequestDTO) {
        return ok(commentService.selectCommentList(null, memberInfoDTO.getMemberNo(), pagingRequestDTO));
    }
}
