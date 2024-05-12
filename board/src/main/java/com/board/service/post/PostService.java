package com.board.service.post;

import com.board.dto.common.PagingRequestWithSearchKeywordDTO;
import com.board.dto.common.PagingResponseDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.PostDetailDTO;
import com.board.dto.post.PostSimpleDTO;
import com.board.dto.post.UpdatePostDTO;

public interface PostService {
    public Integer insPost(InsPostDTO insPosDTO);

    public PostDetailDTO selectPost(int postNo);

    public Integer updatePost(UpdatePostDTO updatePostDTO);

    public void deletePost(int currMemberNo, int postNo);

    public PagingResponseDTO<PostSimpleDTO> selectPostList(
        PagingRequestWithSearchKeywordDTO pagingRequestDTO);

    public PagingResponseDTO<PostSimpleDTO> selectPostList(
        Integer authorNo,
            PagingRequestWithSearchKeywordDTO pagingRequestDTO);

    public Integer insTempPost(InsPostDTO insPostDTO);

    public Integer selectTempPostNo(int memberNo);

    public Integer updateTempPost(int memberNo, UpdatePostDTO updatePostDTO);

    public Integer finalizeTempPost(int memberNo, int postNo);
}
