package com.board.service.member;

import org.springframework.transaction.annotation.Transactional;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.dto.member.SignoutRequestDTO;
import com.board.dto.member.WithdrawRequestDTO;

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
     * @param signoutRequestDTO
     */
    @Transactional
    public void signout(SignoutRequestDTO signoutRequestDTO);

    /**
     * 회원 탈퇴
     * 
     * @param withdrawRequestDTO
     */
    public void withdraw(WithdrawRequestDTO withdrawRequestDTO);
}
