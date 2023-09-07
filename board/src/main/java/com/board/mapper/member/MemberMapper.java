package com.board.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.MemberDTO;

@Mapper
public interface MemberMapper {
    public void insMember(InsMemberDTO insMemberDTO);

    public MemberDTO selectById(String id);

    public MemberDTO selectByEmail(String email);
}
