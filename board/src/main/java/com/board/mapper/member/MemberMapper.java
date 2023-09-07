package com.board.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberDTO;
import com.board.dto.member.SelectMemberDTO;

@Mapper
public interface MemberMapper {
    public void insMember(InsMemberDTO insMemberDTO);

    public MemberDTO selectOne(SelectMemberDTO selectMemberDTO);
}
