package com.board.service.comment;

import org.springframework.stereotype.Service;

import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.SelectCommentListDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.mapper.comment.CommentMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

    @Override
    public Integer insComment(InsCommentDTO insCommentDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insComment'");
    }

    @Override
    public PagingResponseDTO<SelectCommentListDTO> selectCommentList(PagingRequestDTO pagingRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectCommentList'");
    }

    @Override
    public Integer updatePost(UpdateCommentDTO updateCommentDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePost'");
    }

    @Override
    public void deleteComment(Integer memberNo, Integer commentNo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteComment'");
    }

    @Override
    public PagingResponseDTO<SelectCommentListDTO> selectCommentList(Integer memberNo,
            PagingRequestDTO pagingRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectCommentList'");
    }
}
