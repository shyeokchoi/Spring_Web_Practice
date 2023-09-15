package com.board.service.member;

import com.board.dto.auth.MemberInfoDTO;
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
    public Integer insMember(InsMemberDTO insMemberDTO);

    /**
     * 로그인
     * 
     * @param signinRequestDTO
     * @return
     */
    public SigninResponseDTO signin(SigninRequestDTO signinRequestDTO);

    /**
     * 로그아웃
     * 
     */
    public void signout(MemberInfoDTO memberInfoDTO);

    /**
     * 자기 자신의 정보 가져오기
     * 
     */
    public SelectMemberDetailDTO selectMemberDetailOfSelf(MemberInfoDTO memberInfoDTO);

    /**
     * 자기 자신의 정보 수정하기
     * 
     * @param memberInfoDTO
     * @param putMemberDetailDTO
     */
    public void updateMemberDetailOfSelf(MemberInfoDTO memberInfoDTO, PutMemberDetailDTO putMemberDetailDTO);

    /**
     * 회원 탈퇴
     * 
     */
    public void withdraw(MemberInfoDTO memberInfoDTO);
}
