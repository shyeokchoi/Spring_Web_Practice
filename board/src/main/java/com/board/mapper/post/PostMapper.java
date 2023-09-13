package com.board.mapper.post;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;

@Mapper
public interface PostMapper {
    public void insPost(InsPostDTO insPosDTO);

    public SelectPostDetailDTO selectPost(Integer postNo);
}
