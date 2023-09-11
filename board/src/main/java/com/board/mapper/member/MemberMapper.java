package com.board.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.member.IdPwDTO;
import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberAuthDTO;
import com.board.dto.member.MemberDTO;
import com.board.dto.member.MemberHistoryDTO;
import com.board.dto.member.SelectMemberDTO;
import com.board.dto.member.SignoutRequestDTO;

@Mapper
public interface MemberMapper {
    /**
     * 회원 가입
     * 
     * @param insMemberDTO
     */
    public void insMember(InsMemberDTO insMemberDTO);

    /**
     * 회원 한 명 검색하기
     * 
     * @param selectMemberDTO 회원을 검색할 수 있는 인덱스. no, email, id 등
     * @return 검색된 회원
     */
    public MemberDTO selectOne(SelectMemberDTO selectMemberDTO);

    /**
     * 로그인
     * 
     * @param signinRequestDTO
     * @return 로그인 성공여부
     */
    public Integer signin(IdPwDTO IdPwDTO);

    /**
     * 멤버 인증 정보 DB 저장
     * 
     * @param accessToken
     */
    public void insMemberAuth(MemberAuthDTO memberAuthDTO);

    /**
     * 이미 로그인되어 있는지 확인하기
     * 
     * @param memberNo
     * @return 로그인 여부
     */
    public Boolean isAlreadySignedIn(Integer memberNo);

    /**
     * 멤버 로그인/로그아웃 히스토리 저장
     * 
     * @param memberHistoryDTO
     */
    public void insMemberHistory(MemberHistoryDTO memberHistoryDTO);

    /**
     * 멤버 로그아웃
     * 
     */
    public void expireMemberAuth(SignoutRequestDTO signoutRequestDTO);

    /**
     * 멤버 로그아웃 시 해당 Access Token에 해당하는 member no 반환
     * 
     * @return 해당 멤버의 no.
     */
    public Integer selectMemberNoByAccessToken(String accessToken);

    /**
     * 회원 탈퇴
     */
    public void withdraw(Integer memberNo);
}
