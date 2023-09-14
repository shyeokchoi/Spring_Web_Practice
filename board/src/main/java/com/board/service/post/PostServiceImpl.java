package com.board.service.post;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.common.PagingDTO;
import com.board.dto.common.SearchDTO;
import com.board.dto.post.InsPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.dto.post.UpdatePostDTO;
import com.board.exception.AlreadyDeletedException;
import com.board.exception.AuthenticationException;
import com.board.mapper.post.PostMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;

    /**
     * 이미 삭제된 게시물인지 확인하는 함수
     * 
     * @param postNo 확인의 대상 post no.
     */
    private void checkIfAlreadyDeleted(Integer postNo) {
        if (postMapper.isAlreadyDeleted(postNo)) {
            throw new AlreadyDeletedException("이미 삭제된 게시물입니다");
        }
    }

    @Override
    public Integer insPost(InsPostDTO insPostDTO) {
        // 새로운 게시물 생성
        postMapper.insPost(insPostDTO);

        return insPostDTO.getNo();
    }

    @Override
    public SelectPostDetailDTO selectPost(Integer postNo) {
        // 이미 삭제되진 않았는지 확인
        checkIfAlreadyDeleted(postNo);

        // 해당 postNo에 해당하는 게시물 검색
        return postMapper.selectPost(postNo);
    }

    @Override
    public void updatePost(UpdatePostDTO updatePostDTO) {
        // 이미 삭제되진 않았는지 확인
        checkIfAlreadyDeleted(updatePostDTO.getPostNo());

        // 게시물 내용 업데이트
        postMapper.updatePost(updatePostDTO);
    }

    @Override
    public void deletePost(Integer currMemberNo, Integer postNo) {
        // 이미 삭제되진 않았는지 확인
        checkIfAlreadyDeleted(postNo);

        // 자신의 글만 삭제할 수 있음.
        if (postMapper.retvAuthorNo(postNo) != currMemberNo) {
            throw new AuthenticationException("자신의 글만 삭제할 수 있습니다");
        }
        postMapper.deletePost(postNo);
    }

    @Override
    public List<SelectPostListDTO> selectPostList(PagingDTO pagingDTO, SearchDTO searchDTO) {
        return postMapper.selectPostList(pagingDTO, searchDTO);
    }

}
