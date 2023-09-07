package com.board.controller.member;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.board.dto.member.InsMemberDTO;
import com.board.framework.base.BaseController;
import com.board.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController extends BaseController {
    private final MemberService memberService;

    /**
     * 회원가입
     * 
     * @param insMemberDTO 회원가입에 필요한 정보
     * @return 회원 no
     */
    @PostMapping("/signup")
    public ResponseEntity<String> insMember(@RequestBody @Valid InsMemberDTO insMemberDTO) {

        return ok(memberService.insMember(insMemberDTO).toString());
    }

}
