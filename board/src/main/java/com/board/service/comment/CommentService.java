package com.board.service.comment;

import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.SelectCommentListDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;

public interface CommentService {

    public Integer insComment(InsCommentDTO insCommentDTO);

    public PagingResponseDTO<SelectCommentListDTO> selectCommentList(PagingRequestDTO pagingRequestDTO);

    public Integer updatePost(UpdateCommentDTO updateCommentDTO);

    public void deleteComment(Integer memberNo, Integer commentNo);

    public PagingResponseDTO<SelectCommentListDTO> selectCommentList(Integer memberNo,
            PagingRequestDTO pagingRequestDTO);
}
