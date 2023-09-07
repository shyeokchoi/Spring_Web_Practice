package com.board.service.member;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.member.InsMemberDTO;
import com.board.exception.EmailConflictException;
import com.board.exception.IdConflictException;
import com.board.mapper.member.MemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public Integer insMember(InsMemberDTO insMemberDTO) {
        // id conflict check
        Optional.ofNullable(memberMapper.selectById(insMemberDTO.getId()))
                .ifPresent(member -> {
                    throw new IdConflictException();
                });

        // email conflict check
        Optional.ofNullable(memberMapper.selectByEmail(insMemberDTO.getEmail()))
                .ifPresent(member -> {
                    throw new EmailConflictException();
                });

        memberMapper.insMember(insMemberDTO);

        return insMemberDTO.getNo();
    }
}
