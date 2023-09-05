package com.board.controller;

import org.springframework.web.bind.annotation.RestController;

import com.board.service.comment.CommentService;
import com.board.service.member.MemberService;
import com.board.service.post.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final CommentService commentService;
    private final MemberService memberService;
    private final PostService postService;

}
