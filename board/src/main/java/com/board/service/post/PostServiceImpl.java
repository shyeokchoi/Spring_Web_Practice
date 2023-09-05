package com.board.service.post;

import org.springframework.stereotype.Service;

import com.board.mapper.post.PostMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
}
