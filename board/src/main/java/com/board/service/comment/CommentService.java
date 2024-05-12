package com.board.service.comment;

import com.board.dto.comment.CommentDTO;
import com.board.dto.comment.CommentSimpleDTO;
import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;

public interface CommentService {

    public CommentDTO selectOne(int commentNo);

    public Integer insComment(InsCommentDTO insCommentDTO);

    public PagingResponseDTO<CommentSimpleDTO> selectCommentList(Integer postNo, Integer memberNo,
            PagingRequestDTO pagingRequestDTO);

    public int updateComment(UpdateCommentDTO updateCommentDTO);

    public void deleteComment(int memberNo, int commentNo);
}
