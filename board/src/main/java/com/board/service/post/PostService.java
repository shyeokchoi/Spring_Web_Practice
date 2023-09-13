package com.board.service.post;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.board.dto.common.PagingDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.dto.post.UpdatePostDTO;

public interface PostService {
    /**
     * 새로운 글 삽입
     * 
     * @param insPosDTO
     * @return 삽입된 글의 no.
     */
    @Transactional
    public Integer insPost(InsPostDTO insPosDTO);

    /**
     * 글 상세보기
     * 
     * @param postNo
     * @return 해당 글의 정보
     */
    @Transactional
    public SelectPostDetailDTO selectPost(Integer postNo);

    /**
     * 글 수정
     * 
     * @param updatePostDTO
     */
    @Transactional
    public void updatePost(UpdatePostDTO updatePostDTO);

    /**
     * 글 삭제
     * 
     * @param currMemberNo 현재 로그인한 멤버의 no.
     * @param postNo
     */
    @Transactional
    public void deletePost(Integer currMemberNo, Integer postNo);

    /**
     * 글 리스트
     * 
     * @param limit
     * @param offset
     * @return
     */
    @Transactional
    public List<SelectPostListDTO> selectPostList(PagingDTO pagingDTO);
}
