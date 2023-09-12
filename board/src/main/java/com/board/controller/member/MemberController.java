package com.board.controller.member;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.SelectMemberDetailDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.framework.base.BaseController;
import com.board.service.member.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "members", description = "회원 관리에 관한 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController extends BaseController {
    private final MemberService memberService;

    /**
     * 회원가입
     * 
     * @param insMemberDTO 회원가입에 필요한 정보
     * @return 회원 no가 body에 들어간 ResponseEntity
     */
    @Operation(summary = "signup", description = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "409", description = "CONFLICT", content = @Content(schema = @Schema())),
    })
    @PostMapping("/signup")
    public ResponseEntity<Integer> insMember(@RequestBody @Valid InsMemberDTO insMemberDTO) {

        return ok(memberService.insMember(insMemberDTO));
    }

    /**
     * 로그인
     * 
     * @param signinRequestDTO
     * @return accessToken이 body로 들어간 ResponseEntity
     */
    @Operation(summary = "signin", description = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED", content = @Content(schema = @Schema()))
    })
    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDTO> signin(@RequestBody @Valid SigninRequestDTO signinRequestDTO) {
        return ok(memberService.signin(signinRequestDTO));
    }

    /**
     * 로그아웃
     */
    @Operation(summary = "signout", description = "로그아웃")
    @PostMapping("/signout")
    public ResponseEntity<Void> signout() {
        memberService.signout();
        return ok();
    }

    /**
     * 자기 자신의 정보 조회
     * 
     * @return 회원정보
     */
    @Operation(summary = "member details of oneself", description = "자기 자신의 회원정보 조회")
    @GetMapping("/self")
    public ResponseEntity<SelectMemberDetailDTO> memberDetailOfSelf() {
        return ok(memberService.selectMemberDetailOfSelf());
    }

    /**
     * 회원탈퇴
     */
    @Operation(summary = "withdraw", description = "회원탈퇴")
    @DeleteMapping("/self")
    public ResponseEntity<Void> withdraw() {
        memberService.withdraw();
        return ok();
    }
}
