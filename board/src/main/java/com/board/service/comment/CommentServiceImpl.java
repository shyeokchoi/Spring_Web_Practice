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
        // 검색 대상 댓글 수 반환
        int totalRows = commentMapper.selectTotalRows(postNo, memberNo, pagingRequestDTO);

        if (totalRows > 0) {
            // 댓글 목록 반환
            List<CommentSimpleDTO> commentList = commentMapper.selectCommentList(postNo, memberNo,
                    pagingRequestDTO);

            // 페이징을 위한 정보에 댓글 목록 더해서 Response 만들기
            PagingResponseDTO<CommentSimpleDTO> pagingResponseDTO = new PagingResponseDTO<>(commentList,
                    pagingRequestDTO.getCurrPage(), pagingRequestDTO.getPageSize(), totalRows);

            return pagingResponseDTO;
        }
        return null;

    }

    @Override
    public int updateComment(UpdateCommentDTO updateCommentDTO) {
        // commentNo 유효성 체크
        Integer commentNo = updateCommentDTO.getCommentNo();
        CommentDTO comment = selectOne(commentNo);

        if (comment == null) {
            throw new NoDataFoundException("댓글이 존재하지 않습니다 : " + commentNo);
        }

        // 자기 자신의 댓글만 수정 가능
        if (comment.getAuthorNo() != updateCommentDTO.getModifierNo()) {
            throw new AuthenticationException("자신의 댓글만 수정할 수 있습니다");
        }

        // 댓글 수정
        commentMapper.updateComment(updateCommentDTO);
        return updateCommentDTO.getCommentNo();
    }

    @Override
    public void deleteComment(int memberNo, int commentNo) {
        // commentNo 유효성 체크
        CommentDTO comment = selectOne(commentNo);

        if (comment == null) {
            throw new NoDataFoundException("댓글이 존재하지 않습니다 : " + commentNo);
        }

        // 자신의 댓글만 삭제 가능
        if (comment.getAuthorNo() != memberNo) {
            throw new AuthenticationException("자신의 댓글만 삭제할 수 있습니다");
        }

        // 댓글 삭제
        commentMapper.deleteComment(commentNo);
    }
}
