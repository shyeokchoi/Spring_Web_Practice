package com.board.mapper.auth;

import com.board.dto.member.IdPwDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    public Integer selectMemberNoByAccessToken(String accessToken);

    public Integer selectMemberNoByIdPW(IdPwDTO idPwDTO);
}
