package com.board.service.member;

import org.springframework.stereotype.Service;

import com.board.mapper.member.MemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
}
