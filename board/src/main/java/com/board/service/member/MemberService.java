package com.board.service.member;

import org.springframework.transaction.annotation.Transactional;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.PutMemberDetailDTO;
import com.board.dto.member.SelectMemberDetailDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;

/**
 * 로그인, 로그아웃, 멤버 가입, 조회, 수정, 삭제 등을 담당하는 Service
 */
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
     * 자기 자신의 정보 가져오기
     * 
     */
    @Transactional
    public SelectMemberDetailDTO selectMemberDetailOfSelf();

    /**
     * 자기 자신의 정보 수정하기
     * 
     * @param putMemberDetailDTO
     */
    @Transactional
    public void updateMemberDetailOfSelf(PutMemberDetailDTO putMemberDetailDTO);

    /**
     * 회원 탈퇴
     * 
     */
    @Transactional
    public void withdraw();
}
