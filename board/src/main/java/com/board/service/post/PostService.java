package com.board.service.post;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.board.dto.common.PagingDTO;
import com.board.dto.common.SearchDTO;
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
    public List<SelectPostListDTO> selectPostList(PagingDTO pagingDTO, SearchDTO searchDTO);

    /**
     * 게시글 임시저장.
     * 만약 해당 유저가 이미 임시저장한 게시글이 있다면 삭제하고 덮어씁니다.
     * 
     * @param insPostDTO
     * @return 임시저장된 게시글의 no.
     */
    @Transactional
    public Integer insTempPost(InsPostDTO insPostDTO);

    /**
     * 기존에 작성된 임시저장글의 post no.를 반환합니다.
     * 
     * @param currMemberNo
     * @return
     */
    @Transactional
    public Integer selectPrevTempPostNo(Integer currMemberNo);
}
