package com.board.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.member.InsMemberDTO;

@Mapper
public interface MemberMapper {
    public void insMember(InsMemberDTO insMemberDTO);
}
