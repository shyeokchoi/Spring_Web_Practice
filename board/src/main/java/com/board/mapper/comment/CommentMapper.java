package com.board.mapper.comment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.board.dto.comment.CommentDTO;
import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.SelectCommentListDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;

@Mapper
public interface CommentMapper {

    CommentDTO selectOne(int commentNo);

    void insComment(InsCommentDTO insCommentDTO);

    int selectTotalRows(
            @Param("postNo") Integer postNo,
            @Param("memberNo") Integer memberNo,
            @Param("pagingRequestDTO") PagingRequestDTO pagingRequestDTO);

    List<SelectCommentListDTO> selectCommentList(
            @Param("postNo") Integer postNo,
            @Param("memberNo") Integer memberNo,
            @Param("pagingRequestDTO") PagingRequestDTO pagingRequestDTO);

    void updatePost(UpdateCommentDTO updateCommentDTO);

    int retvAuthorNo(int commentNo);

    void deleteComment(int commentNo);

}
