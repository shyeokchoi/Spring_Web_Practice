package com.board.service.member;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;

public interface MemberService {
    /**
     * 회원가입
     * 
     * @param insMemberDTO
     * @return 회원가입된 멤버의 no
     */
    public Integer insMember(InsMemberDTO insMemberDTO);

    /**
     * 로그인
     * 
     * @param signinRequestDTO
     * @return
     */
    public SigninResponseDTO signin(SigninRequestDTO signinRequestDTO);
}
