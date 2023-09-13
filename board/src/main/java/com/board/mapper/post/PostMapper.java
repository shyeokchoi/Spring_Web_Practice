package com.board.mapper.post;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.UpdatePostDTO;

@Mapper
public interface PostMapper {
    public void insPost(InsPostDTO insPosDTO);

    public SelectPostDetailDTO selectPost(Integer postNo);

    public void updatePost(UpdatePostDTO updatePostDTO);
}
