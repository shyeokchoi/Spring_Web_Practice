package com.board.service.comment;

import com.board.dto.comment.InsCommentDTO;
import com.board.dto.comment.SelectCommentListDTO;
import com.board.dto.comment.UpdateCommentDTO;
import com.board.dto.common.PagingRequestDTO;
import com.board.dto.common.PagingResponseDTO;

public interface CommentService {

    /**
     * 댓글 등록
     * 
     * @param insCommentDTO
     * @return 등록된 댓글의 no.
     */
    public Integer insComment(InsCommentDTO insCommentDTO);

    /**
     * 댓글 목록 반환
     * 
     * @param postNo           null이 아니라면 해당 게시글에 달린 댓글만 반환.
     * @param memberNo         null이 아니라면 해당 멤버가 작성한 댓글만 반환.
     * @param pagingRequestDTO
     * @return
     */
    public PagingResponseDTO<SelectCommentListDTO> selectCommentList(Integer postNo, Integer memberNo,
            PagingRequestDTO pagingRequestDTO);

    /**
     * 댓글 수정
     * 
     * @param updateCommentDTO
     * @return 수정된 댓글의 no.
     */
    public Integer updatePost(UpdateCommentDTO updateCommentDTO);

    /**
     * 댓글 삭제
     * 
     * @param memberNo  현재 접속한 사용자의 no. 자신의 댓글이 맞는지 확인하기 위해 필요.
     * @param commentNo
     */
    public void deleteComment(Integer memberNo, Integer commentNo);
}
