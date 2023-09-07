package com.board.service.member;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.SelectMemberDTO;
import com.board.exception.ConflictException;
import com.board.mapper.member.MemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
/**
 * 로그인, 로그아웃, 멤버 가입, 조회, 수정, 삭제 등을 담당하는 Service
 */
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public Integer insMember(InsMemberDTO insMemberDTO) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        // 아이디 중복체크
        selectMemberDTO.setId(insMemberDTO.getId());
        Optional.ofNullable(memberMapper.selectOne(selectMemberDTO))
                .ifPresent(member -> {
                    throw new ConflictException("id");
                });

        // 이메일 중복체크
        selectMemberDTO.setId(null);
        selectMemberDTO.setEmail(insMemberDTO.getEmail());
        Optional.ofNullable(memberMapper.selectOne(selectMemberDTO))
                .ifPresent(member -> {
                    throw new ConflictException("email");
                });

        memberMapper.insMember(insMemberDTO);

        return insMemberDTO.getNo();
    }
}
