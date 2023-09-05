package com.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.board.dto.ResponseDTO;
import com.board.dto.member.InsMemberDTO;
import com.board.framework.base.BaseController;
import com.board.service.comment.CommentService;
import com.board.service.member.MemberService;
import com.board.service.post.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController extends BaseController {
    private final CommentService commentService;
    private final MemberService memberService;
    private final PostService postService;

    // Member
    /**
     * 회원가입
     * 
     * @param insMemberDTO 회원가입에 필요한 정보
     * @return 회원 no
     */
    @PostMapping("/members")
    public ResponseEntity<ResponseDTO<Integer>> insMember(@RequestBody InsMemberDTO insMemberDTO) {
        return ok(memberService.insMember(insMemberDTO));
    }

    // Post

    // Comment

}
