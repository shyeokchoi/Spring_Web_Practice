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
    public void insMember(InsMemberDTO insMemberDTO);

    public MemberDTO selectOne(SelectMemberDTO selectMemberDTO);

    public void insMemberAuth(MemberAuthDTO memberAuthDTO);

    public void expireMemberAuth(Integer memberNo);

    public MemberDetailDTO selectMemberDetail(Integer memberNo);

    public void withdraw(Integer memberNo);

    public void updateMemberDetail(UpdateMemberDetailDTO updateMemberDetailDTO);
}
