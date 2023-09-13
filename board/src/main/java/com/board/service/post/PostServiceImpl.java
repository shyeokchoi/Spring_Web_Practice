package com.board.service.post;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.post.InsPostDTO;
import com.board.dto.post.PutPostDTO;
import com.board.dto.post.SelectPostDetailDTO;
import com.board.dto.post.SelectPostListDTO;
import com.board.mapper.post.PostMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;

    @Override
    public Integer insPost(InsPostDTO insPosDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insPost'");
    }

    @Override
    public SelectPostDetailDTO selectPost(Integer postNo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectPost'");
    }

    @Override
    public void updatePost(PutPostDTO putPostDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePost'");
    }

    @Override
    public void deletePost(Integer postNo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePost'");
    }

    @Override
    public List<SelectPostListDTO> selectPostList(Integer limit, Integer offset) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectPostList'");
    }
}
