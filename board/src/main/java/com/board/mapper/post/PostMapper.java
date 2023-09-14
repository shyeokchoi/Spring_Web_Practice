package com.board.mapper.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.board.dto.common.PagingDTO;
import com.board.dto.common.SearchDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.dto.post.UpdatePostDTO;

@Mapper
public interface PostMapper {
    public Boolean isAlreadyDeleted(Integer postNo);

    public void insPost(InsPostDTO insPosDTO);

    public SelectPostDetailDTO selectPost(Integer postNo);

    public void updatePost(UpdatePostDTO updatePostDTO);

    public Integer retvAuthorNo(Integer postNo);

    public void deletePost(Integer postNo);

    public List<SelectPostListDTO> selectPostList(
            @Param("pagingDTO") PagingDTO pagingDTO,
            @Param("searchDTO") SearchDTO searchDTO);

}
