package com.board.mapper.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.board.dto.common.PagingDTO;
import com.board.dto.common.SearchDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.dto.post.UpdatePostDTO;

@Mapper
public interface PostMapper {
    /**
     * 이미 삭제된 게시글인지 확인
     * 
     * @param postNo
     * @return
     */
    public Boolean isAlreadyDeleted(Integer postNo);

    /**
     * 게시글 등록
     * 
     * @param insPosDTO
     */
    public void insPost(InsPostDTO insPosDTO);

    /**
     * 게시글 상세정보 불러오기
     * 
     * @param postNo
     * @return
     */
    public SelectPostDetailDTO selectPost(Integer postNo);

    /**
     * 게시글 수정
     * 
     * @param updatePostDTO
     */
    public void updatePost(UpdatePostDTO updatePostDTO);

    /**
     * 게시글 no로부터 해당 게시글 작성자의 no 불러오기
     * 
     * @param postNo
     * @return
     */
    public Integer retvAuthorNo(Integer postNo);

    /**
     * 게시글 삭제
     * 
     * @param postNo
     */
    public void deletePost(Integer postNo);

    /**
     * 게시글 리스트 (+ 검색)
     * 
     * @param pagingDTO
     * @param searchDTO
     * @return
     */
    public List<SelectPostListDTO> selectPostList(
            @Param("authorNo") Integer authorNo,
            @Param("pagingDTO") PagingDTO pagingDTO,
            @Param("searchDTO") SearchDTO searchDTO);

    /**
     * 기존에 작성된 임시저장글 번호 찾기
     * 
     * @param currMemberNo
     * @return 기존에 작성된 임시저장글 번호
     */
    public Integer selectPrevTempPostNo(Integer currMemberNo);
}
