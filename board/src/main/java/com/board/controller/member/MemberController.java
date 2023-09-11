package com.board.controller.member;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.framework.base.BaseController;
import com.board.service.member.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
                        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                        @ApiResponse(responseCode = "409", description = "CONFLICT")
        })
        // @Parameters({
        // @Parameter(name = "name", description = "이름", example = "홍길동"),
        // @Parameter(name = "id", description = "아이디", example = "hongildong"),
        // @Parameter(name = "pw", description = "비밀번호", example = "password123!"),
        // @Parameter(name = "email", description = "이메일", example =
        // "hongildong@gmail.com"),
        // @Parameter(name = "phone", description = "휴대폰 번호", example = "010-0000-0000")
        // })
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
                        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                        @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
        })
        @Parameters({
                        @Parameter(name = "id", description = "아이디", example = "hongildong"),
                        @Parameter(name = "pw", description = "비밀번호", example = "password123!"),
        })
        @PostMapping("/signin")
        public ResponseEntity<SigninResponseDTO> signin(@RequestBody @Valid SigninRequestDTO signinRequestDTO) {
                return ok(memberService.signin(signinRequestDTO));
        }

}
