package com.board.service.post;

import java.util.List;

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
    public Integer insPost(InsPostDTO insPosDTO);

    /**
     * 글 상세보기
     * 
     * @param postNo
     * @return 해당 글의 정보
     */
    public SelectPostDetailDTO selectPost(Integer postNo);

    /**
     * 글 수정
     * 
     * @param updatePostDTO
     */
    public void updatePost(UpdatePostDTO updatePostDTO);

    /**
     * 글 삭제
     * 
     * @param currMemberNo 현재 로그인한 멤버의 no.
     * @param postNo
     */
    public void deletePost(Integer currMemberNo, Integer postNo);

    /**
     * 모든 게시물 리스트 반환
     * 
     * @param authorNo  게시글 작성자 no. null일 경우 모든 게시글 리스트 반환
     * @param pagingDTO
     * @param searchDTO null일 경우 검색 기능 없음.
     * @return
     */
    public List<SelectPostListDTO> selectPostList(PagingDTO pagingDTO, SearchDTO searchDTO);

    /**
     * 특정 유저의 게시물 리스트 반환
     * 
     * @param authorNo  검색하고자 하는 게시글 작성자 no.
     * @param pagingDTO
     * @param searchDTO null일 경우 검색 기능 없음.
     * @return
     */
    public List<SelectPostListDTO> selectPostList(Integer authorNo, PagingDTO pagingDTO, SearchDTO searchDTO);

    /**
     * 게시글 임시저장.
     * 만약 해당 유저가 이미 임시저장한 게시글이 있다면 삭제하고 덮어씁니다.
     * 
     * @param insPostDTO
     * @return 임시저장된 게시글의 no.
     */
    public Integer insTempPost(InsPostDTO insPostDTO);

    /**
     * 임시저장 게시글 불러옴
     * 
     * @param memberNo
     * @return
     */
    public SelectPostDetailDTO selectTempPost(Integer memberNo);
}
