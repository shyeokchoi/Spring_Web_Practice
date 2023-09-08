package com.board.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberAuthDTO;
import com.board.dto.member.MemberDTO;
import com.board.dto.member.SelectMemberDTO;
import com.board.dto.member.SigninRequestDTO;

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
    public Integer signin(SigninRequestDTO signinRequestDTO);

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
}
