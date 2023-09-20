package com.board.mapper.comment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.SelectCommentListDTO;
import com.board.dto.common.PagingRequestDTO;

@Mapper
public interface CommentMapper {

    void insComment(InsCommentDTO insCommentDTO);

    int selectTotalRows(
            @Param("postNo") Integer postNo,
            @Param("memberNo") Integer memberNo,
            @Param("pagingRequestDTO") PagingRequestDTO pagingRequestDTO);

    List<SelectCommentListDTO> selectCommentList(
            @Param("postNo") Integer postNo,
            @Param("memberNo") Integer memberNo,
            @Param("pagingRequestDTO") PagingRequestDTO pagingRequestDTO);

}
