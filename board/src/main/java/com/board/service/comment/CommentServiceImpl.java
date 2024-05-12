package com.board.service.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.comment.CommentDTO;
import com.board.dto.comment.CommentSimpleDTO;
import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.exception.AuthenticationException;
import com.board.exception.NoDataFoundException;
import com.board.mapper.comment.CommentMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

    @Override
    public CommentDTO selectOne(int commentNo) {
        return commentMapper.selectOne(commentNo);
    }

    @Override
    public Integer insComment(InsCommentDTO insCommentDTO) {
        commentMapper.insComment(insCommentDTO);
        return insCommentDTO.getNo();
    }

    @Override
    public PagingResponseDTO<CommentSimpleDTO> selectCommentList(Integer postNo, Integer memberNo,
            PagingRequestDTO pagingRequestDTO) {
        int totalRows = commentMapper.selectTotalRows(postNo, memberNo, pagingRequestDTO);

        if (totalRows > 0) {
            List<CommentSimpleDTO> commentList =
                commentMapper.selectCommentList(postNo, memberNo, pagingRequestDTO);
            return new PagingResponseDTO<>(
                commentList,
                pagingRequestDTO.getCurrPage(),
                pagingRequestDTO.getPageSize(),
                totalRows);
        }
        return null;

    }

    @Override
    public int updateComment(UpdateCommentDTO updateCommentDTO) {
        Integer commentNo = updateCommentDTO.getCommentNo();
        CommentDTO comment = selectOne(commentNo);

        if (comment == null) {
            throw new NoDataFoundException("댓글이 존재하지 않습니다 : " + commentNo);
        }
        if (!equalsAuthor(comment, updateCommentDTO)) {
            throw new AuthenticationException("자신의 댓글만 수정할 수 있습니다");
        }
        commentMapper.updateComment(updateCommentDTO);
        return updateCommentDTO.getCommentNo();
    }

    @Override
    public void deleteComment(int memberNo, int commentNo) {
        CommentDTO comment = selectOne(commentNo);

        if (comment == null) {
            throw new NoDataFoundException("댓글이 존재하지 않습니다 : " + commentNo);
        }
        if (comment.getAuthorNo() != memberNo) {
            throw new AuthenticationException("자신의 댓글만 삭제할 수 있습니다");
        }
        commentMapper.deleteComment(commentNo);
    }

    private boolean equalsAuthor(
        CommentDTO comment, UpdateCommentDTO updateDto
    ) {
        return comment.getAuthorNo().equals(updateDto.getModifierNo());
    }
}
