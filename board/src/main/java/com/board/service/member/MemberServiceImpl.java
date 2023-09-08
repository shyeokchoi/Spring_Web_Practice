package com.board.service.member;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.dto.member.InsMemberDTO;
import com.board.dto.member.SelectMemberDTO;
import com.board.dto.member.SigninRequestDTO;
import com.board.dto.member.SigninResponseDTO;
import com.board.exception.ConflictException;
import com.board.mapper.member.MemberMapper;
import com.board.util.PwEncryptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
/**
 * 로그인, 로그아웃, 멤버 가입, 조회, 수정, 삭제 등을 담당하는 Service
 */
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final PwEncryptor pwEncryptor;

    @Override
    @Transactional
    public Integer insMember(InsMemberDTO insMemberDTO) {
        // 아이디, 이메일 중복 여부를 확인하고 중복이면 예외 발생
        checkDuplicateId(insMemberDTO);
        checkDuplicateEmail(insMemberDTO);

        // 비밀번호 암호화 후 DTO에 대체해서 넣어줌
        insMemberDTO.setPw(pwEncryptor.encryptPw(insMemberDTO.getPw()));

        // DB에 저장
        memberMapper.insMember(insMemberDTO);

        return insMemberDTO.getNo();
    }

    /**
     * id 중복 여부 확인
     * 
     * @param insMemberDTO
     */
    private void checkDuplicateId(InsMemberDTO insMemberDTO) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        selectMemberDTO.setId(insMemberDTO.getId());
        Optional.ofNullable(memberMapper.selectOne(selectMemberDTO))
                .ifPresent(member -> {
                    throw new ConflictException("id");
                });
    }

    /**
     * email 중복 여부 확인
     * 
     * @param insMemberDTO
     */
    private void checkDuplicateEmail(InsMemberDTO insMemberDTO) {
        SelectMemberDTO selectMemberDTO = new SelectMemberDTO();

        selectMemberDTO.setEmail(insMemberDTO.getEmail());
        Optional.ofNullable(memberMapper.selectOne(selectMemberDTO))
                .ifPresent(member -> {
                    throw new ConflictException("email");
                });
    }

    @Override
    public SigninResponseDTO signin(SigninRequestDTO signinRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signin'");
    }
}
