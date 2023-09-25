package com.board.service.member;

import com.board.dto.member.IdPwDTO;
import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberDetailDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.dto.member.UpdateMemberDetailDTO;

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
     * @param idPwDTO
     * @return
     */
    public SigninResponseDTO signin(IdPwDTO idPwDTO);

    /**
     * 로그아웃
     * 
     */
    public void signout(Integer memberNo);

    /**
     * 특정 멤버 정보 불러오기
     * 
     */
    public MemberDetailDTO selectMemberDetail(int memberNo);

    /**
     * 자기 자신의 정보 수정하기
     * 
     * @param memberNo
     * @param updateMemberDetailDTO
     */
    public void updateMemberDetail(int memberNo, UpdateMemberDetailDTO updateMemberDetailDTO);

    /**
     * 회원 탈퇴
     * 
     */
    public void withdraw(Integer memberNo);
}
