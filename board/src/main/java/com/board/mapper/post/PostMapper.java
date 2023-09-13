package com.board.mapper.post;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.post.InsPostDTO;

@Mapper
public interface PostMapper {
    public void insPost(InsPostDTO insPosDTO);
}
