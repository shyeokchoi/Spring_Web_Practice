package com.board.mapper.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.board.dto.common.PagingRequestWithSearchKeywordDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.PostDetailDTO;
import com.board.dto.post.PostSimpleDTO;
import com.board.dto.post.UpdatePostDTO;

@Mapper
public interface PostMapper {
        public int selectTotalRows(
            @Param("authorNo") Integer authorNo,
            @Param("pagingRequestDTO") PagingRequestWithSearchKeywordDTO pagingRequestDTO);

        public void insPost(InsPostDTO insPosDTO);

        public PostDetailDTO selectOne(int postNo);

        public List<Integer> selectFileNoList(int postNo);

        public void updatePost(UpdatePostDTO updatePostDTO);

        public int retvAuthorNo(int postNo);

        public void deletePost(int postNo);

        public List<PostSimpleDTO> selectPostList(
                @Param("authorNo") Integer authorNo,
                @Param("pagingRequestDTO") PagingRequestWithSearchKeywordDTO pagingRequestDTO);

        public Integer selectPrevTempPostNo(int currMemberNo);

        public int finalizeTempPost(int postNo);

        public void deleteCommentsRelatedToPost(int postNo);
}
