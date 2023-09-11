package com.board.service.member;

import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Integer insMember(InsMemberDTO insMemberDTO);

    /**
     * 로그인
     * 
     * @param signinRequestDTO
     * @return
     */
    @Transactional
    public SigninResponseDTO signin(SigninRequestDTO signinRequestDTO);

    /**
     * 로그아웃
     * 
     */
    @Transactional
    public void signout();

    /**
     * 회원 탈퇴
     * 
     */
    @Transactional
    public void withdraw();
}
