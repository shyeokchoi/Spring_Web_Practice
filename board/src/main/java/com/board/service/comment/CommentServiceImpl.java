package com.board.service.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.SelectCommentListDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.exception.AuthenticationException;
import com.board.mapper.comment.CommentMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;

    @Override
    public Integer insComment(InsCommentDTO insCommentDTO) {
        commentMapper.insComment(insCommentDTO);
        return insCommentDTO.getNo();
    }

    @Override
    public PagingResponseDTO<SelectCommentListDTO> selectCommentList(Integer postNo, Integer memberNo,
            PagingRequestDTO pagingRequestDTO) {
        // 검색 대상 댓글 수 반환
        int totalRows = commentMapper.selectTotalRows(postNo, memberNo, pagingRequestDTO);

        // 댓글 목록 반환
        List<SelectCommentListDTO> commentList = commentMapper.selectCommentList(postNo, memberNo, pagingRequestDTO);

        // 페이징을 위한 정보에 댓글 목록 더해서 Response 만들기
        PagingResponseDTO<SelectCommentListDTO> pagingResponseDTO = new PagingResponseDTO<>(commentList,
                pagingRequestDTO.getCurrPage(), pagingRequestDTO.getPageSize(), totalRows);

        return pagingResponseDTO;
    }

    @Override
    public Integer updatePost(UpdateCommentDTO updateCommentDTO) {
        commentMapper.updatePost(updateCommentDTO);
        return updateCommentDTO.getPostNo();
    }

    @Override
    public void deleteComment(Integer memberNo, Integer commentNo) {
        // 자신의 댓글만 삭제할 수 있음.
        if (commentMapper.retvAuthorNo(commentNo) != memberNo) {
            throw new AuthenticationException("ERROR : 자신의 댓글만 삭제할 수 있습니다");
        }

        // 댓글 삭제
        commentMapper.deleteComment(commentNo);
    }
}
