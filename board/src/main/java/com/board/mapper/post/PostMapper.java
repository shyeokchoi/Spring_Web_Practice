package com.board.mapper.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.board.dto.common.PagingRequestDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.dto.post.UpdatePostDTO;

@Mapper
public interface PostMapper {
        /**
         * 전체 게시물 수
         * 
         * @param authorNo
         * @return
         */
        public int selectTotalRows(@Param("authorNo") Integer authorNo,
                        @Param("pagingRequestDTO") PagingRequestDTO pagingRequestDTO);

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
        public SelectPostDetailDTO selectPost(int postNo);

        /**
         * 게시글과 연결된 파일 no들 불러오기
         * 
         * @param postNo
         * @return 파일 이름들의 리스트
         */
        public List<Integer> selectFileNoList(int postNo);

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
         * @param pagingRequestDTO
         * @param searchDTO
         * @return
         */
        public List<SelectPostListDTO> selectPostList(
                        @Param("authorNo") Integer authorNo,
                        @Param("pagingRequestDTO") PagingRequestDTO pagingRequestDTO);

        /**
         * 기존에 작성된 임시저장글 번호 찾기
         * 
         * @param currMemberNo
         * @return 기존에 작성된 임시저장글 번호
         */
        public Integer selectPrevTempPostNo(Integer currMemberNo);

        /**
         * 기존에 작성된 임시저장글 최종등록
         * 
         * @param postNo
         * @return
         */
        public Integer finalizeTempPost(Integer postNo);

}
