package com.board.service.post;

import com.board.dto.common.PagingRequestWithSearchKeywordDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.PostDetailDTO;
import com.board.dto.post.PostSimpleDTO;
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
    public PostDetailDTO selectPost(int postNo);

    /**
     * 글 수정
     * 
     * @param updatePostDTO
     * @return 수정된 게시글 no.
     */
    public Integer updatePost(UpdatePostDTO updatePostDTO);

    /**
     * 글 삭제
     * 
     * @param currMemberNo 현재 로그인한 멤버의 no.
     * @param postNo
     */
    public void deletePost(int currMemberNo, int postNo);

    /**
     * 모든 게시물을 pagingRequestDTO의 파라미터 값들에 맞게 리스트로 만들어 반환합니다.
     * 
     * @param pagingRequestDTO
     * @return
     */
    public PagingResponseDTO<PostSimpleDTO> selectPostList(PagingRequestWithSearchKeywordDTO pagingRequestDTO);

    /**
     * 주어진 authorNo가 글쓴이인 글들을 pagingRequestDTO의 파라미터 값들에 맞게 리스트로 만들어 반환합니다.
     * 
     * @param authorNo         글쓴이의 no.
     * @param pagingRequestDTO paging을 위한 파라미터
     * @return
     */
    public PagingResponseDTO<PostSimpleDTO> selectPostList(Integer authorNo,
            PagingRequestWithSearchKeywordDTO pagingRequestDTO);

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
    public Integer selectTempPostNo(int memberNo);

    /**
     * 임시저장 게시물 수정
     * 
     * @param memberNo
     * @param updatePostDTO
     * @return
     */
    public Integer updateTempPost(int memberNo, UpdatePostDTO updatePostDTO);

    /**
     * 임시저장 게시물 최종 등록
     * 
     * @param memberNo
     * @param postNo
     * @return
     */
    public Integer finalizeTempPost(int memberNo, int postNo);
}
