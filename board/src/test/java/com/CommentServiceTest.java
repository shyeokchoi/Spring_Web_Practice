package com;

import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.board.dto.comment.CommentDTO;
import com.board.dto.comment.CommentSimpleDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.enums.CommentStatusEnum;
import com.board.exception.AuthenticationException;
import com.board.exception.NoDataFoundException;
import com.board.mapper.comment.CommentMapper;
import com.board.service.comment.CommentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    CommentServiceImpl commentService;
    @Mock
    CommentMapper commentMapper;

    CommentDTO comment = CommentDTO.builder()
            .no(1)
            .postNo(1)
            .createdAt(1)
            .deletedAt(null)
            .authorNo(1)
            .content("some content")
            .modifiedAt(1L)
            .modifierNo(1)
            .status(CommentStatusEnum.POSTED)
            .build();

    @Test
    @DisplayName("검색된 대상이 없을 때")
    public void noRowsSearched() {
        // given
        Integer postNo = null;
        Integer memberNo = null;
        PagingRequestDTO pagingRequestDTO = new PagingRequestDTO(1, 200);

        // stub
        when(commentMapper.selectTotalRows(postNo, memberNo, pagingRequestDTO)).thenReturn(0);

        // when
        PagingResponseDTO<CommentSimpleDTO> res = commentService.selectCommentList(postNo, memberNo, pagingRequestDTO);

        // then
        Assertions.assertThat(res).isEqualTo(null);
    }

    @Test
    @DisplayName("검색되었을 때")
    public void searched() {
        // given
        Integer postNo = null;
        Integer memberNo = null;
        long currPage = 1;
        long pageSize = 200;
        PagingRequestDTO pagingRequestDTO = new PagingRequestDTO(currPage, pageSize);

        // stub
        CommentSimpleDTO commentSimpleDTO = new CommentSimpleDTO();
        commentSimpleDTO.setContent("댓글 내용");
        commentSimpleDTO.setAuthorName("작성자 이름");
        commentSimpleDTO.setCreatedAt(1L);
        List<CommentSimpleDTO> commentList = List.of(commentSimpleDTO);

        int totalCnt = commentList.size();

        when(commentMapper.selectCommentList(postNo, memberNo, pagingRequestDTO)).thenReturn(commentList);
        when(commentMapper.selectTotalRows(postNo, memberNo, pagingRequestDTO)).thenReturn(totalCnt);

        // when
        PagingResponseDTO<CommentSimpleDTO> res = commentService.selectCommentList(postNo, memberNo, pagingRequestDTO);

        // then
        Assertions.assertThat(res.getCurrPage()).isEqualTo(currPage);
        Assertions.assertThat(res.getPageSize()).isEqualTo(pageSize);
        Assertions.assertThat(res.getDataList()).isEqualTo(commentList);
        Assertions.assertThat(res.getTotalCount()).isEqualTo(totalCnt);
    }

    @Test
    @DisplayName("댓글 수정 - 수정할 댓글이 없을 때")
    public void noCommentToUpdate() {
        // given
        int commentNo = 1;

        UpdateCommentDTO updateCommentDTO = new UpdateCommentDTO();
        updateCommentDTO.setCommentNo(commentNo);
        updateCommentDTO.setContent("content");
        updateCommentDTO.setModifierNo(1);

        // stub
        when(commentMapper.selectOne(commentNo)).thenReturn(null);

        // then
        Assertions.assertThatThrownBy(() -> commentService.updateComment(updateCommentDTO))
                .isInstanceOf(NoDataFoundException.class);
    }

    @Test
    @DisplayName("댓글 수정 - 자기 자신의 댓글이 아닐 때")
    public void noRightToUpdateComment() {
        // given
        int commentNo = 1;

        UpdateCommentDTO updateCommentDTO = new UpdateCommentDTO();
        updateCommentDTO.setCommentNo(commentNo);
        updateCommentDTO.setContent("content");
        updateCommentDTO.setModifierNo(2);

        // stub
        CommentDTO comment = CommentDTO.builder()
                .no(commentNo)
                .postNo(1)
                .createdAt(1)
                .deletedAt(1L)
                .authorNo(1)
                .content("content")
                .modifiedAt(1L)
                .modifierNo(1)
                .status(CommentStatusEnum.POSTED)
                .build();

        when(commentMapper.selectOne(commentNo)).thenReturn(comment);

        // then
        Assertions.assertThatThrownBy(() -> commentService.updateComment(updateCommentDTO))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 삭제할 댓글이 없을 때")
    public void noCommentToDelete() {
        // given
        int commentNo = 1;

        // stub
        when(commentMapper.selectOne(commentNo)).thenReturn(null);

        // then
        Assertions.assertThatThrownBy(() -> commentService.deleteComment(1, commentNo))
                .isInstanceOf(NoDataFoundException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 자기 자신의 댓글이 아닐 때")
    public void noRightToDeleteComment() {
        // given
        int commentNo = 1;
        int memberNo = 2;

        // stub
        CommentDTO comment = CommentDTO.builder()
                .no(commentNo)
                .postNo(1)
                .createdAt(1)
                .deletedAt(1L)
                .authorNo(1)
                .content("content")
                .modifiedAt(1L)
                .modifierNo(1)
                .status(CommentStatusEnum.POSTED)
                .build();

        when(commentMapper.selectOne(commentNo)).thenReturn(comment);

        // then
        Assertions.assertThatThrownBy(() -> commentService.deleteComment(memberNo, commentNo))
                .isInstanceOf(AuthenticationException.class);
    }
}
