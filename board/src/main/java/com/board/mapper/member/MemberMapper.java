package com.board.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberAuthDTO;
import com.board.dto.member.MemberDTO;
import com.board.dto.member.MemberDetailDTO;
import com.board.dto.member.SelectMemberDTO;
import com.board.dto.member.UpdateMemberDetailDTO;

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
     * 멤버 인증 정보 DB 저장
     * 
     * @param accessToken
     */
    public void insMemberAuth(MemberAuthDTO memberAuthDTO);

    /**
     * 멤버 로그아웃
     * 
     */
    public void expireMemberAuth(Integer memberNo);

    /**
     * 자기 자신의 정보
     */
    public MemberDetailDTO selectMemberDetail(Integer memberNo);

    /**
     * 회원 탈퇴
     */
    public void withdraw(Integer memberNo);

    /**
     * 회원정보 수정
     * 
     * @param updateMemberDetailDTO
     */
    public void updateMemberDetail(UpdateMemberDetailDTO updateMemberDetailDTO);
}
